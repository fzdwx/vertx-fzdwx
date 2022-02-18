package vertx.fzdwx.cn.serv;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
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

    public static VerticleStarter create(ChatServerProps chatServerProps) {
        return new VerticleStarter(chatServerProps);
    }

    /**
     * 添加需要部署的 verticle
     *
     * @param verticleClassName verticle类名
     * @param verticleLifeCycle verticle生命周期
     * @return {@link VerticleStarter }
     */
    public VerticleStarter addDeploy(String verticleClassName, Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>> verticleLifeCycle) {
        deploy.put(verticleClassName, verticleLifeCycle);
        return this;
    }

    /**
     * 启动
     */
    public void start() {
        verticleBootStrap = new VerticleBootStrap(chatServerProps, deploy);
        verticleBootStrap.deploy();
    }

    /**
     * 销毁
     */
    public void destroy() {
        verticleBootStrap.stop();
    }

    /**
     * vertx
     */
    public Vertx vertx() {
        return verticleBootStrap.vertx;
    }

    private VerticleStarter(final ChatServerProps chatServerProps) {
        this.chatServerProps = chatServerProps;
    }
}