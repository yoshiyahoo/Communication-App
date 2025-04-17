/*
These are text messages that will be passed between the server and client application
 */

// Needs to implement Serializeable
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String msg;
    private Date time; // Discussion? Redo a int with an actual time?
    private String accountName;
    private String chatname;
    private int chatID;

    public Message(String msg, String accountName, String chatname, int chatID) {
    	this.msg = msg;
    	this.time = new Date();
    	this.accountName = accountName;
    	this.chatname = chatname;
    	this.chatID = chatID;
    }

    public String getMsg() {
    	return this.msg;
    }

    public Date getTime() {
    	return this.time;
    }

    public String getAccountName() {
    	return this.accountName;
    }

	public String getChatname() {
		return this.chatname;
	}

	public int getChatID() {
		return this.chatID;
	}
}
