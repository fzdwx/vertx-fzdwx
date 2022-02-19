package vertx.fzdwx.cn.core.verticle.init;

import io.vertx.core.AsyncResult;
import vertx.fzdwx.cn.core.verticle.Verticle;

import java.util.List;
import java.util.function.Supplier;

/**
 * verticle deploy 生命周期类
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/18 13:42
 */
public interface VerticleDeployLifeCycle<V extends Verticle> {

    /**
     * 部署配置属性的前缀
     *
     * @return {@link String}
     */
    String deployPropsPrefix();

    /**
     * 部署之前
     *
     * @param action 行动
     * @return 强制返回this
     * @apiNote 只执行一次，用来做一些初始化的操作
     */
    Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>> preDeploy(InitCallable<List<Object>> action);

    /**
     * 部署完成
     *
     * @param completion completion
     */
    default void deployComplete(final AsyncResult<String> completion) {
        System.out.println(this.getClass() + " deploy finish");
    }
}