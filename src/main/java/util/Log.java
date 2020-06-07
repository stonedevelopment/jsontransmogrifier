package util;

public class Log {
    public static void f(String message, String var) {
        Log.d(String.format("%s: %s", message, var));
    }

    public static void d(String message) {
        System.out.println(message);
    }
}
