(ns leiningen.heroku.apps
  (:require [clojure.java.shell :as sh]
            [clojure.java.browse :as browse]
            [clojure.java.io :as io]
            [clojure.xml :as xml]
            [leiningen.heroku.util :as util]
            [doric.core :as doric])
  (:import (com.heroku.api.command.app AppList)
           (com.heroku.api Heroku$Stack)))

(defn- add-git-remote [name]
  (when (not (.exists (io/file ".git")))
    (sh/sh "git" "init"))
  (when (pos? (:exit (sh/sh "git" "remote" "show" "heroku")))
    (let [remote (format "git@heroku.com:%s.git" name)]
      (sh/sh "git" "remote" "add" "heroku" remote)
      remote)))

(defn apps:create
  "Create a new Heroku app."
  [name] ;; TODO: infer app name from project.clj
  (let [response (-> (util/api) (.newapp Heroku$Stack/Cedar name))]
    (println "Created app" (.getAppName response))
    (let [remote (add-git-remote name)]
      (println "Added git remote:" remote))))

(defn apps:delete
  "Delete the given app."
  []
  (let [name (util/current-app-name)]
    (println " !    WARNING: Potentially Destructive Action.")
    (println " !    This command will affect the app:" name)
    (println " !    To proceed, type" name)
    (print "> ")
    (flush)
    (when (= name (read-line))
      (.destroy (.app (util/api) name))
      (println "Deleted app" name))))

(defn apps:info
  "Show detailed app information."
  []
  (println "==" (util/current-app-name))
  (util/print-map (.info (util/app-api))))

(defn apps:open
  "Open the app in a web browser."
  []
  (-> (util/app-api)
      (.info)
      (.get "web_url")
      (browse/browse-url)))

;; TODO: this currently isn't exposed in JSON, so the underlying Java
;; API doesn't handle it. Replace this when it does.

(def ^{:private true} list-request
  (reify com.heroku.api.request.Request
    (getHttpMethod [this]
      com.heroku.api.http.Http$Method/GET)
    (getEndpoint [this]
      (.value com.heroku.api.Heroku$Resource/Apps))
    (hasBody [this]
      false)
    (getBody [this]
      (throw (com.heroku.api.http.HttpUtil/noBody)))
    (getResponseType [this]
      com.heroku.api.http.Http$Accept/XML)
    (getHeaders [this]
      {})
    (getResponse [this in code]
      (if (= 200 code)
        (java.io.ByteArrayInputStream. in)
        (throw (com.heroku.api.exception.RequestFailedException.
                "AppList Failed" code in))))))

(defn- list-apps []
  (for [{:keys [content]} (:content (xml/parse (util/execute list-request)))
        :let [app (reduce #(assoc %1 (:tag %2) (:content %2)) {} content)]]
    (zipmap (keys app) (map first (vals app)))))

(defn apps:list
  "List all your apps."
  []
  (println (doric/table [:name :owner] (list-apps))))

;; TODO: implement rename