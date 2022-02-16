package vertx.fzdwx.cn.serv.core;

import vertx-fzdwx.cn.serv.constants.SharDataCons;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class ChatServerBootStrap {

    public static ChatServerProps chatServerProps;
    public static List<HttpHandlerMapping> httpHandlerSup;
    public static List<WebSocketListenerMapping> websocketSup;
    private final Vertx vertx;

    public ChatServerBootStrap(final ChatServerProps chatServerProps, final List<HttpHandlerMapping> httpHandlerSup,
                               final List<WebSocketListenerMapping> websocketSup) {
        this.vertx = Vertx.vertx(chatServerProps.getVertxOps());
        SharDataCons.vertx = vertx;
        vertx.sharedData().getAsyncMap(SharDataCons.INIT_VERTX_MAp).onItem()
                .invoke(map -> {
                    map.put(SharDataCons.chatServerProps, chatServerProps).subscribe();
                    map.put(SharDataCons.httpHandlerSup, httpHandlerSup).subscribe();
                    map.put(SharDataCons.websocketSup, websocketSup).subscribe();
                }).subscribe();

        // TODO: 2022/2/16  SharData 包装
        ChatServerBootStrap.chatServerProps = chatServerProps;
        ChatServerBootStrap.httpHandlerSup = httpHandlerSup;
        ChatServerBootStrap.websocketSup = websocketSup;
    }

    public Uni<String> start() {
        return vertx.deployVerticle("vertx-fzdwx.cn.serv.core.ChatServerVertx", chatServerProps.getDeployOps());
    }

    public void stop() {
        this.vertx.closeAndAwait();
    }
}
