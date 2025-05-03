
import java.awt.*;
import javax.swing.*;




public class LoginScreen
{
    private JPanel mainPanel;
    private JFrame frame;
    private JButton loginButton;
    private JPanel titlePanel;
    private JPanel secondaryPanel;
    private JTextField userNameField;
    private JTextField passwordField;
    private JLabel messageLabel;
    private Client client;
    private boolean success = false;

    public LoginScreen(Client client)
    {
      
        this.client = client;

        //Frame setup
        frame = new JFrame("Login Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        
        frame.setSize(480, 480);
        frame.setLocationRelativeTo(null);  
        
        //Colors
        Color backgroundColor = new Color(30, 30, 30);
        Color textFieldColor = new Color(50, 50, 50);
        Color borderColor = new Color(70, 70, 70);

        //Panel setup
        //Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);

       //Title Label/Panel
        JLabel titleLabel = new JLabel("Enter Credentials");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 30));

        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(backgroundColor);
        titlePanel.add(titleLabel);

        //Added this here to keep the textfields/labels more organzied
        //Probably going to tinker with it more.
        secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.Y_AXIS));
        secondaryPanel.setBackground(backgroundColor);

        //Username label/field
        JLabel userNameLabel =  new JLabel("Username");
        userNameLabel.setForeground(Color.WHITE);

        userNameField = new JTextField(15);
        userNameField.setMaximumSize(new Dimension(900, 50));
        userNameField.setBackground(textFieldColor);
        userNameField.setForeground(Color.WHITE);
        userNameField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        userNameField.setCaretColor(Color.WHITE);

        //Password label/field
        JLabel passwordLabel =  new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);

        passwordField = new JTextField( 15);
        passwordField.setMaximumSize(new Dimension(900, 50));
        passwordField.setBackground(textFieldColor);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        passwordField.setCaretColor(Color.WHITE);

        //Message labels
        messageLabel = new JLabel("");
        messageLabel.setVisible(true);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setForeground(Color.WHITE);

        //Panel everything else is added to
        secondaryPanel.add(Box.createVerticalStrut(50));
        secondaryPanel.add(userNameLabel);
        secondaryPanel.add(userNameField);
        secondaryPanel.add(Box.createVerticalStrut(40));
        secondaryPanel.add(passwordLabel);
        secondaryPanel.add(passwordField);
        secondaryPanel.add(Box.createVerticalStrut(20));

        //Login button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(backgroundColor);

        //Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> doLogin());
        loginButton.setBackground(Color.gray);
        loginButton.setForeground(Color.white);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(300, 75));
        loginButton.setFont(new Font(loginButton.getFont().getName(), Font.BOLD, 30));

        //Additional Listeners so the button changes color when hovered.
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() 
        {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            loginButton.setBackground(Color.white);
            loginButton.setForeground(Color.black);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) 
            {
                loginButton.setBackground(Color.gray);
                loginButton.setForeground(Color.WHITE);
            }
            });

        //Adds login button to button panel
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(10));

        //Adds everything else to the main panel
        mainPanel.add(buttonPanel);
        buttonPanel.add(messageLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(secondaryPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
       
        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);

    }


    private void doLogin() {

        String username = userNameField.getText();
        String password = passwordField.getText();
    
        if (username == null || username.isEmpty()) 
        {
            System.out.println("Please input username.");
            messageLabel.setText("Please input username.");
            messageLabel.setForeground(Color.red);
            return;
        }
    
        if (password == null || password.isEmpty()) 
        {
            System.out.println("Please input password.");
            messageLabel.setText("Please input password.");
            messageLabel.setForeground(Color.red);
            return;
        }
    
         success = client.login(username, password);

        if(success)
        {
            System.out.println("Login is successful!");
            messageLabel.setText("Login is successful!");
            messageLabel.setForeground(Color.green);
        }
        else
        {
            System.out.println("Login failed!");
            messageLabel.setText("Login failed!");
            messageLabel.setForeground(Color.red);
        }
          
    }

    public boolean getLoginStatus()
    {
        return success;
    }
        
    //Should setup colors automatically for GUI objects hopefully???????
    //Maybe will or won't add idk yet.
    private void SetupColorsForElement(Object object)
    {
        
    }
    


    
}

    