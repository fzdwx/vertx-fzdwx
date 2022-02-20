package vertx.fzdwx.cn.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;

import java.util.Map;

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

    default Future<Response> setnx(String key, String value) {
        return send(Command.SETNX, key, value);
    }

    default RedisApi setnx(String key, String value, Handler<AsyncResult<Response>> h) {
        send(Command.SETNX, key, value);
        return this;
    }

    default Future<Response> get(String key) {
        return send(Command.GET, key);
    }

    default RedisApi get(String key, Handler<AsyncResult<Response>> h) {
        send(Command.GET, key).onComplete(h);
        return this;
    }

    default Future<Response> hset(String key, String field, String value) {
        return send(Command.HSET, key, field, value);
    }

    default RedisApi hset(String key, String field, String value, Handler<AsyncResult<Response>> handler) {
        send(Command.HSET, key, field, value).onComplete(handler);
        return this;
    }

    default RedisApi hget(String key, String field, Handler<AsyncResult<Response>> handler) {
        send(Command.HGET, key, field).onComplete(handler);
        return this;
    }

    default Future<Response> hget(String key, String field) {
        return send(Command.HGET, key, field);
    }

    default RedisApi hmset(String key, Map<String, String> fv, Handler<AsyncResult<Response>> handler) {
        final var flat = flat(1, fv);
        flat[0] = key;
        send(Command.HMSET, flat).onComplete(handler);
        return this;
    }

    default Future<Response> hmset(String key, Map<String, String> fv) {
        final var flat = flat(1, fv);
        flat[0] = key;
        return send(Command.HMSET, flat);
    }

    default Future<Response> hdel(String key, String field) {
        return send(Command.HDEL, key, field);
    }

    default RedisApi hdel(String key, String field, Handler<AsyncResult<Response>> handler) {
        send(Command.HDEL, key, field).onComplete(handler);
        return this;
    }

    default Future<Response> del(String key) {
        return send(Command.DEL, key);
    }

    default RedisApi del(String key, Handler<AsyncResult<Response>> handler) {
        send(Command.DEL, key).onComplete(handler);
        return this;
    }

    Future<Response> send(final Command cmd, final String... args);

    private static String[] flat(int start, final Map<String, String> map) {
        final var s = new String[map.size() << 1];
        int curr = start;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            s[curr++] = k;
            s[curr++] = v;
        }
        return s;
    }

    class RedisApiImpl implements RedisApi {

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
