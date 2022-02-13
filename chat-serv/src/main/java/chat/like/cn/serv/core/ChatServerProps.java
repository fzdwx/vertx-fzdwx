package chat.like.cn.serv.core;

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
public class ChatServerProps extends VertxOptions {

    /** 当前chat server监听的端口 */
    private int port = 8888;
}
