package chat.like.cn.serv.core;

import io.vertx.mutiny.ext.web.Router;
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
     * @param router    router
     * @param firstFlag
     */
    public void attach(final Router router, final Boolean firstFlag) {
        if (firstFlag) log.info("WebSocket Endpoint Registered: " + path);

        router.get(path)
                .handler(ctx -> {
                    ctx.request().toWebSocket()
                            .onItem()
                            .invoke(ws -> {
                                // onClose
                                ws.closeHandler(() -> {
                                    source.onclose(ws);
                                });

                                //region websocket 握手  1
                                source.doOnHandShake(ws.headers(), ar -> {
                                    if (ar.failed()) {
                                        ws.writeTextMessage(ar.cause().getMessage())
                                                .onTermination()
                                                .invoke(ws::close)
                                                .subscribe();
                                    }
                                });
                                //endregion

                                // onOpen               2
                                source.onOpen(ws);

                                //region 处理来自客户端的数据
                                ws.pongHandler(buf -> {
                                    source.handlePong(ws, buf);
                                });

                                ws.frameHandler(f -> {
                                    if (f.isPing()) {
                                        source.handlePing(ws, f);
                                    }
                                });

                                ws.binaryMessageHandler(buf -> {
                                    source.handleBinary(ws, buf);
                                });

                                ws.textMessageHandler(text -> {
                                    source.handleText(ws, text);
                                });
                                //endregion

                                // websocket 中的异常
                                ws.exceptionHandler(exc -> {
                                    source.onError(ws, exc);
                                });

                                // 在onClose后执行  todo 是否要加入webSocketListener中？
                                ws.endHandler(() -> {
                                    log.info("Websocket Stream End");
                                });
                            }).subscribe();
                });
    }
}
