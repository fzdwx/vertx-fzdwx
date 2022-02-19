package vertx.fzdwx.cn.core.verticle;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.util.PrintUtil;
import vertx.fzdwx.cn.core.verticle.init.VerticleDeployLifeCycle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:20
 */
@Slf4j
public class VerticleStarter {

    private static Vertx vertx;
    private final Map<String, VerticleDeployLifeCycle<? extends Verticle>> deploy = new HashMap<>();
    private final JsonObject config;
    private VerticleBootStrap verticleBootStrap;

    private VerticleStarter(final JsonObject config, final Vertx vertx) {
        VerticleStarter.vertx = vertx;
        this.config = config;
    }

    public static VerticleStarter create(final JsonObject config, final Vertx vertx) {
        return new VerticleStarter(config, vertx);
    }

    /**
     * vertx
     */
    public static Vertx vertx() {
        return vertx;
    }

    /**
     * 添加需要部署的 verticle
     *
     * @param verticleClassName verticle类名
     * @param verticleLifeCycle verticle生命周期
     * @return {@link VerticleStarter }
     */
    public VerticleStarter addDeploy(String verticleClassName, VerticleDeployLifeCycle<? extends Verticle> verticleLifeCycle) {
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

    private static void printBanner() {
        System.out.println("    ____          __             ");
        System.out.println("   / __/___  ____/ /      __" + PrintUtil.ANSI_CYAN + "_  __" + PrintUtil.ANSI_RESET);
        System.out.println("  / /_/_  / /" + PrintUtil.ANSI_RED + " __" + PrintUtil.ANSI_RESET + "  / | /| / " + PrintUtil.ANSI_CYAN + "/ |/_/" + PrintUtil.ANSI_RESET);
        System.out.println(" / __/ / /_/ " + PrintUtil.ANSI_RED + "/_/" + PrintUtil.ANSI_RESET + " /| |/ |/ /" + PrintUtil.ANSI_CYAN + ">  <  " + PrintUtil.ANSI_RESET);
        System.out.println("/_/   /___/\\__,_/ |__/|__" + PrintUtil.ANSI_CYAN + "/_/|_|  " + PrintUtil.ANSI_RESET + "version - " + "0.01" + " ::https://github.com/fzdwx:: ");
    }
}