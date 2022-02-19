package vertx.fzdwx.cn.core.verticle.init;

import io.vertx.core.AsyncResult;
import vertx.fzdwx.cn.core.verticle.Verticle;

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
     * @see io.vertx.core.DeploymentOptions
     */
    String deployPropsPrefix();

    /**
     * 部署之前的操作
     */
    void preDeploy();

    /**
     * 部署完成
     *
     * @param completion completion
     */
    default void deployComplete(final AsyncResult<String> completion) {
        System.out.println(this.getClass() + " deploy finish");
    }
}