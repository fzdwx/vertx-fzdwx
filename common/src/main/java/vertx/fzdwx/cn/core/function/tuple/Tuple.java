package vertx.fzdwx.cn.core.function.tuple;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 元组
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 15:00
 */
public interface Tuple extends Iterable<Object> {

    static <T1, T2> Tuple2<T1, T2> of(final T1 t1, final T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    /**
     * 将当前元组转换为{@link List}
     *
     * @return {@link List<Object> }
     * @apiNote 每次都是一个新的list
     */
    List<Object> toList();

    /**
     * 获取对应索引处的元素
     *
     * @param index 索引
     * @return {@link Object }
     */
    Object get(int index);

    /**
     * 返回当前元组最多能有多少个元素
     *
     * @return int
     */
    int size();

    @Override
    default Iterator<Object> iterator() {
        return Collections.unmodifiableList(toList()).iterator();
    }

    default void assertIndexInBounds(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException(
                    "Cannot retrieve item at position " + index + ", size is " + size());
        }
    }

}
