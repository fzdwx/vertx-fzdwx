package chat.like.cn.serv;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:10
 */
@Controller
public class TestController {

    @Get
    @Mapping("/hello")
    public void hello() {
        System.out.println("123");
    }
}
