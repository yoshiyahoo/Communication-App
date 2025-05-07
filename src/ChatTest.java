import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for chat functionality
 */
public class ChatTest {
	private Chat chat;
	private String[] users;
	private Message[] msgs;

	@Before
	public void buildChat() {
		this.users = new String[] {
				"Karen",
				"Bill"
		};
		this.msgs = new Message[] {
				new Message("My Boss Sucks", "Karen", "KarenBill", LocalDateTime.now()),
				new Message("You can't delete that", "Bill", "KarenBill", LocalDateTime.now()),
				new Message("Oh shit", "Karen", "KarenBill", LocalDateTime.now()),
				new Message("You can't delete that either", "Bill", "KarenBill", LocalDateTime.now())
		};
		this.chat = new Chat(users, msgs, "KarenBill");
	}

	@Test
	public void CheckChatLength() {
		assertEquals(this.chat.getUsersNames().length, this.users.length);
    }


	@Test
	public void CheckMessageHistory() {
		assertEquals(this.msgs, this.chat.getMsgHistory());
	}
}
