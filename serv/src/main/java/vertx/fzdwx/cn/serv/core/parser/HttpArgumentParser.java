package vertx.fzdwx.cn.serv.core.parser;

import io.vertx.ext.web.RoutingContext;
import vertx.fzdwx.cn.core.wraper.HttpParamWrap;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/17 14:53
 */
public interface HttpArgumentParser {

    String type();

    Object parser(RoutingContext context, HttpParamWrap httpParamWrap);
}