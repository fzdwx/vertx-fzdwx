package chat.like.cn.core.util;

import static org.noear.solon.core.util.PrintUtil.*;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 14:57
 */
public class Print {

    private final static String PREFIX = "[Chat] ";

    public static void debug(Object content) {
        System.out.print(PREFIX);
        blueln(content);
    }

    public static void debug(String label, Object content) {
        System.out.print(PREFIX);
        blueln(label + ": " + content);
    }

    public static void info(Object content) {
        System.out.print(PREFIX + content);
    }

    public static void info(String label, Object content) {
        System.out.print(PREFIX);
        greenln(label + ": " + content);
    }

    public static void infoS(Object content) {
        System.out.print(PREFIX);
        greenln("Server" + ": " + content);
    }

    public static void warn(Object content) {
        System.out.print(PREFIX);
        yellowln(content);
    }

    public static void warn(String label, Object content) {
        System.out.print(PREFIX);
        yellowln(label + ": " + content);
    }

    public static void error(Object content) {
        System.out.print(PREFIX);
        redln(content);
    }

    public static void error(String label, Object content) {
        System.out.print(PREFIX);
        redln(label + ": " + content);
    }
}
