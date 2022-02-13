package chat.like.cn.core.exc;

/**
 * chat exception.
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:34
 */
public class ChatException extends RuntimeException {

    public ChatException(final String errorMessage) {
        super(errorMessage);
    }
}
