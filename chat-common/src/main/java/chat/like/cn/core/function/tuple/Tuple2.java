package chat.like.cn.core.function.tuple;

import java.util.Objects;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 15:05
 */
public final class Tuple2<T1, T2> implements Tuple {

    public final T1 t1;
    public final T2 t2;

    Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Tuple2) obj;
        return Objects.equals(this.t1, that.t1) &&
                Objects.equals(this.t2, that.t2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t1, t2);
    }

    @Override
    public String toString() {
        return "Tuple2[" +
                "t1=" + t1 + ", " +
                "t2=" + t2 + ']';
    }
}