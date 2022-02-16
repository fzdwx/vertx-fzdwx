package chat.like.cn.serv.solon;

import chat.like.cn.core.util.Exc;
import chat.like.cn.core.util.StopWatch;
import chat.like.cn.core.util.Utils;
import chat.like.cn.core.wraper.HttpMethodWrap;
import chat.like.cn.serv.core.ChatServerBootStrap;
import chat.like.cn.serv.core.ChatServerProps;
import chat.like.cn.serv.core.HttpHandlerMapping;
import chat.like.cn.serv.core.WebSocketListener;
import chat.like.cn.serv.core.WebSocketListenerMapping;
import chat.like.cn.solon.annotation.Destroy;
import cn.hutool.core.util.StrUtil;
import io.smallrye.mutiny.Multi;
import io.vertx.core.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Init;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.ServerEndpoint;
import org.noear.solon.core.Aop;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.handle.MethodTypeUtil;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;

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
    private ChatServerBootStrap chatServerBootStrap;

    @Init
    public void init() {
        StopWatch.start();

        chatServerBootStrap = new ChatServerBootStrap(chatServerProps, collectHttp(), collectWs())
                .start()
                .onItem().invoke(bs -> {
                    log.info(chatServerProps.getAppName() + " started in " + StopWatch.stop() + " ms. Listening on: " + ChatServerBootStrap.serverURL);
                    Aop.inject(bs);
                })
                .await()
                .atMost(Duration.ofSeconds(3));
    }

    @Destroy
    public void destroy() {
        chatServerBootStrap.stop();
    }

    public Multi<WebSocketListenerMapping> collectWs() {
        return Multi.createFrom().iterable(Aop.beanFind((ChatServSolonStarter::wxCondition))
                .stream()
                .map(beanWrap -> {
                    final var serverEndpoint = beanWrap.annotationGet(ServerEndpoint.class);
                    log.info("WebSocket Endpoint Find: " + format("[ {}, Path: {} ]", beanWrap.clz(), defVal(serverEndpoint.value(), serverEndpoint.path())));

                    return WebSocketListenerMapping.create(beanWrap.get(), initWsPath(serverEndpoint));
                }).toList());
    }

    public Multi<HttpHandlerMapping> collectHttp() {
        return Multi.createFrom().iterable((Aop.beanFind((ChatServSolonStarter::httpCondition))
                .stream()
                .flatMap(beanWrap -> {
                    final var parentMapping = beanWrap.annotationGet(Mapping.class);
                    final var rootPath = defVal(defVal(parentMapping.value(), parentMapping.path()), "/");

                    log.info("Http Controller Find: " + format("[ {}, rootPath: {} ]", beanWrap.clz(), rootPath));

                    return Utils.collectMethod(beanWrap.clz().getDeclaredMethods(), Utils.allHttpType()).stream()
                            .map(method -> initMethod(rootPath, method))
                            .map(methodWrap -> HttpHandlerMapping.create(beanWrap.get(), methodWrap));
                }).toList()));
    }

    private static boolean wxCondition(final BeanWrap beanWrap) {
        final var serverEndpoint = beanWrap.annotationGet(ServerEndpoint.class);
        if (serverEndpoint == null) return false;

        if (!contains(beanWrap.clz().getInterfaces(), WebSocketListener.class)) {
            log.error("Websocket endpoint 必须实现: chat.like.cn.serv.core.WebSocketListener.java");
            System.exit(1);
        }
        return true;
    }

    private static boolean httpCondition(final BeanWrap beanWrap) {
        return beanWrap.annotationGet(Controller.class) != null;
    }

    private HttpMethodWrap initMethod(String rootPath, Method method) {
        if (!rootPath.endsWith("/")) {
            rootPath = rootPath.concat("/");
        }
        var subPath = defVal(defVal(method.getAnnotation(Mapping.class).value(), method.getAnnotation(Mapping.class).path()), "");
        if (subPath.startsWith("/")) {
            subPath = subPath.substring(1);
        }
        return HttpMethodWrap.init(method, rootPath + subPath, initMethodType(method));
    }

    private HttpMethod initMethodType(Method method) {
        return HttpMethod.valueOf(MethodTypeUtil.findAndFill(new ArrayList<>(), c -> method.getAnnotation(c) != null).stream().findFirst().orElseThrow(() -> Exc.chat(method.getName() + " 未识别出Http method Type")).name);
    }

    private String initWsPath(ServerEndpoint anno) {
        final var path = defVal(anno.value(), anno.path());
        if (StrUtil.isBlank(path)) {
            throw Exc.chat("路径不能为空");
        }
        return path;
    }
}