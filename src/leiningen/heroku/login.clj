(ns leiningen.heroku.login
  (:require [leiningen.heroku.util :as util]
            [clojure.java.io :as io])
  (:import (com.heroku.api.command.login BasicAuthLogin)))

(defn- read-password []
  (.readPassword (System/console) "%s" (into-array ["Password: "])))

(defn login []
  (println "Enter your Heroku credentials.")
  (print "Email: ") (flush)
  (let [email (read-line)
        password (String. (read-password))
        command (BasicAuthLogin. email password)
        response nil ;; API key is currently not exposed by underlying Java API
        api-key (.api_key response)]
    (with-open [w (io/writer (util/credentials-file))]
      (.write email)
      (.write "\n")
      (.write api-key))))