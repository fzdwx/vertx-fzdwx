package chat.vertx.fzdwx.core.parser;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import vertx.fzdwx.cn.core.annotation.Body;
import vertx.fzdwx.cn.core.wraper.HttpParamWrap;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/17 14:54
 */
public class BodyParser implements HttpArgumentParser {

    @Override
    public String type() {
        return Body.class.getName();
    }

    @Override
    public Object parser(final RoutingContext context, HttpParamWrap httpParamWrap) {
        final Class<?> type = httpParamWrap.getParameter().getType();
        return Json.decodeValue(context.getBody(), type);
    }
}