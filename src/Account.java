public class Account {
    private Role role;
    private String name;
    private String password;
    private static int counter = 0;
    private int ID;
   
    public Account(Role role, String name, String password) {
    	this.role = role;
    	this.name = name;
    	this.password = password;
    	this.ID = counter++;
    }

    public void setRole(Role role) {
    	
    	this.role = role;

    }
    
    public void setPassword(String password) {
    	this.password = password;
    }

    public Role getRole() {
    	
    	return this.role;

    }

    public String getName() {
    	
    	return this.name;

    }
    
    
    public String getPassword() {
    	return this.password;
    }

    public int getID() {
    	
    	return this.ID;

    } 
}
