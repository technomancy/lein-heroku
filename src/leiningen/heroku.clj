(ns leiningen.heroku
  (:require [leiningen.help :as help]
            [leiningen.heroku.apps :as apps]
            [leiningen.heroku.config :as config]))

(try ; Leiningen 2.0 compatibility
  (use '[leiningen.core :only [abort]])
  (catch Exception _
    (use '[leiningen.main :only [abort]])))

(defn ^{:no-project-needed true
        :subtasks [apps/apps:create apps/apps:destroy
                   apps/apps:info apps/apps:open apps/apps:list
                   config/config:add config/config:remove
                   config/config:list]}
  heroku
  "Manage Heroku apps.

Use \"lein new heroku MYAPP\" to generate a new project skeleton."
  [command & args]
  (let [command-ns (str "leiningen.heroku." (first (.split command ":")))
        _ (require (symbol command-ns))
        subtask (ns-resolve (symbol command-ns) (symbol command))]
    (try
      ;; TODO: help
      ;; TODO: accept --app flag when not in project dir
      (apply subtask args)
      (catch Exception e
        (abort (.getMessage e))))))