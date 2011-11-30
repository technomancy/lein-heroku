(ns leiningen.heroku.keys
  (:require [leiningen.heroku.util :as util]
            [clojure.java.io :as io]
            [clojure.string :as string]))

(defn local-keys []
  (filter #(.endsWith % ".pub")
          (.list (io/file (System/getProperty "user.home") ".ssh"))))

(defn- read-key [key]
  (if (.startsWith key "/")
    (slurp key)
    (slurp (io/file (System/getProperty "user.home") ".ssh" key))))

(defn remote-keys []
  ;; TODO: will this be exposed on HerokuAPI object?
  (-> (util/get-credentials)
      (second)
      (com.heroku.api.connection.HttpClientConnection.)
      (.execute (com.heroku.api.request.key.KeyList.))
      (.getData)))

(defn- key-comment [key]
  (let [contents (try (read-key key)
                      (catch java.io.FileNotFoundException _
                        key))]
    (string/trim (last (.split contents " ")))))

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
     (doseq [key (local-keys)]
       (when (util/prompt (format "Found key %s. Add? [Y/n] " key))
         (keys:add key)))))

(defn keys:remove
  "Remove the given SSH key or keys from your account. If no keys are given,
will list all uploaded keys and prompt for removal of each."
  ([key & keys]
     (doseq [key (cons key keys)]
       (keys:remove key)))
  ([key]
     (println (format "Removing %s." key))
     (try
       (.removeKey (util/api) (key-comment key))
       (catch Exception e
         (println (or (.getMessage e) e)))))
  ([]
     (doseq [key (remote-keys)
             :let [comment (key-comment (get key "contents"))]]
       (when (util/prompt (format "Found key %s. Remove? [Y/n] " comment))
         (keys:remove comment)))))

(defn keys:list
  "Display all keys for the current user."
  []
  (let [keys (remote-keys)
        [email] (util/get-credentials)
        keys-or-key (if (> (count keys) 1) "keys" "key")]
    (println "===" (count keys) keys-or-key "for" email)
    (doseq [key (remote-keys)
            :let [[type key comment] (.split (get key "contents") " ")
                  summary (str (apply str (take 10 key)) "..."
                               (apply str (drop (- (count key) 10) key)))]]
      (println (format "%s %s %s"
                       type summary comment)))))
