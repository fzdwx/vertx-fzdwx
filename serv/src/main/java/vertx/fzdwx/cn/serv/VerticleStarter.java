package vertx.fzdwx.cn.serv;

import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.annotation.Destroy;
import vertx.fzdwx.cn.serv.core.ChatServerProps;
import vertx.fzdwx.cn.serv.core.verticle.Verticle;
import vertx.fzdwx.cn.serv.core.verticle.VerticleBootStrap;
import vertx.fzdwx.cn.serv.core.verticle.init.VerticleDeployLifeCycle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:20
 */
@Slf4j
public class VerticleStarter {

    private ChatServerProps chatServerProps;
    private VerticleBootStrap verticleBootStrap;
    private Map<String, Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>>> deploy = new HashMap<>();

    public VerticleStarter(final ChatServerProps chatServerProps) {
        this.chatServerProps = chatServerProps;
    }

    public void start() {
        verticleBootStrap = new VerticleBootStrap(chatServerProps, deploy);
        verticleBootStrap.deploy();
    }

    public static VerticleStarter create(ChatServerProps chatServerProps) {
        return new VerticleStarter(chatServerProps);
    }

    public VerticleStarter addDeploy(String verticleClassName, Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>> verticle) {
        deploy.put(verticleClassName, verticle);
        return this;
    }

    @Destroy
    public void destroy() {
        verticleBootStrap.stop();
    }
}