package vertx.fzdwx.cn.serv;

import vertx-fzdwx.cn.serv.core.WebSocketListener;
import io.vertx.mutiny.core.http.ServerWebSocket;
import org.noear.solon.annotation.ServerEndpoint;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:37
 */
@ServerEndpoint("/123123")
public class TestServerEndpoint implements WebSocketListener {

    @Override
    public void onOpen(final ServerWebSocket ws) {
        ws.writeTextMessage("你好").subscribe();
    }

    @Override
    public void onclose(final ServerWebSocket ws) {
        System.out.println("web socket 关闭");
    }
}
