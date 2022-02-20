package chat.vertx.fzdwx.ws;

import chat.vertx.fzdwx.core.ws.WsWrap;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.redis.client.Response;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.core.Aop;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.core.util.Utils;
import vertx.fzdwx.cn.redis.RedisApi;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/20 10:38
 */
@Slf4j
public class WsServUserManager {

    private static final ConcurrentSkipListMap<String, WsWrap> userToConn = new ConcurrentSkipListMap<>();
    private static final ConcurrentSkipListMap<WsWrap, String> connToUser = new ConcurrentSkipListMap<>();
    private static final RedisApi redis;
    private static final String ip = Utils.localhost();
    //                          u:uid - host
    private static final String USER_HOST = "u:id:";

    static {
        redis = Aop.get(RedisApi.class);
    }

    public static void addUser(String id, WsWrap ws) {
        redis.setnx(USER_HOST + id, WsServUserManager.ip).onComplete(ha -> {
            if (ha.succeeded()) {
                final var result = ha.result();
                if (lang.eq(result.toInteger(), 0)) {
                    ws.sendAndClose("您已经登录,请不要重复登录!");
                    throw new RuntimeException("用户当前已经登录: " + id);
                }

                userToConn.put(id, ws);
                connToUser.put(ws, id);

                log.info("user conn success: " + id);

                log.info("curr user count: " + userToConn.size());
            } else throw new RuntimeException(ha.cause());
        });
    }

    public static void delUser(final String id, final WsWrap ws) {
        redis.del(USER_HOST + id);
        userToConn.remove(id);
        connToUser.remove(ws);

        log.info("user dis conn: " + id);
        log.info("curr user count: " + userToConn.size());
    }

    /**
     * 获取用户ip
     *
     * @param id id
     */
    private static Future<Response> getUserIp(final String id) {
        return redis.hget(USER_HOST, id);
    }

    /**
     * 获取用户ip
     *
     * @param id id
     */
    private static void getUserIp(final String id, Handler<AsyncResult<Response>> ha) {
        redis.get(USER_HOST + id).onComplete(ha);
    }

}
