# {{name}}

A [Heroku](http://www.heroku.com) web app using Compojure.

This generated project has a few basics set up beyond the bare Compojure defaults:

* Cookie-backed session store
* Stack traces when in development
* Environment-based config via [enviorn](https://github.com/weavejester/environ)
* [HTTP-based REPL debugging](https://devcenter.heroku.com/articles/debugging-clojure) via [drawbridge](https://github.com/cemerick/drawbridge)

## Usage

To start a local web server for development you can either eval the
commented out forms at the bottom of `web.clj` from your editor or
launch from the command line:

    $ lein run -m {{name}}.web

Initialize a git repository for your project.

    $ git init
    $ git add .
    $ git commit -m "Initial commit."

You'll need the [heroku toolbelt](https://toolbelt.herokuapp.com)
installed to manage the heroku side of your app. Once it's installed,
get the app created:

    $ heroku apps:create {{name}}
    Creating {{name}}... done, stack is cedar
    http://{{name}}.herokuapp.com/ | git@heroku.com:{{name}}.git
    Git remote heroku added

You can deploy the skeleton project immediately:

    $ git push heroku master
    Writing objects: 100% (13/13), 2.87 KiB, done.
    Total 13 (delta 0), reused 0 (delta 0)

    -----> Heroku receiving push
    -----> Clojure app detected
    -----> Installing Leiningen
           Downloading: leiningen-2.0.0-preview7-standalone.jar
    [...]
    -----> Launching... done, v3
           http://{{name}}.herokuapp.com deployed to Heroku

    To git@heroku.com:{{name}}.git
     * [new branch]      master -> master

It's live! Hit it with `curl`:

    $ curl http://{{name}}.herokuap.com
    ["Hello" :from Heroku]

The cookie-backed session store needs a session secret configured for encryption:

    $ heroku config:add SESSION_SECRET=$RANDOM_16_CHARS

## Remote REPL

The [devcenter article](https://devcenter.heroku.com/articles/debugging-clojure)
has a detailed explanation, but using the `repl` task from Leiningen
2.x lets you connect a REPL to a remote process over HTTP. The first
step is setting up credentials:

    $ heroku config:add REPL_USER=[...] REPL_PASSWORD=[...]

Then you can launch the REPL:

    $ lein repl :connect http://$REPL_USER:$REPL_PASSWORD@myapp.herokuapp.com/repl

Everything you enter will be evaluated remotely in the running dyno,
which can be very useful for debugging or inspecting live data.

## License

Copyright © {{year}} FIXME

Distributed under the Eclipse Public License, the same as Clojure.
