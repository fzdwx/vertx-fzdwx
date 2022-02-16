package chat.like.cn.serv.solon;

import chat.like.cn.core.util.StopWatch;
import chat.like.cn.core.util.Utils;
import chat.like.cn.serv.core.ChatServerBootStrap;
import chat.like.cn.serv.core.ChatServerProps;
import chat.like.cn.serv.core.HttpHandlerMapping;
import chat.like.cn.serv.core.WebSocketListener;
import chat.like.cn.serv.core.WebSocketListenerMapping;
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

import java.util.List;
import java.util.function.Supplier;

import static chat.like.cn.core.util.lang.contains;
import static chat.like.cn.core.util.lang.defVal;
import static chat.like.cn.core.util.lang.format;

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
        Aop.inject(
                new ChatServerBootStrap(chatServerProps, collectHttp(), collectWs()).start()
        );
    }

    public Supplier<List<WebSocketListenerMapping>> collectWs() {
        return () -> Aop.beanFind((ChatServSolonStarter::wxCondition))
                .stream()
                .map(beanWrap -> {
                    final var serverEndpoint = beanWrap.annotationGet(ServerEndpoint.class);
                    log.info("WebSocket Endpoint Find: " + format("[ {}, Path: {} ]", beanWrap.clz(), defVal(serverEndpoint.value(), serverEndpoint.path())));

                    return WebSocketListenerMapping.create(beanWrap.get(), serverEndpoint);
                }).toList();
    }

    public Supplier<List<HttpHandlerMapping>> collectHttp() {
        return () -> Aop.beanFind((ChatServSolonStarter::httpCondition))
                .stream()
                .flatMap(beanWrap -> {
                    final var parentMapping = beanWrap.annotationGet(Mapping.class);
                    final var rootPath = defVal(defVal(parentMapping.value(), parentMapping.path()), "/");

                    log.info("Http Controller Find: " + format("[ {}, rootPath: {} ]", beanWrap.clz(), rootPath));

                    return Utils.collectMethod(beanWrap.clz().getDeclaredMethods(), Utils.allHttpType())
                            .stream().map(MethodWrap::get)
                            .map(methodWrap -> HttpHandlerMapping.create(beanWrap.get(), methodWrap, rootPath));
                })
                .toList();
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