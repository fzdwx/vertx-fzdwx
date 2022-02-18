package vertx.fzdwx.cn.serv.core.verticle;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.serv.core.ChatServerProps;
import vertx.fzdwx.cn.serv.core.init.InitVerticle;
import vertx.fzdwx.cn.serv.core.init.InitVerticleRunnable;

import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class VerticleBootStrap {

    private final ChatServerProps chatServerProps = new ChatServerProps();
    private final Map<String, InitVerticleRunnable<? extends InitVerticle<? extends Verticle>>> deploy;
    static Vertx vertx;

    public VerticleBootStrap(final ChatServerProps chatServerProps,
                             final Map<String, InitVerticleRunnable<? extends InitVerticle<? extends Verticle>>> deploy) {
        vertx = Vertx.vertx(chatServerProps.getVertxOps());
        this.deploy = deploy;
    }

    /**
     * 部署
     */
    public void deploy() {
        deploy.forEach((s, init) -> {
            init.run();
            vertx.deployVerticle(s, chatServerProps.getDeployOps(), f -> {
            });
        });
    }

    public void stop() {
        vertx.close();
    }
}