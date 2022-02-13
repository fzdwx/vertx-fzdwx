package chat.like.cn.serv.plugin;

import chat.like.cn.core.util.StopWatch;
import chat.like.cn.serv.core.ChatServerBootStrap;
import chat.like.cn.serv.core.ChatServerProps;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.ServerEndpoint;
import org.noear.solon.core.Aop;

import java.net.http.WebSocket.Listener;

import static chat.like.cn.core.util.Func.contains;

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
        // final var chatServerProp = app.cfg().getBean("chat.serv", ChatServerProps.class);

        StopWatch.start();
        final var websocketEndpoint = Aop.beanFind((beanWrap -> {
            final var serverEndpoint = beanWrap.annotationGet(ServerEndpoint.class);
            if (serverEndpoint == null) return false;

            if (!contains(beanWrap.clz().getInterfaces(), Listener.class)) {
                log.error("Websocket Endpoint 必须实现: java.net.http.Listener.java");
                System.exit(1);
            }
            return true;
        }));

        final var chatServerBootStrap = new ChatServerBootStrap(chatServerProps);
        chatServerBootStrap.start();
        Aop.inject(chatServerBootStrap);
    }
}
