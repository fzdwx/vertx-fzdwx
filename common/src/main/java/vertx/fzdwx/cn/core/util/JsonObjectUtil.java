package vertx.fzdwx.cn.core.util;

import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">fzdwx</a>
 * @date 2022/2/19 19:58
 */
public class JsonObjectUtil {

    public static JsonObject collect(String prefix, JsonObject object) {
        var obj = new JsonObject();
        for (Map.Entry<String, Object> entry : object) {
            if (entry.getKey().startsWith(prefix)) {
                obj.put(entry.getKey().replace(prefix + ".", ""), entry.getValue());
            }
        }
        return obj;
    }
}
