# toren-server
An experimental mmo server in Java. There is very little documentation as this was simply for the learning experience.

This server uses apache MINA core for networking. It takes inputs/commands from clients and simulates physics server side. The clients also do their own simulation for client prediction. 

It also uses MySQL as a database. I have not included a .sql for set up so if you really want to run this, you'll have to make your own database/tables based on the code in TorenClient.java

## Problems
I had issues with clients getting out of sync and network congestion or thread locking. There were no problems until I tried to run the server over WAN rather than just my local network. This is because of variant latency. I learned that sending commands to clients rather than position updates is a terrible idea and isn't worth the saved bandwidth.

For clients to remain in sync, the server must send the result of each command to the clients rather than trusting each client to derive the same result. The client should only be calculating the result of it's own state and sending it to the server for reconciliation.
