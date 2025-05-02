import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LoginTest 
{
    @Test
    public void LoginSetterandGetterTest() {
        String testUsername = "Yo Mama";
        String testPassword = "password";
        
        Login login = new Login(testUsername, testPassword);
        
        assertEquals("Username should match", login.getUsername(), testUsername);
        assertEquals("Passwords should match", login.getPassword(), testPassword);
    }
}
