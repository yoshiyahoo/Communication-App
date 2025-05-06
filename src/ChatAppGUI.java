import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;
/*
 * Skeleton of the ChatAppGUI that needs to be hooked up to the methods, made based on the Figma Design
 */
import java.util.ArrayList;

public class ChatAppGUI extends Client {
    private JFrame frame;
    private CardLayout cards;
    private JPanel root;

    // Login components
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JTextField addressField = new JTextField();
    private JLabel loginError = new JLabel();
    private String currentUsername;
    private JLabel userLabel; //for adding on top of the add chat button
    
    //Chat components
//    private DefaultListModel<String> chatListModel;
//    private JList<String> chatList;
    private DefaultListModel<String> chatListModel;
    private JList<String> chatList;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JTextArea historyArea;
    private JTextField messageField;
    private JLabel chatTitle;
    private JLabel chatError;
    private Chat currentChat;
    
    // uncomment to test
//    public static void main(String [] args) {
//    	SwingUtilities.invokeLater(() -> new ChatAppGUI().init());
//    }
    
    // Called by the client's BackgroundHandlerCLient(view SwingUtilities.invokeLater) when a new Chat arrives from the server
    public void addChatToList(String chatName) {
    	//avoid duplicates
    	if(!chatListModel.contains(chatName)) {
    		chatListModel.addElement(chatName);
    	}
    	
//    	System.out.println(chatName + " made it to: " + super.getUserAccount().getName());
    	
    	//select the newly added chat(this dont work maybe later fix bugs with it if possible)
    	//int idx = chatListModel.indexOf(chatName);
    	//chatList.setSelectedIndex(idx);
    	//chatList.ensureIndexIsVisible(idx);
    }
    
    //Called by client's BackgroundHandler Client(or locally on send)) to append a line of chat text to history area
    public void appendMessage(String line) {
    	historyArea.append(line);
    }
    
    
    
    public ChatAppGUI() {
//    	SwingUtilities.invokeLater(() -> new ChatAppGUI().init());
    	
    	this.currentChat = null;
    	this.currentUsername = "";
    }
    
    public void init() {
    	frame = new JFrame("Chat Application");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(900,600);
    	
    	cards = new CardLayout();
    	root = new JPanel(cards);
    	
    	root.add(buildLoginPanel(), "login");
    	root.add(buildChatPanel(), "chat");
    	
    	frame.setContentPane(root);
    	frame.setVisible(true);
    }
    
    private JPanel buildLoginPanel() {
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    	
    	//Configure text fields to fixed size
    	usernameField.setColumns(15);
    	usernameField.setMaximumSize(usernameField.getPreferredSize());
    	usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	passwordField.setColumns(15);
    	passwordField.setMaximumSize(passwordField.getPreferredSize());
    	passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	addressField.setColumns(15);
    	addressField.setMaximumSize(passwordField.getPreferredSize());
    	addressField.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	JLabel header = new JLabel("Enter Credentials");
    	header.setAlignmentX(Component.CENTER_ALIGNMENT);
    	panel.add(header);
    	panel.add(Box.createVerticalStrut(15));
    	
    	JLabel userLabel = new JLabel("Username:");
    	userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	panel.add(userLabel);
    	panel.add(usernameField);
    	panel.add(Box.createVerticalStrut(10));
    	
    	JLabel passLabel = new JLabel("Password:");
    	passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	panel.add(passLabel);
    	panel.add(passwordField);
    	panel.add(Box.createVerticalStrut(15));
    	
    	JLabel addressLabel = new JLabel("Server Address:");
    	addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	panel.add(addressLabel);
    	panel.add(addressField);
    	panel.add(Box.createVerticalStrut(10));
    	
    	JButton loginBtn = new JButton("Login");
    	loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    	panel.add(loginBtn);
    	panel.add(Box.createVerticalStrut(5));
    	
    	loginError.setForeground(Color.RED);
    	loginError.setAlignmentX(Component.CENTER_ALIGNMENT);
    	panel.add(loginError);
    	
    	loginBtn.addActionListener(e -> doLogin());
    	return panel;
    }
    
