(ns IRC-Client.core
  (:use IRC-Client.core)
  (:use clojure.test)
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader)
           (panels SettingsFrame ChatWindow)))

(deftest frames-test
  (is (= (identify (SettingsFrame.)) (SettingsFrame.)) "identify should return the same frame")
  (is (= (identify (ChatWindow.)) (ChatWindow.)) "identify should return the same frame")
  (is (= (login-form) (SettingsFrame.)) "login-form should return a SettingsFrame")
  (is (= (chat-form) (ChatWindow.)) "login-form should return a ChatWindow"))

;(deftest connect-test
;  (is (= (connect "server" "port") true) "server should be connected"))