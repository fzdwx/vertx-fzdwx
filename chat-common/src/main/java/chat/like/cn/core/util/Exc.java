package chat.like.cn.core.util;

import chat.like.cn.core.exc.ChatException;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:34
 */
public class Exc {

    public static ChatException chat(String errorMessage) {
        return new ChatException(errorMessage);
    }
}
