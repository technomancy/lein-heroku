(ns leiningen.heroku.apps
  (:require [clojure.java.shell]
            [clojure.java.browse :as browse]
            [leiningen.heroku.util :as util])
  (:import (com.heroku.api.command.app AppList)
           (com.heroku.api Heroku$Stack)))

(defn apps:create
  "Create a new Heroku app."
  [name]
  (let [response (-> (util/api) (.newapp Heroku$Stack/Cedar name))]
    (println "Created app" (.getAppName response))))

(defn apps:delete
  "Delete the given app."
  []
  (.destroy (util/app-api))
  (println "Deleted app" (util/current-app-name)))

(defn apps:info
  "Show detailed app information."
  []
  (println "==" (util/current-app-name))
  (util/print-map (.info (util/app-api))))

(defn apps:open
  "Open the app in a web browser."
  []
  (-> (util/app-api)
      (.info)
      (.get "web_url")
      (browse/browse-url)))

;; TODO: implement rename

(defn apps:list
  "List all your apps."
  []
  (println "= Heroku Applications")
  (doseq [app (-> (util/api) .apps .getData)]
    (println "*" (.get app "name"))))
