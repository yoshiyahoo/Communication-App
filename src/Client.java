
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.io.*;

public class Client {

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private Queue<Message> offlineQ = new LinkedList<>(); // need to rename QueueForOfflineMessages in Design
    private static Account account;
    private Message msg; // why do we need a message here?
    private static String[] userList;
    private static GUI display;
    private static RqstStore requestStore;
    private static List<Chat> chats;

    public static void main(String[] args) {
    	//remove later
    	System.out.println("Running Client\n");
    	
    	try {
    		socket = new Socket("localhost", 42069);
    		out = new ObjectOutputStream(socket.getOutputStream());
    		in = new ObjectInputStream(socket.getInputStream());
    		
    		//makes the Request Store object
    		requestStore = new RqstStore();

    		//login and gets chat info, then starts background thread, and goes to main screen
        	display();
    		
		} catch (IOException e)
         {
			e.printStackTrace();
		} 
        
        /*finally {
			try {
				socket.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
    	System.out.println("\nClient Done");
    	System.exit(0); //for cleaning up and running thread
        */
    }

   
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
     * @return	List<Chat>
     */
    public List<Chat> getChats() {
    	return chats;
    }
    
    /**
     * gets userList from Client for GUI
     * 
     * @return String[]
     */
    public String[] getUserList() {
    	return userList;
    }
    
    /**
     * gets user account for GUI when creating a message object
     * 
     * @return Account
     */
    public Account getUserAccount() {
    	return account;
    }

    public static void display() {
    	display = new GUI();
    	
    	display.loginScreen();
    	

        //Read these later; probably in display.mainScreen();
        /*
    	getChatFromServer();
    	
    	//getUserNamesFromServer();
        */
		
		new Thread(new BackgroundHandlerClient(), "Background Message Handler").start();
		new Thread(new IncomingHandler(), "Incoming Chat Handler").start();
		new Thread(new OutgoingHandler(), "Outgoing Chat Handler").start();

    }

    public boolean login(String username, String password) {
        boolean loginSucceeded = false;

        Login newLogin = new Login(username, password);
    
        try {
            //Sends login to server
            out.writeObject(newLogin);
            out.flush();
            
            // Receive response from server
            Login loginResponse = (Login) in.readObject();
    
            if (loginResponse.getLoginStatus() == LoginType.SUCCESS) 
            {
                System.out.println("Login successful!");
                loginSucceeded = true;
                
                //gets account info from server if the login was successful
                account = (Account) in.readObject();
            } 
            else 
            {
                System.out.println("Login failed! Please try again.");
            }
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loginSucceeded;

    }

    /**
     * Called by main for getting chat list for clients account after login.
     */
    private static void getChatFromServer() {
    	try {
    		chats = (List<Chat>) in.readObject();
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Called by main for getting user names for clients account after login.
     */
    private static void getUserNamesFromServer() {
    	try {
    		userList = (String[]) in.readObject();
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * This takes in a partial name entry from a user search and returns an List<String>
     * that has all user's names that contains partialName String.
     *
     * @param partialName	A partial name string for searching
     * @return 				List<String> for all names hat contains partialName
     */
    private List<String> searchUserList(String partialName) {
    	List<String> temp = new ArrayList<String>();

        for (String name : this.userList) {
            if (name.contains(partialName)) {
                temp.add(name);
            }
        }

        return temp;
    }

    public void makeChat(String[] users, String chatname) {
    	Chat newChat = new Chat(users, chatname);
    	
    	chats.add(newChat);
    	
    	try {
			requestStore.addToOutGoing(newChat);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    //handles the objects in request store getIncoming()
    private static class BackgroundHandlerClient implements Runnable {

    	public BackgroundHandlerClient() {}

    	@Override
    	public void run() {
    		//            TODO.todo();
    		try {
    			//handles incoming messages from client request store queue
    			while(true) {
    				//should block thread because its a blocking queue in request store
    				Object obj = requestStore.getIncoming();
    				
    				//if object is a Message
    				if(obj.getClass() == Message.class) {
    					Message msg = (Message) obj;
    					
    					//this is ugly wrote while tired, might want to change later
        				for(Chat chat : chats) {
        					for(String user : chat.getUsersNames()) {
        						if(user.equals(msg.getAccountName())) {
        							chat.addMessage(msg);
        						}
        					}
        				}
        				
        				continue;
    				}

    				//if object is a Chat
    				if(obj.getClass() == Chat.class) {
    					Chat chat = (Chat) obj;
    					
    					chats.add(chat);
    				}
    			}

    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    //puts the incoming objects into request store addToIncoming()
    private static class IncomingHandler implements Runnable {
    	
    	public IncomingHandler() {}
    	
		@Override
		public void run() {
			while(true) {
				try {
					Object incomingObj = in.readObject();
					requestStore.addToIncoming(incomingObj);
					
				} catch (InterruptedException | ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
    
    //sends objects from request store getOutgoing() to server
    private static class OutgoingHandler implements Runnable {
    	
    	public OutgoingHandler() {}
    	
		@Override
		public void run() {
			while(true) {
				try {
					//should block thread because its a blocking queue in request store
					out.writeObject(requestStore.getOutgoing());
					
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
}
