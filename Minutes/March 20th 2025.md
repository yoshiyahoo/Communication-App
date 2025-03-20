# Time
1 hr 5 mins, 3:00pm - 4:05pm

# Attendance
- [x] Adam
- [ ] Anna (had an internship fair)
- [x] Jacob
- [x] Josiah
- [x] Kamari

# General Agreements
## Client
- Main thread is the GUI, Handling messages in worker thread
- Client composed of two classes, MessageHandeler and GUI

- We have two queues to hold messages to send to the server and messages recieved from the server

- All shared data between all classes is inside the client class
    - List of chat objects containing messages 

### When do we connect
- Once we hit the login button
 
### RequestHandeler Class
- Have queues for incoming and outgoing messages
- Methods to send and recieve data
- Different objects per request

### Message Class
- string of text you send to chat
- Time and other stuff

### GUI Class
- Load every message in the chat
- All GUI related state like design elements or border padding or things like that are in GUI

## Server
- One queue for sending messages, one queue for retrieving messages
- Different threads per client
- HashSet for active users

### Handling Logins
- Need more discussion on how we need to login

# Task Agreements 
- Adam: First Design iteration of UML Document
- Josiah: Format and add more use cases, add issues for design stuff
- Jacob: Help Adam with classes and do research
- Kamari: Help Adam with classes
- Anna: Help Josiah with Use Cases

# What Happened
- Discussed client design and broke it down into subsequent classes
- Discussed how a message should work
- Discussed how a server should handle clients messages, and requests
