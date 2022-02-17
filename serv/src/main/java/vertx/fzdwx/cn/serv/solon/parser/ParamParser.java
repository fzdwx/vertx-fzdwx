package vertx.fzdwx.cn.serv.solon.parser;

import cn.hutool.core.convert.Convert;
import io.vertx.ext.web.RoutingContext;
import vertx.fzdwx.cn.core.annotation.Param;
import vertx.fzdwx.cn.core.wraper.HttpParamWrap;
import vertx.fzdwx.cn.serv.core.parser.HttpArgumentParser;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/17 14:54
 */
public class ParamParser implements HttpArgumentParser {

    @Override
    public String type() {
        return Param.class.getName();
    }

    @Override
    public Object parser(final RoutingContext context, HttpParamWrap httpParamWrap) {
        return Convert.convert(httpParamWrap.getType(), context.request().params().get(httpParamWrap.getName()));
    }
}