(ns leiningen.heroku
  (:require [leiningen.help :as help]
            [leiningen.heroku.apps :as apps]
            [leiningen.heroku.config :as config]
            [leiningen.heroku.keys :as keys]
            [leiningen.heroku.login :as login]
            [leiningen.heroku.logs :as logs]
            [leiningen.heroku.ps :as ps]
            [leiningen.heroku.util :as util]
            [clojure.tools.cli :as cli]))

(try ; Leiningen 2.0 compatibility
  (use '[leiningen.core :only [abort]])
  (catch Exception _
    (use '[leiningen.core.main :only [abort]])))

(def ^{:private true} cli-options
  ["-a" "--app" "App to use if not in project dir."])

(defn- task-not-found [& _]
  (abort "That's not a task. Use \"lein heroku help\" to list all subtasks."))

;; like leiningen.core, but with special subtask handling for colon-grouping
(defn- resolve-task
  ([task not-found]
     (let [task-ns (symbol (str "leiningen.heroku." (first (.split task ":"))))
           task (symbol task)]
       (try
         (when-not (find-ns task-ns)
           (require task-ns))
         (or (ns-resolve task-ns task)
             not-found)
         (catch java.io.FileNotFoundException e
           not-found))))
  ([task] (resolve-task task #'task-not-found)))

;; TODO: re-jigger help so that every subtask doesn't need to be required here
(defn ^{:no-project-needed true :help-arglists '([command & args])
        :subtasks [#'apps/apps:create #'apps/apps:delete
                   #'apps/apps:info #'apps/apps:open #'apps/apps:list
                   #'config/config:add #'config/config:remove
                   #'config/config:list
                   #'keys/keys:add #'keys/keys:remove #'keys/keys:list
                   #'login/login #'logs/logs
                   #'ps/ps #'ps/ps:restart #'ps/ps:scale]}
  heroku
  "Manage Heroku apps.

To invoke a task on an app that is not in the current directory, use
the --app argument.

Use \"lein new heroku MYAPP\" to generate a new project skeleton."

  [& args]
  (let [[opts [command & args] help] (cli/cli args cli-options)
        subtask (resolve-task (or command "help"))]
    (try
      (binding [util/*app* (:app opts)]
        (apply subtask args))
      (catch Exception e
        (abort (or (.getMessage e) (pr e)))))))