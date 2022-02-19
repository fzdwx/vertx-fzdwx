package vertx.fzdwx.cn.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 16:36
 * @see io.vertx.redis.client.RedisAPI
 */
public interface RedisApi {

    static RedisApi api(Redis redis) {
        return new RedisApiImpl(redis);
    }

    void close();

    default Future<Response> set(String key, String value) {
        return send(Command.SET, key, value);
    }

    default RedisApi set(String key, String value, Handler<AsyncResult<Response>> h) {
        send(Command.SET, key, value).onComplete(h);
        return this;
    }

    public Future<Response> send(final Command cmd, final String... args);

    public static class RedisApiImpl implements RedisApi {

        private final Redis redis;

        private RedisApiImpl(final Redis redis) {
            this.redis = redis;
        }
        
        @Override
        public void close() {
            redis.close();
        }

        @Override
        public Future<Response> send(Command cmd, String... args) {
            final Promise<Response> promise = Promise.promise();
            final Request req = Request.cmd(cmd);

            if (args != null) {
                for (String o : args) {
                    if (o == null) {
                        req.nullArg();
                    } else {
                        req.arg(o);
                    }
                }
            }

            redis.send(req, promise);
            return promise.future();
        }
    }
}
