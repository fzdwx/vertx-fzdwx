package chat.like.cn.serv.solon;

import chat.like.cn.core.util.Print;
import chat.like.cn.core.util.StopWatch;
import chat.like.cn.core.util.Utils;
import chat.like.cn.core.wraper.HttpHandlerWrap;
import chat.like.cn.serv.core.ChatServerBootStrap;
import chat.like.cn.serv.core.ChatServerProps;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Patch;
import org.noear.solon.annotation.Post;
import org.noear.solon.annotation.Put;
import org.noear.solon.annotation.ServerEndpoint;
import org.noear.solon.core.Aop;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.wrap.MethodWrap;

import java.net.http.WebSocket.Listener;
import java.util.List;

import static chat.like.cn.core.util.Func.contains;
import static chat.like.cn.core.util.Func.defVal;
import static chat.like.cn.core.util.Func.format;

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
        collectWs();
        final var httpHandlerWraps = collectHttp();
        final var server = new ChatServerBootStrap(chatServerProps)
                .start();
        Aop.inject(server);
    }

    public void collectWs() {
        Aop.beanFind((ChatServSolonStarter::wxCondition))
                .stream()
                .map(beanWrap -> {
                    return null;
                });

    }

    private static boolean wxCondition(final BeanWrap beanWrap) {
        final var serverEndpoint = beanWrap.annotationGet(ServerEndpoint.class);
        if (serverEndpoint == null) return false;

        if (!contains(beanWrap.clz().getInterfaces(), Listener.class)) {
            log.error("Websocket endpoint 必须实现: java.net.http.Listener.java");
            System.exit(1);
        }

        Print.info("WebSocket Find", format("[ {}, Path: {} ]", beanWrap.clz(), defVal(serverEndpoint.value(), serverEndpoint.path())));

        return true;
    }

    public List<HttpHandlerWrap> collectHttp() {
        return Aop.beanFind((ChatServSolonStarter::httpCondition))
                .stream()
                .flatMap(beanWrap -> {
                    final var parentMapping = beanWrap.annotationGet(Mapping.class);
                    final var rootPath = defVal(defVal(parentMapping.value(), parentMapping.path()), "/");

                    Print.info("Http Controller", format("[ {}, ParentPath: {} ]", beanWrap.clz(), rootPath));

                    return Utils.collectMethod(beanWrap.clz().getDeclaredMethods(), List.of(Get.class, Post.class, Delete.class, Put.class, Patch.class))
                            .stream().map(MethodWrap::get)
                            .map(methodWrap -> {
                                return HttpHandlerWrap.create(beanWrap.get(), methodWrap, rootPath);
                            });
                })
                .toList();
    }

    private static boolean httpCondition(final BeanWrap beanWrap) {
        return beanWrap.annotationGet(Controller.class) != null;
    }
}