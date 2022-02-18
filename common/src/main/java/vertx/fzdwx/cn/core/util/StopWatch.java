package vertx.fzdwx.cn.core.util;

/**
 * 不建议外部使用
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 14:31
 */
public class StopWatch {

    public long time;

    private StopWatch() {
        this.time = System.currentTimeMillis();
    }

    public static StopWatch start() {
        return new StopWatch();
    }

    public long stop() {
        return System.currentTimeMillis() - time;
    }
}