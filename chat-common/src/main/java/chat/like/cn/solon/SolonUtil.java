package chat.like.cn.solon;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.PluginEntity;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 15:37
 */
public interface SolonUtil {

    static void addStopHook(Runnable task) {
        Solon.cfg().plugs().add(new PluginEntity(new Plugin() {
            @Override
            public void start(final SolonApp app) { }

            @Override
            public void stop() throws Throwable { task.run(); }
        }));
    }
}