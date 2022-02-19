package chat.vertx;

import chat.vertx.fzdwx.TestController;
import chat.vertx.fzdwx.TestServerEndpoint;
import chat.vertx.fzdwx.core.ChatServerVertx;
import chat.vertx.fzdwx.core.parser.HttpArgumentParser;
import chat.vertx.fzdwx.core.parser.ParamParser;
import chat.vertx.fzdwx.core.parser.RoutingContextParser;
import cn.hutool.core.io.FileUtil;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.verticle.VerticleStarter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        var config = initConfig(args);
        var vertx = Vertx.vertx();

        // 部署
        VerticleStarter.create(vertx, config)
                .addDeploy("chat.vertx.fzdwx.core.ChatServerVertx",
                        new ChatServerVertx.ChatInit().preDeploy(() -> listOf(config,
                                List.of(new TestController()),
                                List.of(new TestServerEndpoint()),
                                Stream.of(new ParamParser(), new RoutingContextParser()).collect(Collectors.toMap(HttpArgumentParser::type, Function.identity())))))
                .start();
    }

    private static JsonObject initConfig(final String[] args) {
        final String obj = FileUtil.readString("conf.json", StandardCharsets.UTF_8);
        final Object o = Json.decodeValue(obj);
        return JsonObject.mapFrom(o);
    }
}

