package chat.vertx.fzdwx.api;

import chat.vertx.fzdwx.api.domain.req.SignInReq;
import lombok.RequiredArgsConstructor;
import org.noear.solon.annotation.Component;
import vertx.fzdwx.cn.core.annotation.Body;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.Post;
import vertx.fzdwx.cn.redis.RedisApi;

/**
 * 用户认证相关
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 11:04
 */
@Component
@Controller
@Mapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    // @Inject
    private RedisApi redis;

    /**
     * Sign in.
     *
     * @param req the req
     */
    @Post
    @Mapping("/signIn")
    public void signIn(@Body SignInReq req) {
        System.out.println(redis);
    }
}
