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
            // Setup the server
            ss = new ServerSocket(42069);
            ss.setReuseAddress(true);

            System.out.println("\n<<< Server is Running >>>");
            System.out.println("\nTo Stop Server Press Ctrl+C\n");
            System.out.println("Server IP: localhost\n"
                    + "Server Port: "
                    + ss.getLocalPort()
                    + "\n");

            Socket cs = ss.accept();  // Accepting the client connection
            ObjectOutputStream out = new ObjectOutputStream(cs.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(cs.getInputStream());

            // Dummy Account setup for login test
            Account test = new Account(Role.EMPLOYEE, "test", "password");

            // Login loop: verify credentials
            while (true) {
                Login login = (Login) in.readObject();
                if (login.getUsername().equals("test") && login.getPassword().equals("password")) {
                    // Sends success login response
                    Login responseLogin = new Login(LoginType.SUCCESS);
                    out.writeObject(responseLogin);

                    // Send client account info
                    out.writeObject(test);
                    break; // Exit the login loop once logged in
                } else {
                    // Sends failure login response
                    Login responseLogin = new Login(LoginType.FAILURE);
                    out.writeObject(responseLogin);
                }
            }
            System.out.println("<<< Login Worked >>>\n");

            // Initial chat setup
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

            // Sending initial chats to the client
            out.writeObject(testChatList);
            out.flush();

            // Send all usernames to the client
            out.writeObject(userNames);

            // Main chat/message handling loop
            while (true) {
                Object objGot = in.readObject();

                if (objGot instanceof Chat) {
                    // Handle new chat creation
                    Chat newChat = (Chat) objGot;
                    System.out.println("<<< Got A New Chat >>>\n");

                    // Add the new chat to the server's list
                    testChatList.add(newChat);

                    // Send the new chat to the client
                    out.writeObject(newChat);
                    out.flush();
                    System.out.println(">>> Sent CHAT_CREATED notification back to client\n");

                } else if (objGot instanceof Message) {
                    // Handle new message
                    System.out.println("<<< Got A New Message >>>\n");
                    Message msg = (Message) objGot;

                    // Process message (e.g., convert to uppercase)
                    Message responseMsg = new Message(
                            msg.getMsg().toUpperCase(),
                            msg.getAccountName(),
                            msg.getChatname()
                    );
                    out.writeObject(responseMsg);

                    // If the message is "done", exit the loop
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
                    ss.close();  // Close the server socket
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n<<< Exiting Server >>>");
    }
}
