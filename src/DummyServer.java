
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DummyServer {

    public static void main(String[] args) {
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
            ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());

            //Sends a dummy object to initalize a stream. For some reason this is the only one that work?
            //CHECK THIS LATER.
            out.writeObject(null);

            ObjectInputStream in = new ObjectInputStream(cs.getInputStream());

            //login test
            Account test = new Account(Role.EMPLOYEE, "test", "password");
            while (true) {
                Login login = (Login) in.readObject();
                if (login.getUsername().equals("test") && login.getPassword().equals("password")) {
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
//            Account[] users = { 
//                test,
//                new Account(Role.EMPLOYEE, "Bob", "password"),
//                new Account(Role.EMPLOYEE, "John", "password")
//            };
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

            //test sending all users names to client on login
            out.writeObject(userNames);

            //tests both new messages and new chats
            while (true) {
                var objGot = in.readObject();

                if (objGot.getClass().equals(Chat.class)) { //for getting a new chat that was made
                    System.out.println("<<< Got A New Chat >>>\n");

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
                    if (msg.getMsg().equals("done")) {
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
                if (ss != null) {
                    ss.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n<<< Exiting Server >>>");
    }
}
