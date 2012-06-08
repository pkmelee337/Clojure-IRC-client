(ns IRC-Client.core
  (:use [seesaw.core])
  (:require [seesaw.selector :as selector])
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader)
           (panels SettingsFrame ChatWindow)))

(defn identify [root]
  (doseq [w (select root [:*])]
    (if-let [n (.getName w)]
      (selector/id-of! w (keyword n))))
  root)

(def defaults 
  { :Nickname "IRC-Client-Test"
    :Server "irc.freenode.net"
    :Port 6667
    :Channel "clojurehu"})
(def textbox (text :multi-line? true :border 20))
(def textboxoutput (text :multi-line? true :border 20 ))

(defn login-form []
  (let [form (identify (SettingsFrame.))]
    form))

(defn chat-form []
  (let [form (identify (ChatWindow.))]
    form))

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
    (.flush))
  (value! textboxoutput (str(value textboxoutput)\newline msg)))

(defn conn-handler [conn]
  (while (nil? (:exit @conn))
    (let [msg (.readLine (:in @conn))]
      (println msg)
      (value! textboxoutput (str(value textboxoutput)\newline msg))
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
    (let [form  (value! (login-form) defaults)
          result (-> (dialog :content form :option-type :ok-cancel) pack! show!)]
      (if (= :success result)
        (do
          (def irc(connect (get-in (value form )[ :Server]) (Integer/parseInt(get-in (value form )[ :Port]))))
          (login irc(get-in (value form )[:Nickname]))
          (write irc (str "join #" (get-in (value form )[ :Channel])))
          (let [form2  (value! (chat-form) defaults)
          result (-> (frame :content (vertical-panel
                                       :items [(scrollable textboxoutput)
                                               :separator
                                               textbox
                                               :separator
                                               (button :text "send" :listen [:action (fn[e] (do
                                                                                              (write irc (str "PRIVMSG #clojurehu :" (value textbox)))
                                                                                              (value! textbox "")))])
                                               (horizontal-panel
                                                 :border "Texxxtt"
                                                 :items (map (partial radio :text)
                                                             ["First" "Second" "Third"]))
                                               (scrollable (text :multi-line? true))]))
                   pack! show!)])
           )
        (println "User canceled")))))
