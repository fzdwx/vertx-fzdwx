import org.noear.solon.annotation.Import;
import vertx.fzdwx.cn.ChatServ;
import vertx.fzdwx.cn.solon.dev.Hotdev;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 14:00
 */
@Import(scanPackages = {"vertx-fzdwx"})
public class RunDev {

    public static void main(String[] args) {
        Hotdev.start(ChatServ.class, args);
    }
}