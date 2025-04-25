import java.io.Serializable;
import java.time.LocalDateTime;

//These are text messages that will be passed between the server and client application
public class Message implements Serializable {
	private String msg;
	private LocalDateTime time;
	private String accountName;
	private String chatname;

	/**
	 * Constructor for making new user Message.
	 * 
	 * @param msg			A string of the user message to be sent over network.
	 * @param accountName	A String of the name from the current account trying to send Message.
	 * @param chatname		A name of the Chat the new Message is being made for.
	 */
	public Message(String msg, String accountName, String chatname) {
		this.msg = msg;
		this.time = LocalDateTime.now();
		this.accountName = accountName;
		this.chatname = chatname;
	}
	
	/**
	 * Constructor for making a Message from saved chat logs.
	 * Should only use in Database.
	 * 
	 * @param msg			A string of the user message to be sent over network.
	 * @param accountName	A String of the name from the current account trying to send Message.
	 * @param chatname		A name of the Chat the new Message is being made for.
	 * @param time			A Time that was saved for this Message on creation in Database.
	 */
	public Message(String msg, String accountName, String chatname, LocalDateTime time) {
		this.msg = msg;
		this.time = time;
		this.accountName = accountName;
		this.chatname = chatname;
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
	 * For retrieving the LocalDateTime Object for when this Message Object was created.
	 * 
	 * @return	The LocalDateTime Object for when this Message Object was made.
	 */
	public LocalDateTime getTime() {
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
}
