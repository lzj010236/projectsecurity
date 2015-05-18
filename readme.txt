X.509 certificates
What are the limitations of using self-signed certificates? 

Self-signed certificates require both the client and server to interact(server to send the certificates and client to accept and verify), while comparing to certificate-authorities-signed certificates, the client only needs to get the certificate from authorities. 
Also, it is relatively hard to deal with large number of clients, and difficult to maintain as every time the certificates is changed, it has to be sent to the all the communicating partners again. 


What are they useful for?
It is efficient when the server has small number of clients. 
Self-signed certificates are relatively easy to generate and customize, the server can change its key size, validity duration, etc.. 