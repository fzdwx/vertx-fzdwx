package chat.like.cn.serv.core;

import chat.like.cn.core.wraper.HttpMethodWrap;
import io.vertx.mutiny.ext.web.Router;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 11:16
 */
@Slf4j
public class HttpHandlerMapping {

    public final Object source;
    public final HttpMethodWrap methodWrap;

    private HttpHandlerMapping(final Object source, final HttpMethodWrap methodWrap) {
        this.source = source;
        this.methodWrap = methodWrap;
    }

    /**
     * 创建一个{@link HttpHandlerMapping}
     *
     * @param source     当前http handler的原始对象
     * @param methodWrap 实际method的包装器
     * @return {@link HttpHandlerMapping }
     */
    public static HttpHandlerMapping create(Object source, HttpMethodWrap methodWrap) {
        return new HttpHandlerMapping(source, methodWrap);
    }

    /**
     * 将当前http method handler挂载到router上去
     *
     * @param router 路由器
     */
    public void attach(final Router router) {
        log.info("Http Handler Registered: {} {}", methodWrap.getHttpType(), this.methodWrap.getPath());
        router.route(methodWrap.getHttpType(), this.methodWrap.getPath())
                .handler(rCtx -> {
                    rCtx.json(this.invoke())
                            .subscribe().with(v -> {
                                rCtx.end().subscribe();
                            });
                });
    }

    @SneakyThrows
    public Object invoke() {
        return this.methodWrap.invoke(source, null);
    }
}