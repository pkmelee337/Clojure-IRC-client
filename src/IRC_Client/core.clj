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
    :Channel "#clojurehu"})
(def textbox (text :multi-line? false :border 5 :margin 0))
(def textboxoutput (text :multi-line? true :border 5 :margin 0 ))

(defn login-form []
  (let [form (identify (SettingsFrame.))]
    form))

(defn chat-form []
  (let [form (identify (ChatWindow.))]
    form))

(declare conn-handler)

(defn make-connection[server port]
  (let [socket (Socket. server port)
        in (BufferedReader. (InputStreamReader. (.getInputStream socket)))
        out (PrintWriter. (.getOutputStream socket))]
    (ref {:in in :out out})))

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

(defn login! [conn user]
  (doto (Thread. #(conn-handler conn)) (.start))
  (write conn (str "NICK " user))
  (write conn (str "USER " user " 0 * :" user)))

  (defn irc-speak! [conn channel message]
    (write conn (str "PRIVMSG " channel " :" message)))
  
  (defn join! [conn channel]
    (write conn (str "join " channel))
    (let [form  (->(frame :content (vertical-panel
                                       :items [(scrollable textboxoutput)
                                               :separator
                                               textbox
                                               :separator
                                               (button :text "send" :listen [:action (fn[e] (do
                                                                                              (irc-speak! conn "#clojurehu" (value textbox))
                                                                                              (value! textbox "")))])
                                               ]) :width 800 :height 600) show!)
                    ]))
    
  
                             

(defn -main [& args]
  (invoke-later
    (let [form  (value! (login-form) defaults)
          result (-> (dialog :content form :option-type :ok-cancel) pack! show!)]
      (if (= :success result)
        (do
          (def irc(make-connection (get-in (value form )[:Server]) (Integer/parseInt(get-in (value form )[ :Port]))))
          (login! irc(get-in (value form )[:Nickname]))
          (join! irc (get-in (value form )[:Channel]))           )
        (println "User canceled")))))
