# Ghuftgu – Chat Rooms Backend

Spring Boot backend where users can create rooms and chat in real time.

## Features

- Create rooms with password.
- Join room using room code.
- View list of participants.
- Send and get messages with REST APIs.
- Real-time chat using WebSocket (STOMP).

## Tech Stack

- Java
- Spring Boot
- Spring WebSocket (STOMP + SockJS)
- Spring Data JPA
- postgresql database


Main REST Endpoints
POST /api/rooms – create room

GET /api/rooms/{roomCode} – get room details

POST /api/rooms/{roomCode}/join – join room

GET /api/rooms/{roomCode}/participants – get participants

POST /api/rooms/{roomCode}/messages – send message

GET /api/rooms/{roomCode}/messages – get messages

WebSocket
Connect: /ws

Send messages: /app/chat/{roomCode}

Subscribe for messages: /topic/room/{roomCode}
