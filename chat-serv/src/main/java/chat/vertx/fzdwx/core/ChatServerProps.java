package chat.vertx.fzdwx.core;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:04
 */
@Setter
@Getter
@DataObject(generateConverter = true)
public class ChatServerProps {

    /**
     * 应用程序名称
     */
    private String appName = "chatServer";

    /**
     * 当前chat server监听的端口
     */
    private int port = 8888;

    /**
     * {@link VertxOptions#getEventLoopPoolSize()}
     */
    private int eventLoopPoolSize = VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE;

    /**
     * {@link DeploymentOptions#getInstances()}
     */
    private int deployInstances = VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE;

    public DeploymentOptions getDeployOps() {
        return new DeploymentOptions().setInstances(deployInstances);
    }

}