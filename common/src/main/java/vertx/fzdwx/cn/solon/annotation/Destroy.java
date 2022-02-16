package vertx.fzdwx.cn.solon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记方法为destroy,当容器关闭时会调用
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 16:02
 * @apiNote 标记了{@link Destroy} 的方法不支持参数注入
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Destroy {
}
