(ns leiningen.heroku.login
  (:require [leiningen.heroku.util :as util]
            [leiningen.heroku.keys :as keys]
            [clojure.java.io :as io]
            [clojure.java.shell :as sh])
  (:import (com.heroku.api.request.login BasicAuthLogin)
           (com.heroku.api.connection HttpClientConnection)))

(defn- read-password []
  (.readPassword (System/console) "%s" (into-array ["Password: "])))

(def ^{:private true} default-key-path
  (.getAbsolutePath (io/file (System/getProperty "user.home") ".ssh" "id_rsa")))

(defn- generate-key []
  (if (zero? (:exit (sh/sh "which" "ssh-keygen")))
    (if (util/prompt "Would you like to generate one? [Y/n] ")
      (let [{:keys [exit out err]} (sh/sh "ssh-keygen" "-t" "rsa"
                                          "-f" default-key-path)]
        (if (zero? exit)
          "id_rsa.pub"
          (println out "\n" err))))
    (println "Please generate one and upload it with the keys:add task.")))

(defn- find-or-generate-key []
  (if-let [key (first (keys/local-keys))]
    (if (util/prompt (format "Would you like to upload %s? [Y/n] " key))
      key)
    (generate-key)))

(defn login
  "Log in with your Heroku credentials."
  []
  (println "Enter your Heroku account credentials.")
  (print "Email: ") (flush)
  (let [email (read-line)
        password (String. (read-password))
        connection (HttpClientConnection. (BasicAuthLogin. email password))
        api-key (.getApiKey connection)]
    (with-open [w (io/writer (util/credentials-file))]
      (.write w email)
      (.write w "\n")
      (.write w api-key))
    (println "Saved API key locally."))
  (when (empty? (keys/remote-keys))
    (println "You appear to have no keys uploaded.")
    (when-let [key (find-or-generate-key)]
      (keys/keys:add key)
      (println (format "Added key %s." key)))))