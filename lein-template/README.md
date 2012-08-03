# heroku

A Leiningen template for Heroku apps.

## Usage

    $ lein new heroku myapp
    $ cd myapp
    $ git init
    $ heroku apps:create myapp
    $ git add .
    $ git commit -m "Initial commit"
    $ git push heroku master
    $ curl http://myapp.herokuapp.com

## License

Copyright © 2012 Heroku

Distributed under the Eclipse Public License, the same as Clojure.
