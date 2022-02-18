package vertx.fzdwx.cn.serv;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Destroy;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.ServerEndpoint;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.core.util.Exc;
import vertx.fzdwx.cn.core.util.StopWatch;
import vertx.fzdwx.cn.core.util.Utils;
import vertx.fzdwx.cn.core.wraper.HttpMethodWrap;
import vertx.fzdwx.cn.serv.core.ChatServerProps;
import vertx.fzdwx.cn.serv.core.http.HttpHandlerMapping;
import vertx.fzdwx.cn.serv.core.parser.HttpArgumentParser;
import vertx.fzdwx.cn.serv.core.verticle.VerticleBootStrap;
import vertx.fzdwx.cn.serv.core.ws.WebSocketListener;
import vertx.fzdwx.cn.serv.core.ws.WebSocketListenerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static vertx.fzdwx.cn.core.function.lang.defVal;
import static vertx.fzdwx.cn.core.function.lang.format;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 15:20
 */
@Slf4j
public class VerticleSolonStarter {

    private ChatServerProps chatServerProps = new ChatServerProps();
    private Map<String, HttpArgumentParser> parsers;
    private VerticleBootStrap verticleBootStrap;

    public void init(List<Object> controllers, List<WebSocketListener> webSocketListeners, Map<String, HttpArgumentParser> parsers) {
        StopWatch.start();
        this.parsers = parsers;
        verticleBootStrap = new VerticleBootStrap(chatServerProps, collectHttp(controllers), collectWs(webSocketListeners));
        verticleBootStrap.deploy()
                .onComplete(res -> {
                    log.info(chatServerProps.getAppName() + " started in " + StopWatch.stop() + " ms. Listening on: " + "http://localhost:" + chatServerProps.getPort());
                });
    }

    @Destroy
    public void destroy() {
        verticleBootStrap.stop();
    }

    public List<WebSocketListenerMapping> collectWs(List<WebSocketListener> webSocketListeners) {
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

    public List<HttpHandlerMapping> collectHttp(List<Object> controllers) {
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


    private HttpMethodWrap initMethod(String rootPath, Method method) {
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

    private HttpMethod initMethodType(Method method) {
        final var httpType = Utils.allHttpType();
        final var last = CollUtil.getLast(lang.split(Arrays.stream(method.getAnnotations())
                .filter(o -> httpType.contains(o.annotationType()))
                .findFirst().orElseThrow(() -> Exc.chat(method.getName() + " 未识别出Http method Type"))
                .annotationType().getName(), "."));
        return HttpMethod.valueOf(last.toUpperCase(Locale.ROOT));
    }

    private String initWsPath(ServerEndpoint anno) {
        final var path = anno.value();
        if (StrUtil.isBlank(path)) {
            throw Exc.chat("路径不能为空");
        }
        return path;
    }
}