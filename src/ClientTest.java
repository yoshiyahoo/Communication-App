import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * This module tests many client methods.
 * Hard to test a lot of the methods in Client because
 * they mainly interact with Server.
 * In other words just tests Client getters.
 */
public class ClientTest {

	private static Client client = new Client();
	private static List<Chat> testChats;
	private static String[] testUsers;
	private static Account testAccount;
	
	@BeforeAll
	public static void initialize() {
		try {
			//putting in test data for chats private variable for getter testing
			Field chats = client.getClass().getDeclaredField("chats");
			chats.setAccessible(true);
			
			testChats = new ArrayList<Chat>();
			testChats.add(new Chat(
					new String[] { "Mario", "Peach" },
					"Mario & Peach Chat"
			));
			testChats.add(new Chat(
					new String[] { "Toad", "Bowser" },
					"Toad & Bowser Chat"
			));
			
			chats.set(chats, testChats);
			
			//putting in test data for userList private variable for getter testing
			Field userList = client.getClass().getDeclaredField("userList");
			userList.setAccessible(true);
			
			testUsers = new String[] {
					"Mario",
					"Peach",
					"Toad",
					"Bowser"
			};
			
			userList.set(userList, testUsers);
			
			//putting in test data for account private variable for getter testing
			Field account = client.getClass().getDeclaredField("account");
			account.setAccessible(true);
			
			testAccount = new Account(Role.EMPLOYEE, "Wario", "Bad_Mario");
			
			account.set(account, testAccount);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(0)
	public void testGetChats() {
		List<Chat> testChatList = client.getChats();
		
		assertEquals(
				testChatList.get(0), 
				testChats.get(0)
		);
		
		assertEquals(
				testChatList.get(1), 
				testChats.get(1)
		);
	}
	
	@Test
	@Order(1)
	public void testGetUserList() {
		String[] testUserList = client.getUserList();
		
		for(int i = 0; i < testUserList.length; i++) {
			assertEquals(testUserList[i], testUsers[i]);
		}
	}
	
	@Test
	@Order(2)
	public void testGetAccount() {
		assertEquals(client.getUserAccount(), testAccount);
	}
}