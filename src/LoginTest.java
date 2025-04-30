import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.io.*;
import org.junit.jupiter.api.Test;

public class LoginTest 
{
    @Test
    private void Login()
    {
        String testUserName = "Yo Mama";
        String testPassword = "Password";
        Login newLogin = new Login(testUserName, testPassword);
        
        String username = newLogin.getUsername();
        String password = newLogin.getPassword();

        Assert.assertEquals(testUserName, username, "Usernames match");
        Assert.assertEquals(testPassword, password, "Passwords match");
    }

}
