package chat.like.cn.serv.core;

import chat.like.cn.core.util.StopWatch;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Supplier;

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
    private final List<HttpHandlerMapping> httpHandlerMappings;
    private final List<WebSocketListenerMapping> webSocketListenerMappings;
    private final Router router;

    public ChatServerBootStrap(final ChatServerProps chatServerProps, final Supplier<List<HttpHandlerMapping>> httpHandlerSup,
                               final Supplier<List<WebSocketListenerMapping>> websocketSup) {
        this.chatServerProps = chatServerProps;
        serverURL = "http://localhost:" + chatServerProps.getPort();
        vertx = Vertx.vertx(chatServerProps.getVertxOps());
        this.router = Router.router(vertx);
        this.httpHandlerMappings = httpHandlerSup.get();
        this.webSocketListenerMappings = websocketSup.get();
    }

    public ChatServerBootStrap start() {
        initHttpHandler();
        initWsHandler();

        chatServer = vertx.createHttpServer()
                // .exceptionHandler(ex -> {
                //     log.error("", ex.getCause());
                // })
                .requestHandler(router)
                .listen(chatServerProps.getPort(), http -> {
                    if (http.succeeded()) {
                        log.info("started in " + StopWatch.stop() + " ms. Listening on: " + serverURL);
                    } else {
                        log.error("started fail ", http.cause());
                    }
                });

        vertx.deployVerticle("chatServer-vert.x", chatServerProps.getDeployOps());
        return this;
    }

    private void initWsHandler() {
        webSocketListenerMappings.forEach(w -> { w.attach(router); });
    }

    private void initHttpHandler() {
        httpHandlerMappings.forEach(h -> h.attach(router));
    }
}