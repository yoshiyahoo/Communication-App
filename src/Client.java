import java.net.Socket;
import java.util.Queue;

public class Client {
    private Socket socket;
    private Queue<Message> offlineQ; // need to rename QueueForOfflineMessages in Design
    private Account account;
    private Message msg; // why do we need a message here?
    private String[] userList;
    private GUI display;
    private RqstStore requestStore;
    private Chat[] chats;

    public static void main(String[] args) {

    }

    // why is this method needed?
    public Queue<Message> getMessageQueue() {

    }

    public void sendMsg() {

    }

    public void recieveMsg() {

    }

    public void display() {

    }

    public boolean login() {

    }

    private void getUserListFromServer() {

    }

    private void searchUserList() {

    }

    public void makeChat() {

    }


    private static class BackgroundHandlerClient implements Runnable {

        @Override
        public void run() {

        }
    }
}
