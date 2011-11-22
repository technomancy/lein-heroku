(ns leiningen.heroku.config
  (:require [leiningen.heroku :as heroku])
  (:import (com.google.gson Gson)))

(defn config:add [configs]
  (->> configs
       (map #(vec (.split % "=")))
       (into {})
       (.toJson (Gson.))
       (.addConfig (heroku/app-api))))

(defn config:remove [& keys]
  (let [api (heroku/app-api)]
    (doseq [k keys]
      (.removeConfig api))))

(defn config []
  (heroku/print-map (.listConfig (heroku/app-api))))