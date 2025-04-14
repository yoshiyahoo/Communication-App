import java.io.File;
import java.util.List;

public class Database {
    private List<File> fileSystem;

    // why are these fields here?
    private boolean inUse; // what is this for?
    private Message msg;
    private Chat[] chats;

    // How do we read the data into the database?
    public Database() {

    }

    // what happens if there are two messages at the same time?
    // Wouldn't we want to pass the account itself and not just the name? idk
    public Message getMessage(String accountName, int time) {

    }

    // which chat is this message saved in?
    public void saveMessage(Message msg) {

    }

    // how do we search for a chat?
    public Chat getChat() {

    }

    // how do we add a chat if we don't get one?
    public void addChat() {

    }

    public Account getAccount(String name) {

    }
}
