
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.SwingUtilities;

import java.io.*;

public class Client {

    private static Socket socket = null;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private Queue<Message> offlineQ = new LinkedList<>(); // need to rename QueueForOfflineMessages in Design
    private static Account account;
    private Message msg; // why do we need a message here?
    private static String[] userList;
    private static GUI display;
    private static RqstStore requestStore;
    private static List<Chat> chats;
    private static Thread background;
    private static Thread outgoing;
    private static Thread incoming;

    public static void main(String[] args) {
        //remove later
        System.out.println("Running Client\n");

        try {
            //makes the Request Store object
            requestStore = new RqstStore();

            //login and gets chat info, then starts background thread, and goes to main screen
            display();

            //blocks main thread until GUI Thread is over
            Thread.currentThread().join();

        } catch (InterruptedException e) {
            e.printStackTrace();

        } 
        System.out.println("\nClient Done");
        System.exit(0); //for cleaning up and running thread
    }

    public void startSocket() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket("localhost", 42069);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   public void closeSocket() {
    try {
        if (socket != null && !socket.isClosed()) {
            out.close();
            in.close();
            socket.close();
            socket = null;
        }
    } catch (IOException e) {
        System.err.println("Error closing socket: " + e.getMessage());
        e.printStackTrace();
    }
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

    public static void display() 
    {
        display = new GUI();
        display.loginScreen();
    }

    public static void startClientThreads() {
        background = new Thread(new BackgroundHandlerClient(), "Background Message Handler");
        incoming = new Thread(new IncomingHandler(), "Incoming Chat Handler");
        outgoing = new Thread(new OutgoingHandler(), "Outgoing Chat Handler");

        background.start();
        incoming.start();
        outgoing.start();
    }

    public static void stopClientThreads() {
        background.interrupt();
//    	incoming.interrupt(); //doesn't need to be interrupted because gets handled when socket closes
//    	outgoing.interrupt();
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
           
            if (loginResponse.getLoginStatus() == LoginType.SUCCESS) {
//                System.out.println("Login successful!");
                loginSucceeded = true;

                //gets account info from server if the login was successful
                account = (Account) in.readObject();
            }
//            else 
//            {
//                System.out.println("Login failed! Please try again.");
//            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return loginSucceeded;

    }

    /**
     * Called by main for getting chat list for clients account after login.
     */
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
     * This takes in a partial name entry from a user search and returns an
     * List<String>
     * that has all user's names that contains partialName String.
     *
     * @param partialName	A partial name string for searching
     * @return ArrayList<String> for all names hat contains partialName
     */
    public static ArrayList<String> searchUserList(String partialName) {
        ArrayList<String> temp = new ArrayList<String>();

        for (String name : userList) {
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

        public BackgroundHandlerClient() {
        }

        @Override
        public void run() {
            try {
                //handles incoming messages from client request store queue
                while (true) {
                    //should block thread because its a blocking queue in request store
                    Object obj = requestStore.getIncoming();

                    //Incoming Chat created
                    if (obj instanceof Chat) {
                        Chat chat = (Chat) obj;
                        chats.add(chat);
                        //update the UI on the EDT
                        SwingUtilities.invokeLater(()
                                -> display.getChatAppGUI().addChatToList(chat.getChatName())
                        );
                        continue;
                    }

                    //Incoming Message
                    if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        //find the right Chat model and add it
                        for (Chat chat : chats) {
                            if (chat.getChatName().equals(msg.getChatname())) {
                                chat.addMessage(msg);
                                //now appends to the open history area
                                SwingUtilities.invokeLater(()
                                        -> display.getChatAppGUI().appendMessage(msg.getAccountName() + ": " + msg.getMsg() + "\n"));
                                break;
                            }
                        }
                        continue;
                    }
                }

            } catch (InterruptedException e) {
//    			System.out.println("Background was interruped");
            }
        }
    }

    //puts the incoming objects into request store addToIncoming()
    private static class IncomingHandler implements Runnable {

        public IncomingHandler() {
        }

        @Override
        public void run() {
            while (true) {
                try {
//					System.out.println("Reading messages!");
                    Object incomingObj = in.readObject();
                    requestStore.addToIncoming(incomingObj);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();

                } catch (InterruptedException e) {
//					System.out.println("Incoming was interruped");
                    break;

                } catch (IOException e) {
//					System.out.println("Socket closed");
                    break;
                }
            }
        }

    }

    //sends objects from request store getOutgoing() to server
    private static class OutgoingHandler implements Runnable {

        public OutgoingHandler() {
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //should block thread because its a blocking queue in request store
                    out.writeObject(requestStore.getOutgoing());
                    out.flush();

                } catch (IOException e) {
//					System.out.println("Socket closed");
                    break;

                } catch (InterruptedException e) {
//					System.out.println("outgoing was interruped");
                    break;
                }
            }
        }

    }
}
