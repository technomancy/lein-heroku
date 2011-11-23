(ns leiningen.heroku.apps
  (:require [clojure.java.shell]
            [clojure.java.browse :as browse]
            [leiningen.heroku.util :as util])
  (:import (com.heroku.api.command.app AppList)
           (com.heroku.api Heroku$Stack)))

(defn apps:create [name]
  (let [response (-> (util/api) (.newapp Heroku$Stack/Cedar name))]
    (println "Created app" (.getAppName response))))

(defn apps:destroy []
  (.destroy (util/app-api))
  (println "Deleted app" (util/current-app-name)))

(defn apps:info []
  (println "==" (util/current-app-name))
  (util/print-map (.info (util/app-api))))

(defn apps:open []
  (-> (util/app-api)
      (.info)
      (.get "web_url")
      (browse/browse-url)))

;; TODO: implement rename

(defn apps []
  (println "= Heroku Applications")
  (doseq [app (-> (util/api) .apps .getData)]
    (println "*" (.get app "name"))))