    private JPanel buildChatPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	//Left: chat list + add button + username
    	chatListModel = new DefaultListModel<>();
    	chatList = new JList<>(chatListModel);
    	JButton addChat = new JButton("+");
    	JPanel left = new JPanel();
    	left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
    	
    	userLabel = new JLabel();
    	userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
    	//make username bold and blue in the corner
    	userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    	userLabel.setForeground(Color.BLUE);
    	left.add(userLabel);
    	left.add(Box.createVerticalStrut(5));
    	
    	left.add(addChat);
    	left.add(Box.createVerticalStrut(5));
    	left.add(new JScrollPane(chatList));
    	left.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    	
    	//Center: title + history + input w/send button
    	chatTitle = new JLabel("Chat Title");
    	historyArea = new JTextArea();
    	historyArea.setEditable(false);
    	historyArea.setLineWrap(true);
    	messageField = new JTextField();
    	messageField.setColumns(20);
    	
    	//Create send button
    	JButton sendBtn = new JButton("Send");
    	sendBtn.addActionListener(e -> sendMessage());
    	
    	//Field + button in a little input panel
    	JPanel inputPanel = new JPanel(new BorderLayout(5,0));
    	inputPanel.add(messageField, BorderLayout.CENTER);
    	inputPanel.add(sendBtn, BorderLayout.EAST);
    	
    	JPanel center = new JPanel(new BorderLayout(5,5));
    	center.add(chatTitle, BorderLayout.NORTH);
    	center.add(new JScrollPane(historyArea), BorderLayout.CENTER);
    	center.add(inputPanel, BorderLayout.SOUTH);
    	center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	
    	//Right: User List (can delete if you want tbh idk if yall still wanted this)
    	userListModel = new DefaultListModel<>();
    	userList = new JList<>(userListModel);
    	JPanel right = new JPanel(new BorderLayout());
    	right.add(new JLabel("Users"), BorderLayout.NORTH);
    	right.add(new JScrollPane(userList), BorderLayout.CENTER);
    	right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	
    	//Bottom: logout + error message(ie invalid credentials)
    	JButton logoutBtn = new JButton("LOGOUT");
    	chatError = new JLabel();
    	chatError.setForeground(Color.RED);
    	JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    	bottom.add(logoutBtn);
    	bottom.add(chatError);
    	
    	//Assemble the parts
    	panel.add(left, BorderLayout.WEST);
    	panel.add(center, BorderLayout.CENTER);
    	panel.add(right, BorderLayout.EAST);
    	panel.add(bottom, BorderLayout.SOUTH);
    	
    	//Wiring
    	addChat.addActionListener(e -> showNewChatDialog());
    	chatList.addListSelectionListener(e -> loadSelectedChat());
    	messageField.addActionListener(e -> sendMessage()); //comment this out if you don't want the user to send msg via enter
    	logoutBtn.addActionListener(e -> doLogout());
    	
