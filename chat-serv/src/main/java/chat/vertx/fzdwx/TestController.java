package chat.vertx.fzdwx;

import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.annotation.Controller;
import vertx.fzdwx.cn.core.annotation.Get;
import vertx.fzdwx.cn.core.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.Param;

import java.util.Map;

/**
 * The type Test controller.
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022 /2/13 16:10
 */
@Controller
@Mapping("/qweqwe")
@Slf4j
public class TestController {

    @SneakyThrows
    @Get
    @Mapping("/hello/:id/:name")
    public Map<String, String> hello(RoutingContext context, @Param("id") int id, @Param("name") String name) {
        log.info("invoke");
        // context.redirect("http://localhost:8080/#/homePage");
        return Map.of("hello", "world");
    }
}