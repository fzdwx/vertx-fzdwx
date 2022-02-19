package chat.vertx.fzdwx.api;

import chat.vertx.fzdwx.api.domain.req.SignInReq;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.Post;

/**
 * 用户认证相关
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 11:04
 */
@Controller
@Mapping("/auth")
public class AuthApi {

    /**
     * Sign in.
     *
     * @param req the req
     */
    @Post
    @Mapping("/signIn")
    public void signIn(SignInReq req) {

    }
}
