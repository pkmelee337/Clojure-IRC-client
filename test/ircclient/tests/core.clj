(ns ircclient.tests.core
  (:use IRC-Client.core)
  (:use clojure.test)
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader)
           (panels SettingsFrame ChatWindow)))

(def conn1 (make-connection "irc.freenode.net" 6667))
(def conn2 (make-connection "irc.freenode.net" 6667))

(deftest speak-test
  (let [lastmessage (atom "")]
	  (do
	    (login! conn1 "Shenkie")
      (login! conn2 "Shenkie2")
	    (write conn1 "join #clojure-hu-privatetestchannel")
	    (write conn2 "join #clojure-hu-privatetestchannel")
	    (irc-speak! conn1 "#clojure-hu-privatetestchannel"  "testmessage!")
      (doseq [line (line-seq (:in @conn2))] 
         (println line)
         (reset! lastmessage line))
      (is (= @lastmessage "testmessage!") "message should be 'testmessage!'"))))

; 1 login:
;:kornbluth.freenode.net NOTICE * :*** Looking up your hostname...
;:kornbluth.freenode.net NOTICE * :*** Checking Ident
;:kornbluth.freenode.net NOTICE * :*** Couldn't look up your hostname
;:kornbluth.freenode.net NOTICE * :*** No Ident response
;:kornbluth.freenode.net 451 * :You have not registered
;ERROR :Closing Link: 127.0.0.1 (Connection timed out)

