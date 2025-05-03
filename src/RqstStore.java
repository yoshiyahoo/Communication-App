import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/*
This will hold all messages incoming from one object and outgoing to another object
 */
public class RqstStore {
    private BlockingQueue<Object> incoming;
    private BlockingQueue<Object> outgoing;

    public RqstStore() {
    	incoming = new LinkedBlockingQueue<>();
    	outgoing = new LinkedBlockingQueue<>();
    }
    //Gets message from incoming queue (blocks until a message is available)
    public Object getIncoming() throws InterruptedException {
    	return incoming.take();
    }
    
    //Adds message to incoming queue (blocks if necessary until space is available)
    public void addToIncoming(Object obj) throws InterruptedException {
    	incoming.put(obj);
    }
    //Gets a message from the outgoing queue (blocks until a message is available)
    public Object getOutgoing() throws InterruptedException {
    	return outgoing.take();
    }
    //Adds message to outgoing queue (blocks if necessary until space is available)
    public void addToOutGoing(Message msg) throws InterruptedException {
    	outgoing.put(msg);
    }
    
    public void addToOutGoing(Chat chat) throws InterruptedException {
    	outgoing.put(chat);
    }
}
