package chat.vertx;

import chat.vertx.fzdwx.TestController;
import chat.vertx.fzdwx.TestServerEndpoint;
import chat.vertx.fzdwx.core.ChatServerProps;
import chat.vertx.fzdwx.core.ChatServerVertx;
import chat.vertx.fzdwx.core.parser.HttpArgumentParser;
import chat.vertx.fzdwx.core.parser.ParamParser;
import chat.vertx.fzdwx.core.parser.RoutingContextParser;
import chat.vertx.fzdwx.core.ws.WebSocketListener;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.core.verticle.VerticleStarter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vertx.fzdwx.cn.core.function.lang.listOf;

/**
 * chat server.
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022-02-13 14:06:09
 */
@Slf4j
public class ChatServ {

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();

        // 加载配置
        final var chatServerProps = new ChatServerProps();
        // 加载controller
        final List<Object> controllers = List.of(new TestController());
        // 加载ws
        final List<WebSocketListener> webSocketListeners = List.of(new TestServerEndpoint());
        // 加载http注解参数解析器
        Map<String, HttpArgumentParser> parsers = lang.listOf(new ParamParser(), new RoutingContextParser()).stream().collect(Collectors.toMap(HttpArgumentParser::type, Function.identity()));
        // 部署
        VerticleStarter.create(vertx, new JsonObject()).addDeploy("chat.vertx.fzdwx.core.ChatServerVertx",
                // ChatServerVertx 生命周期实现类，用于初始化数据
                new ChatServerVertx.ChatInit().preDeploy(() -> listOf(chatServerProps, controllers, webSocketListeners, parsers))).start();

    }
}

