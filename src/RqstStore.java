import java.util.concurrent.SynchronousQueue;
/*
This will hold all messages incoming from one object and outgoing to another object
 */
public class RqstStore {
    private SynchronousQueue<Message> incoming;
    private SynchronousQueue<Message> outgoing;

    public RqstStore() {
    	incoming = new SynchronousQueue<>();
    	outgoing = new SynchronousQueue<>();
    }
    //Gets message from incoming queue (blocks until a message is available)
    public Message getIncoming() throws InterruptedException {
    	return incoming.take(); //retrieves and removes the head of queue
    }
    
    //Adds message to incoming queue (blocks until a receiver is ready)
    public void addToIncoming(Message msg) throws InterruptedException {
    	incoming.put(msg);
    }
    //Gets a message from the outgoing queue (blocks until a message is available)
    public Message getOutgoing() throws InterruptedException {
    	return outgoing.take(); 
    }
    //Adds message to outgoing queue (blocks until a receiver is ready)
    public void addToOutGoing(Message msg) throws InterruptedException {
    	outgoing.put(msg);
    }
    
}
