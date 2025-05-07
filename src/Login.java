import java.io.Serializable;

/**
 * This is the first step in the protocl.
 * This object is a login processor. It has two main states, a login request state which uses the username and password,
 * and a login response state, which uses the loginType field.
 * The Client uses the login request state while the server gives the response state.
 */
public class Login implements Serializable {
	private LoginType loginType;
    private String username;
    private String password; 
   
    /**
     * Use for making a login object on client to send to server.
     * 
     * @param username
     * @param password
     */
    public Login(String username, String password)
    {
        this.username = username;
        this.password = password; 
    }
    
    /**
     * Only use this constructor for passing success or failure from server.
     * 
     * @param loginType
     */
    public Login(LoginType loginType) {
    	this.loginType = loginType;
    	this.username = null;
    	this.password = null;
    }

    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public LoginType getLoginStatus() {return this.loginType;}

}
