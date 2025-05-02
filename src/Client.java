import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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
    private static GUI display;
    private RqstStore requestStore;
    private static ArrayList<Chat> chats;

    public static void main(String[] args) {
    	//remove later
    	System.out.println("Running Client");
    	
    	try {
    		socket = new Socket("134.154.68.196", 42069);
    		
//    		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//    		
//			out.writeObject(new Login(
//					"test",
//					"password"
//			));
//			
//			Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	//login and gets chat info, then starts background thread, and goes to main screen
    	display();
    	
    	System.out.println("Done");
    }

    // why is this method needed?
    //public Queue<Message> getMessageQueue() {
    //	return offlineQ;
    //}

    public void sendMsg(Message msg) {
    	try {
    		requestStore.addToOutGoing(msg); //hand off to outgoing queue
    	} catch(InterruptedException e) {
    		System.out.println("Error while sending message: " + e.getMessage());
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
    	return chats;
    }

    //change later, made for testing
    public static void display() {
    	display = new GUI();
    	
    	display.loginScreen();
    	
    	//probably need to change location
    	ObjectInputStream objectInputStream;
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			getChatFromServer(objectInputStream);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new Thread(new BackgroundHandlerClient(socket)).start();
    	
    	display.mainScreen();
    }

    public boolean login(String username, String password) {
        boolean loginSucceeded = false;
    
        Login newLogin = new Login(username, password);
    
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); 
        		ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
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
    private static void getChatFromServer(ObjectInputStream chatInputStream) {
    	try {
			chats = (ArrayList<Chat>) chatInputStream.readObject();
			
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
        String testName;
        Chat newChat = Chat(account, testName);
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Receive response from server
            Database dataBase = (Database) in.readObject();
            dataBase.addChat(newChat);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static class BackgroundHandlerClient extends Client implements Runnable {
    	private Socket socket;
    	
    	public BackgroundHandlerClient(Socket socket) {
    		this.socket = socket;
    	}
    	
        @Override
        public void run() {
//            TODO.todo();
        	ObjectInputStream in;
        	ObjectOutputStream out;
			try {
				in = new ObjectInputStream(this.socket.getInputStream());
				out = new ObjectOutputStream(this.socket.getOutputStream());
				
				new Thread(new IncomingHandler(in)).start();
				new Thread(new OutgoingHandler(out)).start();
				
				//need to handle what is in the incoming queue
				//need to handle what is to be put in the outgoing queue
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    private static class IncomingHandler extends Client implements Runnable {
    	private ObjectInputStream in;
    	
    	public IncomingHandler(ObjectInputStream in) {
    		this.in = in;
    	}
    	
		@Override
		public void run() {
			while(true) {
				try {
					Message msg = (Message) in.readObject();
					super.requestStore.addToIncoming(msg);
					
				} catch (InterruptedException | ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
    
    private static class OutgoingHandler extends Client implements Runnable {
    	private ObjectOutputStream out;
    	
    	public OutgoingHandler(ObjectOutputStream out) {
    		this.out = out;
    	}
    	
		@Override
		public void run() {
			while(true) {
				try {
					out.writeObject(super.requestStore.getOutgoing());
					
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
}
