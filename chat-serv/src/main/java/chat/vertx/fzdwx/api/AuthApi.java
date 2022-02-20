package chat.vertx.fzdwx.api;

import chat.vertx.fzdwx.api.domain.req.SignInReq;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import vertx.fzdwx.cn.core.annotation.Body;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.Post;
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
@Slf4j
public class AuthApi {

    @Inject
    private RedisApi redis;

    /**
     * Sign in.
     */
    @Post
    @Mapping("/signIn")
    public Map<Integer, Integer> signIn(RoutingContext context, @Body SignInReq req) {
        System.out.println(req);
        return Map.of(1, 2);
    }
}
