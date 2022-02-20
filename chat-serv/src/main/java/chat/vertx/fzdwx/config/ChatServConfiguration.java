package chat.vertx.fzdwx.config;

import chat.vertx.fzdwx.core.ChatServerProps;
import chat.vertx.fzdwx.core.ChatServerVertx;
import chat.vertx.fzdwx.core.parser.BodyParser;
import chat.vertx.fzdwx.core.parser.HttpArgumentParser;
import chat.vertx.fzdwx.core.parser.ParamParser;
import chat.vertx.fzdwx.core.parser.RoutingContextParser;
import chat.vertx.fzdwx.core.ws.WebSocketListener;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.redis.client.RedisOptions;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.Aop;
import org.noear.solon.core.BeanWrap;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.ServerEndpoint;
import vertx.fzdwx.cn.redis.Redis;
import vertx.fzdwx.cn.redis.RedisApi;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 17:31
 */
@Slf4j
@Configuration
public class ChatServConfiguration {

    @Bean
    public void verticleStarter(@Inject("${chatServ}") ChatServerProps props,
                                @Inject Vertx vertx) {

        final var controller = Aop.context().beanFind(p -> p.annotationGet(Controller.class) != null)
                .stream().map(BeanWrap::get)
                .toList();

        final List<WebSocketListener> ws = Aop.context().beanFind(p -> p.annotationGet(ServerEndpoint.class) != null)
                .stream().map(b -> ((WebSocketListener) b.get()))
                .toList();

        new ChatServerVertx(props, vertx, controller, ws, parsers())
                .start();
    }

    @Bean
    public Vertx vertx(@Inject("${chatServ.vertx}") VertxOptions vertxOps) {
        return Vertx.vertx(vertxOps);
    }

    @Bean
    public Stream<HttpArgumentParser> parsers() {
        return Stream.of(new ParamParser(), new RoutingContextParser(), new BodyParser());
    }

    @Bean
    public RedisApi redisApi(@Inject Vertx vertx, @Inject("${chatServ.redis}") RedisOptions redisOptions) {
        return Redis.client(vertx, redisOptions);
    }

}
