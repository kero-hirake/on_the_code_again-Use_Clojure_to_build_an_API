(ns user-api.core
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja])
  (:gen-class))

(defonce server (atom nil))
(def users (atom {}))

" '/' "
" '/users'     全ユーザー取得"
" '/users/:id' ID指定でユーザー取得"
" '/uses' POST ユーザー追加"

(defn string-handler [_]
  {:status 200
   :body "hello!"})

(defn create-user [{user :body-params}]
  (let [id (str (java.util.UUID/randomUUID))
        users (->> (assoc user :id id)
                  (swap! users assoc id))]
    {:status 200
     :body (get users id)}))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["" string-handler]
     ["users" {:post create-user}]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn start-server [] 
  (when-not @server
    (reset! server (jetty/run-jetty #'app {:port 3000
                                              :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn reset-server []
  (when @server
    (stop-server)
    (start-server)))

(defn -main
  [& args]
  (println "Hello, World!"))
