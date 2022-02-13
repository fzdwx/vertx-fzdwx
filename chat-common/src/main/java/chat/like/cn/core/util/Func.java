package chat.like.cn.core.util;

import cn.hutool.core.util.ArrayUtil;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:46
 */
public interface Func {

    static <T> boolean contains(T[] array, T value) {
        return ArrayUtil.contains(array, value);
    }
}
