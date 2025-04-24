public class Login implements Serializable
{

    private String username;
    private String password; 
   
    public Login(String username, String password)
    {
        this.username = username;
        this.password = password; 
    }

    public String getUsername() {return this.username;}
    public String getPassword() {return this.password; }

}
