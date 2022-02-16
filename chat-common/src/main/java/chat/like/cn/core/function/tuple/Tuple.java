package chat.like.cn.core.function.tuple;

/**
 * 元组
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 15:00
 */
public interface Tuple {

    static <T1, T2> Tuple2<T1, T2> of(final T1 t1, final T2 t2) {
        return new Tuple2<>(t1, t2);
    }

}