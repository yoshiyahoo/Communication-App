import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Database {
    private HashMap<Integer, String> users;
    private Path chats;

    public Database() throws IOException {
        // populate the Users hashmap using users file
        users = new HashMap<>();
        File usersFile = new File("./database/Users.txt");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(usersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cols = line.split(",");
                if (cols.length == 2) {
                    int id = Integer.parseInt(cols[0].trim());
                    String name = cols[1].trim();
                    users.put(id, name);
                } else {
                    throw new IOException("Invalid line format in Users.txt: " + line);
                }
            }
        }

        // get Chats path
        Path currentRelativePath = Path.of("");
        String s = currentRelativePath.toAbsolutePath().toString();
        chats = Path.of(s + "/database/Chats/");
    }

    public Message getMessage(String accountName, int time) {

    }

    public void saveMessage(Message msg) {

    }

    public Chat getChat() {

    }

    public void addChat(String[] userList, String chatName) throws IOException {
        // create a new chat
        Chat chat = new Chat(userList, new Message[0], chatName);
        // save the chat to the database
        try {
            Files.createFile(chats.resolve(chatName + ".txt"));
        } catch (FileAlreadyExistsException faee) {
            // alert user that the chat already exists
            // TODO use uuids instead if duplicate chat names are okay
            System.out.println("Chat with this name already exists.");
        }
    }

    public Account getAccount(String name) {

    }
}
