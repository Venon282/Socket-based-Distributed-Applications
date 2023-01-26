# Socket-based-Distributed-Applications

Esteban 
Akram

## Open questions : 

2- If we want to use UDP for file download, we would have to implement our own mechanism to ensure all bytres are recieved in the correct order,
a way to do this would be by using a sliding window protocol to keep track of which bytes have been received, and retransmitting any missing or out of order bytes.


3- to adress the issue of determining the appropriate pool size for a multi-threaded server, we could use a dynamic pool size that can be adjusted depending on the current workload, in this case we could implement a system to monitor the server's performance and adjust the pool size accordingly.
It is also possible to use a hybrid approach that combines both fixed and dynamic pool sizes. A fixed pool size can handle small to medium loads but for large loads, a dynamic pool size can be used.


