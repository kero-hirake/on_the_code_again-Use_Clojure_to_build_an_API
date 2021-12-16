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
" '/users/:id DELETE ユーザー削除"
" '/users/:id PUT 更新"

(defn string-handler [_]
  {:status 200
   :body "hello!"})

(defn create-user [{user :body-params}]
  (let [id (str (java.util.UUID/randomUUID))
        users (->> (assoc user :id id)
                  (swap! users assoc id))]
    {:status 201
     :body (get users id)}))

(defn get-users [_]
  {:status 200
   :body @users})

(defn get-user-by-id [{{:keys [id]} :path-params}]
  {:status 200
   :body (get @users id)})

(defn update-user [{{:keys [id]} :path-params user :body-params}]
  (swap! users id user)
  {:status 200
   :body  "updated"}
  )

(defn delete-user [{{:keys [id]} :path-params}]
  (swap! users dissoc id)
  {:status 204
   :body  ""}
  )

(comment
  (clojure.pprint/pprint @users)
  (swap! users dissoc "96d211ae-5728-4481-8b6e-b0af5fc0b96c")
  ;(swap! users )
  )

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["" string-handler]
     ["users" {:get get-users
               :post create-user}]
     ["users/:id" {:get get-user-by-id
                   :put update-user
                   :delete delete-user}]]
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
