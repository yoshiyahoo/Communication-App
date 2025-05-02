import java.io.Serializable;

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
