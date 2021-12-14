(ns user-api.core
  (:require [ring.adapter.jetty :as jetty])
  (:gen-class))

(def server (atom nil))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn start-server [] 
  (when-not @server
    (reset! server  (jetty/run-jetty handler {:port 3000
                                              :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn -main
  [& args]

  (println "Hello, World!"))
