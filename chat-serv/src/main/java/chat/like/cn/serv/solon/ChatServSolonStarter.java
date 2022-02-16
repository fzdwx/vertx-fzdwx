package chat.like.cn.serv.solon;

import chat.like.cn.core.util.StopWatch;
import chat.like.cn.core.util.Utils;
import chat.like.cn.serv.core.ChatServerBootStrap;
import chat.like.cn.serv.core.ChatServerProps;
import chat.like.cn.serv.core.HttpHandlerMapping;
import chat.like.cn.serv.core.WebSocketListener;
import chat.like.cn.serv.core.WebSocketListenerMapping;
import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.ServerEndpoint;
import org.noear.solon.core.Aop;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.wrap.MethodWrap;

import java.time.Duration;

import static chat.like.cn.core.function.lang.*;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:20
 */
@Slf4j
@Component
public class ChatServSolonStarter {

    @Inject("${chat.serv}")
    private ChatServerProps chatServerProps;

    @Init
    public void init() {
        StopWatch.start();

        new ChatServerBootStrap(chatServerProps, collectHttp(), collectWs())
                .start()
                .onItem().invoke(bs -> {
                    log.info("started in " + StopWatch.stop() + " ms. Listening on: " + ChatServerBootStrap.serverURL);
                    Aop.inject(bs);
                })
                .await()
                .atMost(Duration.ofSeconds(3));
    }

    public Multi<WebSocketListenerMapping> collectWs() {
        return Multi.createFrom().iterable(Aop.beanFind((ChatServSolonStarter::wxCondition))
                .stream()
                .map(beanWrap -> {
                    final var serverEndpoint = beanWrap.annotationGet(ServerEndpoint.class);
                    log.info("WebSocket Endpoint Find: " + format("[ {}, Path: {} ]", beanWrap.clz(), defVal(serverEndpoint.value(), serverEndpoint.path())));

                    return WebSocketListenerMapping.create(beanWrap.get(), serverEndpoint);
                }).toList());
    }

    public Multi<HttpHandlerMapping> collectHttp() {
        return Multi.createFrom().iterable((Aop.beanFind((ChatServSolonStarter::httpCondition))
                .stream()
                .flatMap(beanWrap -> {
                    final var parentMapping = beanWrap.annotationGet(Mapping.class);
                    final var rootPath = defVal(defVal(parentMapping.value(), parentMapping.path()), "/");

                    log.info("Http Controller Find: " + format("[ {}, rootPath: {} ]", beanWrap.clz(), rootPath));

                    return Utils.collectMethod(beanWrap.clz().getDeclaredMethods(), Utils.allHttpType())
                            .stream().map(MethodWrap::get)
                            .map(methodWrap -> HttpHandlerMapping.create(beanWrap.get(), methodWrap, rootPath));
                }).toList()));
    }

    private static boolean wxCondition(final BeanWrap beanWrap) {
        final var serverEndpoint = beanWrap.annotationGet(ServerEndpoint.class);
        if (serverEndpoint == null) return false;

        if (!contains(beanWrap.clz().getInterfaces(), WebSocketListener.class)) {
            log.error("Websocket endpoint 必须实现: java.net.http.Listener.java");
            System.exit(1);
        }
        return true;
    }

    private static boolean httpCondition(final BeanWrap beanWrap) {
        return beanWrap.annotationGet(Controller.class) != null;
    }
}