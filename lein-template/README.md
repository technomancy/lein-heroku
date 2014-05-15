# Heroku Leiningen Template

A Leiningen template for Compojure web apps using Heroku.

The generated project has a few basics set up beyond the bare Compojure defaults:

* Cookie-backed session store
* Stack traces when in development
* Environment-based config via [environ](https://github.com/weavejester/environ)
* [HTTP-based REPL debugging](https://devcenter.heroku.com/articles/debugging-clojure) via [drawbridge](https://github.com/cemerick/drawbridge)

## Usage

    $ lein new heroku myapp
    $ cd myapp
    $ git init
    $ heroku apps:create myapp
    $ git add .
    $ git commit -m "Initial commit"
    $ git push heroku master
    $ curl http://myapp.herokuapp.com

See the [README of the generated project](https://github.com/technomancy/lein-heroku/blob/master/lein-template/resources/leiningen/new/heroku/README.md) for details.

## License

Copyright © 2012-2014 Heroku

Distributed under the Eclipse Public License, the same as Clojure.
