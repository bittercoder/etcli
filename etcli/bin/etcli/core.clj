(ns etcli.core 
  (:require [clj-http.client :as client]))

(defn -main
  "I don't do a whole lot."
  [& args]
  (println "Hello, World!"))

(def server "http://ettrial.catchsoftware.net")
(def username "Administrator")
(def password "password")

(defn api-action [method path & [opts]]
  (client/request 
    (merge {:basic-auth [username password]
            :method method
            :url (str server "/api/" path)
            :content-type :json
            :proxy-host "10.1.1.216" 
            :proxy-port 8888
            :accept :json
            :as :json} opts)))

(defn read-id [response] (:Id (:body response))) 

(defn read-first-id [response] (:Id (first (:Items (:body response)))))

(defn add-project [project-name]
  (read-id (api-action :post "projects" { :form-params { :Name project-name }})))

(defn add-user [{:keys [user-name first-name last-name password email]}]
  (read-id (api-action :post "users" { :form-params { :UserName user-name :FirstName first-name :LastName last-name :Email email :Password password }})))

(defn get-user-id-by-name [user-name]
  (read-first-id (api-action :get "users" { :query-params { "$filter" (str "UserName eq '" user-name "'")}})))

(defn get-project-id-by-name [project-name]
  (read-first-id (api-action :get "projects" { :query-params { "$filter" (str "Name eq '" project-name "'")}})))