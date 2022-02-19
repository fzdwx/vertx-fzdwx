package chat.vertx.fzdwx.api;

import io.vertx.ext.web.RoutingContext;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Get;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.redis.RedisApi;

import java.util.Map;

/**
 * 用户认证相关
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 11:04
 */
@Component
@Controller
@Mapping("/auth")
public class AuthApi {

    @Inject
    private RedisApi redis;

    /**
     * Sign in.
     */
    @Get
    @Mapping("/signIn")
    public Map<Integer, Integer> signIn(RoutingContext context) {
        System.out.println(redis);
        return Map.of(1, 2);
    }
}
