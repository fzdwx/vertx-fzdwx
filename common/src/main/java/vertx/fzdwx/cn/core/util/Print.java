package vertx.fzdwx.cn.core.util;

import static org.noear.solon.core.util.PrintUtil.ANSI_BLUE;
import static org.noear.solon.core.util.PrintUtil.ANSI_CYAN;
import static org.noear.solon.core.util.PrintUtil.ANSI_RESET;
import static org.noear.solon.core.util.PrintUtil.blueln;
import static org.noear.solon.core.util.PrintUtil.greenln;
import static org.noear.solon.core.util.PrintUtil.redln;
import static org.noear.solon.core.util.PrintUtil.yellowln;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 14:57
 */
public class Print {

    private final static String PREFIX = "[Chat] ";

    public static void print(String label, Object content) {
        System.out.print(PREFIX);
        System.out.println(ANSI_BLUE + label+" => " + ANSI_CYAN + content);
        System.out.print(ANSI_RESET);
    }

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
