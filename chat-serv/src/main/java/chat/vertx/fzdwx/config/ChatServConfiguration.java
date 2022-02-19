package chat.vertx.fzdwx.config;

import chat.vertx.fzdwx.core.ChatServerVertx;
import chat.vertx.fzdwx.core.parser.BodyParser;
import chat.vertx.fzdwx.core.parser.HttpArgumentParser;
import chat.vertx.fzdwx.core.parser.ParamParser;
import chat.vertx.fzdwx.core.parser.RoutingContextParser;
import cn.hutool.json.JSONUtil;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.Aop;
import org.noear.solon.core.BeanWrap;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.ServerEndpoint;
import vertx.fzdwx.cn.core.verticle.VerticleStarter;
import vertx.fzdwx.cn.redis.Redis;
import vertx.fzdwx.cn.redis.RedisApi;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vertx.fzdwx.cn.core.function.lang.listOf;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 17:31
 */
@Slf4j
@Configuration
public class ChatServConfiguration {

    @Bean
    void verticleStarter(@Inject Vertx vertx) {
        final var config = getConfig(null);
        final var controller = Aop.context().beanFind(p -> {
            return p.annotationGet(Controller.class) != null;
        }).stream().map(BeanWrap::get).toList();

        final var ws = Aop.context().beanFind(p -> {
            return p.annotationGet(ServerEndpoint.class) != null;
        }).stream().map(BeanWrap::get).toList();

        // 优化
        final var chatInit = new ChatServerVertx.ChatInit();
        VerticleStarter.create(config, vertx).addDeploy("chat.vertx.fzdwx.core.ChatServerVertx", chatInit.preDeploy(() -> listOf(config, controller, ws, parsers().collect(Collectors.toMap(HttpArgumentParser::type, Function.identity()))))).start();
    }

    @Bean
    Stream<HttpArgumentParser> parsers() {
        return Stream.of(new ParamParser(), new RoutingContextParser(), new BodyParser());
    }


    @Bean
    public RedisApi redisApi(@Inject Vertx vertx) {
        final var config = getConfig("fzdwx.redis");
        final var redis = Redis.client(vertx, config);

        log.info("redis inject");
        return redis;
    }

    @Bean
    public Vertx vertx() {
        final var config = getConfig("fzdwx.vertx");
        final var vertx = Vertx.vertx(new VertxOptions(config));

        log.info("vertx inject");
        return vertx;
    }

    public JsonObject getConfig(String prefix) {
        if (prefix == null || prefix.isBlank())
            return JsonObject.mapFrom(Json.decodeValue(JSONUtil.toJsonStr(Solon.cfg())));

        final var raw = JSONUtil.toJsonStr(Solon.cfg().getProp(prefix));
        return JsonObject.mapFrom(Json.decodeValue(raw));
    }

}
