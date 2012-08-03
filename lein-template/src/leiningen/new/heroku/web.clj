(ns {{name}}.web
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [ring.middleware.stacktrace :as trace]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.basic-authentication :as basic]
            [cemerick.drawbridge :as drawbridge]
            [environ.core :as env]))

(defroutes app
  (GET "/" []
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (pr-str ["Hello" :from 'Heroku])}))

(defn authenticated? [name pass]
  (= [name pass] [(System/getenv "AUTH_USER") (System/getenv "AUTH_PASS")]))

(defn wrap-drawbridge [handler]
  (fn [req]
    (let [handler (if (= "/repl" (:uri req))
                    (basic/wrap-basic-authentication
                     cemerick.drawbridge/ring-handler authenticated?)
                    handler)]
      (handler req))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env/env :port) 5000))]
    (jetty/run-jetty (-> app
                         (wrap-drawbridge)
                         ((if (env/env :dev)
                            trace/wrap-stacktrace
                            identity))
                         (site))
                     {:port port :join? false})))

;; For interactive development:
;; (.stop)
;; (def server (-main))
