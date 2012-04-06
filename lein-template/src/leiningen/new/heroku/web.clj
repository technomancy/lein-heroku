(ns {{name}}.web
  (:require [ring.adapter.jetty :as jetty]))

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body (pr-str ["Hello" :from 'Heroku])})

(defn -main []
  (let [port (Integer/parseInt (System/getenv "PORT"))]
    (jetty/run-jetty app {:port port})))

