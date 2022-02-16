package vertx.fzdwx.cn.solon.plugin;

import vertx-fzdwx.cn.core.function.lang;
import vertx-fzdwx.cn.solon.annotation.Destroy;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Aop;
import org.noear.solon.core.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 16:14
 */
public class DestroyPlugin implements Plugin {

    private final List<Runnable> destroyMethod = new ArrayList<>();

    @Override
    public void start(final SolonApp app) {
        Aop.context().beanExtractorAdd(Destroy.class, (be, method, anno) -> {
            if (method.getParameters().length > 0) {
                throw new IllegalArgumentException(lang.format("@Destroy 注解不支持参数注入: {}", method.toString()));
            }

            destroyMethod.add(() -> {
                try {
                    method.invoke(be.get());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void prestop() throws Throwable {
        destroyMethod.forEach(Runnable::run);
    }
}
