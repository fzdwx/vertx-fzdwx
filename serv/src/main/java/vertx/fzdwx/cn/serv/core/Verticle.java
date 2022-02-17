package vertx.fzdwx.cn.serv.core;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.mutiny.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/17 12:15
 */
@Slf4j
public abstract class Verticle extends AbstractVerticle {

    public Vertx vertx() {
        return vertx;
    }

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
        if (first()) {
            log.info("deploy init invoke");
            init().await().indefinitely();
        }
        super.start(startPromise);
    }

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

    /**
     * 初始化,只会执行一次
     *
     * @return {@link Uni<Void> }
     */
    protected abstract Uni<Void> init();

    @Override
    public abstract Uni<Void> asyncStart();
}