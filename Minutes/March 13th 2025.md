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
