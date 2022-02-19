package vertx.fzdwx.cn.redis;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.client.RedisOptions;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 16:00
 */
public class Redis {

    private Redis() {
    }

    /**
     * Factory method for creating a Redis client in Vert.x context.
     *
     * @param vertx  Vertx instance
     * @param config configuration
     * @return the new Redis Api instance
     */
    public static RedisApi client(Vertx vertx, JsonObject config) {
        return RedisApi.api(new io.vertx.redis.client.impl.RedisClient(vertx, options(config)));
    }

    public static RedisApi client(Vertx vertx, RedisOptions config) {
        return RedisApi.api(new io.vertx.redis.client.impl.RedisClient(vertx, config));
    }


    /**
     * Factory method for creating a default local Redis client configuration.
     *
     * @param config configuration from Vert.x context
     * @return the new configuration instance
     */
    private static RedisOptions options(JsonObject config) {
        return new RedisOptions(config);
    }
}
