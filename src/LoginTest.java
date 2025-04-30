import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.io.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest 
{
    @Test
    public void Login()
    {
        String testUserName = "Yo Mama";
        String testPassword = "Password";
        Login newLogin = new Login(testUserName, testPassword);
        
        String username = newLogin.getUsername();
        String password = newLogin.getPassword();

        assertEquals(testUserName, "Usernames match", username);
        assertEquals(testPassword, "Passwords match", password);
    }

}
