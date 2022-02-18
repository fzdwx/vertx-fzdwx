package chat.vertx;

import chat.vertx.fzdwx.TestController;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.serv.VerticleStarter;
import vertx.fzdwx.cn.serv.core.parser.HttpArgumentParser;
import vertx.fzdwx.cn.serv.core.parser.ParamParser;
import vertx.fzdwx.cn.serv.core.parser.RoutingContextParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * chat server.
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022-02-13 14:06:09
 */
@Slf4j
public class ChatServ {

    public static void main(String[] args) {
        final var verticleSolonStarter = new VerticleStarter();

        final List<Object> controllers = List.of(new TestController());

        Map<String, HttpArgumentParser> parsers =
                lang.<HttpArgumentParser>listOf(new ParamParser(), new RoutingContextParser()).
                        stream().collect(Collectors.toMap(HttpArgumentParser::type, Function.identity()));

        verticleSolonStarter.init(controllers, new ArrayList<>(), parsers);
    }
}