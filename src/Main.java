/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        // Some silly sample code here
        // These comments look silly af
        System.out.println("Hello world");

		Other obj = new Other("John");
       
        String str = String.format("%s",obj.getName());
        System.out.println(str);
    }
}
