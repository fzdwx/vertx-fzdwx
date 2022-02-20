package chat.vertx.fzdwx.core;

import chat.vertx.fzdwx.core.http.HttpHandlerMapping;
import chat.vertx.fzdwx.core.parser.HttpArgumentParser;
import chat.vertx.fzdwx.core.ws.WebSocketListener;
import chat.vertx.fzdwx.core.ws.WebSocketListenerMapping;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.ServerEndpoint;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.core.util.Exc;
import vertx.fzdwx.cn.core.util.Utils;
import vertx.fzdwx.cn.core.verticle.Verticle;
import vertx.fzdwx.cn.core.wraper.HttpMethodWrap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vertx.fzdwx.cn.core.function.lang.defVal;
import static vertx.fzdwx.cn.core.function.lang.format;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 17:52
 */
@Slf4j
public class ChatServerVertx extends Verticle {

    private static Map<String, HttpArgumentParser> parsers;
    private static ChatServerProps chatServerProps;
    private static Router router;
    private static List<HttpHandlerMapping> httpHandlerMappings;
    private static List<WebSocketListenerMapping> webSocketListenerMappings;

    private static AtomicInteger instanceCount = new AtomicInteger(0);

    private static boolean first = false;  // 是否 第一个httpServer实例

    {
        //初始化时， 先要new一个实例， 读取VerticleConfig中的信息， 所以除去第一次new， count是从 1开始的。
        int number = instanceCount.getAndIncrement();
        first = number == 1;
    }

    public static void init(ChatServerProps pops, List<Object> controllers, List<WebSocketListener> webSocketListeners, Stream<HttpArgumentParser> parsers) {
        log.info(pops.getChatServerName() + " start up");

        chatServerProps = pops;
        ChatServerVertx.parsers = parsers.collect(Collectors.toMap(HttpArgumentParser::type, Function.identity()));
        httpHandlerMappings = collectHttp(controllers);
        webSocketListenerMappings = collectWs(webSocketListeners);
    }

    @Override
    public Vertx vertx() {
        return vertx;
    }

    @Override
    public void start() {
        router = Router.router(vertx);
        initRouter();
        initWsHandler();
        initHttpHandler();
        vertx.createHttpServer().exceptionHandler(ex -> {
            log.error("get exc", ex.getCause());
        }).requestHandler(router).listen(chatServerProps.getPort());
    }

    private static void initHttpHandler() {
        httpHandlerMappings.forEach(w -> {
            w.attach(router, first);
        });
    }

    private static void initRouter() {
        router.route().handler(BodyHandler.create());
    }

    private static void initWsHandler() {
        webSocketListenerMappings.forEach(w -> {
            w.attach(router, first);
        });
    }

    private static HttpMethodWrap initMethod(String rootPath, Method method) {
        if (rootPath.endsWith("/")) {
            rootPath = StrUtil.removeSuffix(rootPath, "/");
        }
        if (!rootPath.startsWith("/")) {
            rootPath = "/" + rootPath;
        }

        var subPath = defVal(method.getAnnotation(Mapping.class).value(), "");
        if (!subPath.startsWith("/")) {
            subPath = "/" + subPath;
        }
        return HttpMethodWrap.init(method, rootPath, subPath, initMethodType(method));
    }

    private static HttpMethod initMethodType(Method method) {
        final var httpType = Utils.allHttpType();
        final var last = CollUtil.getLast(lang.split(Arrays.stream(method.getAnnotations()).filter(o -> httpType.contains(o.annotationType())).findFirst().orElseThrow(() -> Exc.chat(method.getName() + " 未识别出Http method Type")).annotationType().getName(), "."));
        return HttpMethod.valueOf(last.toUpperCase(Locale.ROOT));
    }

    private static String initWsPath(ServerEndpoint anno) {
        final var path = anno.value();
        if (StrUtil.isBlank(path)) {
            throw Exc.chat("路径不能为空");
        }
        return path;
    }

    private static List<WebSocketListenerMapping> collectWs(List<WebSocketListener> webSocketListeners) {
        return webSocketListeners.stream().map(source -> {
            final var serverEndpoint = source.getClass().getAnnotation(ServerEndpoint.class);
            if (serverEndpoint == null) {
                throw new IllegalArgumentException("webSocketListener 必须有@ServerEndpoint注解");
            }

            log.info("WebSocket Endpoint Find: " + format("[ {}, Path: {} ]", source.getClass(), serverEndpoint.value()));
            return WebSocketListenerMapping.create(source, initWsPath(serverEndpoint));
        }).toList();
    }

    private static List<HttpHandlerMapping> collectHttp(List<Object> controllers) {
        return controllers.stream().flatMap(c -> {
            if (c.getClass().getAnnotation(Controller.class) == null) {
                throw new IllegalArgumentException("controller 必须携带@Controller注解");
            }

            final var mapping = c.getClass().getAnnotation(Mapping.class);
            if (mapping == null) {
                throw new IllegalArgumentException("controller 必须携带@Mapping注解");
            }

            final var rootPath = defVal(mapping.value(), "/");
            log.info("Http Controller Find: " + format("[ {}, rootPath: {} ]", c.getClass(), rootPath));

            return Utils.collectMethod(c.getClass().getDeclaredMethods(), Utils.allHttpType()).stream().map(method -> initMethod(rootPath, method)).map(methodWrap -> HttpHandlerMapping.create(c, methodWrap, parsers));
        }).toList();
    }
}