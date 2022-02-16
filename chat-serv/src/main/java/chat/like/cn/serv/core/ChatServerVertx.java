package chat.like.cn.serv.core;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 17:52
 */
@Slf4j
public class ChatServerVertx extends AbstractVerticle {

    public HttpServer chatServer;
    private Router router;
    private ChatServerProps chatServerProps = ChatServerBootStrap.chatServerProps;
    private Multi<HttpHandlerMapping> httpHandlerSup = ChatServerBootStrap.httpHandlerSup;
    private Multi<WebSocketListenerMapping> websocketSup = ChatServerBootStrap.websocketSup;
    private volatile Boolean startedFlag = Boolean.FALSE;

    @Override
    public Uni<Void> asyncStart() {
        return this.start0().replaceWithVoid();
    }

    public Uni<ChatServerVertx> start0() {
        synchronized (this) {
            if (!startedFlag) {
                this.router = Router.router(vertx);
                initWsHandler(websocketSup);
                initHttpHandler(httpHandlerSup);

                return vertx.createHttpServer()
                        // .exceptionHandler(ex -> {
                        //     log.error("", ex.getCause());
                        // })
                        .requestHandler(router)
                        .listen(chatServerProps.getPort())
                        .onItem().invoke(h -> {
                            chatServer = h;
                            startedFlag = Boolean.TRUE;
                        })
                        .onFailure().invoke(thr -> {
                            log.error("started fail {}", thr.getMessage());
                            System.exit(1);
                        })
                        .replaceWith(Uni.createFrom().item(() -> this));
            } else throw new RuntimeException(chatServerProps.getAppName() + " is started!");
        }
    }

    private void initWsHandler(final Multi<WebSocketListenerMapping> websocketSup) {
        // TODO: 2022/2/16 有问题
        websocketSup.subscribe()
                .asIterable().forEach(w -> {
                    w.attach(router);
                });
    }

    private void initHttpHandler(final Multi<HttpHandlerMapping> httpHandlerSup) {
        httpHandlerSup.subscribe()
                .asIterable().forEach(w -> {
                    w.attach(router);
                });
    }
}