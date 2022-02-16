package chat.like.cn.serv.core;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class ChatServerBootStrap {

    public static HttpServer chatServer;
    public static Vertx vertx;
    public static String serverURL;

    private final ChatServerProps chatServerProps;
    private final Router router;
    private volatile Boolean startedFlag = Boolean.FALSE;

    public ChatServerBootStrap(final ChatServerProps chatServerProps, final Multi<HttpHandlerMapping> httpHandlerSup,
                               final Multi<WebSocketListenerMapping> websocketSup) {
        this.chatServerProps = chatServerProps;
        serverURL = "http://localhost:" + chatServerProps.getPort();
        vertx = Vertx.vertx(chatServerProps.getVertxOps());
        this.router = Router.router(vertx);
        initWsHandler(websocketSup);
        initHttpHandler(httpHandlerSup);
    }

    public Uni<ChatServerBootStrap> start() {
        synchronized (this) {
            if (!startedFlag) {
                return vertx.createHttpServer()
                        // .exceptionHandler(ex -> {
                        //     log.error("", ex.getCause());
                        // })
                        .requestHandler(router)
                        .listen(chatServerProps.getPort())
                        .onItem().invoke(h -> {
                            chatServer = h;
                            vertx.deployVerticle(chatServerProps.getAppName() + "-vert.x", chatServerProps.getDeployOps()).subscribe();
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