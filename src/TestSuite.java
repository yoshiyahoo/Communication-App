import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ 
	AccountTest.class, 
	ChatTest.class, 
	DatabaseTest.class, 
	LoginTest.class, 
	MessageTest.class,
	RqstStoreTest.class,
	ClientTest.class
})
public class TestSuite {
	// Nothing needed in the body b/c you have test classes outside of it
}
