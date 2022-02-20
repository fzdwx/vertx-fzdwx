package chat.vertx.fzdwx.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:04
 */
@Setter
@Getter
public class ChatServerProps {

    /**
     * 应用程序名称
     */
    private String chatServerName;

    /**
     * 当前chat server监听的端口
     */
    private int port = 8888;
}