package chat.vertx.fzdwx.core.ws;

import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 16:56
 */
@Slf4j
public class WebSocketListenerMapping {

    /**
     * {@link WebSocketListener} 实现类
     */
    private final WebSocketListener source;
    /**
     * 当前的路径
     */
    private final String path;

    private WebSocketListenerMapping(final WebSocketListener webSocketListener, String path) {
        this.source = webSocketListener;
        this.path = path;
    }

    public static WebSocketListenerMapping create(WebSocketListener source, String path) {
        return new WebSocketListenerMapping(source, path);
    }

    /**
     * 将当前websocket请求处理器挂载到router上
     *
     * @param router router
     */
    public void attach(final Router router) {
        log.info("WebSocket Endpoint Registered: " + path);

        router.get(path)
                .handler(ctx -> {
                    ctx.request().toWebSocket()
                            .onSuccess(s -> {
                                final var wrap = WsWrap.of(s, ctx);

                                // onClose
                                s.closeHandler(v -> {
                                    source.onclose(wrap);
                                });

                                //region websocket 握手  1
                                source.doOnHandShake(s.headers(), ar -> {
                                    if (ar.failed()) {
                                        s.writeTextMessage(ar.cause().getMessage())
                                                .onComplete(res -> s.close());
                                    }
                                });
                                //endregion

                                // onOpen               2
                                source.onOpen(wrap);

                                //region 处理来自客户端的数据
                                s.pongHandler(buf -> {
                                    source.handlePong(wrap, buf);
                                });

                                s.frameHandler(f -> {
                                    if (f.isPing()) {
                                        source.handlePing(wrap, f);
                                    }
                                });

                                s.binaryMessageHandler(buf -> {
                                    source.handleBinary(wrap, buf);
                                });

                                s.textMessageHandler(text -> {
                                    source.handleText(wrap, text);
                                });
                                //endregion

                                // websocket 中的异常
                                s.exceptionHandler(exc -> {
                                    source.onError(wrap, exc);
                                });

                                // 在onClose后执行  todo 是否要加入webSocketListener中？
                                s.endHandler(v -> {
                                    source.onEnd(wrap);
                                });
                            });
                });
    }
}