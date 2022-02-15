package chat.like.cn.serv.core;

import chat.like.cn.core.util.Exc;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.handle.MethodTypeUtil;
import org.noear.solon.core.wrap.MethodWrap;

import java.util.ArrayList;

import static chat.like.cn.core.util.Func.defVal;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 11:16
 */
@Slf4j
public class HttpHandlerMapping {

    public final Object source;
    public final MethodWrap methodWrap;
    public final String path;
    private final MethodType httpMethodType;

    private HttpHandlerMapping(final Object source, final MethodWrap methodWrap, final String rootPath) {
        this.source = source;
        this.methodWrap = methodWrap;
        this.path = initPath(rootPath);
        this.httpMethodType = initMethodType(methodWrap);
    }

    /**
     * 创建一个{@link HttpHandlerMapping}
     *
     * @param source     当前http handler的原始对象
     * @param methodWrap 实际method的包装器
     * @param rootPath   rootPath
     * @return {@link HttpHandlerMapping }
     */
    public static HttpHandlerMapping create(Object source, MethodWrap methodWrap, final String rootPath) {
        return new HttpHandlerMapping(source, methodWrap, rootPath);
    }

    /**
     * 将当前http method handler挂载到router上去
     *
     * @param router 路由器
     */
    public void attach(final Router router) {
        log.info("Http Handler Registered: {}", this.path);

        router.route(HttpMethod.valueOf(this.httpMethodType.name), this.path)
                .handler(rCtx -> {
                    final var result = this.invoke();
                    rCtx.json(result);
                });
    }

    @SneakyThrows
    public Object invoke() {
        return this.methodWrap.invoke(source, null);
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

    private MethodType initMethodType(final MethodWrap methodWrap) {
        return MethodTypeUtil.findAndFill(new ArrayList<>(), c -> methodWrap.getAnnotation(c) != null).stream().findFirst().orElseThrow(() -> Exc.chat(methodWrap.getName() + " 为识别出Http method Type"));
    }
}