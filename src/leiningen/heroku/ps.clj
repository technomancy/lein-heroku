(ns leiningen.heroku.ps
  (:require [leiningen.heroku.util :as util]
            [clojure.walk :as walk]
            [doric.core :as doric]))

(defn list-processes [app]
  (-> (second (util/get-credentials))
      (com.heroku.api.connection.HttpClientConnection.)
      (.execute (com.heroku.api.request.ps.ProcessList. app))
      (.getData)))

(defn state-for [state elapsed]
  (if (< elapsed (* 60 60))
    (str state " for "(int (/ elapsed (* 60))) " minutes")
    (str state " for "(int (/ elapsed (* 60 60))) " hours")))

(defn parse [process]
  (let [{:keys [process state elapsed command]} (walk/keywordize-keys
                                                 (into {} process))]
    {:process process
     :state (state-for state (Integer. elapsed))
     :command command}))

(defn ps
  "List processess for an app."
  []
  (println (doric/table [:process :state :command]
                        (map parse (list-processes (util/current-app-name))))))

(defn ps:restart [])

(defn ps:scale [])