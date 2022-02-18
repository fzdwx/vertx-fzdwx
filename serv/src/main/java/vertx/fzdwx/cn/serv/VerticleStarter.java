package vertx.fzdwx.cn.serv;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.util.PrintUtil;
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

    private final String version = "0.01";
    private final ChatServerProps chatServerProps;
    private final Map<String, Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>>> deploy = new HashMap<>();
    private VerticleBootStrap verticleBootStrap;

    private VerticleStarter(final ChatServerProps chatServerProps) {
        this.chatServerProps = chatServerProps;
    }

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
        printBanner();
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

    private void printBanner() {
        System.out.println("    ____          __             ");
        System.out.println("   / __/___  ____/ /      __" + PrintUtil.ANSI_CYAN + "_  __" + PrintUtil.ANSI_RESET);
        System.out.println("  / /_/_  / /" + PrintUtil.ANSI_RED + " __" + PrintUtil.ANSI_RESET + "  / | /| / " + PrintUtil.ANSI_CYAN + "/ |/_/" + PrintUtil.ANSI_RESET);
        System.out.println(" / __/ / /_/ " + PrintUtil.ANSI_RED + "/_/" + PrintUtil.ANSI_RESET + " /| |/ |/ /" + PrintUtil.ANSI_CYAN + ">  <  " + PrintUtil.ANSI_RESET);
        System.out.println("/_/   /___/\\__,_/ |__/|__" + PrintUtil.ANSI_CYAN + "/_/|_|  " + PrintUtil.ANSI_RESET + "version - " + version + " ::https://github.com/fzdwx:: ");
    }
}