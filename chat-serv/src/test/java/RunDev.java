import chat.like.cn.ChatServ;
import chat.like.cn.core.solon.dev.Hotdev;
import org.noear.solon.annotation.Import;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 14:00
 */
@Import(scanPackages = {"chat.like"})
public class RunDev {

    public static void main(String[] args) {
        Hotdev.start(ChatServ.class, args);
    }
}