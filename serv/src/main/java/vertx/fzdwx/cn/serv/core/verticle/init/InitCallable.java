package vertx.fzdwx.cn.serv.core.verticle.init;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/18 14:56
 */
@FunctionalInterface
public interface InitCallable<V> {

    V call();
}