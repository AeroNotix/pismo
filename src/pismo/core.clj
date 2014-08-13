(ns pismo.core
  (:require [clj-yaml.core :as yaml]
            [awizo.core :as awizo]
            [clojure.core.async :as async])
  (:use [clojure.java.shell :only [sh]])
  (:import [javax.mail.internet MimeMessage])
  (:import [javax.mail Session])
  (:gen-class))


(def config (yaml/parse-string (slurp "/etc/pismo.yaml")))
(def seen (atom #{}))


(defn seen? [p]
  (contains? @seen p))

(defn update-seen [p]
  (swap! seen conj (.. p (getFileName) (toString))))

(defn read-message [p]
  (let [content (slurp p)
        bais    (java.io.ByteArrayInputStream. (.getBytes content))
        msg     (MimeMessage. (Session/getInstance (java.util.Properties.)) bais)
        from    (.toString (first (.getFrom msg)))]
    (format "From: %s\n Subject: %s" from (.getSubject msg))))

(defn new-email-notify [p]
  (update-seen p)
  (when (.exists (clojure.java.io/file (.toString p)))
    (.start (Thread. #(sh (config :notifier-command) (config :notifier-args))))
    (let [email-info (read-message (.toString p))]
      (sh "notify-send" "-t" "1500" "New mail!: " email-info))))

(defn mailmessage [p]
  (let [path (awizo/string->path p)]
    #(let [filepath (.context %)
           fullpath (.resolve path filepath)]
       (when-not (seen? fullpath)
         (try
           (new-email-notify fullpath)
           (catch java.io.FileNotFoundException error
             (prn error)))))))

(defn dir-seq [path]
  (let [dir-contents (file-seq (clojure.java.io/file path))
        dirs (filter #(.isDirectory %) dir-contents)]
    (map #(.getPath %) dirs)))

(defn is-maildir? [path]
  (.contains path "new"))

(defn create-watcher [path]
  (awizo/attach-handler path (mailmessage path) [awizo/CREATE]))

(defn create-watchers [config]
  (doall
    (flatten
      (for [path (config :paths)]
        (if (config :recursive)
          (let [paths (filter is-maildir? (dir-seq path))]
            (map create-watcher paths))
          (create-watcher path))))))

(defn -main [& args]
  (create-watchers config))
