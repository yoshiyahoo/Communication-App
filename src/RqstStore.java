import java.util.LinkedList;
import java.util.Queue;
/*
This will hold all messages incoming from one object and outgoing to another object
 */
public class RqstStore {
    private Queue<Message> incoming;
    private Queue<Message> outgoing;

    public RqstStore() {
    	incoming = new LinkedList<>();
    	outgoing = new LinkedList<>();
    }

    public Message getIncoming() {
    	synchronized (incoming) {
    		return incoming.poll(); //removes and returns the head of the queue
    	}
    }
    
    //For storing incoming messages
    public void addToIncoming(Message msg) {
    	synchronized (incoming) {
    		incoming.add(msg);
    	}
    }

    public void addToOutGoing(Message msg) {
    	synchronized (outgoing) {
    		outgoing.add(msg);
    	}
    }
    
}
