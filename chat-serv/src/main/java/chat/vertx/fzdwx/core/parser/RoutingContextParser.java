package chat.vertx.fzdwx.core.parser;

import io.vertx.ext.web.RoutingContext;
import vertx.fzdwx.cn.core.wraper.HttpParamWrap;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/17 15:36
 */
public class RoutingContextParser implements HttpArgumentParser {

    @Override
    public String type() {
        return RoutingContext.class.getName();
    }

    @Override
    public Object parser(final RoutingContext context, final HttpParamWrap httpParamWrap) {
        return context;
    }
}