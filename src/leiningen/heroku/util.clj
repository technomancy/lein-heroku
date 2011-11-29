(ns leiningen.heroku.util
  (:require [clojure.java.io :as io])
  (:import (com.heroku.api.command.login BasicAuthLogin)
           (com.heroku.api.connection HttpClientConnection)
           (com.heroku.api HerokuAPI)))

(def ^{:dynamic true} *app* nil)

(defn- space-key [k longest-key]
  (apply str k ":" (repeat (- longest-key (count k)) " ")))

(defn print-map [m]
  (let [longest-key (apply max (map count (keys m)))]
    (doseq [[k v] m]
      (println (space-key k longest-key) v))))

(defn credentials-file []
  (io/file (System/getProperty "user.home") ".heroku" "credentials"))

(defn- get-credentials []
  (-> (credentials-file)
      (slurp)
      (.split "\n")))

(defn api []
  ;; (when-not (.exists (credentials-file))
  ;;   (login))
  (let [[_ key] (get-credentials)]
    (HerokuAPI/with (HttpClientConnection. key))))

(defn current-app-name []
  (or *app*
      (->> (io/file ".git/config")
           slurp
           (re-find #"git@heroku.com:(.+).git")
           second)))

(defn app-api []
  (.app (api) (current-app-name)))
