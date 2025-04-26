import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class ChatTest {

	@Test
	private void AddMessages() {
		Account[] users = new Account[] {
			new Account(Role.EMPLOYEE, "Bill"),
			new Account(Role.ADMINISTRATOR, "Michael"),
			new Account(Role.ADMINISTRATOR, "Joe"),
			new Account(Role.EMPLOYEE, "Karen"),
		};
		
		//public Message(String msg, String accountName, String chatname, int chatID) {
		Message[] msgs = new Message[] {
			new Message("My Boss Sucks", "Karen", "KarenBill", 3)
		};
		
		Chat chat = new Chat(users,msgs, "KarenBill");
		
		assertEquals(chat.getMsgHistory().length, users.length);
	}

}
