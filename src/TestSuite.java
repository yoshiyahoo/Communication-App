import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite; 

@Suite
@SelectClasses({ChatTest.class, DatabaseTest.class})
public class TestSuite {
	// Nothing needed in the body b/c you have test classes outside of it
}
