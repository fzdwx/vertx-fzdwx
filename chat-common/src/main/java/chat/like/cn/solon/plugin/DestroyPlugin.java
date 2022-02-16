package chat.like.cn.solon.plugin;

import chat.like.cn.solon.SolonUtil;
import chat.like.cn.solon.annotation.Destroy;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.Plugin;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 16:14
 */
public class DestroyPlugin implements Plugin {

    @Override
    public void start(final SolonApp app) {
        Aop.context().beanExtractorAdd(Destroy.class, (be, method, anno) -> {
            if (method.getParameters().length > 0) {
                throw new IllegalArgumentException("@Destroy 注解不能支持参数注入");
            }

            SolonUtil.addStopHook(() -> {
                try {
                    method.invoke(be.get());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}