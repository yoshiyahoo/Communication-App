import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	// NOTE client gets added inside bg handler after login success
    // The string is the account username
    private static ConcurrentHashMap<String, BackgroundHandlerServer> clients;
    private static Database data;
    private static ServerSocket server;
    private static RqstStore requestStore;
    private static final int PORT = 42069;

    /**
     * Init server
     * start listening for clients
     * on host IP, port 42069
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	initialStartUp();
    	while(true) {
    		Socket client = server.accept();
            // Start the client thread
    		Thread clientThread = new Thread(new BackgroundHandlerServer(client));
    		clientThread.start();
    	}
    }

    private static void initialStartUp() throws IOException {
        // 1. Open a server socket on a specific port
        // create ServerSocket listeningSocket on PORT
        server = new ServerSocket(PORT);
        server.setReuseAddress(true);

        // 2. Print startup message
        // print "Server started on port " + PORT
        System.out.println("Server started on IP: " + getHostAddr() + 
        		", Port: " + PORT);

        // 3. (Optional) Initialize data structures for clients
        // create List or Map to track connected clients
        clients = new ConcurrentHashMap<>();
        data = new Database();
        requestStore = new RqstStore();

        // 4. (Optional) Set up a thread pool (for handling clients)
        // initialize ExecutorService or thread pool

        // 5. (Optional) Load server config or resources
        // load config file if any

        // Start the RqstHandler thread
        new Thread(new RqstHandler()).start();
    }
    
    /**
     * Return the host machines ip address. Should only be used on init
     * 
     * @return the IPv4 address of the system running server
     * @throws SocketException
     */
    private static String getHostAddr() throws SocketException {
    	Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    	while (interfaces.hasMoreElements()) {
    	    NetworkInterface ni = interfaces.nextElement();
    	    if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) continue;

            boolean isWifi = ni.getDisplayName().contains("Wi-Fi");
    	    Enumeration<InetAddress> addresses = ni.getInetAddresses();
    	    while (addresses.hasMoreElements()) {
    	        InetAddress addr = addresses.nextElement();
    	        if (addr instanceof Inet4Address && !addr.isLoopbackAddress() && isWifi) {
    	        	return addr.getHostAddress();
    	        }
    	    }
    	}
    	return null;
    }

    /**
     * Compare a login obj with login info on the database 
     * 
     * @param login Login object with username and password that needs verification
     * @return True if login is in database, False otherwise
     */
    protected static Login checkLoginStatus(Login login) {
    	// get account info from db
    	Account acct = data.getAccount(login.getUsername());
        if (acct == null || !login.getPassword().equals(acct.getPassword()))
            return new Login(LoginType.FAILURE);
        else if (clients.containsKey(login.getUsername()))
            return new Login(LoginType.LOGGED_IN);
        else
            return new Login(LoginType.SUCCESS);
    }

    /**
     * Send message history of a chat to client as a Message[]
     * 
     * @param userName name of recipient
     * @param chatName Desired chat
     * @throws IOException 
     */
    protected void sendMsgHistory(String userName, String chatName) throws IOException {
    	Message[] toSend = data.getChat(chatName).getMsgHistory();
    	
    	ObjectOutputStream clientStream = clients.get(userName).out;
    	clientStream.writeObject(toSend);
    }
    
    /**
     * Send the user list of a chat to client as a String[]
     * 
     * @param userName recipient of the userList 
     * @throws IOException 
     */
     protected void sendAllUsers(String userName) throws IOException {
    	String[] userNames = data.getAccounts().stream()
    			.map(acct -> acct.getName())
    			.toArray(String[]::new);
    	
    	ObjectOutputStream clientStream = clients.get(userName).out;
    	clientStream.writeObject(userNames);
    }
    
    /**
     * Send a chat to client as a Chat obj
     * 
     * @param userName name of recipient
     * @param chatName Desired chat
     * @throws IOException 
     */
    protected void sendChat(String userName, String chatName) throws IOException {
    	Chat toSend = data.getChat(chatName);
    	
    	ObjectOutputStream clientStream = clients.get(userName).out;
    	clientStream.writeObject(toSend);
    }

    /**
     * Send a message obj to client with userName
     * 
     * @param userName recipient of message
     * @param msg message to send
     * @throws IOException
     */
    protected void sendMsg(String userName, Message msg) throws IOException {
    	ObjectOutputStream clientStream = clients.get(userName).out;
    	clientStream.writeObject(msg);
    }
    
    /**
     * Push a message to the store
     * @param msg message to queue
     */
    protected void pushStore(Message msg) throws ClassNotFoundException {
    	try {
            requestStore.addToIncoming(msg);
    	} catch (InterruptedException e) {
    	    Thread.currentThread().interrupt(); // propagate interrupt status
    	}
    }

    /**
     * Push a chat to the store
     * @param ch the chat to push to the queue
     */
    protected void pushStore(Chat ch) throws ClassNotFoundException {
        try {
            //requestStore.addToIncoming(username);
            requestStore.addToIncoming(ch);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // propagate interrupt status
        }
    }
    /**
     * Pull a message or a chat from the store
     * @return obj from the store
     */
    protected Object popStore() {
    	try {
			return requestStore.getIncoming();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // propagate interrupt status
		}
    	return null; // should never happen
    }

    /**
     * Get Chat obj from its chatName
     * 
     * @param chatName name of desired chat
     * @return chat obj with its name = chatName
     */
    protected Chat getChat(String chatName) {
    	return data.getChat(chatName);
    }

    /**
     * Log out the user from the system
     * @param username is the name of the user to log out
     */
    protected void logoutHandler(String username) {
        clients.remove(username);
    }

    // to broadcast messages in rqstStore
    private static class RqstHandler extends Server implements Runnable {
        //private final Server server;

	    public RqstHandler() {
	        //this.server = server;
	    }
	    
	    @Override
	    public void run() {
            while (true) {
                Object obj = popStore();
                switch (obj) {
                    case Message msg -> {
                        // Show the message
                        System.out.println("Message sent!:" + msg);
                        // Save the current message
                        try {
                            data.saveMessage(msg);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Chat c = getChat(msg.getChatname());

                        String[] ul = c.getUsersNames();
                        HashSet<String> userSet = new HashSet<String>();
                        for (String userName : ul) {
                            userSet.add(userName);
                        }
                        // broadcast to active users and
                        // members of the chat (even implicit admins)
                        for (String userName : clients.keySet()) {
                            // skip user if they aren't online
                            if (!Server.clients.containsKey(userName)) continue;
                            // skip user if they aren't in the chat and arent admin
                            if (!userSet.contains(userName) &&
                                    data.getAccount(userName).getRole() != Role.ADMINISTRATOR) continue;

                            // don't resend the same user the message he sent
                            if (msg.getAccountName().equals(userName)) continue;

                            try {
                                sendMsg(userName, msg);
                            } catch (IOException e) {
                                // client just disconnected
                                if (e instanceof EOFException) {
                                    Server.clients.remove(userName);
                                    continue;
                                }

                                String errStr = "Broadcast thread failed: " +
                                        msg.toString() +
                                        " | " + userName;

                                System.err.println(errStr);
                                throw new RuntimeException(errStr, e);
                            }
                        }
                    }
                    case Chat ch -> {
                        // pop the user who sent the chat
                        //String userWhoSent = (String) popStore();
                        // save the current chat
                        System.out.println("Chat sent!:" + ch);
                        try {
                            data.addChat(ch.getUsersNames(), ch.getChatName());
                        } catch (IOException e) {
                            // The client disconnected
                            //clients.remove(userWhoSent);
                        }

                        HashSet<String> userSet = new HashSet<String>();
                        for (String user : ch.getUsersNames()) {
                            userSet.add(user);
                        }
                        // broadcast to active users and
                        // members of the chat (even implicit admins)
                        for (String userName : clients.keySet()) {
                            // skip user if they aren't online
                            if (!Server.clients.containsKey(userName)) continue;
                            // skip user if they aren't in the chat and arent admin
                            if (!userSet.contains(userName) &&
                                    data.getAccount(userName).getRole() != Role.ADMINISTRATOR) continue;

                            // don't resend the same user the chat they made
                            // creator always goes first
                            if (userName.equals(ch.getUsersNames()[0])) continue;

                            try {
                                sendChat(userName, ch.getChatName());
                            } catch (IOException e) {
                                Server.clients.remove(userName);
                                // client just disconnected
                                if (e instanceof EOFException) {
                                    continue;
                                }

                                String errStr = "Broadcast thread failed: " +
                                        ch.getChatName() +
                                        " | " + userName;

                                System.err.println(errStr);
                                throw new RuntimeException(errStr, e);
                            }

                        }
                    }
                    case null, default -> {
                        // This should not happen
                        System.err.println("RqstHandler run super unexpected object was found in RqstStore");
                        System.err.println(obj);
                    }
                }
            }
	    }
    }
    
    private static class BackgroundHandlerServer extends Server implements Runnable {
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private final Socket conn;
        private String userKey;
        public BackgroundHandlerServer(Socket conn) {
            this.conn = conn;
        }
        
        @Override
        public void run() {
            // Perform the initial login of the client
            try {
                // Get the streams
                this.out = new ObjectOutputStream(conn.getOutputStream());
                this.in = new ObjectInputStream(conn.getInputStream());

                // Grab the login object from the client and check credentials
                Login userCredentials = (Login) this.in.readObject();
                Login status = checkLoginStatus(userCredentials);
                this.out.writeObject(status);
                switch (status.getLoginStatus()) {
                    case SUCCESS -> {
                        System.out.println("Login for User [" + userCredentials.getUsername() + "]" + " at " + LocalDateTime.now() + " Succeeded");

                        out.writeObject(data.getAccount(userCredentials.getUsername())); // Also send the client the account object
                        userKey = userCredentials.getUsername(); // Add the client to the list of connected clients
                        clients.put(userKey, this);

                        List<Chat> chats = data.getChats(userCredentials.getUsername()); // Send the client all the chats they need
                        this.out.writeObject(chats);

                        sendAllUsers(userKey); // Send all usernames to the client
                    }
                    case FAILURE -> {
                        System.out.println("Login for User [" + userCredentials.getUsername() + "]" + " at " + LocalDateTime.now() + " Failed");
                        return;
                    }
                    case LOGGED_IN -> {
                        System.out.println("Login for already admitted User [" + userCredentials.getUsername() + "]" + " at " + LocalDateTime.now());
                        return;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                // Close the thread and stop handling this client data
                return;
            }

            // loop to handle switching chats/sending messages/stuff like that
            while (true) {
                try {
                    Object obj = this.in.readObject();
                    switch (obj) {
                        case Message msg -> {
                            pushStore(msg);
                        }
                        case Chat newChat -> {
                            pushStore(newChat);
                        }
                        case null, default -> {
                            clients.remove(userKey);
                            return;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    // Logout the client, no mercy
                    clients.remove(userKey);
                    return;
                }
            }
        }
    }
}
