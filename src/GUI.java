
import java.util.*; //remove scanner when implementing graphics

public class GUI extends Client{
//	private Client client; // why do we have the client as an attribute?
	private Scanner scan; // remove when implementing graphics

	//just for testing change later
	public GUI() {
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
//					System.out.println("Not made yet");
					
					for(String name : super.getUserList()) {
						System.out.println(name);
					}
					System.out.println();
					
					ArrayList<String> users = new ArrayList<String>();
					String input = "";
					System.out.println("Input user's names for chat: "
							+ "( enter '!' to finish )"
					);
					while(true) {
						System.out.print("enter name >>> ");
						input = this.scan.nextLine();
						if(input.equals("!")) {
							break;
						}
						users.add(input);
					}
					
					System.out.print("\nNow enter the chat name >>> ");
					input = this.scan.nextLine();
					
					super.makeChat(users.toArray(new String[0]), input);
					
					System.out.println("\n<<< Made Chat: " + input + " >>>\n");
					break;
				case 2:
					boolean isAChat = false;
					do {
						System.out.println();
						for(Chat chat : super.getChats()) {
							System.out.println(chat.getChatName());
						}
						
						System.out.print("\nEnter Chatname >>> ");
						String chatToGoTo = this.scan.nextLine();
						
						for(Chat chat : super.getChats()) {
							if(chat.getChatName().equals(chatToGoTo)) {
								currentChat = chat;
								isAChat = true;
								break;
							}
						}
						
					} while(!isAChat);
					
					System.out.println("\n=== Switched to " + currentChat.getChatName() + " ===\n");
					break;
				case 3:
					if(currentChat == null) {
						System.out.println("\nCan't see chat history if not in a chat\n");
						break;
					}
					
					System.out.println("\n=== Chat History for " + currentChat.getChatName() + " ===\n");
					
					for(Message msg : currentChat.getMsgHistory()) {
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
					if(currentChat == null) {
						System.out.println("\nCan't send a message if your not in a chat\n");
						break;
					}
					
					String text = "";
					
					System.out.print("\nenter new message >>> ");
					text = this.scan.nextLine();
					
					Message msg = new Message(text, super.getUserAccount().getName(), currentChat.getChatName());
					
					super.sendMsg(msg);
					
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
				
			} catch(InputMismatchException e) {
				System.out.println("Bad input!!!");
			}
			
		} while(choice != 5);
		
		System.out.println("\n=== Exiting Client ===");
	}
}