    	return panel;
    }
    
    private void doLogin() {
    	//might want to remove this later
    	chatListModel.clear();
    	this.currentChat = null; 
    	
    	String user = usernameField.getText();
    	String pass = new String(passwordField.getPassword());
    	String address = addressField.getText();
    	
    	boolean success = true;
    	
    	try {
			super.startSocket(address);
		} catch (Exception e) {
			success = false;
		}
    	
    	success = success && super.login(user, pass);

    	if(success) {
    		currentUsername = user;
    		userLabel.setText(currentUsername);
    		cards.show(root, "chat");
    		
    		loginError.setText("");

    		super.getChatFromServer();
    		for(Chat chat : super.getChats()) {
    			this.chatListModel.addElement(chat.getChatName());
    		}
    		
        	super.getUserNamesFromServer();

        	super.startClientThreads();
        	
    	} else {
    		loginError.setText("Invalid credentials");
    		super.closeSocket();
    	}
    }
    
    private void doLogout() {
    	//might want to remove this later
//    	chatList.clearSelection();
    	
    	historyArea.setText("");
    	
    	usernameField.setText("");
    	passwordField.setText("");
    	cards.show(root, "login");
    	
    	super.stopClientThreads();
    	
//    	System.out.println("Threads should have stopped");
//    	System.exit(0);
    	
    	super.cleanUpOnLogout();
    	super.closeSocket();
    } 
    
    private void loadSelectedChat() {
    	String chat = chatList.getSelectedValue();
    	
    	if(chat == null) {
    		return;
    	}
    	
    	for(Chat c : super.getChats()) {
    		if(chat.equals(c.getChatName())) {
    			this.currentChat = c;
    		}
    	}
    	
    	if(this.currentChat == null) {
    		return;
    	}
    	
    	chatTitle.setText(chat);
    	//wipe out old text
    	historyArea.setText("");
    	//append each msg with newline
//    	for(Message msg : this.currentChat.getMsgHistory()) {
//    		historyArea.setText(
//    				historyArea.getText()
//    				+ "\n"
//    				+ msg.getAccountName()
//    				+ " "
//    				+ msg.getTime().getHour()
//    				+ ":"
//    				+ msg.getTime().getMinute()
//    				+ " >>> "
//    				+ msg.getMsg()
//    		);
    	
    	//}
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
    	ArrayList<String> newChatUsers = new ArrayList<String>();
    	newChatUsers.add(super.getUserAccount().getName());
    	
    	JDialog dlg = new JDialog(frame, "New Chat", true);
    	dlg.setSize(300,400);
    	dlg.setLayout(new BorderLayout(5,5));

    	JTextField nameIn = new JTextField();
    	nameIn.setBorder(BorderFactory.createTitledBorder("Chat Name"));
    	JTextField usersIn = new JTextField();
    	usersIn.setBorder(BorderFactory.createTitledBorder("Selected Users"));
    	usersIn.setEditable(false);
    	JTextField searchIn = new JTextField();
    	searchIn.setBorder(BorderFactory.createTitledBorder("Add User"));
    	DefaultListModel<String> searchModel = new DefaultListModel<>();
    	JList<String> searchList = new JList<>(searchModel);

    	//any time text is inserted, removed, or its attributes change, refresh user-search results
    	searchIn.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    		public void insertUpdate(javax.swing.event.DocumentEvent e) {
    			update();
    		}
    		public void removeUpdate(javax.swing.event.DocumentEvent e) {
    			update();
    		}
    		public void changedUpdate(javax.swing.event.DocumentEvent e) {
    			update();
    		}
    		private void update() {
    			String input = searchIn.getText();

    			ArrayList<String> matching = searchUserList(input);

    			searchModel.clear();
    			for(String name : matching) {
    				if(newChatUsers.contains(name)) {
    					continue;
    				}
    				searchModel.addElement(name);
    			}
    		}
    	});

    	JButton createBtn = new JButton("Create");
    	createBtn.addActionListener(e -> createNewChat(dlg, newChatUsers, nameIn));

    	JPanel top = new JPanel(new GridLayout(3, 1, 5, 5));
    	top.add(nameIn);
    	top.add(usersIn);
    	top.add(searchIn);
    	dlg.add(top, BorderLayout.NORTH);
    	dlg.add(new JScrollPane(searchList), BorderLayout.CENTER);
    	dlg.add(createBtn, BorderLayout.SOUTH);
    	dlg.setLocationRelativeTo(frame);
    	
    	searchList.addListSelectionListener(
    			e -> addUserToNewChat(searchList, searchModel, newChatUsers, searchIn, usersIn)
    	);
    	
    	dlg.setVisible(true);
    }
    
    private void addUserToNewChat(JList<String> searchList, 
    		DefaultListModel<String> searchModel, 
    		ArrayList<String> newChatUsers,
    		JTextField searchIn,
    		JTextField usersIn) {
    	
    	String user = searchList.getSelectedValue();
    	
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
   


