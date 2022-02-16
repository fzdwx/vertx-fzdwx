package vertx.fzdwx.cn.serv;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;

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
    @Mapping("/hello")
    public String hello() {
        log.info("invoke");
        return "key:" + "helloworld1231231";
    }
}
