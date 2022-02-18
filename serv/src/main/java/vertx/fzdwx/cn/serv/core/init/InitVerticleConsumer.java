package vertx.fzdwx.cn.serv.core.init;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/18 14:46
 */
@FunctionalInterface
public interface InitVerticleConsumer<V, T> {

    T accept(V v);
}