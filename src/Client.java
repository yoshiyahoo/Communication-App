import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import java.io.*;

public class Client {
    private static Socket socket = null;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static Account account;
    private static String[] userList;
    private static ChatAppGUI display;
    private static RqstStore requestStore;
    private static List<Chat> chats;
    private static Thread background;
    private static Thread outgoing;
    private static Thread incoming;

    public static void main(String[] args) {
    	try {
    		//login and gets chat info, then starts background thread, and goes to main screen
        	display();
        	
        	//blocks main thread until other threads are done
        	Thread.currentThread().join();
    		
		} catch (InterruptedException e) {
			//here if main thread was interrupted during .join()
			
		} finally {
			closeSocket();
		}
    	
    	//for cleaning up any running threads
    	//threads should already be done before here but just for safety
    	System.exit(0);
    }
    
    /**
    * For starting socket and in/out socket streams
    * Also make a new request store
    */
    public static void startSocket(String host) throws UnknownHostException, IOException {
      requestStore = new RqstStore();

      socket = new Socket(host, 42069);
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
    }
    
    /**
     * For safely closing socket
     */
    public static void closeSocket() {
    	try {
    		if(socket != null) {
    			socket.close();
    			socket = null;
    		}
			
		} catch (IOException e) {
			//here if socket failed to close properly
		}
    }
    
    /**
     * Just cleans up chats, userList, requestStore, and account on a logout
     */
    public static void cleanUpOnLogout() {
    	chats = null;
    	userList = null;
    	account = null;
    	requestStore = null;
    }

    /**
     * For adding new messages to the outgoing queue
     * Client --> Server
     * 
     * @param msg
     */
    public void sendMsg(Message msg) {
    	try {
    		requestStore.addToOutGoing(msg); //hand off to outgoing queue
    	} catch(InterruptedException e) {
    		System.out.println("Interrupted while sending message: " + e.getMessage());
    		Thread.currentThread().interrupt();
    	}
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
    	display = new ChatAppGUI();
    	
    	SwingUtilities.invokeLater(() -> display.init());
    }
    
    /**
     * User after startSocket()
     * Handles starting background, incoming, and outgoing threads
     */
    public static void startClientThreads() {
    	background = new Thread(new BackgroundHandlerClient(), "Background Message Handler");
		incoming = new Thread(new IncomingHandler(), "Incoming Chat Handler");
		outgoing = new Thread(new OutgoingHandler(), "Outgoing Chat Handler");
		
		background.start();
		incoming.start();
		outgoing.start();
    }
    
    /**
     * Use before closeSocket()
     * Stops background threads for safe socket close
     */
    public static void stopClientThreads() {
    	//only need to stop background thread with interrupt because
    	//outgoing and incoming stop on socket close (IOException handling)
    	background.interrupt();
    }

    /**
     * For handling user login
     * 
     * @param username	String username tied to users account
     * @param password	String password tied to users account
     * @return			LoginType that indicates whether or not login was successful
     */
    public LoginType login(String username, String password) {
        Login newLogin = new Login(username, password);
        try {
            //Sends login to server
            out.writeObject(newLogin);
            out.flush();
            
            // Receive response from server
            Login loginResponse = (Login) in.readObject();
			if (loginResponse.getLoginStatus() == LoginType.SUCCESS) {
				account = (Account) in.readObject();
			}
           	return loginResponse.getLoginStatus();
        } catch (IOException | ClassNotFoundException e) {
        	//if here probably because socket closed when waiting for server objects
        }
		return null;
    }

    /**
     * Called by main for getting chat list for clients account after login.
     */
    @SuppressWarnings("unchecked")
	public static void getChatFromServer() {
    	try {
    		chats = (List<Chat>) in.readObject();
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Called by main for getting user names for clients account after login.
     */
    public static void getUserNamesFromServer() {
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
     * @return 				ArrayList<String> for all names hat contains partialName
     */
    public static ArrayList<String> searchUserList(String partialName) {
    	ArrayList<String> temp = new ArrayList<String>();

    	for(String name : userList) {
    		if(name.contains(partialName)) {
    			temp.add(name);
    		}
    	}

    	return temp;
    }

    /**
     * For sending new chat info to server and adds new chat to clients chat list locally
     * 
     * @param users			String[] of all users in new chat
     * @param chatname		String for the name of the new chat
     */
    public void makeChat(String[] users, String chatname) {
    	Chat newChat = new Chat(users, chatname);
    	
    	chats.add(newChat);
    	
    	try {
			requestStore.addToOutGoing(newChat);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    /**
     * handles the objects in request store getIncoming()
     */
    private static class BackgroundHandlerClient implements Runnable {

    	public BackgroundHandlerClient() {}

    	@Override
    	public void run() {
    		try {
    			//handles incoming messages/chats from client request store queue
    			while(true) {
    				//should block thread because its a blocking queue in request store
    				Object obj = requestStore.getIncoming();
    				
    				//Incoming Chat created
    				if(obj instanceof Chat) {
    					Chat chat = (Chat) obj;
    					chats.add(chat);
    					//update the UI on the EDT
    					SwingUtilities.invokeLater(() ->
    						display.addChatToList(chat.getChatName())
    					);
    					continue;
    				}
    				
    				//Incoming Message
    				if(obj instanceof Message) {
    					Message msg = (Message) obj;
    					//TODO
    					System.out.println(msg);
    					//find the right Chat model and add it
    					for(Chat chat : chats) {
    						if(chat.getChatName().equals(msg.getChatname())) {
    							chat.addMessage(msg);
    							//now appends to the open history area
    							SwingUtilities.invokeLater(() ->
    								display.appendMessage(
    										String.format(
    					    				  "%s %02d:%02d >>> %s%n",
    					    				  msg.getAccountName(),
    					    				  msg.getTime().getHour(),
    					    				  msg.getTime().getMinute(),
    					    				  msg.getMsg()
    					    		)));
    							break;
    						}
    					}
    					continue;
    				}
    			}

    		} catch (InterruptedException e) {
    			//here when thread is interrupted before closing the socket
    		}
    	}
    }
    
    /**
     * puts the incoming objects into request store addToIncoming()
     */
    private static class IncomingHandler implements Runnable {
    	
    	public IncomingHandler() {}
    	
		@Override
		public void run() {
			while(true) {
				try {
					//should block thread will wait for object to add to queue
					Object incomingObj = in.readObject();
					requestStore.addToIncoming(incomingObj);
					
				} catch (ClassNotFoundException e) {
					//here is bad object comes in from socket stream
					
				} catch (InterruptedException e) {
					//if thread gets interrupted while waiting for queue
					break;
					
				} catch (IOException e) {
					//if the socket closes break out of loop
					break;
				}
			}
		}
    	
    }
    
    /**
     * sends objects from request store getOutgoing() to server
     */
    private static class OutgoingHandler implements Runnable {
    	
    	public OutgoingHandler() {}
    	
		@Override
		public void run() {
			while(true) {
				try {
					//should block thread because its a blocking queue in request store
					out.writeObject(requestStore.getOutgoing());
					out.flush();
					
				} catch (IOException e) {
					//if the socket closes break out of loop
					break;
					
				} catch (InterruptedException e) {
					//if thread gets interrupted while waiting for queue
					break;
				}
			}
		}
    	
    }
}
