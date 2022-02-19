package vertx.fzdwx.cn.core.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/17 12:15
 */
@Slf4j
public abstract class Verticle extends AbstractVerticle {

    /**
     * 是否为第一个实例
     *
     * @return boolean
     * <pre>
     *     private static AtomicInteger instanceCount = new AtomicInteger(0);
     *     private final boolean first;
     *     {
     *      int number = instanceCount.getAndIncrement();
     *      first = number == 1;
     *     }
     * </pre>
     */
    protected abstract boolean first();

    public Vertx vertx() {
        return vertx;
    }

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
        start0(startPromise);
    }

    public abstract void start0(final Promise<Void> startPromise);
}