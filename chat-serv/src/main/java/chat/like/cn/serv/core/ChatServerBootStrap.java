package chat.like.cn.serv.core;

import chat.like.cn.core.util.ChatUtil;
import chat.like.cn.core.util.StopWatch;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
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
        vertx = Vertx.vertx(chatServerProps);
    }

    public ChatServerBootStrap start() {
        chatServer = vertx.createHttpServer()
                .requestHandler(this::dispatcher)
                .listen(chatServerProps.getPort(), http -> {
                    if (http.succeeded()) {
                        ServerURL = "http://localhost:" + chatServerProps.getPort();
                        log.info("started in " + StopWatch.stop() + " ms. Listening on: " + ServerURL);
                    } else {
                        log.error("started fail ", http.cause());
                    }
                });

        return this;
    }

    private void dispatcher(final HttpServerRequest req) {
        if (ChatUtil.isWs(req))
            doWs(req);
        else doHttp(req);
    }

    private void doHttp(final HttpServerRequest req) {
        req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!");
    }

    private void doWs(final HttpServerRequest req) {
        req.toWebSocket()
                .onSuccess(ws -> {
                    ws.frameHandler(f -> {
                        // f. todo
                    });
                });
    }
}
