package chat.like.cn.serv.core;

import chat.like.cn.core.util.Exc;
import cn.hutool.core.util.StrUtil;
import io.vertx.mutiny.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.ServerEndpoint;

import static chat.like.cn.core.function.lang.*;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 16:56
 */
@Slf4j
public class WebSocketListenerMapping {

    /**
     * 添加了{@link ServerEndpoint}注解的类的实体
     */
    private final WebSocketListener source;
    /**
     * source上对应的注解
     */
    private final ServerEndpoint anno;
    /**
     * 当前的路径
     */
    private final String path;

    private WebSocketListenerMapping(final WebSocketListener webSocketListener, final ServerEndpoint anno) {
        this.source = webSocketListener;
        this.anno = anno;
        this.path = initPath();
    }

    public static WebSocketListenerMapping create(WebSocketListener source, ServerEndpoint anno) {
        return new WebSocketListenerMapping(source, anno);
    }

    /**
     * 将当前websocket请求处理器挂载到router上
     *
     * @param router router
     */
    public void attach(final Router router) {
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


    private String initPath() {
        final var path = defVal(anno.value(), anno.path());
        if (StrUtil.isBlank(path)) {
            throw Exc.chat("路径不能为空");
        }
        return path;
    }
}