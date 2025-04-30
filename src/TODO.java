/**
 * This class represents items that need to be completed.
 * Whenever this class is created, it will allow java to compile unfinished code.
 * When you hit a method that creates this class, the system will close
 * and tell you which method to implement.
 * To use in a void function, call it.
 * To use in any other function, return this method and cast it to the return type.
 */
public class TODO {
    /**
     * @return null
     * Returning null tricks the java compiler to compile your code
     */
    public static Object todo() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        // The stacktrace at element 2 is the method that called this one
        StackTraceElement caller = stack[2];
        System.err.println("Implement " + caller.getClassName() + ":" + caller.getMethodName() + ":" + caller.getLineNumber());
        System.exit(-1);
        return null;
    }

    /**
     * This method uses a custom message as well to clarify the thing to be done
     * @return null
     * Retuning null tricks the java compiler to compile your code
     */
    public static Object todo(String msg) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        // The stacktrace at element 2 is the method that called this one
        StackTraceElement caller = stack[2];
        System.err.println(msg + caller.getClassName() + ":" + caller.getMethodName() + ":" + caller.getLineNumber());
        System.exit(-1);
        return null;
    }
}
