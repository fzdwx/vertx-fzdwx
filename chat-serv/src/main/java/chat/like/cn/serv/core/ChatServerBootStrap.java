package chat.like.cn.serv.core;

import chat.like.cn.core.util.StopWatch;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class ChatServerBootStrap {

    private final ChatServerProps chatServerProps;
    public static HttpServer chatServer;
    public static Vertx vertx;
    public static String ServerURL;

    public ChatServerBootStrap(final ChatServerProps chatServerProps) {
        this.chatServerProps = chatServerProps;
        vertx = Vertx.vertx(chatServerProps.getVertxOps());
    }

    public ChatServerBootStrap start() {
        final Router router = Router.router(vertx);

        chatServer = vertx.createHttpServer()
                .requestHandler(router)
                .listen(chatServerProps.getPort(), http -> {
                    if (http.succeeded()) {
                        ServerURL = "http://localhost:" + chatServerProps.getPort();
                        log.info("started in " + StopWatch.stop() + " ms. Listening on: " + ServerURL);
                    } else {
                        log.error("started fail ", http.cause());
                    }
                });

        vertx.deployVerticle("chatServer-vert.x", chatServerProps.getDeployOps());
        return this;
    }
}