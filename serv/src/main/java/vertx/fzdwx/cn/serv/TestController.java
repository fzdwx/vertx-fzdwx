package vertx.fzdwx.cn.serv;

import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import vertx.fzdwx.cn.core.annotation.Param;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:10
 */
@Controller
@Mapping("/qweqwe")
@Slf4j
public class TestController {

    @SneakyThrows
    @Get
    @Mapping("/hello/:id/:name")
    public void hello(RoutingContext context, @Param("id") int id, @Param("name") String name) {
        log.info("invoke");
        context.redirect("http://localhost:8080/#/homePage");
        // return Map.of("hello", "world");
    }
}