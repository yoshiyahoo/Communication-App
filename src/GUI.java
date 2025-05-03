
import java.util.*; //remove scanner when implementing graphics

public class GUI {

    private Client client; // why do we have the client as an attribute?
    private Scanner scan; // remove when implementing graphics

    //just for testing change later
    public GUI(Client client) {
        this.client = client;
        this.scan = new Scanner(System.in);
    }

    
    public void loginScreen() 
	{
		boolean success = false;
		LoginScreen loginScreen = new LoginScreen(this.client);
        do 
		{
            success = loginScreen.getLoginStatus();
			
        } 
		while (!success);

    }
		
    //just for testing change later
    public void mainScreen() {
        Chat currentChat = null;
        int choice = -1;

        do {
            System.out.println(
                    "1) make chat\n"
                    + "2) switch to chat\n"
                    + "3) current chat history\n"
                    + "4) send msg to current chat\n"
                    + "5) logout/exit\n"
            );

            try {
                choice = this.scan.nextInt();
                this.scan.nextLine();

                switch (choice) {
                    case 1:
                        //finish this when makeChat() is made
//					this.client.makeChat();
                        System.out.println("Not made yet");
                        break;
                    case 2:
                        System.out.println("Enter Chatname >>> ");
                        String chatToGoTo = this.scan.nextLine();

                        for (Chat chat : this.client.getChats()) {
                            if (chat.getChatName().equals(chatToGoTo)) {
                                currentChat = chat;
                                break;
                            }
                        }

                        System.out.println("\n=== Switched to " + currentChat.getChatName() + " ===\n");
                        break;
                    case 3:
                        System.out.println("\n=== Chat History for " + currentChat.getChatName() + " ===\n");

                        for (Message msg : currentChat.getMsgHistory()) {
                            System.out.println(
                                    msg.getAccountName()
                                    + " "
                                    + msg.getTime().getHour()
                                    + ":"
                                    + msg.getTime().getMinute()
                                    + " >>> "
                                    + msg.getMsg()
                            );
                        }

                        System.out.println();
                        break;
                    case 4:
                        //need to be able to switch to a chat to make
//					System.out.println("Not made yet");
                        break;
                    case 5:
                        //maybe should have a logout method on client and server
                        //instead of just catching and handling on server
                        //re think about it
                        System.out.println("=== Logging out ===");
                        break;
                    default:
                        System.out.println("Bad input!!!");
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Bad input!!!");
            }

        } while (choice != 5);

        System.out.println("\n=== Exiting Client ===");
    }
}
