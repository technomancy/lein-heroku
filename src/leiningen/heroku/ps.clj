(ns leiningen.heroku.ps
  (:require [leiningen.heroku.util :as util]
            [clojure.walk :as walk]
            [doric.core :as doric])
  (:import (com.heroku.api.request.ps ProcessList Restart Scale)))

(defn- state-for [state elapsed]
  (if (< elapsed (* 60 60))
    (str state " for "(int (/ elapsed (* 60))) " minutes")
    (str state " for "(int (/ elapsed (* 60 60))) " hours")))

(defn- parse [process]
  (let [{:keys [process state elapsed command]} (walk/keywordize-keys
                                                 (into {} process))]
    {:process process
     :state (state-for state (Integer. elapsed))
     :command command}))

(defn- list-processes [app]
  (.getData (util/execute (ProcessList. app))))

(defn ps
  "List processess for an app."
  []
  (println (doric/table [:process :state :command]
                        (map parse (list-processes (util/current-app-name))))))

(defn ps:restart
  "Restart all processess for an app.

TODO: implement restarting only specified processes."
  ([process]
     (util/execute (Restart. (util/current-app-name) process)))
  ([process & processes]
     (doseq [process (cons process processes)]
       (ps:restart process)))
  ([]
     (print "Restarting processes... ")
     (flush)
     (util/execute (Restart. (util/current-app-name)))
     (println "done.")))

(defn ps:scale
  "scale processes by the given amount"
  ([type-and-quantity]
     (let [app (util/current-app-name)
           [type quantity] (.split type-and-quantity "=")]
       (print "Scaling" type "processes... ")
       (flush)
       (let [response (util/execute (Scale. app type (Integer. quantity)))]
         (println (format "done, now running %s." quantity)))))
  ([type-and-quantity & more]
     (doseq [type-and-quantity (cons type-and-quantity more)]
       (ps:scale type-and-quantity))))