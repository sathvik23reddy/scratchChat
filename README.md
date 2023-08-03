# scratchChat

Basic chat app written in Java<br>
Uses sockets (TCP) to establish connection<br>
CS Arch w/ Multiple clients<br>
<br>
Concept: Initial handshake on a generic port, register+assign personalised port, tear down and establish separate connection and launch necessary threads<br>
Pros: Most optimal usage of dedicated threads[Multithreading], ThreadSafe[ConcurrentHashMap], hassle-free [TCP+Server based Message forwarding]
