
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

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
        if (!chatListModel.contains(chatName)) {
            chatListModel.addElement(chatName);
        }

//    	System.out.println(chatName + " made it to: " + super.getUserAccount().getName());
        //select the newly added chat(this dont work maybe later fix bugs with it if possible)
        //int idx = chatListModel.indexOf(chatName);
        //chatList.setSelectedIndex(idx);
        //chatList.ensureIndexIsVisible(idx);
    }

    //Called by client's BackgroundHandler Client(or locally on send)) to append a line of chat text to history area
    public void appendMessage(String line, String chatName) {
    	
    	if(this.currentChat != null && this.currentChat.getChatName() == chatName) {
    		historyArea.append(line);
    	}
    }

    public ChatAppGUI() {
//    	SwingUtilities.invokeLater(() -> new ChatAppGUI().init());

        this.currentChat = null;
        this.currentUsername = "";
    }

    public void init() {
        frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        cards = new CardLayout();
        root = new JPanel(cards);

        root.add(buildLoginPanel(), "login");
        root.add(buildChatPanel(), "chat");

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    private JPanel buildLoginPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GUITools.ColorGUIComponents(mainPanel);

        // Title panel
        JLabel header = new JLabel("Enter Credentials");
        header.setFont(new Font(header.getFont().getName(), Font.BOLD, 30));
        GUITools.ColorGUIComponents(header);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GUITools.ColorGUIComponents(titlePanel);
        titlePanel.add(header);

        // Input fields panel
        JPanel secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.Y_AXIS));
        GUITools.ColorGUIComponents(secondaryPanel);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(userLabel);

        usernameField.setColumns(15);
        usernameField.setMaximumSize(new Dimension(500, 30));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(usernameField);

// Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(passLabel);

        passwordField.setColumns(15);
        passwordField.setMaximumSize(new Dimension(500, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(passwordField);

// Address
        JLabel addressLabel = new JLabel("Server Address:");
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(addressLabel);

        addressField.setColumns(15);
        addressField.setMaximumSize(new Dimension(500, 30));
        addressField.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(addressField);

        secondaryPanel.add(Box.createVerticalStrut(30));
        secondaryPanel.add(userLabel);
        secondaryPanel.add(usernameField);
        secondaryPanel.add(Box.createVerticalStrut(20));
        secondaryPanel.add(passLabel);
        secondaryPanel.add(passwordField);
        secondaryPanel.add(Box.createVerticalStrut(20));
        secondaryPanel.add(addressLabel);
        secondaryPanel.add(addressField);
        secondaryPanel.add(Box.createVerticalStrut(10));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		secondaryPanel.add(Box.createVerticalStrut(20));
        GUITools.ColorGUIComponents(buttonPanel);

        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(500, 40));
        loginBtn.setFont(new Font(loginBtn.getFont().getName(), Font.BOLD, 30));
        GUITools.ColorGUIComponents(loginBtn);
        loginBtn.addActionListener(e -> doLogin());

        loginError.setForeground(Color.RED);
        loginError.setAlignmentX(Component.CENTER_ALIGNMENT);

        secondaryPanel.add(loginBtn);
        secondaryPanel.add(loginError);

        // Assemble layout
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(secondaryPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
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

        userLabel = new JLabel(); // optionally pass `currentUsername` if available
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        GUITools.ColorGUIComponents(userLabel);
        if(this.getUserAccount() != null && this.getUserAccount().getRole() == Role.ADMINISTRATOR)
        {
            userLabel.setForeground(Color.RED);
        }

        left.add(userLabel);
        left.add(Box.createVerticalStrut(5));
        left.add(addChat);
        left.add(Box.createVerticalStrut(5));
        left.add(new JScrollPane(chatList));
        left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Center: title + history + input w/send button
        chatTitle = new JLabel("Chat Title");
        chatTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
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
        center.setFont(new Font(center.getFont().getName(), Font.BOLD, 16));


        // Right: user list
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        GUITools.ColorGUIComponents(userList);

        JPanel right = new JPanel(new BorderLayout());
        GUITools.ColorGUIComponents(right);

        JLabel userListLabel = new JLabel("Users");
        GUITools.ColorGUIComponents(userListLabel);
        right.add(userListLabel, BorderLayout.NORTH);
        right.add(new JScrollPane(userList), BorderLayout.CENTER);
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        userListLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Bottom: logout + error message
        JButton logoutBtn = new JButton("LOGOUT");
        GUITools.ColorGUIComponents(logoutBtn);

        chatError = new JLabel();
        chatError.setForeground(Color.RED);
        GUITools.ColorGUIComponents(chatError);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
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

    private void doLogin() {
        //might want to remove this later
        chatListModel.clear();
        this.currentChat = null;

        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String address = addressField.getText().trim();

		if (user == null || user.isEmpty()) {
			System.out.println("Please input username.");
			loginError.setText("Please input username.");
			loginError.setForeground(Color.RED);
			return;
		}
	
		if (pass == null || pass.isEmpty()) {
			System.out.println("Please input password.");
			loginError.setText("Please input password.");
			loginError.setForeground(Color.RED);
			return;
		}

		if (address == null || address.isEmpty()) {
			System.out.println("Please input address.");
			loginError.setText("Please input address.");
			loginError.setForeground(Color.RED);
			return;
		}

        try {
            super.startSocket(address);
        } catch (Exception e) {
            loginError.setText("Connection Unsuccessful");
        }

        LoginType status = super.login(user, pass);

        switch (status) {
            case SUCCESS -> {
                currentUsername = user;
                userLabel.setText(currentUsername);
                cards.show(root, "chat");

                loginError.setText("");

                super.getChatFromServer();
                for (Chat chat : super.getChats()) {
                    this.chatListModel.addElement(chat.getChatName());
                }

                super.getUserNamesFromServer();

                super.startClientThreads();
            }
            case FAILURE -> {
                loginError.setText("Invalid credentials");
                super.closeSocket();
            }
            case LOGGED_IN -> {
                loginError.setText("Account Already Logged In");
            }
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

        if (chat == null) {
            return;
        }

        for (Chat c : super.getChats()) {
            if (chat.equals(c.getChatName())) {
                this.currentChat = c;
            }
        }

        if (this.currentChat == null) {
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
        for (String name : this.currentChat.getUsersNames()) {
            userListModel.addElement(name);
        }
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        messageField.setText("");

        if (msg.isEmpty() || this.currentChat == null) {
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
    }

    private void showNewChatDialog() {
        ArrayList<String> newChatUsers = new ArrayList<String>();
        newChatUsers.add(super.getUserAccount().getName());

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

        //populates the searchModel at the start with all users
        populateSearchModel(newChatUsers, searchModel);

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
                searchModel.clear();

                if (input.isBlank()) {
                    populateSearchModel(newChatUsers, searchModel);
                    return;
                }

                ArrayList<String> matching = searchUserList(input);

                for (String name : matching) {
                    if (newChatUsers.contains(name)) {
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
        GUITools.ColorGUIComponents(dlg);

        searchList.addListSelectionListener(
                e -> addUserToNewChat(searchList, searchModel, newChatUsers, searchIn, usersIn)
        );

        dlg.setVisible(true);
    }

    private void populateSearchModel(ArrayList<String> newChatUsers, DefaultListModel<String> searchModel) {
        for (String user : super.getUserList()) {
            if (newChatUsers.contains(user)) {
                continue;
            }
            searchModel.addElement(user);
        }
    }

    private void addUserToNewChat(JList<String> searchList,
            DefaultListModel<String> searchModel,
            ArrayList<String> newChatUsers,
            JTextField searchIn,
            JTextField usersIn) {

        String user = searchList.getSelectedValue();

        if (user == null) {
            return;
        }

        newChatUsers.add(user);
        usersIn.setText(usersIn.getText() + "\n" + user);
        searchModel.clear();
        populateSearchModel(newChatUsers, searchModel);
        searchIn.setText("");
    }

    private void createNewChat(JDialog dlg, ArrayList<String> newChatUsers, JTextField nameIn) {
        //super.makeChat(newChatUsers.toArray(new String[0]), nameIn.getText());
        String chatName = nameIn.getText().trim();
        if (chatName.isEmpty()) {
            return;
        }
        //build + send chat obj
        super.makeChat(newChatUsers.toArray(new String[0]), chatName);

        //immediately add to the UI
        addChatToList(chatName);

        dlg.dispose();
    }
}