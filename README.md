POC for Web sockets:

Router Sequence diagram:
![ItemRouter_routes](https://user-images.githubusercontent.com/2307617/147381169-c02968a0-0a18-4897-9843-502502ac56ee.png)

Websocket handler Sequence diagram:
![WebSocketConfiguration_webSocketHandler](https://user-images.githubusercontent.com/2307617/147381348-92b8f2d4-8f3c-48e6-a3c7-87a711626d2a.png)


Sample curl for adding items:
curl -H "content-type: application/json" -d '{"name":"milk2", "price": 2.2}' http://localhost:8080/items
