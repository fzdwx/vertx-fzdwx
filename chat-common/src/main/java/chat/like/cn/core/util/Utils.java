package chat.like.cn.core.util;

import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Head;
import org.noear.solon.annotation.Patch;
import org.noear.solon.annotation.Post;
import org.noear.solon.annotation.Put;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static chat.like.cn.core.util.lang.eq;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 10:33
 */
public interface Utils {

    /**
     * 收集含有指定注解的方法
     *
     * @param methods        方法
     * @param annoCollection 注解集合
     * @return {@link List<Method> }
     */
    static List<Method> collectMethod(final Method[] methods, final List<Class<? extends Annotation>> annoCollection) {
        return Arrays.stream(methods)
                .filter(method -> Arrays.stream(method.getAnnotations())
                        .filter(methodAnno -> annoCollection.contains(methodAnno.annotationType()))
                        .toList()
                        .size() > 0)
                .collect(Collectors.toList());

    }

    /**
     * 收集含有指定注解的方法
     *
     * @param methods 方法
     * @param anno    注解
     * @return {@link List<Method> }
     */
    static List<Method> collectMethod(final Method[] methods, final Class<? extends Annotation> anno) {
        return Arrays.stream(methods)
                .filter(method -> Arrays.stream(method.getAnnotations())
                        .filter(methodAnno -> eq(methodAnno.annotationType(), anno))
                        .toList()
                        .size() > 0)
                .collect(Collectors.toList());
    }

    static List<Class<? extends Annotation>> allHttpType() {
        return List.of(
                Get.class,
                Post.class,
                Put.class,
                Delete.class,
                Patch.class,
                Head.class
        );
    }
}