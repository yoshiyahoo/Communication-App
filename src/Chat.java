import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
/*
This is a chat that contains users and messages
 */
// Needs to implement Serializable?
public class Chat implements Serializable {
    private List<Account> users; // can't we make this accounts?
    private List<Message> msgHistory;
    private String chatName;

    public Chat(Account[] users, Message[] msgHistory, String chatName) {
    	this.users = Arrays.asList(users);
    	this.msgHistory = Arrays.asList(msgHistory);
    	this.chatName = chatName;
    }

    public void addUser(Account account) {
    	this.users.add(account);
    }

    public void removeUser(Account account) {
    	this.users.remove(account);
    }
    
    // Added extra methods
    public void addMessage(Message msg) {
    	msgHistory.add(msg);
    }

    public Message[] getMsgHistory() {
    	return (Message[]) this.msgHistory.toArray();
    }
}
