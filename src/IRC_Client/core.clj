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
    :Channel "clojurehu"})

(defn my-form []
  (let [form (identify (IRC_Client.SettingsFrame.))]
    form))

(defn my-form2 []
  (let [form (identify (IRC_Client.ChatWindow.))]
    form))

(def freenode {:name "irc.freenode.net" :port 6667})
(def user {:name "Test Name" :nick "IRC-Client-HU"})

(declare conn-handler)

(defn connect [server port]
  (let [socket (Socket. server port)
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
  (write conn (str "NICK " user))
  (write conn (str "USER " user " 0 * :" user)))

(defn -main [& args]
  (invoke-later
    (let [form  (value! (my-form) defaults)
          result (-> (dialog :content form :option-type :ok-cancel) pack! show!)]
      (if (= :success result)
        (do
          (def irc(connect (get-in (value form )[ :Server]) (Integer/parseInt(get-in (value form )[ :Port]))))
          (login irc(get-in (value form )[:Nickname]))
          (write irc (str "join #" (get-in (value form )[ :Channel])))
          [form  (value! (my-form2) defaults)
          result (-> (dialog :content form :option-type :ok-cancel) pack! show!)])
        (println "User canceled")))))
