package chat.vertx.fzdwx.api.domain.req;

import io.vertx.codegen.annotations.DataObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 登录请求
 *
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/02/19 12:05:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@DataObject(generateConverter = true)
public class SignInReq {

    /**
     * username.
     */
    private String name;
    /**
     * user password.
     */
    private String passwd;
}
