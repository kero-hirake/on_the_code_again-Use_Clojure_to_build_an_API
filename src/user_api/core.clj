(ns user-api.core
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja])
  (:gen-class))

(defonce server (atom nil))

" '/' "
" '/users' "
" '/users/:id' "
" '/uses' POST "

(defn string-handler [_]
  {:status 200
   :body "hello!"})

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["" string-handler]]
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
