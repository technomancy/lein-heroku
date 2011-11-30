(ns leiningen.heroku.keys
  (:require [leiningen.heroku.util :as util]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn- discover-keys []
  (filter #(.endsWith % ".pub")
          (.list (io/file (System/getProperty "user.home") ".ssh"))))

(defn- read-key [key]
  (if (.startsWith key "/")
    (slurp key)
    (slurp (io/file (System/getProperty "user.home") ".ssh" key))))

(defn keys:add
  "Add the given SSH key or keys to your account. If no keys are given,
will search in $HOME/.ssh and prompt for each."
  ([key & keys]
     (doseq [key (cons key keys)]
       (keys:add key)))
  ([key]
     (try
       (.addKey (util/api) (read-key key))
       (catch Exception e
         (println (or (.getMessage e) e)))))
  ([]
     (doseq [key (discover-keys)]
       (when (util/prompt-for (format "Found key %s. Add? [Y/n] " key))
         (keys:add key)))))

(defn keys:remove
  "Remove the given SSH key or keys from your account. If no keys are given,
will search in $HOME/.ssh and prompt for each."
  ([key & keys]
     (doseq [key (cons key keys)]
       (keys:remove key)))
  ([key]
     (println (format "Removing %s." key))
     (try
       (.removeKey (util/api) (string/trim (last (.split (read-key key) " "))))
       (catch Exception e
         (println (or (.getMessage e) e)))))
  ([]
     (when (util/prompt-for "Remove all keys? ")
       ;; TODO: should really list keys from API, not locally.
       (doseq [key (discover-keys)]
         (keys:remove key)))))

(defn keys:list
  "Display keys for the current user."
  []
  ;; TODO: will this be exposed on HerokuAPI?
  (let [[email key] (util/get-credentials)
        connection (com.heroku.api.connection.HttpClientConnection. key)
        response (.execute connection (com.heroku.api.request.key.KeyList.))]
    (println "=== Keys for" email)
    (doseq [key (.getData response)
            :let [[type key comment] (.split (get key "contents") " ")
                  summary (str (apply str (take 10 key)) "..."
                               (apply str (drop (- (count key) 10) key)))]]
      (println (format "%s %s %s"
                       type summary comment)))))