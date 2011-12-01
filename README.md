# lein-heroku

A Leiningen plugin for managing your Heroku apps.

## Usage

    $ lein plugin install lein-heroku 0.1.0-SNAPSHOT

    $ lein heroku help
    Manage Heroku apps.

    To invoke a task on an app that is not in the current directory, use
    the --app argument.

    Use "lein new heroku MYAPP" to generate a new project skeleton.

    Subtasks available:
    apps:create     Create a new Heroku app.
    apps:delete     Delete the given app.
    apps:info       Show detailed app information.
    apps:list       List all your apps.
    apps:open       Open the app in a web browser.
    config:add      Add one or more config vars in KEY1=VAL1 format.
    config:list     Display the config vars for an app.
    config:remove   Remove one or more config vars.
    keys:add        Add the given SSH key or keys to your account.
    keys:list       Display all keys for the current user.
    keys:remove     Remove the given SSH key or keys from your account.
    login           Log in with your Heroku account credentials.
    logs            Display recent log output.
    ps              List processess for an app.
    ps:restart      Restart all processess for an app.
    ps:scale        Scale processes by the given amount.

    Arguments: ([command & args])

## License

Copyright © 2011 Heroku Inc.

Distributed under the Eclipse Public License, the same as Clojure.
