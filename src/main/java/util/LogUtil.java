package util;

public class LogUtil {
    public static void logf(String message, String var) {
        log(String.format("%s: %s", message, var));
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
