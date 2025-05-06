import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
 * User.txt schema
 * ROLE,NAME,PASSWORD
 * 
 * Chat folder schema
 * filename=[chatname].txt
 * 
 * user1,user2,user3...
 * TIME,USERNAME,MSG1
 * TIME,USERNAME,MSG2
 * TIME,USERNAME,MSG3
 * ...
 */

public class Database {
    //NOTE: multiple client threads can access and write to the db at the same time
    //We can either make the maps concurrent or make the methods synchronous
    private HashMap<String, Account> acctNameObjMap;
    private HashMap<String, Chat> chatNameObjMap;
    public static final String USERS_PATH = "./Database/Users.txt";
    public static final String CHATS_DIR = "./Database/Chats/";
    // key = username (non-admin), val = set of chatnames w/ user
    private HashMap<String, HashSet<String>> userChatMap;

    /**
     * Constructor for Database class
     * loads users and chats from files
     * Users.txt MUST exist
     * 
     * @throws IOException 
     */
    public Database() throws IOException {
    	this.acctNameObjMap = new HashMap<>();
        this.chatNameObjMap = new HashMap<>();
        this.userChatMap = new HashMap<>();
    	
    	// get users into acct map
        {        			
            List<String> lines = Files.readAllLines(Paths.get(USERS_PATH));
            for(String line : lines) {
                String[] cols = line.split(",");
                Role r = (cols[0].equals("admin"))? Role.ADMINISTRATOR : Role.EMPLOYEE;
                acctNameObjMap.put(cols[1], new Account(r, cols[1], cols[2]));
            }
        }

        // get chats into chat map
        File chatsFolder = new File(CHATS_DIR);
        File[] files = chatsFolder.listFiles();
        if (files == null) return;

        for(File file : files) {
            if(!file.isFile()) continue;
            List<String> lines = Files.readAllLines(file.toPath());
            
            int dotIndex = file.getName().indexOf('.');
            String chatName = file.getName().substring(0, dotIndex);

            String[] userList = lines.get(0).split(",");

            // TODO do we need a chat id anymore?
            Message[] msgHistory = lines.stream()
                .skip(1)
                .map(line -> line.split(","))
                .map(cols -> new Message(
                		cols[3], 
                		cols[2], 
                		chatName, 
                		LocalDateTime.parse(cols[1])
                ))
                .toArray(Message[]::new);

            chatNameObjMap.put(chatName, new Chat(userList, msgHistory, chatName));

            // add chat to userChatMap for each user
            for(String user : userList) {
                // creates set if DNE, always inserts chatname
                userChatMap
                    .computeIfAbsent(user, k -> new HashSet<>())
                    .add(chatName);
            }
        }
    }

    /**
     * Get all messages in a given chat
     * based on sender and send time
     * 
     * @param accountName Name of sender
     * @param ldt Time message was sent
     * @return List of messages, ordered by chatname
     */
    public List<Message> getMessages(String accountName, LocalDateTime ldt) {
        List<Message> mList = null;

        for(String chatName : userChatMap.get(accountName)) {
            Chat toCheck = chatNameObjMap.get(chatName);
            for(Message m : toCheck.getMsgHistory()) {
                if(!m.getTime().equals(ldt)) continue;
                if(m.getAccountName().equals(accountName)) {
                    if(mList == null) mList = new ArrayList<>();
                    mList.add(m);
                }
            }
        }

        return mList;
    }

    /**
     * Save new message to chat obj msgHistory
     * and to chat log file
     *  
     * @param msg Message that needs to be saved
     * @throws IOException
     */
    public void saveMessage(Message msg) throws IOException {
        Path filePath = Path.of(CHATS_DIR + msg.getChatname() + ".txt");

        Files.write(
            filePath,
            (msg.toString() + System.lineSeparator()).getBytes(), // ensure newline
            StandardOpenOption.APPEND   // append to existing file
        ); 
        
       Chat c = chatNameObjMap.get(msg.getChatname());
       c.addMessage(msg);
    }

    /**
     * Get chat object with chatName
     * 
     * @param chatName Name of desired chat
     * @return Chat object with chatName
     */
    public Chat getChat(String chatName) {
        return chatNameObjMap.get(chatName);
    }

    /**
     * @param accountName Name of the account that resides in the chats
     * @return List of chats needed
     */
    public List<Chat> getChats(String accountName) {
    	 List<Chat> chats = new ArrayList<>();
    	
    	 // if user is admin,just get all da chats
    	if(acctNameObjMap.get(accountName).getRole() == Role.ADMINISTRATOR) {
    		for (Chat chat : chatNameObjMap.values()) {
                chats.add(chat);
            }
    		
    		return chats;
    	}
    	
        HashSet<String> chatNames = userChatMap.get(accountName);
        
        // TODO FORCE making chat b/c client freaks out when user gets no chats
        if (chatNames == null) {
            String[] lonelyUser = new String[] { accountName };
            Chat emptyChat = new Chat(lonelyUser, accountName);
            try {
                this.addChat(lonelyUser, accountName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return List.of(emptyChat);
        }

        for (String chat : chatNames) {
            chats.add(getChat(chat));
        }
        return chats;
    }

    /**
     * Create a new chat in the database files
     * 
     * @param userList Userlist for the chat
     * @param chatName Name of the chat
     * @throws IOException
     */
    public void addChat(String[] userList, String chatName) throws IOException {
    	// Save to local files
        Path filePath = Path.of(CHATS_DIR + chatName + ".txt");

        Files.write(
                filePath,
                (String.join(",", userList) + System.lineSeparator()).getBytes(),  // add newline
                StandardOpenOption.CREATE
        );

        Chat toAdd = new Chat(userList, new Message[0], chatName);
        chatNameObjMap.put(chatName, toAdd);
        
        // add chat name for each user
        for(String user : userList) {
            // creates set if DNE, always inserts chatname
            userChatMap
                .computeIfAbsent(user, k -> new HashSet<>())
                .add(chatName);
        }
    }

    /**
     * Get account object with name
     * 
     * @param name Name of desired account
     * @return Account object with name
     */
    public Account getAccount(String name) {
        return acctNameObjMap.get(name);
    }

    /**
     * Get all the accounts
     * @return all the accounts
     */
    public List<Account> getAccounts() {
        return List.copyOf(acctNameObjMap.values());
    }
}
