import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/*
 * User.txt schema
 * ROLE,NAME
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
    private HashMap<String, Account> acctNameObjMap;
    private HashMap<String, Chat> chatNameObjMap;

    // key = username (non-admin), val = set of chatnames w/ user
    private HashMap<String, HashSet<String>> userChatMap;

    public Database() throws IOException {
        // get users into acct map
        {
            List<String> lines = Files.readAllLines(Paths.get("./Database/Users.txt"));
            for(String line : lines) {
                String[] cols = line.split(",");
                Role r = (cols[0].equals("admin"))? Role.ADMINISTRATOR : Role.EMPLOYEE;
                acctNameObjMap.put(cols[1], new Account(r, cols[1]));
            }
        }

        // get chats into chat map
        File chatsFolder = new File("./Database/Chats");
        File[] files = chatsFolder.listFiles();
        if (files == null) return;

        for(File file : files) {
            if(!file.isFile()) continue;
            List<String> lines = Files.readAllLines(file.toPath());
            
            int dotIndex = file.getName().indexOf('.');
            String chatName = file.getName().substring(0, dotIndex);
            String[] userList = lines.get(0).split(",");
            Message[] msgHistory = lines.stream()
                .skip(1)
                .map(line -> line.split(","))
                .map(cols -> new Message(
                    cols[2],
                    LocalDateTime.parse(cols[0]),
                    cols[1]
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

    // TODO change ldt to actual localDateTime
    public List<Message> getMessages(String accountName, Date ldt) {
        List<Message> mList = null;

        for(String chatName : userChatMap.get(accountName)) {
            Chat toCheck = chatNameObjMap.get(chatName);
            for(Message m : toCheck.getMsgHistory()) {
                if(m.getTime().compareTo(ldt) != 0) continue;
                if(m.getAccountName().equals(accountName)) {
                    if(mList == null) mList = new ArrayList<>();
                    mList.addLast(m);
                }
            }
        }

        return mList;
    }

    // TODO get method for adding message to chat obj
    public void saveMessage(Message msg) throws IOException {
        Path filePath = Path.of(msg.getChatname() + ".txt");

        Files.write(
            filePath,
            (msg.toString() + System.lineSeparator()).getBytes(), // ensure newline
            StandardOpenOption.APPEND   // append to existing file
        );    
    }

    public Chat getChat(String chatName) {
        return chatNameObjMap.get(chatName);
    }

    public void addChat(String[] userList, String chatName) throws IOException {
        Chat toAdd = new Chat(userList, null, chatName);
        chatNameObjMap.put(chatName, toAdd);
        
        // add chat name for each user
        for(String user : userList) {
            // creates set if DNE, always inserts chatname
            userChatMap
                .computeIfAbsent(user, k -> new HashSet<>())
                .add(chatName);
        }
    }

    public Account getAccount(String name) {
        return acctNameObjMap.get(name);
    }
}
