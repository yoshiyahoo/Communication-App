import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {

	@Test
	public void AddMessages() {
		Account[] users = new Account[] {
			new Account(Role.EMPLOYEE, "Bill", ""),
			new Account(Role.ADMINISTRATOR, "Michael", ""),
			new Account(Role.ADMINISTRATOR, "Joe", ""),
			new Account(Role.EMPLOYEE, "Karen", ""),
		};
		
		//public Message(String msg, String accountName, String chatname, int chatID) {
		Message[] msgs = new Message[] {
			new Message("My Boss Sucks", "Karen", "KarenBill", LocalDateTime.now())
		};
		
		Chat chat = new Chat(users,msgs, "KarenBill");
		
		assertEquals(chat.getUsers().length, users.length);
    }

	@Test
	public void CheckMessageTiming() {
		Message msg = new Message("Hi mom", "Tommy", "TommyMom", LocalDateTime.now());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail();
		}

		assertNotEquals(msg.getTime(), LocalDateTime.now());
	}
}
