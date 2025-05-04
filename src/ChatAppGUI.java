
import java.awt.*;
import javax.swing.*;

/*
 * Skeleton of the ChatAppGUI that needs to be hooked up to the methods, made based on the Figma Design
 */

public class ChatAppGUI {

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
    private Client client;
    private DefaultListModel<String> chatListModel;
    private JList<String> chatList;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JTextArea historyArea;
    private JTextField messageField;
    private JLabel chatTitle;
    private JLabel chatError;

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new ChatAppGUI().init());
    }

    //For testing the chat itself
   /* private void init() {
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
	*/

    //Login Integration
    public ChatAppGUI(Client client, Account account) {
        this.client = client;
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

    /*private JPanel buildLoginPanel() {
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
     */
    private JPanel buildChatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        GUITools.ColorGUIComponents(panel);

        // Left: chat list + add button + username
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);
        GUITools.ColorGUIComponents(chatList);

        JButton addChat = new JButton("+");
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
        GUITools.ColorGUIComponents(userListLabel);
        right.add(userListLabel, BorderLayout.NORTH);
        right.add(new JScrollPane(userList), BorderLayout.CENTER);
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bottom: logout + error message
        JButton logoutBtn = new JButton("LOGOUT");
        GUITools.ColorGUIComponents(logoutBtn);

        chatError = new JLabel();
        chatError.setForeground(Color.RED);
        GUITools.ColorGUIComponents(chatError);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
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

    /*private void doLogin() {
    	String user = usernameField.getText();
    	String pass = new String(passwordField.getPassword());
    	//TODO: send login request to server to authenticate
    	boolean success = true;
    	if(success) {
    		currentUsername = user;
    		userLabel.setText(currentUsername);
    		cards.show(root, "chat");
    	} else {
    		loginError.setText("Invalid credentials");
    	}
    }
     */
    private void doLogout() {
        //TODO: notify server
        cards.show(root, "login");
    }

    private void loadSelectedChat() {
        String chat = chatList.getSelectedValue();
        if (chat == null) {
            return;
        }
        chatTitle.setText(chat);
        historyArea.setText("");
        userListModel.clear();
    }

    private void sendMessage() {
        String msg = messageField.getText().trim();
        if (msg.isEmpty()) {
            return;
        }
        //TODO: send message to server
        messageField.setText("");
    }

    private void showNewChatDialog() {
        JDialog dlg = new JDialog(frame, "New Chat", true);
        dlg.setSize(300, 400);
        dlg.setLayout(new BorderLayout(5, 5));
        GUITools.ColorGUIComponents(dlg);

        JTextField nameIn = new JTextField();
        nameIn.setBorder(BorderFactory.createTitledBorder("Chat Name"));
        GUITools.ColorGUIComponents(nameIn);

        JTextField searchIn = new JTextField();
        searchIn.setBorder(BorderFactory.createTitledBorder("Add User"));
        GUITools.ColorGUIComponents(searchIn);

        DefaultListModel<String> searchModel = new DefaultListModel<>();
        JList<String> searchList = new JList<>(searchModel);
        GUITools.ColorGUIComponents(searchList);

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
                String q = searchIn.getText();
                searchModel.clear();
                searchModel.addElement("User1"); // placeholder
            }
        });

        JButton createBtn = new JButton("Create");
        GUITools.ColorGUIComponents(createBtn);
        createBtn.addActionListener(e -> dlg.dispose());

        JPanel top = new JPanel(new GridLayout(2, 1, 5, 5));
        GUITools.ColorGUIComponents(top);
        top.add(nameIn);
        top.add(searchIn);

        dlg.add(top, BorderLayout.NORTH);
        dlg.add(new JScrollPane(searchList), BorderLayout.CENTER);
        dlg.add(createBtn, BorderLayout.SOUTH);
        dlg.setLocationRelativeTo(frame);
        dlg.setVisible(true);
    }
}
