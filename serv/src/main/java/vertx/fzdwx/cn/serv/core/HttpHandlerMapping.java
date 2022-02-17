package vertx.fzdwx.cn.serv.core;

import io.vertx.mutiny.ext.web.Router;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.wraper.HttpMethodWrap;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 11:16
 */
@Slf4j
public class HttpHandlerMapping {

    public final Object source;
    public final HttpMethodWrap methodWrap;
    public final String rootPath;

    private HttpHandlerMapping(final Object source, final HttpMethodWrap methodWrap) {
        this.source = source;
        this.methodWrap = methodWrap;
        this.rootPath = methodWrap.getRootPath();
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
     * @param router    路由器
     * @param firstFlag
     */
    public void attach(final Router router, final Boolean firstFlag) {
        if (firstFlag)
            log.info("Http Handler Registered: {} {}", methodWrap.getHttpType(), this.methodWrap.getRootPath() + this.methodWrap.getSubPath());

        router.route(methodWrap.getHttpType(), this.methodWrap.getRootPath() + this.methodWrap.getSubPath())
                .handler(rCtx -> {
                    rCtx.json(this.invoke())
                            .subscribe().with(v -> {
                                rCtx.end().subscribe();
                            });
                });

        // if (firstFlag)
        //     log.info("Http Handler Registered: {} {}", methodWrap.getHttpType(), this.methodWrap.getSubPath());
        //
        // router.route(methodWrap.getHttpType(), this.methodWrap.getSubPath())
        //         .handler(rCtx -> {
        //             rCtx.json(this.invoke())
        //                     .subscribe().with(v -> {
        //                         rCtx.end().subscribe();
        //                     });
        //         });
    }

    @SneakyThrows
    public Object invoke() {
        return this.methodWrap.invoke(source, null);
    }
}