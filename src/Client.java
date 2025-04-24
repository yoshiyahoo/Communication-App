import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.io.*;

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

    public boolean login(String username, String password) {
        /*Needs to check if the users inputted information exists within the database
        First the database checks for the username
        If the username isn't found, then the loops breaks and an error message is printed to the GUI
        If the username is found, then the corresponding password is checked to see if it matches'
        if it doesn't match, then a password error is displayed on the gui
        once the user information is found, the bool for login returns true.
        if the password or username is not found, then the bool returns false
         */
        boolean loginSucceeded = false;
    
        Login newLogin = new Login(username, password);
    
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            //Sends login to server
            out.writeObject(newLogin);
            out.flush();
    
            // Receive response from server
            Login loginResponse = (Login) in.readObject();
    
            //Need line getting a response from server wheter or the login passed.
            //System.out.println("Server says: " + loginResponse.getText());
    
            /* if (response.getStatus() == MessageStatus.SUCCESS) 
            {
                System.out.println("Login successful!");
                loginSucceeded = true;
            } 
            else 
            {
                System.out.println("Login failed! Please try again.");
            }
             */
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    
        return loginSucceeded;
    
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