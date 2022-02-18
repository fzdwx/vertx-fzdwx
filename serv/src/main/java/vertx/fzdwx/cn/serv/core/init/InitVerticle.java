package vertx.fzdwx.cn.serv.core.init;

import vertx.fzdwx.cn.serv.core.verticle.Verticle;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/18 13:42
 */
public interface InitVerticle<V extends Verticle> {

    InitVerticleConsumer<List<Object>, ? extends InitVerticle> init();

    default void log() {
        System.out.println(this.getClass() + " deploy finish");
    }
}