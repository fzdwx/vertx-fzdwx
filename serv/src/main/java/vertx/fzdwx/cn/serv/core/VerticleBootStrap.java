package vertx.fzdwx.cn.serv.core;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:02
 */
@Slf4j
public class VerticleBootStrap {

    public static Map<Object, Object> map = new HashMap<>();
    private static ChatServerProps chatServerProps;
    private static List<HttpHandlerMapping> httpHandlerMappings;
    private static List<WebSocketListenerMapping> webSocketListenerMappings;
    private static Vertx vertx;

    public VerticleBootStrap(final ChatServerProps chatServerProps, final List<HttpHandlerMapping> httpHandlerMappings,
                             final List<WebSocketListenerMapping> webSocketListenerMappings) {
        vertx = Vertx.vertx(chatServerProps.getVertxOps());

        VerticleBootStrap.chatServerProps = chatServerProps;
        VerticleBootStrap.httpHandlerMappings = httpHandlerMappings;
        VerticleBootStrap.webSocketListenerMappings = webSocketListenerMappings;
        init();
    }

    // TODO: 2022/2/17 优化
    public void init() {
        map.put("chatServerProps", chatServerProps);
        map.put("httpHandlerMappings", httpHandlerMappings);
        map.put("webSocketListenerMappings", webSocketListenerMappings);
    }

    /**
     * 部署
     *
     * @return {@link Uni<String> }
     */
    public Uni<String> deploy() {
        return vertx.deployVerticle("vertx.fzdwx.cn.serv.core.ChatServerVertx", chatServerProps.getDeployOps());
    }

    public void stop() {
        vertx.closeAndAwait();
    }
}