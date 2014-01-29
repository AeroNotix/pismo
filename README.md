# pismo

Pismo is a small Maildir watcher which looks for new e-mails and
notifies you of their presence.


## Installation

```shell

$ git clone git@github.com:AeroNotix/pismo.git
$ cd pismo
$ lein uberjar
```

## Usage

Simply start the jar:

```shell
    $ java -jar pismo-0.1.0-standalone.jar [args]
```
## Options

Pismo is configured via /etc/pismo.yaml so that file must exist and be
readable by whichever user is running pismo.


## Example configuration

```yaml
paths:
- /home/xeno/.mail/
- /home/xeno/.ubnt-mail/

recursive: true
notifier-command: mpg123
notifier-args: /home/xeno/.mutt/notify.mp3
```


### Bugs

Yes.

...

## License

Copyright © 2014 Aaron France

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
