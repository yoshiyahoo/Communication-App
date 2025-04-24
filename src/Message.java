import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

//These are text messages that will be passed between the server and client application
public class Message implements Serializable {
	private String msg;
	private Date time;
	private String accountName;
	private String chatname;
	private int chatID;

	/**
	 * Only constructor for Message class
	 * 
	 * @param msg			A string of the user message to be sent over network.
	 * @param accountName	A String of the name from the current account trying to send Message.
	 * @param chatname		A name of the Chat the new Message is being made for.
	 * @param chatID		The ID integer for the given Chat the Message is being made for.
	 */
	public Message(String msg, String accountName, String chatname, int chatID) {
		this.msg = msg;
		this.time = new Date();
		this.accountName = accountName;
		this.chatname = chatname;
		this.chatID = chatID;
	}

	public Message(String string, LocalDateTime localDateTime, String string2) {
        //TODO Auto-generated constructor stub
    }

    /**
	 * For retrieving the message string from this Message Object.
	 * 
	 * @return	The message string from this Object.
	 */
	public String getMsg() {
		return this.msg;
	}

	/**
	 * For retrieving the Date Object for when this Message Object was created.
	 * 
	 * @return	The Date Object for when this Message Object was made.
	 */
	public Date getTime() {
		return this.time;
	}

	/**
	 * For retrieving the string name of the user that made this Message Object.
	 * 
	 * @return	The string name of user who made this Message.
	 */
	public String getAccountName() {
		return this.accountName;
	}

	/**
	 * For retrieving the string Chat name that this Message was made for.
	 * 
	 * @return	The string name of the Chat that this Message is tied to.
	 */
	public String getChatname() {
		return this.chatname;
	}

	/**
	 * For retrieving the integer ID of the Chat this Message was made for.
	 * 
	 * @return	The integer for the Chat ID this Message is tied to.
	 */
	public int getChatID() {
		return this.chatID;
	}
}
