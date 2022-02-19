package vertx.fzdwx.cn.core.verticle;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.util.PrintUtil;
import vertx.fzdwx.cn.core.verticle.init.VerticleDeployLifeCycle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:20
 */
@Slf4j
public class VerticleStarter {

    private final Map<String, Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>>> deploy = new HashMap<>();
    private final Vertx vertx;
    private final JsonObject config;
    private VerticleBootStrap verticleBootStrap;

    private VerticleStarter(final Vertx vertx, final JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    public static VerticleStarter create(Vertx vertx, final JsonObject config) {
        return new VerticleStarter(vertx, config);
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
        printBanner();
        verticleBootStrap = new VerticleBootStrap(vertx, config, deploy);
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
        return vertx;
    }

    private static void printBanner() {
        System.out.println("    ____          __             ");
        System.out.println("   / __/___  ____/ /      __" + PrintUtil.ANSI_CYAN + "_  __" + PrintUtil.ANSI_RESET);
        System.out.println("  / /_/_  / /" + PrintUtil.ANSI_RED + " __" + PrintUtil.ANSI_RESET + "  / | /| / " + PrintUtil.ANSI_CYAN + "/ |/_/" + PrintUtil.ANSI_RESET);
        System.out.println(" / __/ / /_/ " + PrintUtil.ANSI_RED + "/_/" + PrintUtil.ANSI_RESET + " /| |/ |/ /" + PrintUtil.ANSI_CYAN + ">  <  " + PrintUtil.ANSI_RESET);
        System.out.println("/_/   /___/\\__,_/ |__/|__" + PrintUtil.ANSI_CYAN + "/_/|_|  " + PrintUtil.ANSI_RESET + "version - " + "0.01" + " ::https://github.com/fzdwx:: ");
    }
}