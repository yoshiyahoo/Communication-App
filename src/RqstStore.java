import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/*
This will hold all messages incoming from one object and outgoing to another object
 */
public class RqstStore {
    private BlockingQueue<Message> incoming;
    private BlockingQueue<Message> outgoing;

    public RqstStore() {
    	incoming = new LinkedBlockingQueue<>();
    	outgoing = new LinkedBlockingQueue<>();
    }
    //Gets message from incoming queue (blocks until a message is available)
    public Message getIncoming() throws InterruptedException {
    	return incoming.take();
    }
    
    //Adds message to incoming queue (blocks if necessary until space is available)
    public void addToIncoming(Message msg) throws InterruptedException {
    	incoming.put(msg);
    }
    //Gets a message from the outgoing queue (blocks until a message is available)
    public Message getOutgoing() throws InterruptedException {
    	return outgoing.take();
    }
    //Adds message to outgoing queue (blocks if necessary until space is available)
    public void addToOutGoing(Message msg) throws InterruptedException {
    	outgoing.put(msg);
    }
    
}
