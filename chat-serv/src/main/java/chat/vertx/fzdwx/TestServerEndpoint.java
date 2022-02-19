package chat.vertx.fzdwx;

import chat.vertx.fzdwx.core.ws.WebSocketListener;
import io.vertx.core.http.ServerWebSocket;
import vertx.fzdwx.cn.core.annotation.ServerEndpoint;

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

    @Override
    public void onError(final ServerWebSocket ws, final Throwable thr) {

    }
}