import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // wouldn't we want the clients to be searchable via a hashmap?
    private Socket[] clients;
    private Database data;
    private ServerSocket server;
    private Message msg; // why do we need this?
    private RqstStore requestStore;

    public static void main(String[] args) {

    }

    private void initialStartUp() {

    }

    private boolean loginHandling() {

    }

    // What specifically do these following 3 methods send and to what?
    // which message history?
    private void sendMsgHistory() {

    }

    // which user list?
    private void sendUserList() {

    }

    // which chat?
    private void sendChat() {

    }

    private void logoutHandler() {

    }

    // wouldn't we want more than just one client?
    private void handleClients(Socket client) {

    }

    // weren't we not gonna do this part?
    private void handleClientOfflineMsgQ() {

    }

    // from which chat?
    public Message getDatabaseMessages(Socket client) {

    }

    // weren't we not gonna do this part?
    private void checkAndSendOfflineMsg(Socket client) {

    }

    private static class BackgroundHandlerServer implements Runnable {
        @Override
        public void run() {

        }
    }
}
