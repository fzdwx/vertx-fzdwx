package vertx.fzdwx.cn.core.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/17 12:15
 */
public abstract class Verticle extends AbstractVerticle {

    public abstract Vertx vertx();

    // public abstract void start();
}