(ns leiningen.heroku.apps
  (:require [clojure.java.shell]
            [clojure.java.browse :as browse]
            [leiningen.heroku :as heroku])
  (:import (com.heroku.api.command.app AppList)
           (com.heroku.api Heroku$Stack)))

(defn apps:create [name]
  (let [response (-> (heroku/api) (.newapp Heroku$Stack/Cedar name))]
    (println "Created app" (.getAppName response))))

(defn apps:destroy []
  (.destroy (heroku/app-api))
  (println "Deleted app" (heroku/current-app-name)))

(defn- space-key [k longest-key]
  (apply str k ": " (repeat (- longest-key (count k)) " ")))

(defn apps:info []
  (let [info (.info (heroku/app-api))
        longest-key (apply max (map count (keys info)))]
    (println "==" (heroku/current-app-name))
    (doseq [[k v] info]
      (println (space-key k longest-key) v))))

(defn apps:open []
  (-> (heroku/app-api)
      (.info)
      (.get "web_url")
      (browse/browse-url)))

;; TODO: implement rename

(defn apps []
  (println "= Heroku Applications")
  (doseq [app (-> (heroku/api) .apps .getData)]
    (println "*" (.get app "name"))))
