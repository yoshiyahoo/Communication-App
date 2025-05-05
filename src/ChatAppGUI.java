
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChatAppGUI extends Client {
    private JFrame frame;
    private CardLayout cards;
    private JPanel root;

    // Login components
    /*private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JLabel loginError = new JLabel();
     */
    //for adding on top of the add chat button
    //Chat components
    private String currentUsername;
    private JLabel userLabel;
    public DefaultListModel<String> chatListModel;
    private JList<String> chatList;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JTextArea historyArea;
    private JTextField messageField;
    private JLabel chatTitle;
    private JLabel chatError;
    private Chat currentChat;
// Called by the client's BackgroundHandlerCLient(view SwingUtilities.invokeLater) when a new Chat arrives from the server
public void addChatToList(String chatName) {
    //avoid duplicates
    if(!chatListModel.contains(chatName)) {
    chatListModel.addElement(chatName);
    }
    
    //select the newly added chat(this dont work maybe later fix bugs with it if possible)
    //int idx = chatListModel.indexOf(chatName);
    //chatList.setSelectedIndex(idx);
    //chatList.ensureIndexIsVisible(idx);
}

//Called by client's BackgroundHandler Client(or locally on send)) to append a line of chat text to history area
public void appendMessage(String line) {
    historyArea.append(line);
}
   

   

    //Login Integration
    public ChatAppGUI(Account account) {
		this.currentUsername = account.getName();
        frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        cards = new CardLayout();
        root = new JPanel(cards);

        //root.add(buildLoginPanel(), "login");
        root.add(buildChatPanel(), "chat");

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private JPanel buildChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        GUITools.ColorGUIComponents(panel);

        // Left: chat list + add button + username
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);
        GUITools.ColorGUIComponents(chatList);

        JButton addChat = new JButton("+");
        addChat.setMaximumSize(new Dimension(1000, 30)); 
        addChat.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(addChat);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        GUITools.ColorGUIComponents(left);

        userLabel = new JLabel(currentUsername);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        GUITools.ColorGUIComponents(userLabel);

        left.add(userLabel);
        left.add(Box.createVerticalStrut(5));
        left.add(addChat);
        left.add(Box.createVerticalStrut(5));
        left.add(new JScrollPane(chatList));
        left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Center: title + history + input w/send button
        chatTitle = new JLabel("Chat Title");
        GUITools.ColorGUIComponents(chatTitle);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        GUITools.ColorGUIComponents(historyArea);

        messageField = new JTextField();
        messageField.setColumns(20);
        GUITools.ColorGUIComponents(messageField);

        JButton sendBtn = new JButton("Send");
        GUITools.ColorGUIComponents(sendBtn);
        sendBtn.addActionListener(e -> sendMessage());

        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        GUITools.ColorGUIComponents(inputPanel);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(5, 5));
        GUITools.ColorGUIComponents(center);
        center.add(chatTitle, BorderLayout.NORTH);
        center.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        center.add(inputPanel, BorderLayout.SOUTH);
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Right: user list
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        GUITools.ColorGUIComponents(userList);

        JPanel right = new JPanel(new BorderLayout());
        GUITools.ColorGUIComponents(right);

        JLabel userListLabel = new JLabel("Users");
        right.add(userListLabel, BorderLayout.NORTH);
        right.add(new JScrollPane(userList), BorderLayout.CENTER);
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GUITools.ColorGUIComponents(userListLabel);

        // Bottom: logout + error message
        JButton logoutBtn = new JButton("LOGOUT");
        GUITools.ColorGUIComponents(logoutBtn);

        chatError = new JLabel();
        chatError.setForeground(Color.RED);
        GUITools.ColorGUIComponents(chatError);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,10));
        GUITools.ColorGUIComponents(bottom);
        bottom.add(logoutBtn);
        bottom.add(chatError);

        // Assemble layout
        panel.add(left, BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        panel.add(right, BorderLayout.EAST);
        panel.add(bottom, BorderLayout.SOUTH);

        // Wiring
        addChat.addActionListener(e -> showNewChatDialog());
        chatList.addListSelectionListener(e -> loadSelectedChat());
        messageField.addActionListener(e -> sendMessage());
        logoutBtn.addActionListener(e -> doLogout());

        return panel;
    }

    private void doLogout() 
    {
    	super.display();
    	super.stopClientThreads();
        frame.dispose();
    } 
    
    private void loadSelectedChat() {
    	String chat = chatList.getSelectedValue();
    	
    	for(Chat c : super.getChats()) {
    		if(chat.equals(c.getChatName())) {
    			this.currentChat = c;
    		}
    	}
    	
    	if(chat == null || this.currentChat == null) {
    		return;
    	}
    	
    	chatTitle.setText(chat);
    	//wipe out old text
    	historyArea.setText("");
    
    	for (Message msg : this.currentChat.getMsgHistory()) {
    		  String line = String.format(
    				  "%s %02d:%02d >>> %s%n",
    				  msg.getAccountName(),
    				  msg.getTime().getHour(),
    				  msg.getTime().getMinute(),
    				  msg.getMsg()
    				  );
    		  historyArea.append(line);
    	  }
    	  
    	historyArea.setCaretPosition(historyArea.getDocument().getLength());
    	//this dont work yet idk but it supposed to add users currently in gc to the right 
    	userListModel.clear();
    	for(String name : this.currentChat.getUsersNames()) {
    		userListModel.addElement(name);
    	}
    }

    private void sendMessage() {
    	String msg = messageField.getText().trim();
    	if(msg.isEmpty()) {
    		return;
    	}
    	
    	Message newMsg = new Message(
				msg,
				this.currentUsername,
				this.currentChat.getChatName()
		);
    	
    	super.sendMsg(newMsg);
    	
    	this.currentChat.addMessage(newMsg);
    	
        loadSelectedChat();
    	
    	messageField.setText("");
    }

    private void showNewChatDialog() {
        ArrayList<String> newChatUsers = new ArrayList<>();
    
        JDialog dlg = new JDialog(frame, "New Chat", true);
        dlg.setSize(300, 400);
        dlg.setLayout(new BorderLayout(5, 5));
    

        JTextField nameIn = new JTextField();
        nameIn.setBorder(BorderFactory.createTitledBorder("Chat Name"));
        
    
        JTextField usersIn = new JTextField();
        usersIn.setBorder(BorderFactory.createTitledBorder("Selected Users"));
        usersIn.setEditable(false);
    
        JTextField searchIn = new JTextField();
        searchIn.setBorder(BorderFactory.createTitledBorder("Add User"));
    
        DefaultListModel<String> searchModel = new DefaultListModel<>();
        JList<String> searchList = new JList<>(searchModel);
    
        searchIn.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
    
            private void update() {
                String input = searchIn.getText().trim();
                ArrayList<String> matching = searchUserList(input);
    
                searchModel.clear();
                for (String name : matching) {
                    if (!newChatUsers.contains(name)) {
                        searchModel.addElement(name);
                    }
                }
            }
        });
    
        
        searchList.addListSelectionListener(
            e -> {
                if (!e.getValueIsAdjusting()) {
                    addUserToNewChat(searchList, searchModel, newChatUsers, searchIn, usersIn);
                }
            }
        );
    
        JButton createBtn = new JButton("Create");
        createBtn.addActionListener(e -> createNewChat(dlg, newChatUsers, nameIn));
    
        JPanel top = new JPanel(new GridLayout(3, 1, 5, 5));
        top.add(nameIn);
        top.add(usersIn);
        top.add(searchIn);
    
        dlg.add(top, BorderLayout.NORTH);
        dlg.add(new JScrollPane(searchList), BorderLayout.CENTER);
        dlg.add(createBtn, BorderLayout.SOUTH);
         GUITools.ColorGUIComponents(dlg);
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true); 
    }
    
    
    private void addUserToNewChat(JList<String> searchList, 
    		DefaultListModel<String> searchModel, 
    		ArrayList<String> newChatUsers,
    		JTextField searchIn,
    		JTextField usersIn) {
    	
    	String user = searchList.getSelectedValue();
    	
    	System.out.println("Here");
    	
    	if(user == null) {
    		return;
    	}
    	
    	newChatUsers.add(user);
    	usersIn.setText(usersIn.getText() + "\n" + user);
    	searchModel.clear();
    	searchIn.setText("");
        
    }
    
    private void createNewChat(JDialog dlg, ArrayList<String> newChatUsers, JTextField nameIn) {
    	//super.makeChat(newChatUsers.toArray(new String[0]), nameIn.getText());
    	String chatName = nameIn.getText().trim();
    	if(chatName.isEmpty()) {
    		return;
    	}
    	//build + send chat obj
    	super.makeChat(newChatUsers.toArray(new String[0]), chatName);
    	
    	//immediately add to the UI
    	addChatToList(chatName);

    	dlg.dispose();
    }
}
   


