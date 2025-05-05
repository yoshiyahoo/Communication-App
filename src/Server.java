import java.util.Arrays;
import java.util.Enumeration;
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

    	    Enumeration<InetAddress> addresses = ni.getInetAddresses();
    	    while (addresses.hasMoreElements()) {
    	        InetAddress addr = addresses.nextElement();
    	        if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
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
    // TODO send login verification here?
    protected static boolean loginHandling(Login login) {
    	// get account info from db
    	Account acct = data.getAccount(login.getUsername());
    	return acct != null && login.getPassword().equals(acct.getPassword()) &&
    			!clients.containsKey(login.getUsername());
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
    // TODO we got sendmsghist too, should we keep both?
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
     * 
     * @param msg message to queue
     */
    protected void pushStore(Message msg) {
    	try {
    	    requestStore.addToIncoming(msg);
    	} catch (InterruptedException e) {
    	    Thread.currentThread().interrupt(); // propagate interrupt status
    	}
    }
    
    /**
     * Pull a message from the store
     * 
     * @return message obj from the store
     */
    protected Message popStore() {
    	try {
			Message msg = (Message) requestStore.getIncoming();
			return msg;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // propagate interrupt status
		}
    	
    	return null; // should never happen tho
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
    
    protected void logoutHandler() {
        TODO.todo("close client connections, remove client from map, end thread");
    }

    protected void handleClients(Socket client) {
        TODO.todo();
    }

    protected void handleClientOfflineMsgQ() {
        TODO.todo();
    }

    public Message getDatabaseMessages(Socket client) {
        return (Message) TODO.todo();
    }

    protected void checkAndSendOfflineMsg(Socket client) {
        TODO.todo();
    }

    // to broadcast messages in rqstStore
    private static class RqstHandler extends Server implements Runnable {
        //private final Server server;

	    public RqstHandler() {
	        //this.server = server;
	    }
	    
	    @Override
	    public void run() {
	    	while(true) {
	    		Message msg = popStore();
	    		if(msg == null) continue;
	    		
	    		Chat c = getChat(msg.getChatname());

                // save the current message
                try {
                    data.saveMessage(msg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String[] ul = c.getUsersNames();
	    		
	    		// broadcast to active users in ul
	    		for(String userName : ul) {
	    			// skip user if they aren't online
	    			if(!Server.clients.containsKey(userName)) continue;

                    // don't resend the same user the message he sent
                    if(msg.getAccountName().equals(userName)) continue;
	    			
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
	    }
    }
    
    // TODO could hold a reference to main server
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
                Login login = (Login) this.in.readObject();
                if (!loginHandling(login)) {
                    System.out.println("Login for User [" + login.getUsername() + "] Failed");
                    out.writeObject(new Login(LoginType.FAILURE));
                    // Close connection with the client
                    return;
                };
                System.out.println("Login for User [" + login.getUsername() + "] Succeeded");
                out.writeObject(new Login(LoginType.SUCCESS));
                out.writeObject(data.getAccount(login.getUsername())); // Also send the client the account object
                // Add the client to the list of connected clients
                userKey = login.getUsername();
                clients.put(userKey, this);

                // Send the client all the chats they need
                List<Chat> chats = data.getChats(login.getUsername());
                this.out.writeObject(chats);

                // Send all usernames to the client
                sendAllUsers(userKey);
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
                            String[] usernames = newChat.getUsersNames();
                            data.addChat(usernames, newChat.getChatName());
                        }
                        case null, default -> {
                            clients.remove(userKey);
                            return;
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    // Drop the client, no mercy
                    clients.remove(userKey);
                    return;
                }
            }
        }
    }
}
