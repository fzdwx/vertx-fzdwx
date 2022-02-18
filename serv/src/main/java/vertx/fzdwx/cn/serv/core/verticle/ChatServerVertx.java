package vertx.fzdwx.cn.serv.core.verticle;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.serv.core.http.HttpHandlerMapping;
import vertx.fzdwx.cn.serv.core.ws.WebSocketListenerMapping;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 17:52
 */
@Slf4j
public class ChatServerVertx extends Verticle {

    public HttpServer chatServer;
    private Router router;

    @Override
    protected boolean first() {
        return first;
    }

    @Override
    protected void init() {
    }

    @Override
    public void start0(final Promise<Void> startPromise) {
        this.router = Router.router(vertx);
        initWsHandler(VerticleBootStrap.webSocketListenerMappings);
        initHttpHandler(VerticleBootStrap.httpHandlerMappings);

        vertx.createHttpServer()
                .exceptionHandler(ex -> {
                    log.error("", ex.getCause());
                })
                .requestHandler(router)
                .listen(VerticleBootStrap.chatServerProps.getPort(), res -> {
                    if (res.succeeded()) {
                        chatServer = res.result();
                        startPromise.complete();
                    } else startPromise.fail(res.cause());
                });
    }

    private void initWsHandler(final List<WebSocketListenerMapping> websocketSup) {
        websocketSup.forEach(w -> {
            w.attach(router, first);
        });
    }

    private void initHttpHandler(final List<HttpHandlerMapping> httpHandlerSup) {
        httpHandlerSup.forEach(w -> {
            w.attach(router, first);
        });

        // httpHandlerSup.stream().collect(Collectors.groupingBy(h -> h.rootPath))
        //         .forEach((rootPath, mappings) -> {
        //             final var subRouter = rootCache.getOrDefault(rootPath, ((Supplier<Router>) () -> {
        //                 final var subRouter0 = Router.router(vertx);
        //                 router.mountSubRouter(rootPath, subRouter0);
        //                 rootCache.put(rootPath, subRouter0);
        //                 return subRouter0;
        //             }).get());
        //
        //             if (first) {
        //                 log.info("Http Handler Root: {}", rootPath);
        //             }
        //             mappings.forEach(m -> {
        //                 m.attach(subRouter, first);
        //             });
        //         });
    }

    private static AtomicInteger instanceCount = new AtomicInteger(0);
    private final boolean first;

    {
        int number = instanceCount.getAndIncrement();
        first = number == 1;
    }
}