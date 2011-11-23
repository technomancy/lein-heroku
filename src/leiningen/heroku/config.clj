(ns leiningen.heroku.config
  (:require [leiningen.heroku.util :as util])
  (:import (com.google.gson Gson)))

(defn config:add [configs]
  (->> configs
       (map #(vec (.split % "=")))
       (into {})
       (.toJson (Gson.))
       (.addConfig (util/app-api))))

(defn config:remove [& keys]
  (let [api (util/app-api)]
    (doseq [k keys]
      (.removeConfig api))))

(defn config:list []
  (util/print-map (.listConfig (util/app-api))))