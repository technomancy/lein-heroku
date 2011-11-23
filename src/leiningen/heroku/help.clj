(ns leiningen.heroku.help
  (:require [leiningen.help :as help]))

(defn help
  ([] (help/help "heroku"))
  ([subtask]
     (let [ns (symbol (str "leiningen.heroku." (first (.split subtask ":"))))
           _ (require ns)
           task (ns-resolve ns (symbol subtask))
           arglists (help/get-arglists task)]
       (println (:doc (meta task)))
       (when-not (every? empty? arglists)
         (println (str "\nArguments: " (pr-str arglists)))))))