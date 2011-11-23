(ns leiningen.heroku
  (:require [leiningen.help :as help]
            [leiningen.heroku.apps :as apps]
            [leiningen.heroku.config :as config]
            [leiningen.heroku.util :as util]
            [clojure.tools.cli :as cli]))

(try ; Leiningen 2.0 compatibility
  (use '[leiningen.core :only [abort]])
  (catch Exception _
    (use '[leiningen.main :only [abort]])))

(def ^{:private true} cli-options
  ["-a" "--app" "App to use if not in project dir."])

(defn ^{:no-project-needed true
        :subtasks [apps/apps:create apps/apps:destroy
                   apps/apps:info apps/apps:open apps/apps:list
                   config/config:add config/config:remove
                   config/config:list]}
  heroku
  "Manage Heroku apps.

Use \"lein new heroku MYAPP\" to generate a new project skeleton."
  [& args]
  (let [[opts [command & args] help] (cli/cli args cli-options)
        command-ns (str "leiningen.heroku." (first (.split command ":")))
        _ (require (symbol command-ns))
        subtask (ns-resolve (symbol command-ns) (symbol command))]
    (try
      (binding [util/*app* (:app opts)]
        (apply subtask args))
      (catch Exception e
        (abort (.getMessage e))))))