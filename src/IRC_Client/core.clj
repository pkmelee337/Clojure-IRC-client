(ns irc
   (:use [seesaw.core])
   (:require [seesaw.selector :as selector])
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader)))

(defn identify
  [root]
  (doseq [w (select root [:*])]
    (if-let [n (.getName w)]
      (selector/id-of! w (keyword n))))
  root)

(def defaults 
  { :Nickname "IRC-Client-Test"
    :Server "irc.freenode.net"
    :Port 6667
    :Channel "#clojurehu"})

(defn my-form 
  []
  (let [form (identify (IRC_Client.SettingsFrame.))]
    form))

(def freenode {:name "irc.freenode.net" :port 6667})
(def user {:name "Test Name" :nick "IRC-Client-HU"})

(declare conn-handler)

(defn connect [server port]
  (let [socket (Socket. (:name server) (:port server))
        in (BufferedReader. (InputStreamReader. (.getInputStream socket)))
        out (PrintWriter. (.getOutputStream socket))
        conn (ref {:in in :out out})]
    (doto (Thread. #(conn-handler conn)) (.start))
    conn))

(defn write [conn msg]
  (doto (:out @conn)
    (.println (str msg "\r"))
    (.flush)))

(defn conn-handler [conn]
  (while (nil? (:exit @conn))
    (let [msg (.readLine (:in @conn))]
      (println msg)
      (cond 
       (re-find #"^ERROR :Closing Link:" msg) 
       (dosync (alter conn merge {:exit true}))
       (re-find #"^PING" msg)
       (write conn (str "PONG "  (re-find #":.*" msg)))))))

(defn login [conn user]
  (write conn (str "NICK " (:nick user)))
  (write conn (str "USER " (:nick user) " 0 * :" (:name user))))

(defn -main [& args]
  (invoke-later
    (let [form  (value! (my-form) defaults)
          result (-> (dialog :content form :option-type :ok-cancel) pack! show!)]
      (if (= :success result)
        (println "User entered: " (get-in (value form )[ :Server]))
        (println "User canceled")))))
