package chat.like.cn.core.util;

/**
 * 不建议外部使用
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 14:31
 */
public class StopWatch {

    public static long time;

    public static void start() {
        time = System.currentTimeMillis();
    }

    public static long stop() {
        return System.currentTimeMillis() - time;
    }
}
