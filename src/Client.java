
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

public class Client {

    private static Socket socket;
    private Queue<Message> offlineQ = new LinkedList<>(); // need to rename QueueForOfflineMessages in Design
    private Account account;
    private Message msg; // why do we need a message here?
    private String[] userList;
    private GUI display;
    private RqstStore requestStore;
    private ArrayList<Chat> chats;

    public static void main(String[] args) {
        //remove later
        /*System.out.println("Running Client");
    	
    	try {
    		socket = new Socket("134.154.68.196", 42069);
    		
    		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
    		
			out.writeObject(new Login(
					"test",
					"password"
			));
			
			Thread.sleep(5000);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("Done");
         */

        try 
        {
            socket = new Socket("localhost", 42069);
            Client client = new Client();
            client.display();
            Thread.sleep(5000);
        } 
        catch (IOException | InterruptedException e) 
        {
			e.printStackTrace();
		}

    }

    // why is this method needed?
    //public Queue<Message> getMessageQueue() {
    //	return offlineQ;
    //}
    public void sendMsg(Message msg) {
        try {
            requestStore.addToOutGoing(msg); //hand off to outgoing queue
        } catch (InterruptedException e) {
            System.out.println("Interrupted while sending message: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void recieveMsg(Message msg) {
        //store message for later
        //putting something into the queue
        //System.out.println("[" + msg.getTime() + "] " + msg.getAccountName() + " in " + msg.getChatname() + ": " + msg.getMsg());
    }

    /**
     * gets chatList from Client for GUI
     *
     * @return	ArrayList<Chat>
     */
    public ArrayList<Chat> getChats() {
        return this.chats;
    }

    //change later, made for testing
    public void display() 
    {
        this.display = new GUI(this); //do in main

        this.display.loginScreen();

        //probably need to change location
        ObjectInputStream objectInputStream;
        try 
        {
            System.out.println("Skipping login screen");
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            this.getChatFromServer(objectInputStream);

        } catch (IOException e) {

            e.printStackTrace();
        }

        this.display.mainScreen();
        
    }

    public boolean login(String username, String password) {
        boolean loginSucceeded = false;

        Login newLogin = new Login(username, password);

        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); 
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) 
            {

            //Sends login to server
            out.writeObject(newLogin);
            out.flush();

            //if a login object is returned at all, that means it failed.
            //we could add something to a login object which states if the login failed or not, but for now, I'm working with what I got.
           Login returnedLogin = (Login) in.readObject();
           if(returnedLogin != null)
           {
                loginSucceeded = false;
           }
           else
           {
                loginSucceeded = true;
           }

        } 
        catch (SocketException e) 
        {
            System.err.println(e);
        }
         catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }

        return loginSucceeded;

    }

    /**
     * Called by main for getting chat list for clients account after login.
     *
     * @param chatInputStream	An ObjectInputStream for retrieving chats[] after
     * login
     */
    private void getChatFromServer(ObjectInputStream chatInputStream) {
        try {
            this.chats = (ArrayList<Chat>) chatInputStream.readObject();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This takes in a partial name entry from a user search and returns an
     * ArrayList<String>
     * that has all user's names that contains partialName String.
     *
     * @param partialName	A partial name string for searching
     * @return ArrayList<String> for all names hat contains partialName
     */
    private ArrayList<String> searchUserList(String partialName) {
        ArrayList<String> temp = new ArrayList<String>();

        for (String name : this.userList) {
            if (name.contains(partialName)) {
                temp.add(name);
            }
        }

        return temp;
    }

    public void makeChat() {
        String testName;
        Chat newChat = Chat(account, testName);
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Receive response from server
            Database dataBase = (Database) in.readObject();
            dataBase.addChat(newChat);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class BackgroundHandlerClient implements Runnable {

        @Override
        public void run() {
            TODO.todo();
        }
    }
}
