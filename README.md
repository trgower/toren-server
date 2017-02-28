# toren-server
An experimental mmo server in Java. There is very little documentation as this was simply for the learning experience.

This server uses apache MINA core for networking. It takes inputs/commands from clients and simulates physics server side. The clients also do their own simulation for client prediction. 

It also uses MySQL as a database. I have not included a .sql for set up so if you really want to run this, you'll have to make your own database/tables based on the code in TorenClient.java

## Problems
I had issues with clients getting out of sync and network congestion or thread locking. There were no problems until I tried to run the server over WAN rather than just my local network. I'm inclined to place all of the blame on TCP but I know these problems could be sorted out with a better lockstep system.
