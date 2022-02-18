package vertx.fzdwx.cn.serv.solon;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.serv.core.parser.HttpArgumentParser;
import vertx.fzdwx.cn.serv.solon.parser.ParamParser;
import vertx.fzdwx.cn.serv.solon.parser.RoutingContextParser;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/18 11:25
 */
@Configuration
@Slf4j
public class VerticleConfiguration {

    @Bean
    Map<String, HttpArgumentParser> parsers() {
        return lang.<HttpArgumentParser>listOf(new ParamParser(), new RoutingContextParser()).
                stream().collect(Collectors.toMap(HttpArgumentParser::type, Function.identity()));
    }
}