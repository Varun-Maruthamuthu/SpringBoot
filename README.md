Simple application using Spring boot.
=====================================

http://localhost:8080/counter-api/top/5
Headers : 
{"Content-Type":"application/json","Authorization":"Basic b3B0dXM6Y2FuZGlkYXRlcw=="}

http://localhost:8080/counter-api/search
Headers : 
{"Content-Type":"application/json","Authorization":"Basic b3B0dXM6Y2FuZGlkYXRlcw=="}
Body :
{ "searchText" : ["Duis","duis", "sed", "123", "lorem", "Lorem"] }

To Deploy in Docker manually:
============================

docker build -t counter-api .
docker images
docker run -d --name word-count -p 8080:8080 counter-api
docker ps
docker exec -it word-count /bin/sh
docker stop word-count


