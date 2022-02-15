package chat.like.cn.serv;

import chat.like.cn.serv.core.WebSocketListener;
import io.vertx.core.http.ServerWebSocket;
import org.noear.solon.annotation.ServerEndpoint;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:37
 */
@ServerEndpoint("/123123")
public class TestServerEndpoint implements WebSocketListener {

    @Override
    public void onOpen(final ServerWebSocket ws) {
        ws.writeTextMessage("你好");
    }

    @Override
    public void onclose(final ServerWebSocket ws) {
        System.out.println("web socket 关闭");
    }
}