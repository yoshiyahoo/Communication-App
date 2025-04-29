import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ConcurrentHashMap<String, Socket> clients;
    private Database data;
    private ServerSocket server;
    private RqstStore requestStore;

    public static void main(String[] args) throws IOException {
    	/*
    	 * start accepting sock
    	 * on each accept, spawn thread, run client handler
    	 */
    	ServerSocket acceptor = new ServerSocket(42069);
    	
    	while(true) {
    		Socket client = acceptor.accept();
    		
    		// TODO Josiah can change da interface
    		Thread clientThread = new Thread(new BackgroundHandlerServer(client));
    		clientThread.start();
    	}
    }

    private void initialStartUp() {

    }

    private boolean loginHandling(Login login) {
    	// get account info from db
    	Account acct = data.getAccount(login.getPassword());
    	
    	return acct != null && login.getPassword().equals(acct.getPassword());
    }

    private void sendMsgHistory(String userName, String chatName) {
    	
    }
    
    private void sendUserList() {

    }
    
    private void sendChat() {

    }

    private void logoutHandler() {

    }

    private void handleClients(Socket client) {

    }

    private void handleClientOfflineMsgQ() {

    }

    public Message getDatabaseMessages(Socket client) {

    }

    private void checkAndSendOfflineMsg(Socket client) {

    }

    private static class BackgroundHandlerServer implements Runnable {
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
            try {
                Login login = (Login) this.in.readObject();
                System.out.println(login);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            // Send chats to the Client

            // loop to handle switching chats/sending messages/stuff like that
        }
    }

    private static class ClientStream {
        private Socket socket;
        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;

        public ClientStream(Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
            this.socket = socket;
            this.outputStream = outputStream;
            this.inputStream = inputStream;
        }

        public Socket getSocket() {
            return socket;
        }

        public ObjectOutputStream getOutputStream() {
            return outputStream;
        }

        public ObjectInputStream getInputStream() {
            return inputStream;
        }

        public void close() {
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (Exception e) {
                System.err.println("Failed to close streams: " + e.getMessage());
            }
        }
    }
}
