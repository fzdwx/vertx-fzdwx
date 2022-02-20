package chat.vertx.fzdwx.core.ws;

import cn.hutool.core.util.IdUtil;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.RoutingContext;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/20 10:54
 */
public class WsWrap implements Comparable<WsWrap> {

    private final RoutingContext ctx;
    private final ServerWebSocket ws;
    private final String wsId;

    private WsWrap(final ServerWebSocket ws, final RoutingContext ctx, final String wsId) {
        this.ws = ws;
        this.ctx = ctx;
        this.wsId = wsId;
    }

    public static WsWrap of(ServerWebSocket ws, final RoutingContext ctx) {
        return new WsWrap(ws, ctx, IdUtil.fastSimpleUUID());
    }

    public String path(final String key) {
        return this.ctx.pathParam(key);
    }

    public ServerWebSocket source() {
        return ws;
    }

    @Override
    public int compareTo(final WsWrap o) {
        return this.wsId.compareTo(o.wsId);
    }

    public void sendAndClose(final String message) {
        ws.writeTextMessage(message).compose(f -> ws.close());
    }
}
