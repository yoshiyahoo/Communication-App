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
    private List<Account> users; 
    private List<Message> msgHistory;
    private String chatName;
    
    // Additional constructor for creating new chat
    public Chat(Account[] users, String chatName) {
    	this.users = new ArrayList<>(Arrays.asList(users));
    	this.chatName = chatName;
    }

    public Chat(Account[] users, Message[] msgHistory, String chatName) {
    	this.users = new ArrayList<>(Arrays.asList(users));
    	this.msgHistory = new ArrayList<>(Arrays.asList(msgHistory));
    	this.chatName = chatName;
    }

    public void addUser(Account account) {
    	this.users.add(account);
    }

    public void removeUser(Account account) {
    	this.users.remove(account);
    }
    
    public String getChatName() {
    	return this.chatName;
    }
    
    public Account[] getUsers() {
    	return (Account[]) this.users.toArray();
    }
    
    public void addMessage(Message msg) {
    	msgHistory.add(msg);
    }

    public Message[] getMsgHistory() {
    	return this.msgHistory.toArray(new Message[0]);
    }
}
