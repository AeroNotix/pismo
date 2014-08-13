(defproject pismo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [awizo "0.0.3"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [clj-yaml "0.4.0"]
                 [javax.mail/mail "1.4.3"]]
  :plugins [[cider/cider-nrepl "0.8.0-SNAPSHOT"]]
  :main ^:skip-aot pismo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
