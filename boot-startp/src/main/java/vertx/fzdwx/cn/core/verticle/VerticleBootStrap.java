package vertx.fzdwx.cn.core.verticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.verticle.init.VerticleDeployLifeCycle;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class VerticleBootStrap {

    private final Vertx vertx;
    private final JsonObject config;
    private final Map<String, Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>>> deploy;

    public VerticleBootStrap(Vertx vertx, final JsonObject config, final Map<String, Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>>> deploy) {
        this.vertx = vertx;
        this.config = config;
        this.deploy = deploy;
    }

    /**
     * 部署
     */
    public void deploy() {
        deploy.forEach((s, init) -> {
            final VerticleDeployLifeCycle<? extends Verticle> lifeCycle = init.get();
            final JsonObject deployConfig = config.getJsonObject(lifeCycle.deployPropsPrefix());
            if (deployConfig == null) {
                throw new IllegalArgumentException(lifeCycle.deployPropsPrefix() + " 未找到对于的部署配置");
            }
            
            vertx.deployVerticle(s, new DeploymentOptions(deployConfig), completion -> {
                lifeCycle.deployComplete(completion);
            });
        });
    }

    public void stop() {
        vertx.close();
    }
}