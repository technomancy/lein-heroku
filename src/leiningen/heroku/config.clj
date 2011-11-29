(ns leiningen.heroku.config
  (:require [leiningen.heroku.util :as util])
  (:import (com.google.gson Gson)))

(defn config:add
  "Add one or more config vars in KEY1=VAL1 format."
  [& configs]
  (->> configs
       (map #(vec (.split % "=")))
       (into {})
       (.toJson (Gson.))
       (.addConfig (util/app-api))))

(defn config:remove
  "Remove one or more config vars."
  [& keys]
  (let [api (util/app-api)]
    (doseq [k keys]
      (.removeConfig api))))

(defn config:list
  "Display the config vars for an app."
  []
  (doseq [[k v] (.listConfig (util/app-api))]
    (println (format "%s=%s" k v))))