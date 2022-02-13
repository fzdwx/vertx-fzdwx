package chat.like.cn.serv.core;

import chat.like.cn.core.util.StopWatch;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Getter
@Setter
@Slf4j
public class ChatServerBootStrap {

    private ChatServerProps chatServerProps;
    public static HttpServer chatServer;
    public static Vertx vertx;

    public ChatServerBootStrap(final ChatServerProps chatServerProps) {
        this.chatServerProps = chatServerProps;
        vertx = Vertx.vertx(chatServerProps);
    }

    public void start() {
        chatServer = vertx.createHttpServer()
                .webSocketHandler(null)
                .requestHandler(req -> {
                    req.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello from Vert.x!");
                })
                .listen(chatServerProps.getPort(), http -> {
                    if (http.succeeded()) {
                        log.info("started in " + StopWatch.stop() + "ms. Listening on: http://localhost:" + chatServerProps.getPort());
                    } else {
                        log.error("started fail ", http.cause());
                    }
                });
    }
}
