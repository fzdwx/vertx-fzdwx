package vertx.fzdwx.cn.serv.core;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class VerticleBootStrap {

    static ChatServerProps chatServerProps = new ChatServerProps();
    static List<HttpHandlerMapping> httpHandlerMappings = new ArrayList<>();
    static List<WebSocketListenerMapping> webSocketListenerMappings = new ArrayList<>();
    static Vertx vertx;

    public VerticleBootStrap(
            final ChatServerProps chatServerProps,
            final List<HttpHandlerMapping> httpHandlerMappings,
            final List<WebSocketListenerMapping> webSocketListenerMappings) {
        vertx = Vertx.vertx(chatServerProps.getVertxOps());
        VerticleBootStrap.chatServerProps.other(chatServerProps);
        VerticleBootStrap.httpHandlerMappings.addAll(httpHandlerMappings);
        VerticleBootStrap.webSocketListenerMappings.addAll(webSocketListenerMappings);
    }

    /**
     * 部署
     *
     * @return {@link String }
     */
    public Future<String> deploy() {
        return vertx.deployVerticle("vertx.fzdwx.cn.serv.core.ChatServerVertx", chatServerProps.getDeployOps());
    }

    public void stop() {
        vertx.close();
    }
}