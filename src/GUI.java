
public class GUI extends Client {

	private ChatAppGUI chatAppGUI = null;

    public void loginScreen() {
        new LoginScreen(this::getChatAppGUI);
    }

	public ChatAppGUI getChatAppGUI()
	{
		if(chatAppGUI == null)
		{
			chatAppGUI = CreateChatAppGUI();
		}
		
		return chatAppGUI;
	}

	private ChatAppGUI CreateChatAppGUI()
	{
		chatAppGUI = new ChatAppGUI(this.getUserAccount());
		super.getChatFromServer();
    		for(Chat chat : super.getChats()) 
			{
    			chatAppGUI.chatListModel.addElement(chat.getChatName());
    		}
    		
        	super.getUserNamesFromServer();

        	super.startClientThreads();
		return chatAppGUI;
	}
}
