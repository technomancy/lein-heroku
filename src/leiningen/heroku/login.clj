(ns leiningen.heroku.login
  (:require [leiningen.heroku.util :as util]
            [clojure.java.io :as io])
  (:import (com.heroku.api.request.login BasicAuthLogin)
           (com.heroku.api.connection HttpClientConnection)))

(defn- read-password []
  (.readPassword (System/console) "%s" (into-array ["Password: "])))

(defn login []
  (println "Enter your Heroku credentials.")
  (print "Email: ") (flush)
  (let [email (read-line)
        password (String. (read-password))
        connection (HttpClientConnection. (BasicAuthLogin. email password))
        api-key (.getApiKey connection)]
    (with-open [w (io/writer (util/credentials-file))]
      (.write w email)
      (.write w "\n")
      (.write w api-key))
    (println "Saved API key locally.")))