package chat.vertx.fzdwx.ws;

import chat.vertx.fzdwx.core.ws.WebSocketListener;
import chat.vertx.fzdwx.core.ws.WsWrap;
import org.noear.solon.annotation.Component;
import vertx.fzdwx.cn.core.annotation.ServerEndpoint;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/20 10:36
 */
@ServerEndpoint("/ws/:id")
@Component
public class WsHandler implements WebSocketListener {

    @Override
    public void onOpen(final WsWrap ws) {
        WsServUserManager.addUser(ws.path("id"), ws);
    }

    @Override
    public void onclose(final WsWrap ws) {

    }

    @Override
    public void onError(final WsWrap ws, final Throwable thr) {
        System.out.println("get error");
    }

    @Override
    public void handleText(final WsWrap ws, final String message) {
        System.out.println(message);
    }

    @Override
    public void onEnd(final WsWrap ws) {
        WsServUserManager.delUser(ws.path("id"), ws);
    }
}
