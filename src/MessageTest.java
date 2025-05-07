import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * This class holds unit tests for messages.
 */
public class MessageTest {
	//makes a test Message without a given time
	private Message test = new Message("Test Message!!!", "Bobby", "BobbyAndFriends");

	//makes a test Message with a given time
	private LocalDateTime testTime = LocalDateTime.now();
	private Message test2 = new Message("Test Message two!!!", "Jeff", "JeffAndFriends", testTime);

	@Test
	@Order(0)
	public void testMessageGetMsg() {
		//makes sure the passed message string is returned correctly
		assertEquals("Test Message!!!", test.getMsg());
		assertEquals("Test Message two!!!", test2.getMsg());
	}
	
	@Test
	@Order(1)
	public void testMessageGetTime() {
		//Tests that the right time object is returned
		assertEquals(LocalDateTime.class, test.getTime().getClass());

		//test if given time is equal to the getTime()
		assertEquals(testTime, test2.getTime());
	}

	@Test
	@Order(2)
	public void testMessageGetAccountName() {
		//makes sure that the passed account name string is returned the same
		assertEquals("Bobby", test.getAccountName());
		assertEquals("Jeff", test2.getAccountName());
	}
	
	@Test
	@Order(3)
	public void testMessageGetChatName() {
		//makes sure that the passed chat name string is returned the same
		assertEquals("BobbyAndFriends", test.getChatname());
		assertEquals("JeffAndFriends", test2.getChatname());
	}

	@Test
	@Order(4)
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