package chat.vertx.fzdwx.core;

import io.vertx.codegen.annotations.DataObject;
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
    private String chatServerName;

    /**
     * 当前chat server监听的端口
     */
    private int port = 8888;

    public VertxOptions getVertxOps() {
        return new VertxOptions();
    }
}