package vertx.fzdwx.cn.core.annotation;

import java.lang.annotation.*;

/**
 * 绑定路由
 * <ul>
 *     <li>/hello/123</li>
 *     <li>/hello/:id</li>
 *     <li>/world/qwe/:id/:name</li>
 * </ul>
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/18 12:16
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapping {


    /**
     * 当前请求的路径
     *
     * @return the string
     */
    String value();
}