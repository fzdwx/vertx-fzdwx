package chat.vertx.fzdwx.core.http;

import chat.vertx.fzdwx.core.parser.HttpArgumentParser;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.core.wraper.HttpMethodWrap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 11:16
 */
@Slf4j
public class HttpHandlerMapping {

    public final Object source;
    public final HttpMethodWrap methodWrap;
    public final String rootPath;
    private List<HttpArgumentParser> parsers;

    private HttpHandlerMapping(final Object source, final HttpMethodWrap methodWrap,
                               final Map<String, HttpArgumentParser> parser) {
        this.source = source;
        this.methodWrap = methodWrap;
        this.rootPath = methodWrap.getRootPath();
        initParser(parser);
    }

    /**
     * 创建一个{@link HttpHandlerMapping}
     *
     * @param source     当前http handler的原始对象
     * @param methodWrap 实际method的包装器
     * @param parser     HttpArgumentParser
     * @return {@link HttpHandlerMapping }
     */
    public static HttpHandlerMapping create(Object source, HttpMethodWrap methodWrap, final Map<String, HttpArgumentParser> parser) {
        return new HttpHandlerMapping(source, methodWrap, parser);
    }

    /**
     * 将当前http method handler挂载到router上去
     *
     * @param router 路由器
     * @param first
     */
    public void attach(final Router router, final boolean first) {
        if (first) {
            log.info("Http Handler Registered: {} {}", methodWrap.getHttpType(), this.methodWrap.getRootPath() + this.methodWrap.getSubPath());
        }

        router.route(methodWrap.getHttpType(), this.methodWrap.getRootPath() + this.methodWrap.getSubPath())
                .handler(rtx -> {
                    final var result = this.invokeAndParseArguments(rtx);

                    // handler http request result
                    if (!methodWrap.getReturnType().getName().equals("void")) {
                        rtx.json(result);
                    }
                });
    }

    @SneakyThrows
    public Object invokeAndParseArguments(final RoutingContext context) {
        final var paramWraps = methodWrap.getParamWraps();
        final var length = paramWraps.length;

        Object[] objects = new Object[length];

        for (int i = 0; i < length; i++) {
            objects[i] = parsers.get(i).parser(context, paramWraps[i]);
        }

        return this.methodWrap.invoke(source, objects);
    }

    private void initParser(final Map<String, HttpArgumentParser> parser) {
        parsers = Arrays.stream(methodWrap.getParamWraps()).map(p -> {
            final var list = Arrays.stream(p.annotations()).map(an -> parser.get(an.annotationType().getName())).toList();
            if (list.size() <= 0 || list.get(0) == null) {
                if (lang.eq(p.getType().getName(), RoutingContext.class.getName()))
                    return parser.get(RoutingContext.class.getName());
                else
                    throw new UnsupportedOperationException(lang.format("未找到支持的解析器:{}", methodWrap.getMethod().toString()));
            }
            return list.get(0);
        }).toList();
    }
}