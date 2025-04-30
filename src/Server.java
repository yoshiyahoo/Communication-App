import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
	// NOTE client gets added in bg handler after login success
    private static ConcurrentHashMap<String, BackgroundHandlerServer> clients;
    private static Database data;
    private static ServerSocket server;
    private static RqstStore requestStore;
    private static final int PORT = 42069;

    /**
     * Init server
     * start listening for clients
     * on host IP, port 42069
     * 
     * @param args 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
    	clients = new ConcurrentHashMap<>();
    	data = new Database();
    	server = new ServerSocket(PORT);
    	
    	while(true) {
    		Socket client = server.accept();
    		Thread clientThread = new Thread(new BackgroundHandlerServer(client));
    		clientThread.start();
    	}
    }

    // TODO this is all being done in main, might move here as things grow
    private void initialStartUp() {
        // 1. Open a server socket on a specific port
        // create ServerSocket listeningSocket on PORT

        // 2. Print startup message
        // print "Server started on port " + PORT

        // 3. (Optional) Initialize data structures for clients
        // create List or Map to track connected clients

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
    private boolean loginHandling(Login login) {
    	// get account info from db
    	Account acct = data.getAccount(login.getPassword());

    	return acct != null && login.getPassword().equals(acct.getPassword());
    }

    /**
     * Send message history of a chat to client as a Message[]
     * 
     * @param clientStream Stream to send history to
     * @param chatName Desired chat
     * @throws IOException 
     */
    private void sendMsgHistory(ObjectOutputStream clientStream, String chatName) throws IOException {
    	Message[] toSend = data.getChat(chatName).getMsgHistory();
    	clientStream.writeObject(toSend);
    }
    
    /**
     * Send the user list of a chat to client as an Account[]
     * 
     * @param clientStream Stream to send user list to
     * @param chatName Desired chat
     * @throws IOException 
     */
    private void sendUserList(ObjectOutputStream clientStream, String chatName) throws IOException {
    	Account[] toSend = data.getChat(chatName).getUsers();
    	clientStream.writeObject(toSend);
    }
    
    /**
     * Send a chat to client as an Chat obj
     * 
     * @param clientStream Stream to send chat to
     * @param chatName Desired chat
     * @throws IOException 
     */
    private void sendChat(ObjectOutputStream clientStream, String chatName) throws IOException {
    	Chat toSend = data.getChat(chatName);
    	clientStream.writeObject(toSend);
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

    private static class BackgroundHandlerServer extends Server implements Runnable {
        private Chat currentChat;
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
                System.out.println(login);
                // If there is any exception to the protocol, drop the client
            } catch (IOException | ClassNotFoundException e) {
                return;
            }

            if (!super.loginHandling(login)) {
                // close the thread
                // The client will infer that it logged in or not
                try {
                    out.writeObject(login);
                } catch (IOException e) {
                    return;
                }
            }
            // Send chats to the Client

            // loop to handle switching chats/sending messages/stuff like that
        }
    }
}
