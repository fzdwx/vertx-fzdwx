package vertx.fzdwx.cn.core.function.tuple;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import vertx.fzdwx.cn.core.function.lang;

import java.util.List;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 15:05
 */
@EqualsAndHashCode
@ToString
public final class Tuple2<A, B> implements Tuple {

    public final A A;
    public final B B;

    Tuple2(A a, B b) {
        this.A = a;
        this.B = b;
    }

    @Override
    public List<Object> toList() {
        return lang.listOf(A, B);
    }

    @Override
    public Object get(final int index) {
        assertIndexInBounds(index);
        if (index == 0) {
            return A;
        }
        return B;
    }

    @Override
    public int size() {
        return 2;
    }
}
