package chat.like.cn.core.util;

import io.vertx.core.http.HttpServerRequest;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 21:30
 */
public class ChatUtil {

    /**
     * 判断当前请求是不是websocket请求
     */
    public static boolean isWs(final HttpServerRequest req) {
        final var upgrade = req.getHeader("Upgrade");
        if (upgrade == null) return false;
        return upgrade.equalsIgnoreCase("websocket");
    }

}
