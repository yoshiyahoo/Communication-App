import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.*; //for managing and controlling thread execution 

/**
 * These are the unit tests for the request store
 */
public class RqstStoreTest{
	
	// A single-threaded scheduler used to defer puts in order to unblock take() in tests
	private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	
	//for cleanup
	@AfterAll
	public static void shutdownScheduler() {
		scheduler.shutdown();
	}
	
	@Test
	@Timeout(1) //fail if still blocked after 1 second
	public void testAddToIncomingAndGetIncoming() throws Exception {
		RqstStore store = new RqstStore();
		Message msg = new Message("hi", "ryan", "chat1");
		
		//schedule an addToIncoming() 100ms later so getIncoming() really blocks first
		scheduler.schedule(() -> {
			try {
				store.addToIncoming(msg);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}, 100, TimeUnit.MILLISECONDS);
		
		//Should unblock and return the same Message
		Message received =  (Message) store.getIncoming();
		assertSame(msg, received, "getIncoming() must return exactly the Message put");
	}
	
	@Test
	@Timeout(1)
	public void testAddToOutgoingAndGetOutgoing() throws Exception {
		RqstStore store = new RqstStore();
		Message msg = new Message("bye", "bob", "chat2");
		
	    // schedule an addToOutGoing() 100ms later so getOutgoing() really blocks first
		scheduler.schedule(() -> {
			try {
				store.addToOutGoing(msg);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}, 100, TimeUnit.MILLISECONDS);
		
		//Should unblock and return the same Message
		Message received = (Message) store.getOutgoing();
		assertSame(msg, received, "getOutgoing() must return exactly the Message put");
	}
}