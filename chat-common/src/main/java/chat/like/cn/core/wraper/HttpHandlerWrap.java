package chat.like.cn.core.wraper;

import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.wrap.MethodWrap;

import static chat.like.cn.core.util.Func.defVal;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 11:16
 */
public class HttpHandlerWrap {

    public final Object source;
    public final MethodWrap methodWrap;
    public final String path;

    public HttpHandlerWrap(final Object source, final MethodWrap methodWrap, final String rootPath) {
        this.source = source;
        this.methodWrap = methodWrap;
        this.path = initPath(rootPath);
    }

    private String initPath(String rootPath) {
        if (!rootPath.endsWith("/")) {
            rootPath = rootPath.concat("/");
        }
        var subPath = defVal(defVal(this.methodWrap.getAnnotation(Mapping.class).value(), this.methodWrap.getAnnotation(Mapping.class).path()), "");
        if (subPath.startsWith("/")) {
            subPath = subPath.substring(1);
        }
        return rootPath + subPath;
    }

    public static HttpHandlerWrap create(Object source, MethodWrap methodWrap, final String rootPath) {
        return new HttpHandlerWrap(source, methodWrap, rootPath);
    }
}