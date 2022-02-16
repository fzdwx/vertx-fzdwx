package chat.like.cn.serv.core;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 17:52
 */
@Slf4j
public class ChatServerVertx extends AbstractVerticle {

    public HttpServer chatServer;
    private Router router;
    private ChatServerProps chatServerProps = ChatServerBootStrap.chatServerProps;
    private List<HttpHandlerMapping> httpHandlerSup = ChatServerBootStrap.httpHandlerSup;
    private List<WebSocketListenerMapping> websocketSup = ChatServerBootStrap.websocketSup;
    private volatile static Boolean firstFlag = Boolean.TRUE;

    @Override
    public Uni<Void> asyncStart() {
        return this.start0().replaceWithVoid();
    }

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
        if (firstFlag == Boolean.TRUE) firstFlag = Boolean.FALSE;
        super.start(startPromise);
    }

    public Uni<ChatServerVertx> start0() {
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
                })
                .onFailure().invoke(thr -> {
                    log.error("started fail {}", thr.getMessage());
                    System.exit(1);
                })
                .replaceWith(Uni.createFrom().item(() -> this));
    }

    private void initWsHandler(final List<WebSocketListenerMapping> websocketSup) {
        websocketSup.forEach(w -> {
            w.attach(router, firstFlag);
        });
    }

    private void initHttpHandler(final List<HttpHandlerMapping> httpHandlerSup) {
        httpHandlerSup.forEach(w -> {
            w.attach(router, firstFlag);
        });
    }
}
