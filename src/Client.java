import java.net.Socket;
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
    private Account account;
    private Message msg; // why do we need a message here?
    private String[] userList;
    private static GUI display;
    private RqstStore requestStore;
    private static List<Chat> chats;

    public static void main(String[] args) {
    	//remove later
    	System.out.println("Running Client");
    	
    	try {
    		socket = new Socket("localhost", 42069);
    		out = new ObjectOutputStream(socket.getOutputStream());
    		in = new ObjectInputStream(socket.getInputStream());
    		
    		//login and gets chat info, then starts background thread, and goes to main screen
        	display();
    		
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
     * @return	List<Chat>
     */
    public List<Chat> getChats() {
    	return chats;
    }

    //change later, made for testing
    public static void display() {
    	display = new GUI();
    	
    	display.loginScreen();
    	
    	getChatFromServer();
		
		new Thread(new BackgroundHandlerClient()).start();
    	
    	display.mainScreen();
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
            //Need line getting a response from server whether or the login passed.
            //System.out.println("Server says: " + loginResponse.getText());
    
            if (loginResponse.getLoginStatus() == LoginType.SUCCESS) 
            {
                System.out.println("Login successful!");
                loginSucceeded = true;
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
     * 
     * @param chatInputStream	An ObjectInputStream for retrieving chats[] after login
     */
    private static void getChatFromServer() {
    	try {
//			chats = (ArrayList<Chat>) in.readObject();
    		chats = Arrays.asList((Chat[]) in.readObject());
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    	
//    	for(Chat chat : chats) {
//    		System.out.println(chat.getChatName());
//    	}
//    	System.exit(0);
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

    	for(String name : this.userList) {
    		if(name.contains(partialName)) {
    			temp.add(name);
    		}
    	}

    	return temp;
    }

     public void makeChat() {
        String testName = "test";
        Account[] testList = { account };
        Chat newChat = new Chat(testList, testName);
//        try {
//
//             Receive response from server
//            Database dataBase = (Database) in.readObject();
//            dataBase.addChat(newChat);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private static class BackgroundHandlerClient extends Client implements Runnable {
    	
    	public BackgroundHandlerClient() {}
    	
        @Override
        public void run() {
//            TODO.todo();
			try {
				new Thread(new IncomingHandler()).start();
				new Thread(new OutgoingHandler()).start();
				
				//remove
				Thread.sleep(5000);
				System.exit(0);
				
				//handles incoming messages from client request store queue
				while(true) {
					//should block thread because its a blocking queue in request store
					Message msg = super.requestStore.getIncoming();
					
					//this is ugly wrote while tired, might want to change later
					for(Chat chat : chats) {
						for(Account account : chat.getUsers()) {
							if(account.getName().equals(msg.getAccountName())) {
								chat.addMessage(msg);
							}
						}
					}
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    
    private static class IncomingHandler extends Client implements Runnable {
    	
    	public IncomingHandler() {}
    	
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
    	
    	public OutgoingHandler() {}
    	
		@Override
		public void run() {
			while(true) {
				try {
					//should block thread because its a blocking queue in request store
					out.writeObject(super.requestStore.getOutgoing());
					
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
}
