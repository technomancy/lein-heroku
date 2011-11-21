(ns leiningen.heroku.apps
  (:require [leiningen.heroku :as heroku])
  (:import (com.heroku.api.command.app AppList)
           (com.heroku.api Heroku$Stack)))

(defn create [name]
  (-> (heroku/api) (.newapp Heroku$Stack/Cedar name)))

(defn apps:create [name]
  (let [response (create name)]
    (println "Created app" (.getAppName response))))

(defn apps:destroy [])

(defn- space-key [k longest-key]
  (apply str k ": " (repeat (- longest-key (count k)) " ")))

(defn apps:info []
  (let [api (heroku/app-api)
        info (.info api)
        longest-key (apply max (map count (keys info)))]
    (println "==" (.getAppName api))
    (doseq [[k v] info]
      (println (space-key k longest-key) v))))

(defn apps:open [])

(defn apps:rename [new-name])

(defn apps []
  (println "= Heroku Applications")
  (doseq [app (-> (heroku/api) .apps .getData)]
    (println "*" (.get app "name"))))
