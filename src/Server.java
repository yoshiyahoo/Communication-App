import java.util.Arrays;
import java.util.List;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
        System.out.println("Server started on Port: " + PORT);

        // 3. (Optional) Initialize data structures for clients
        // create List or Map to track connected clients
        clients = new ConcurrentHashMap<>();
        data = new Database();
        requestStore = new RqstStore();

        // 4. (Optional) Set up a thread pool (for handling clients)
        // initialize ExecutorService or thread pool

        // 5. (Optional) Load server config or resources
        // load config file if any
    }

    /**
     * Compare a login obj with login info on the database 
     * 
     * @param login Login object with username and password that needs verification
     * @return True if login is in database, False otherwise
     */
    // TODO send login verification here?
    private static boolean loginHandling(Login login) {
    	// get account info from db
    	Account acct = data.getAccount(login.getUsername());
    	return acct != null && login.getPassword().equals(acct.getPassword());
    }

    /**
     * Send message history of a chat to client as a Message[]
     * 
     * @param userName name of recipient
     * @param chatName Desired chat
     * @throws IOException 
     */
    private void sendMsgHistory(String userName, String chatName) throws IOException {
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
    private void sendAllUsers(String userName) throws IOException {
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
    private void sendChat(String userName, String chatName) throws IOException {
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
    private void sendMsg(String userName, Message msg) throws IOException {
    	ObjectOutputStream clientStream = clients.get(userName).out;
    	clientStream.writeObject(msg);
    }
    
    /**
     * Push a message to the store
     * 
     * @param msg message to queue
     */
    private void pushStore(Message msg) {
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
    private Message popStore() {
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
    private Chat getChat(String chatName) {
    	return data.getChat(chatName);
    }
    
    private void logoutHandler() {
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
    private static class RqstHandler implements Runnable {
    	 private final Server server;

	    public RqstHandler(Server server) {
	        this.server = server;
	    }
	    
	    @Override
	    public void run() {
	    	while(true) {
	    		Message msg = server.popStore();
	    		if(msg == null) continue;
	    		
	    		Chat c = server.getChat(msg.getChatname());
	    		Account[] ul = c.getUsers();
	    		
	    		// broadcast to active users in ul
	    		for(Account acct : ul) {
	    			String userName = acct.getName();
	    			// skip user if they aren't online
	    			if(!Server.clients.containsKey(userName)) continue;
	    			
	    			try {
						server.sendMsg(userName, msg);
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
    
    // TODO a lil hard to read, boss
    // TODO why extend? class has access to server attributes/methods. 
    // TODO could hold a reference to main server
    private static class BackgroundHandlerServer extends Server implements Runnable {
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private Socket conn;
        public BackgroundHandlerServer(Socket conn) {
            this.conn = conn;
        }
        
        @Override
        public void run() {
            // Gather the output stream information
            try {
                this.out = new ObjectOutputStream(conn.getOutputStream());
                this.in = new ObjectInputStream(conn.getInputStream());
            } catch (IOException e) {
                // Close the thread and stop handling this client data
                return;
            }

            // Process the login
            Login login;
            try {
                login = (Login) this.in.readObject();
                while (!loginHandling(login)) {
                    System.out.println("Login for User [" + login.getUsername() + "] Failed");
                    out.writeObject(new Login(LoginType.FAILURE));
                    login = (Login) this.in.readObject();
                };
                System.out.println("Login for User [" + login.getUsername() + "] Succeeded");
                out.writeObject(new Login(LoginType.SUCCESS));
                out.writeObject(data.getAccount(login.getUsername())); // Also send the client the account object

                // If there is any exception to the protocol, drop the client
            } catch (IOException | ClassNotFoundException e) {
                return;
            }

            System.out.println("Login for " + login.getUsername() + " Succeeded");

            // Send chats to the Client
            List<Chat> chats = data.getChats(login.getUsername());

            try {
                this.out.writeObject(chats);
            } catch (IOException e) {
                // close the thread
                return;
            }

            // loop to handle switching chats/sending messages/stuff like that
            while (true) {
                Object test = null;
                Message msg = null;
                Chat newChat = null;
                try {
                    test = this.in.readObject();
                    System.out.println(test.toString());
                } catch (IOException e) {
                    return;
                } catch (ClassNotFoundException e) {
                    // Do nothing here, it might not be over yet
                }
                // The client could send over a chat object to create a new chat
                try {
                    newChat = (Chat) this.in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    // At this point, we shouldn't expect anything good from the client
                    return;
                }

                if (msg != null) {
                    try {
                        data.saveMessage(msg);
                        // Tell the other clients to get a new message
                        for (Account user : newChat.getUsers()) {
                            clients.get(user.getName()).out.writeObject(msg);
                        }
                    } catch (IOException e) {
                        // Stop the thread if it's not done
                        return;
                    }
                }
                else if (newChat == null) {
                    // move on to the next iteration
                    continue;
                }


                // Why do we need to convert accounts to strings when you just convert strings back to accounts
                // in the addChat() method?
                String[] usernames = Arrays.stream(newChat.getUsers())
                        .map(Account::getName)
                        .toArray(String[]::new);
                try {
                    data.addChat(usernames, newChat.getChatName());
                    TODO.todo("figure out a way to get the chats to all new users, possible to use observers");
                } catch (IOException e) {
                    return;
                }
            }
        }
    }
}
