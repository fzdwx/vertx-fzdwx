package vertx.fzdwx.cn.serv;

import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.annotation.Destroy;
import vertx.fzdwx.cn.core.util.StopWatch;
import vertx.fzdwx.cn.serv.core.ChatServerProps;
import vertx.fzdwx.cn.serv.core.init.InitVerticle;
import vertx.fzdwx.cn.serv.core.init.InitVerticleRunnable;
import vertx.fzdwx.cn.serv.core.verticle.Verticle;
import vertx.fzdwx.cn.serv.core.verticle.VerticleBootStrap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:20
 */
@Slf4j
public class VerticleStarter {

    private ChatServerProps chatServerProps = new ChatServerProps();
    private VerticleBootStrap verticleBootStrap;
    private Map<String, InitVerticleRunnable<? extends InitVerticle<? extends Verticle>>> deploy = new HashMap<>();

    public void start() {
        StopWatch.start();
        verticleBootStrap = new VerticleBootStrap(chatServerProps, deploy);
        verticleBootStrap.deploy();
    }

    public VerticleStarter addDeploy(String verticleClassName, InitVerticleRunnable<? extends InitVerticle<? extends Verticle>> verticle) {
        deploy.put(verticleClassName, verticle);
        return this;
    }

    @Destroy
    public void destroy() {
        verticleBootStrap.stop();
    }
}