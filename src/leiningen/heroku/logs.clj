(ns leiningen.heroku.logs
  (:require [leiningen.heroku.util :as util]
            [clojure.java.io :as io]))

(defn- pump [reader out]
  (let [buffer (make-array Character/TYPE 1000)]
    (loop [len (.read reader buffer)]
      (when-not (neg? len)
        (.write out buffer 0 len)
        (.flush out)
        (Thread/sleep 100)
        (recur (.read reader buffer))))))

(defn logs []
  (with-open [stream (-> (util/app-api) .getLogChunk .openStream)]
    (pump (io/reader stream) *out*)))

;; TODO: support tailing