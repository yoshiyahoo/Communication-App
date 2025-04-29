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
    		Thread clientThread = new Thread(new BackgroundHandlerServer());
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

        @Override
        public void run() {
            // Send chats to the Client

            // loop to handle switching chats
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