; 2 login:
;:wolfe.freenode.net NOTICE * :*** Looking up your hostname...
;:wolfe.freenode.net NOTICE * :*** Checking Ident
;:wolfe.freenode.net NOTICE * :*** Couldn't look up your hostname
;:wolfe.freenode.net NOTICE * :*** Looking up your hostname...
;:wolfe.freenode.net NOTICE * :*** Checking Ident
;:wolfe.freenode.net NOTICE * :*** No Ident response
;:wolfe.freenode.net NOTICE * :*** No Ident response
;:wolfe.freenode.net 002 Shenkie2 :Your host is wolfe.freenode.net[89.16.176.16/6667], running version ircd-seven-1.1.3
;:wolfe.freenode.net 005 Shenkie2 CHANTYPES=# EXCEPTS INVEX CHANMODES=eIbq,k,flj,CFLMPQcgimnprstz CHANLIMIT=#:120 PREFIX=(ov)@+ MAXLIST=bqeI:100 MODES=4 NETWORK=freenode KNOCK STATUSMSG=@+ CALLERID=g :are supported by this server
;:wolfe.freenode.net 254 Shenkie2 45100 :channels formed
;:wolfe.freenode.net 265 Shenkie2 4431 7408 :Current local users 4431, max 7408
;:wolfe.freenode.net 250 Shenkie2 :Highest connection count: 7409 (7408 clients) (2044507 connections received)
;:wolfe.freenode.net 372 Shenkie2 :- Welcome to wolfe.freenode.net in Manchester, England, Uk!  Thanks to
;:wolfe.freenode.net 372 Shenkie2 :- Bytemark Computer Consulting Ltd.  (www.bytemark.co.uk) for sponsoring
;:wolfe.freenode.net 372 Shenkie2 :- WOLFE, GENE [1931-].  Prolific writer of short stories and
;:wolfe.freenode.net 001 Shenkie :Welcome to the freenode Internet Relay Chat Network Shenkie
;:wolfe.freenode.net 002 Shenkie :Your host is wolfe.freenode.net[89.16.176.16/6667], running version ircd-seven-1.1.3
;:wolfe.freenode.net 003 Shenkie :This server was created Sun Nov 20 2011 at 13:26:13 GMT
;:wolfe.freenode.net 004 Shenkie wolfe.freenode.net ircd-seven-1.1.3 DOQRSZaghilopswz CFILMPQbcefgijklmnopqrstvz bkloveqjfI
;:wolfe.freenode.net 005 Shenkie CHANTYPES=# EXCEPTS INVEX CHANMODES=eIbq,k,flj,CFLMPQcgimnprstz CHANLIMIT=#:120 PREFIX=(ov)@+ MAXLIST=bqeI:100 MODES=4 NETWORK=freenode KNOCK STATUSMSG=@+ CALLERID=g :are supported by this server
;:wolfe.freenode.net 372 Shenkie2 :- network is prohibited. Public channel logging should only
;:wolfe.freenode.net 372 Shenkie2 :-  
;:wolfe.freenode.net 372 Shenkie2 :- ***************************************************************
;:wolfe.freenode.net 372 Shenkie2 :- Please read http://blog.freenode.net/2010/11/be-safe-out-there/
;:wolfe.freenode.net 005 Shenkie CASEMAPPING=rfc1459 CHARSET=ascii NICKLEN=16 CHANNELLEN=50 TOPICLEN=390 ETRACE CPRIVMSG CNOTICE DEAF=D MONITOR=100 FNC TARGMAX=NAMES:1,LIST:1,KICK:1,WHOIS:1,PRIVMSG:4,NOTICE:4,ACCEPT:,MONITOR: :are supported by this server
;:wolfe.freenode.net 372 Shenkie2 :- ***************************************************************
;:wolfe.freenode.net 005 Shenkie EXTBAN=$,arx WHOX CLIENTVER=3.0 SAFELIST ELIST=CTU :are supported by this server
;:wolfe.freenode.net 376 Shenkie2 :End of /MOTD command.
;:wolfe.freenode.net 251 Shenkie :There are 250 users and 68775 invisible on 28 servers
;:wolfe.freenode.net 252 Shenkie 38 :IRC Operators online
;:wolfe.freenode.net 253 Shenkie 13 :unknown connection(s)
;:wolfe.freenode.net 254 Shenkie 45100 :channels formed
;:wolfe.freenode.net 255 Shenkie :I have 4432 clients and 1 servers
;:wolfe.freenode.net 265 Shenkie 4432 7408 :Current local users 4432, max 7408
;:wolfe.freenode.net 266 Shenkie 69025 81621 :Current global users 69025, max 81621
;:wolfe.freenode.net 250 Shenkie :Highest connection count: 7409 (7408 clients) (2044508 connections received)
;:wolfe.freenode.net 375 Shenkie :- wolfe.freenode.net Message of the Day - 
;:wolfe.freenode.net 372 Shenkie :- Welcome to wolfe.freenode.net in Manchester, England, Uk!  Thanks to
;:wolfe.freenode.net 372 Shenkie :- Bytemark Computer Consulting Ltd.  (www.bytemark.co.uk) for sponsoring
;:wolfe.freenode.net 372 Shenkie :- this server!
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- WOLFE, GENE [1931-].  Prolific writer of short stories and
;:wolfe.freenode.net 372 Shenkie :- novels. His best-known work is the multi-volume novel The
;:wolfe.freenode.net 372 Shenkie :- Book of the New Sun. He has won multiple awards including
;:wolfe.freenode.net 372 Shenkie :- the Nebula Award, the World Fantasy Award, The Campell
;:wolfe.freenode.net 372 Shenkie :- Memorial Award and the Locus Award. He was awarded the World
;:wolfe.freenode.net 372 Shenkie :- Fantasy Award for lifetime achievement in 1996.
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- You're using freenode, a service of Peer-Directed Projects
;:wolfe.freenode.net 372 Shenkie :- Center Ltd (http://freenode.net/pdpc.shtml).
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- By connecting to freenode you indicate that you have read
;:wolfe.freenode.net 372 Shenkie :- and agree to adhere to our policies and procedures as per
;:wolfe.freenode.net 372 Shenkie :- the website (http://freenode.net). We would like to remind
;:wolfe.freenode.net 372 Shenkie :- you that unauthorized public logging of channels on the
;:wolfe.freenode.net 372 Shenkie :- network is prohibited. Public channel logging should only
;:wolfe.freenode.net 372 Shenkie :- take place where the channel owner(s) has requested this
;:wolfe.freenode.net 372 Shenkie :- and users of the channel are all made aware (if you are
;:wolfe.freenode.net 372 Shenkie :- publically logging your channel, you may wish to keep a
;:wolfe.freenode.net 372 Shenkie :- notice in topic and perhaps as a on-join message).
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- freenode runs an open proxy scanner. Your use of the network
;:wolfe.freenode.net 372 Shenkie :- indicates your acceptance of this policy. For details on
;:wolfe.freenode.net 372 Shenkie :- freenode network policy, please take a look at our policy
;:wolfe.freenode.net 372 Shenkie :- page (http://freenode.net/policy.shtml). Thank you for using
;:wolfe.freenode.net 372 Shenkie :- the network!
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- Don't forget to check out these other Peer-Directed Projects:
;:wolfe.freenode.net 372 Shenkie :- FOSSCON [http://www.fosscon.org] and fossevents 
;:wolfe.freenode.net 372 Shenkie :- [http://www.fossevents.org], and soon we'll repeat last years 
;:wolfe.freenode.net 372 Shenkie :- success with Picnics for Geeks across the globe, more info at 
;:wolfe.freenode.net 372 Shenkie :- [http://geeknic.org]
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- freenode is a service of Peer-Directed Projects Center Ltd,
;:wolfe.freenode.net 372 Shenkie :- a not for profit organisation registered in England and Wales.
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- If you support the work we do and wish to donate to the PDPC, 
;:wolfe.freenode.net 372 Shenkie :- you may do so over at http://freenode.net/pdpc_donations.shtml
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- Thank you for using freenode!
;:wolfe.freenode.net 372 Shenkie :-  
;:wolfe.freenode.net 372 Shenkie :- ***************************************************************
;:wolfe.freenode.net 372 Shenkie :- Please read http://blog.freenode.net/2010/11/be-safe-out-there/
;:wolfe.freenode.net 372 Shenkie :- ***************************************************************
;:wolfe.freenode.net 376 Shenkie :End of /MOTD command.
;:Shenkie MODE Shenkie :+i
;:Shenkie!~Shenkie@145.89.154.241 JOIN #clojure-hu-privatetestchannel
;:wolfe.freenode.net 353 Shenkie @ #clojure-hu-privatetestchannel :Shenkie @Shenkie3
;:wolfe.freenode.net 366 Shenkie #clojure-hu-privatetestchannel :End of /NAMES list.
;:Shenkie2!~Shenkie2@145.89.154.241 JOIN #clojure-hu-privatetestchannel
;:wolfe.freenode.net 366 Shenkie2 #clojure-hu-privatetestchannel :End of /NAMES list.

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