import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
	
	@Test
	public void testConstructor(){
		Account anna = new Account(Role.EMPLOYEE,"Anna", "Coco!987");
		String role = anna.getRole().toString();
		assertTrue(role == "EMPLOYEE");
		assertEquals(anna.getName(), "Anna");
		assertEquals(anna.getPassword(), "Coco!987");
		assertNotNull(anna);
	}
	
	@Test
	public void testSetRole() {
		Account anna = new Account(Role.EMPLOYEE,"Anna", "Coco!987");
		anna.setRole(Role.ADMINISTRATOR);
		String role = anna.getRole().toString();
		assertTrue(role == "ADMINISTRATOR");
	}
	
	@Test
	public void testSetPassword() {
		Account anna = new Account(Role.EMPLOYEE,"Anna", "");
		anna.setPassword("Coco!987");
		assertEquals(anna.getPassword(), "Coco!987");
	}
	
	@Test
	public void testGetRole() {
		Account anna = new Account(Role.EMPLOYEE,"Anna", "");
		String role = anna.getRole().toString();
		assertTrue(role == "EMPLOYEE");
	}
	
	@Test
	public void testGetName() {
		Account anna = new Account(Role.EMPLOYEE,"Anna", "");
		assertTrue(anna.getName() == "Anna");
	}
	
	@Test
	public void testGetPassword() {
		Account anna = new Account(Role.EMPLOYEE,"Anna", "Coco!987");
		assertEquals(anna.getPassword(), "Coco!987");
	}
	
	@Test
	public void testGetID() {
		Account anna = new Account(Role.EMPLOYEE,"Anna", "Coco!987");
		assertTrue(anna.getID() > 0);
	}

}
