
import java.awt.*;
import javax.swing.*;

public class LoginScreen extends Client
{
    private JPanel mainPanel;
    private JFrame frame;
    private JButton loginButton;
    private JPanel titlePanel;
    private JPanel secondaryPanel;
    private JTextField userNameField;
    private JTextField passwordField;
    private JLabel messageLabel;
    private boolean success = false;
    private Runnable runOnSuccess;

    public LoginScreen(Runnable runOnSuccess)
    {
        this.runOnSuccess = runOnSuccess;
        //Frame setup
        frame = new JFrame("Login Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(480, 480);
        frame.setLocationRelativeTo(null);  

        //Panel setup
        //Main panel
        mainPanel = new JPanel(new BorderLayout());
        
         //Color is applied through a static method.
        GUITools.ColorGUIComponents(mainPanel);


        //Title Label/Panel
        JLabel titleLabel = new JLabel("Enter Credentials");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 30));
        GUITools.ColorGUIComponents(titleLabel);

        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GUITools.ColorGUIComponents(titlePanel);
        titlePanel.add(titleLabel);

        //Added this here to keep the textfields/labels more organzied
        //Probably going to tinker with it more.
        secondaryPanel = new JPanel();
        secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.Y_AXIS));
        GUITools.ColorGUIComponents(secondaryPanel);

        //Username label/field
        JLabel userNameLabel = new JLabel("Username");
        GUITools.ColorGUIComponents(userNameLabel);

        userNameField = new JTextField(15);
        userNameField.setMaximumSize(new Dimension(900, 50));
        GUITools.ColorGUIComponents(userNameField); 

        //Password label/field
        JLabel passwordLabel = new JLabel("Password");
        GUITools.ColorGUIComponents(passwordLabel); 

        passwordField =  new JTextField(15);
        passwordField.setMaximumSize(new Dimension(900, 50));
        GUITools.ColorGUIComponents(passwordField); 

        //Message labels
        messageLabel = new JLabel("");
        messageLabel.setVisible(true);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GUITools.ColorGUIComponents(messageLabel);

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
        GUITools.ColorGUIComponents(buttonPanel); 

        //Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> doLogin());
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(300, 75));
        loginButton.setFont(new Font(loginButton.getFont().getName(), Font.BOLD, 30));
        GUITools.ColorGUIComponents(loginButton); 

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

        String username = userNameField.getText().trim();
        String password = passwordField.getText().trim();

        //super.startSocket();
    
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
    
         //success = super.login(username, password);
       

        if(success)
        {
            System.out.println("Login is successful!");
            messageLabel.setText("Login is successful!");
            messageLabel.setForeground(Color.green);
            loginButton.setEnabled(false);
            runOnSuccess.run();
            frame.dispose();
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
        


    
}

    