package vertx.fzdwx.cn.core.verticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.util.JsonObjectUtil;
import vertx.fzdwx.cn.core.verticle.init.VerticleDeployLifeCycle;

import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class VerticleBootStrap {

    private final Vertx vertx;
    private final JsonObject config;
    private final Map<String, VerticleDeployLifeCycle<? extends Verticle>> deploy;

    public VerticleBootStrap(Vertx vertx, final JsonObject config, final Map<String, VerticleDeployLifeCycle<? extends Verticle>> deploy) {
        this.vertx = vertx;
        this.config = config;
        this.deploy = deploy;
    }

    /**
     * 部署
     */
    public void deploy() {
        deploy.forEach((s, lifeCycle) -> {
            lifeCycle.preDeploy();
            JsonObject deployConfig = config.getJsonObject(lifeCycle.deployPropsPrefix());
            if (deployConfig == null) {
                deployConfig = JsonObjectUtil.collect(lifeCycle.deployPropsPrefix(), config);
                if (deployConfig.size() == 0) {
                    throw new IllegalArgumentException(lifeCycle.deployPropsPrefix() + " 未找到对应的部署配置");
                }
            }

            final var options = new DeploymentOptions();
            options.setInstances(Integer.parseInt(deployConfig.getString("instances")));
            vertx.deployVerticle(s, options, completion -> {
                lifeCycle.deployComplete(completion);
            });
        });
    }

    public void stop() {
        vertx.close();
    }
}