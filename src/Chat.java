import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
This is a chat that contains users and messages
The object is meant to be sent over the network
You can only add messages, you can't remove
 */
public class Chat implements Serializable {
    private List<Message> msgHistory;
    private String chatName;
    private List<String> usersNames;
    
    //Clients shouldn't get Accounts of other users
    //So I made this one but probably should remove/change the others
    public Chat(String[] usersNames, String chatName) {
    	this.usersNames = new ArrayList<>(Arrays.asList(usersNames));
    	this.chatName = chatName;
    	this.msgHistory = new ArrayList<Message>();
    }
    
    public Chat(String[] usersNames, Message[] msgHistory, String chatName) {
    	this.usersNames = new ArrayList<>(Arrays.asList(usersNames));
    	this.msgHistory = new ArrayList<>(Arrays.asList(msgHistory));
    	this.chatName = chatName;
    }

    public String getChatName() {
    	return this.chatName;
    }
    
    public String[] getUsersNames() {
    	return this.usersNames.toArray(new String[0]);
    }
    
    public void addMessage(Message msg) {
    	msgHistory.add(msg);
    }

    public Message[] getMsgHistory() {
    	return this.msgHistory.toArray(Message[]::new);
    }
}
