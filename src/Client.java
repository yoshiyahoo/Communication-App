import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
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

    /**
     * Called by main for getting chat list for clients account after login.
     * 
     * @param chatInputStream	An ObjectInputStream for retrieving chats[] after login
     */
    private void getChatFromServer(ObjectInputStream chatInputStream) {
    	try {
			this.chats = (Chat[]) chatInputStream.readObject();
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * This takes in a partial name entry from a user search and returns an ArrayList<String>
     * that has all user's names that contains partialName String.
     * 
     * @param partialName	A partial name string for searching
     * @return 				ArrayList<String> for all names hat contains partialName
     */
    private ArrayList<String> searchUserList(String partialName) {
    	ArrayList<String> temp = new ArrayList<String>();
    	
    	for(String name : this.userList) {
    		if(name.contains(partialName)) {
    			temp.add(name);
    		}
    	}
    	
    	return temp;
    }

    public void makeChat() {

    }


    private static class BackgroundHandlerClient implements Runnable {

        @Override
        public void run() {

        }
    }
}
