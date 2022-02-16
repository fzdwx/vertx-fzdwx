package vertx.fzdwx.cn;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Import;

/**
 * chat server.
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022-02-13 14:06:09
 */
@Import(scanPackages = {"vertx-fzdwx"})
public class ChatServ {

    public static void main(String[] args) {
        final var app = Solon.start(ChatServ.class, args);
    }
}
