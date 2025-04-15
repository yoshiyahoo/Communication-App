public class Account {
    private Role role;
    private String name;
    private static int counter = 0;
    private int ID;

    public Account(Role role, String name) {
    	this.role = role;
    	this.name = name;
    	this.ID = counter++;
    }

    public void setRole(Role role) {
    	
    	this.role = role;

    }

    public void setName(String name) {
    	
    	this.name = name;

    }

    public Role getRole() {
    	
    	return this.role;

    }

    public String getName() {
    	
    	return this.name;

    }

    public int getID() {
    	
    	return this.ID;

    }
}
