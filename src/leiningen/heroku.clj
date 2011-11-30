(ns leiningen.heroku
  (:require [leiningen.help :as help]
            [leiningen.heroku.apps :as apps]
            [leiningen.heroku.config :as config]
            [leiningen.heroku.keys :as keys]
            [leiningen.heroku.login :as login]
            [leiningen.heroku.util :as util]
            [clojure.tools.cli :as cli]))

(try ; Leiningen 2.0 compatibility
  (use '[leiningen.core :only [abort]])
  (catch Exception _
    (use '[leiningen.main :only [abort]])))

(def ^{:private true} cli-options
  ["-a" "--app" "App to use if not in project dir."])

(defn ^{:no-project-needed true :help-arglists '([command & args])
        :subtasks [apps/apps:create apps/apps:delete
                   apps/apps:info apps/apps:open apps/apps:list
                   config/config:add config/config:remove
                   config/config:list login/login
                   keys/keys:add keys/keys:remove keys/keys:list]}
  heroku
  "Manage Heroku apps.

Use \"lein new heroku MYAPP\" to generate a new project skeleton. To
use an app that is not in the current directory, use the --app argument.
You can get help for each individual subtask with \"lein heroku help SUBTASK\"."
  [& args]
  (let [[opts [command & args] help] (cli/cli args cli-options)
        command (or command "help")
        command-ns (str "leiningen.heroku." (first (.split command ":")))
        _ (require (symbol command-ns))
        subtask (ns-resolve (symbol command-ns) (symbol command))]
    (try
      (binding [util/*app* (:app opts)]
        (apply subtask args))
      (catch Exception e
        (abort (.getMessage e))))))