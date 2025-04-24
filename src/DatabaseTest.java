import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class DatabaseTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File usersFile;
    private File chatsFolder;

    @Before
    public void setUp() throws IOException {
        File dbFolder = tempFolder.newFolder("Database");
        usersFile = new File(dbFolder, "Users.txt");

        try (FileWriter writer = new FileWriter(usersFile)) {
            writer.write("employee,Alice\n");
            writer.write("employee,Bob\n");
        }

        chatsFolder = new File(dbFolder, "Chats");
        chatsFolder.mkdir();

        File chatFile = new File(chatsFolder, "testchat.txt");
        try (FileWriter writer = new FileWriter(chatFile)) {
            writer.write("Alice,Bob\n");
            writer.write("2024-01-01T10:00,Alice,Hello\n");
            writer.write("2024-01-01T10:01,Bob,Hi\n");
        }

        System.setProperty("user.dir", tempFolder.getRoot().getAbsolutePath());
    }

    @Test
    public void testDatabaseInitialization() throws IOException {
        Database db = new Database();

        Account alice = db.getAccount("Alice");
        assertNotNull(alice);
        assertEquals("Alice", alice.getName());

        Chat chat = db.getChat("testchat");
        assertNotNull(chat);
        assertEquals("testchat", chat.getMsgHistory()[0].getChatname()));
    }

    @Test
    public void testGetMessages() throws IOException {
        Database db = new Database();
        LocalDateTime ldt = LocalDateTime.parse("2024-01-01T10:00");
        List<Message> msgs = db.getMessages("Alice", ldt);

        assertNotNull(msgs);
        assertEquals(1, msgs.size());
        assertEquals("Hello", msgs.get(0).getMsg());
    }

    // Add more tests for saveMessage(), addChat(), etc.

}
