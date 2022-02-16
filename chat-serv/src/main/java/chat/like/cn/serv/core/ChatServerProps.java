package chat.like.cn.serv.core;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import lombok.Getter;
import lombok.Setter;
import org.noear.solon.annotation.Get;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:04
 */
@Setter
@Getter
@Get
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
    private int eventLoopPoolSize;


    public VertxOptions getVertxOps() {
        return new VertxOptions();
    }

    public DeploymentOptions getDeployOps() {
        return new DeploymentOptions();
    }

}