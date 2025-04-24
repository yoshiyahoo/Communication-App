import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    // wouldn't we want the clients to be searchable via a hashmap?
    private ConcurrentHashMap<String, Obj> clients = new ConcurrentHashMap<>();
    private Database data;
    private ServerSocket server;
    private Message msg; // why do we need this?
    private RqstStore requestStore;

    public static void main(String[] args) {

    }

    // me
    private void initialStartUp() {

    }

    // me
    private boolean loginHandling() {

    }

    // What specifically do these following 3 methods send and to what?
    // which message history?
    // me
    private void sendMsgHistory() {

    }

    // which chat?
    // me
    private void sendChats(String clientName) {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        data.getChat();
    }

    private void logoutHandler(String clientName) {
        ServerSocket toClose = clients.remove(clientName);
        if (toClose != null) {
            try {
                toClose.close();
            } catch (Exception e) {
                System.err.println("Failed to close connection for client: " + clientName);
            }
        }
    }

    // wouldn't we want more than just one client?
    private void handleClients(Socket client) {

    }

    // weren't we not gonna do this part?
    private void handleClientOfflineMsgQ() {

    }

    // from which chat?
    // me
    public Message getDatabaseMessages(Socket client) {

    }

    // weren't we not gonna do this part?
    // me
    private void checkAndSendOfflineMsg(Socket client) {

    }

    private static class BackgroundHandlerServer implements Runnable {
        @Override
        public void run() {

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
