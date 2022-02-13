package chat.like.cn.serv;

import org.noear.solon.annotation.ServerEndpoint;

import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:37
 */
@ServerEndpoint("/123123")
public class TestServerEndpoint implements Listener {

    @Override
    public void onOpen(final WebSocket webSocket) {
    }
}
