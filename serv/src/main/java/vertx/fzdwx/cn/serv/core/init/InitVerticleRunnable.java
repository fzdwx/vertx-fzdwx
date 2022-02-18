package vertx.fzdwx.cn.serv.core.init;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/18 14:10
 */
@FunctionalInterface
public interface InitVerticleRunnable<T> {

    T run();
}