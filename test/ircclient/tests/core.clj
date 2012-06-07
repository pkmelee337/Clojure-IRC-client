(ns ircclient.tests.core
  (:use IRC-Client.core)
  (:use clojure.test)
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader)
           (panels SettingsFrame ChatWindow)))

; frames not testable, returns frame
;(deftest frames-test
;  (is (= (identify (SettingsFrame.)) (SettingsFrame.)) "identify should return the same frame")
;  (is (= (identify (ChatWindow.)) (ChatWindow.)) "identify should return the same frame")
;  (is (= (login-form) (SettingsFrame.)) "login-form should return a SettingsFrame")
;  (is (= (chat-form) (ChatWindow.)) "login-form should return a ChatWindow"))

; connect not testable, returns BufferedReader
;(deftest connect-test
;  (is (= (connect "irc.freenode.net" 6667) true) "server should be connected"))

; login not testable, returns PrintWriter
;(deftest login-test
;  (do
;    (def test-irc (connect "irc.freenode.net" 6667))
;    (is (= (login test-irc "IRC-Client-Test") true) "user should be logged in")))

; conn-handler not testable, returns PrintWriter
;(deftest conn-handler-test
;  (do
;    (def test-irc (connect "irc.freenode.net" 6667))
;    (is (= (conn-handler test-irc) true) "Connection should be handled")))