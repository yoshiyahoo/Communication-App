import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseTest {
    private Database db;
    private LocalDateTime testTime;

    @Before
    public void setUp() throws IOException {
        // Setup temporary Users.txt
        Path usersPath = Paths.get("./src/Database/Users.txt");
        Files.createDirectories(usersPath.getParent());
        Files.write(usersPath, "employee,John\nadmin,Alice".getBytes());

        db = new Database();

        // Setup temporary Chats directory and a sample chat file;
        testTime = LocalDateTime.now();
        String[] ul = {"John", "Alice"};
        db.addChat(ul, "General");
        Message m = new Message("Hello everyone!", "John", "General", 0, testTime);
        db.saveMessage(m);
    }

    @After
    public void tearDown() throws IOException {
        // Cleanup created files/folders
        Path dbPath = Paths.get("./Database");
        if (Files.exists(dbPath)) {
            Files.walk(dbPath)
                 .sorted((a, b) -> b.compareTo(a)) // delete children first
                 .forEach(path -> {
                     try { Files.delete(path); } catch (IOException e) { /* ignore */ }
                 });
        }
    }

    @Test
    public void testGetAccount() {
        Account account = db.getAccount("John");
        assertNotNull(account);
        assertEquals("John", account.getName());
    }

    @Test
    public void testGetChat() {
        Chat chat = db.getChat("General");
        assertNotNull(chat);
        assertEquals("General", chat.getChatName());
    }

    @Test
    public void testGetMessages() {
        List<Message> messages = db.getMessages("John", testTime);
        assertNotNull(messages);
        assertFalse(messages.isEmpty());
        assertEquals("John", messages.get(0).getAccountName());
    }

    @Test
    public void testAddChat() throws IOException {
        String[] users = {"John", "Alice"};
        db.addChat(users, "NewChat");
        Chat chat = db.getChat("NewChat");
        assertNotNull(chat);
        assertEquals("NewChat", chat.getChatName());
    }

    @Test
    public void testSaveMessage() throws IOException {
        Message msg = new Message("This is a test", "John", "General", 0, LocalDateTime.now());

        db.saveMessage(msg);

        // Check if file was updated (very basic check)
        Path chatFile = Paths.get("./src/Database/Chats/General.txt");
        String content = new String(Files.readAllBytes(chatFile));
        assertTrue(content.contains("This is a test"));
    }
}
