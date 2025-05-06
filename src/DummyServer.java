import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DummyServer {

    public static void main(String[] args) {
    	while(true) {
            ServerSocket ss = null;
            try {
                //dummy server setup
                ss = new ServerSocket(42069);
                ss.setReuseAddress(true);
                
                System.out.println("\n<<< Server is Running >>>");
    			System.out.println("\nTo Stop Server Press Ctrl+C\n");
                System.out.println("Server IP: localhost\n" 
                                    + "Server Port: "
                                    + ss.getLocalPort() 
                                    + "\n");

                Socket cs = ss.accept();
                ObjectInputStream in = new ObjectInputStream(cs.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());

                //login test
                Account test = new Account(Role.EMPLOYEE, "test", "password");
                while(true) {
                    Login login = (Login) in.readObject();
                    if(login.getUsername().equals("test") && login.getPassword().equals("password")) {
                        //sends a success login
                        Login responseLogin = new Login(LoginType.SUCCESS);
                        out.writeObject(responseLogin);

                        //sending over clients account info
                        out.writeObject(test);
                        break;
                    } else {
                        //sends a failure login
                        Login responseLogin = new Login(LoginType.FAILURE);
                        out.writeObject(responseLogin);
                    }
                }
                System.out.println("<<< Login Worked >>>\n");

                //test sending initial chats
//                Account[] users = { 
//                    test,
//                    new Account(Role.EMPLOYEE, "Bob", "password"),
//                    new Account(Role.EMPLOYEE, "John", "password")
//                };
                String[] userNames = {
                    "test",
                    "Bob",
                    "John"
                };
                Message[] msgHistory = { 
                    new Message("test msg 1", "test", "TestChat"),
                    new Message("test msg 2", "test", "TestChat")
                };
                List<Chat> testChatList = new ArrayList<Chat>();
                testChatList.add(new Chat(userNames, msgHistory, "TestChat"));
                testChatList.add(new Chat(userNames, msgHistory, "TestChat2"));
                out.writeObject(testChatList);
                out.flush();

                //test sending all users names to client on login
                out.writeObject(userNames);

                //tests both new messages and new chats
                while(true) {
                    Object objGot = in.readObject();

                    if(objGot instanceof Chat) { //for getting a new chat that was made
                        Chat newChat = (Chat) objGot;
                    	System.out.println("<<< Got A New Chat >>>\n");
                    	
                    	//add it to the server's list
                    	testChatList.add(newChat);
                    	
                    	//send that new chat back so the client can immediately display it
                    	out.writeObject(newChat);
                    	out.flush();
                    	System.out.println(">>> Sent CHAT_CREATED notification back to client\n");

                    } else { //for getting a new message
                        System.out.println("<<< Got A New Message >>>\n");
                        Message msg = (Message) objGot;
                        // Message msg = (Message) in.readObject();
                        Message responseMsg = new Message(
                            msg.getMsg().toUpperCase(), 
                            msg.getAccountName(), 
                            msg.getChatname()
                        );
                        out.writeObject(responseMsg);
                        if(msg.getMsg().equals("done")) {
                            break;
                        }
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                
            } catch (IOException e) {
                System.out.println("<<< Client Logged Out >>>");
                
            } finally {
                try {
                    if(ss != null) {
                        ss.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    	}

//        System.out.println("\n<<< Exiting Server >>>");
    }
}
