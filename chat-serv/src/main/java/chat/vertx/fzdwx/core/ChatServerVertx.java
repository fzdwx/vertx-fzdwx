package chat.vertx.fzdwx.core;

import chat.vertx.fzdwx.core.http.HttpHandlerMapping;
import chat.vertx.fzdwx.core.parser.HttpArgumentParser;
import chat.vertx.fzdwx.core.ws.WebSocketListener;
import chat.vertx.fzdwx.core.ws.WebSocketListenerMapping;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.ServerEndpoint;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.core.util.Exc;
import vertx.fzdwx.cn.core.util.StopWatch;
import vertx.fzdwx.cn.core.util.Utils;
import vertx.fzdwx.cn.core.verticle.Verticle;
import vertx.fzdwx.cn.core.verticle.init.InitCallable;
import vertx.fzdwx.cn.core.verticle.init.VerticleDeployLifeCycle;
import vertx.fzdwx.cn.core.wraper.HttpMethodWrap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static vertx.fzdwx.cn.core.function.lang.defVal;
import static vertx.fzdwx.cn.core.function.lang.format;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 17:52
 */
@Slf4j
public class ChatServerVertx extends Verticle {

    private static ChatServerProps chatServerProps = new ChatServerProps();
    private static Map<String, HttpArgumentParser> parsers;
    private static List<HttpHandlerMapping> httpHandlerMappings;
    private static List<WebSocketListenerMapping> webSocketListenerMappings;
    private static AtomicInteger instanceCount = new AtomicInteger(0);
    private final boolean first;
    public HttpServer chatServer;
    private Router router;

    {
        int number = instanceCount.getAndIncrement();
        first = number == 1;
    }

    static List<WebSocketListenerMapping> collectWs(List<WebSocketListener> webSocketListeners) {
        return webSocketListeners.stream()
                .map(source -> {
                    final var serverEndpoint = source.getClass().getAnnotation(ServerEndpoint.class);
                    if (serverEndpoint == null) {
                        throw new IllegalArgumentException("webSocketListener 必须有@ServerEndpoint注解");
                    }

                    log.info("WebSocket Endpoint Find: " + format("[ {}, Path: {} ]", source.getClass(), serverEndpoint.value()));
                    return WebSocketListenerMapping.create(source, initWsPath(serverEndpoint));
                })
                .toList();
    }

    static List<HttpHandlerMapping> collectHttp(List<Object> controllers) {
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

            return Utils.collectMethod(c.getClass().getDeclaredMethods(), Utils.allHttpType()).stream()
                    .map(method -> initMethod(rootPath, method))
                    .map(methodWrap -> HttpHandlerMapping.create(c, methodWrap, parsers));
        }).toList();
    }

    static HttpMethodWrap initMethod(String rootPath, Method method) {
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

    static HttpMethod initMethodType(Method method) {
        final var httpType = Utils.allHttpType();
        final var last = CollUtil.getLast(lang.split(Arrays.stream(method.getAnnotations())
                .filter(o -> httpType.contains(o.annotationType()))
                .findFirst().orElseThrow(() -> Exc.chat(method.getName() + " 未识别出Http method Type"))
                .annotationType().getName(), "."));
        return HttpMethod.valueOf(last.toUpperCase(Locale.ROOT));
    }

    static String initWsPath(ServerEndpoint anno) {
        final var path = anno.value();
        if (StrUtil.isBlank(path)) {
            throw Exc.chat("路径不能为空");
        }
        return path;
    }

    @Override
    protected boolean first() {
        return first;
    }

    @Override
    public void start0(final Promise<Void> startPromise) {
        this.router = Router.router(vertx);
        initWsHandler();
        initHttpHandler();

        vertx.createHttpServer()
                .exceptionHandler(ex -> {
                    log.error("", ex.getCause());
                })
                .requestHandler(router)
                .listen(chatServerProps.getPort(), res -> {
                    if (res.succeeded()) {
                        chatServer = res.result();
                        startPromise.complete();
                    } else startPromise.fail(res.cause());
                });
    }

    private void initWsHandler() {
        webSocketListenerMappings.forEach(w -> {
            w.attach(router, first);
        });
    }

    private void initHttpHandler() {
        httpHandlerMappings.forEach(w -> {
            w.attach(router, first);
        });
    }

    @Slf4j
    public static class ChatInit implements VerticleDeployLifeCycle<Verticle> {

        static StopWatch t;

        @Override
        public String deployPropsPrefix() {
            return "chatServ";
        }

        @Override
        public Supplier<? extends VerticleDeployLifeCycle<? extends Verticle>> preDeploy(InitCallable<List<Object>> action) {
            return () -> {
                final var objects = action.call();

                final JsonObject rawConfig = ((JsonObject) objects.get(0)).getJsonObject(deployPropsPrefix());
                if (rawConfig == null) {
                    throw new IllegalArgumentException(deployPropsPrefix() + " 配置文件为空");
                }

                ChatServerPropsConverter.fromJson(rawConfig, chatServerProps);

                log.info(ChatServerVertx.chatServerProps.getChatServerName() + " start up");

                t = StopWatch.start();
                List<Object> controllers = (List<Object>) objects.get(1);
                List<WebSocketListener> webSocketListeners = (List<WebSocketListener>) objects.get(2);

                parsers = (Map<String, HttpArgumentParser>) objects.get(3);
                httpHandlerMappings = collectHttp(controllers);
                webSocketListenerMappings = collectWs(webSocketListeners);
                return this;
            };
        }

        @Override
        public void deployComplete(final AsyncResult<String> completion) {
            log.info(chatServerProps.getChatServerName() + " started in " + t.stop() + " ms. Listening on: " + "http://localhost:" + chatServerProps.getPort());
        }
    }
}