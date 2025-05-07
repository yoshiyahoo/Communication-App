# Feb 25th

# Time
1 hr, 3:00 - 4:00pm

# Attendance
- [x] Adam
- [x] Anna
- [ ] Jacob, In the Philippines
- [x] Josiah
- [x] Kamari

# Agreements
Nothing binding

# What Happened
Worked on SRS
- External interface requirements
- Assumptions
- Dependencies
- Common requirements
- Scope
- Defined modules

# Feb 27th

# Time
1 hr 10 mins, 11:10am - 12:20pm 

# Attendance
- [x] Adam, 50 mins late, got stuck on 580
- [x] Anna
- [ ] Jacob, in the Philippines
- [x] Josiah
- [x] Kamari, 1 hr late, got stuck on 580

# Agreements
- Anna - Working on Basic Use Cases
- Kamari - Refining Requirements
- Adam - Basic Class Suggestions
- Josiah - Track Minutes, work on github stuff, format minutes into document
- Jacob - Refining Requirements

# What Happened

## SRS
### Functional Requirements 
- Added User Module
    - Added User requirements into user module
- Improved Overall requirements organization

### Non-Functional Requirements
- Improved Security, Environmental, and Performance Requirements
- Added Developer Requirements

# March 4th 

# Time
1 hr 10 mins, 3 - 4:10pm

# Attendance
- [x] Adam 
- [x] Anna
- [x] Jacob
- [x] Josiah
- [x] Kamari

# General Agreements
- Every design document lives inside the SRS
- Timeline (Gantt Chart) inside the SRS

# Task Agreements 
- Josiah: Submit All Documents, Work on Case Diagram (get this done first), Look at Developer constraints in SRS, Work on Gantt Chart, Write some stuff for marketing
- Anna: Work on Case Diagram (get this done first)
- Adam: Work on UML Class Diagram
- Jacob: Work on Sequence Diagram
- Kamari: Work on Sequence Diagram

# What Happened
## Class Concepts
- Reviewed basic class concepts
- Keep UML class diagram 
## SRS
### Purpose
- Refined the programming constraints
- Refined Definitions and Abbreviations
- Updated References to include sections of the document with UML diagrams
### Overall Description
- Discuseed Developer constraints & overall constraints 
- Fleshed out assumptions
### Specifc Requirements
#### Functional Requirements
- Reorganized Client/Server into Client and Server requirements
- Fleshed out client and server requirements

# March 6th

# Time
1 hr, 11:00am - 12:00am

# Attendance
- [x] Adam: 15 minutes late
- [x] Anna
- [x] Jacob: 25 minutes late
- [x] Josiah
- [x] Kamari: 40 minutes late

# General Agreements
Nothing binding
# Task Agreements 
Nothing binding
# What Happened
- Discussed Anna's Use Case Diagram
    - Changed DM's and group chats to just chats

- Refined internal interface requirements

- Refined presentation

# March 13th

# Time
1 hr, 3:00pm - 4:00pm

# Attendance
- [x] Adam
- [x] Anna
- [ ] Jacob
- [x] Josiah
- [x] Kamari

# General Agreements
nothing binding, just something specific here

# Task Agreements 
- Adam, Josiah: Add to existing classes, come up with some more details about these classes 
- Kamari, Anna: Create a FIGMA diagram for the client GUI
- Jacob: Move Use Cases to Design Document


# What Happened
## Discussed Login Page
- Client object
    - Create server connection at start of the program
    - Processes GUI inputs like entering username and password and clicking button
    - Sends the Login object to the server
    - Client recieves stuff back from server

- Login object that contains validation aspect stuff, implements serializable
```java
public class Login implements Serializeable {
    private String username;
    private String password;
}
```
- Server object
    - Load all possible accounts
    - Recieve client's login object
    - Load the text file as a string and gather
    - Send chat logs back if successful and an error
    - Error is the same type as a chat log message

## Discussed Socket Handling in Server
- Have a socket array containing sockets, each socket is an account, use a hashset

## Discussed Updating Chat
- Broke chat down into several objects
- Group chat class, contains list of chats as an attribute, chats contain history as an attribute
- Search box client side
- List of users as an object

## Discussed Client GUI
![](Client_GUI.png)

## Discussed User Account Search
- Grab all user data from the server 
- Update list when chat is updated

## Account Information
- Accounts can't be deleted
- Accounts 

# March 20th

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

# March 25th

# Time
1 hr 5 mins, 3:00pm - 4:05pm

# Attendance
- [x] Adam
- [x] Anna 
- [x] Jacob
- [x] Josiah
- [x] Kamari

# General Agreements
- Rename MsgHandler to RqstHandler
- Place Use Case Diagram in design document

- New users are created outside of the program!!!!

## Netowrking
- Pass the Message, Login, and Chat objects over the sockets!

## Client
- Contains a list of all current users
- Extra static class to handle data, implements runnable

## Server
- Needs ClientHandler static class to handle threads, implements runnable
- Admin gets EVERY SINGLE CHAT!
- Every update to the database is sent to admins

## Database
- Just a list of text files, each text file is a chat
- Message metadata is stored before the message
    - Metadata is time, name of user, and an untypeable delimiter
    - Message has an untypeable delimiter as well
 
- Include user login data as txt file

## GUI
- Add new pupup that creates new chats
- New chats have autogenerated names, can modify later
- Discuss how to split up GUI design wise

## Message Object
- Create second constructor that takes unparsed msg from database as a string
    - Parse inside the object
- Make a .toDatabase() to store it inside of database

# Task Agreements 
- Adam, Jacob: Work on UML Class Diagram
- Josiah, Anna: Finalize Use Cases
- Kamari: Finalize sequence diagrams
# What Happened
- Discussed GUI and what 
- Discussed Server needing a ClientHandler class
- Discussed database and messages

# March 27th
Talk about the GUI stuff

# April 8th
Talk about who does what. Build Gantt Chart
Make sure default constructors are disallowed if they need to be

# Time 
3:00 - 4:15pm

# Attendance
- [x] Adam
- [x] Anna
- [x] Jacob, Completing network lab, 45 minutes late 
- [x] Josiah
- [x] Kamari

# What Happened
Finished design
Refined use cases enough

# Tasks
Anna, use case 2 sequence diagram (Optional: add send chat logs in UML Case Diagram)
Josiah, use case 1 gantt chart
Kamari, use case 3 sequence diagram
Adam, use case 4 sequence diagram
Jacob, use case 5 sequence diagram

# April 17th

# Time 
3:00 - 4:15pm

# Attendance
- [x] Adam
- [x] Anna
- [x] Jacob, On Call 
- [x] Josiah
- [x] Kamari

# What Happened
Account: ensures that usernames are unique in account class
Message: add which chat this message belongs to as an attribute, getters & setters in there too
Server: how are we gonna save chats in the server?
# Tasks
Chat: remove default constructor.
Chat Database Schema File Format: Users.txt (ID,ROLE,NAME),
Chat Log: Filename = User list comma seperated. msg history (TIME,ID,MSG)
Message: Change the time to a DateTime object
Message: Remove the time and generate it in the constructor

UML: Change the message class

Enforce PRs

# April 22nd

# Time 
3:00 - 4:15pm

# Attendance
- [x] Adam
- [x] Anna
- [x] Jacob 
- [x] Josiah
- [x] Kamari

# What Happened
discussed about who is going to do what with database

# Tasks
all tasks were mentioned in the github issues
