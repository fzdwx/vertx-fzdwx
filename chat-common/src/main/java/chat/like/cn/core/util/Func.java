package chat.like.cn.core.util;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2022/2/13 16:46
 */
public interface Func {

    /**
     * 数组中是否包含元素
     */
    static <T> boolean contains(T[] array, T value) {
        for (T t : array) {
            if (eq(t, value)) return true;
        }
        return false;
    }

    /**
     * 判断两个对象是否相等
     */
    static boolean eq(Object a, Object b) {
        if (a instanceof BigDecimal a1 && b instanceof BigDecimal b1) {
            if (a1.equals(b1)) {
                return true;
            }
            return 0 == a1.compareTo(b1);
        }
        return Objects.equals(a, b);
    }

    /**
     * 如果 value 为null 则返回defVal
     */
    static <T> T defVal(T value, T defVal) {
        if (Objects.isNull(value)) {
            return defVal;
        }
        return value;
    }

    /**
     * 如果 value 为null 则返回defVal
     */
    static String defVal(String value, String defVal) {
        if (StrUtil.isBlank(value)) {
            return defVal;
        }
        return value;
    }

    /**
     * 格式化，占位符使用{}
     */
    static String format(String template, Object... params) {return format(template, StrPool.EMPTY_JSON, params);}

    /**
     * 格式化，占位符使用bePlace
     */
    static String format(String template, String bePlace, Object... params) {
        if (StrUtil.isBlank(template) || StrUtil.isBlank(bePlace) || ArrayUtil.isEmpty(params)) {
            return template;
        }
        final int strPatternLength = template.length();
        final int placeHolderLength = bePlace.length();

        // 初始化定义好的长度以获得更好的性能
        final StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;// 记录已经处理到的位置
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < params.length; argIndex++) {
            delimIndex = template.indexOf(bePlace, handledPosition);
            if (delimIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return template;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                sbuf.append(template, handledPosition, strPatternLength);
                return sbuf.toString();
            }

            // 转义符
            if (delimIndex > 0 && template.charAt(delimIndex - 1) == StrUtil.C_BACKSLASH) {// 转义符
                if (delimIndex > 1 && template.charAt(delimIndex - 2) == StrUtil.C_BACKSLASH) {// 双转义符
                    // 转义符之前还有一个转义符，占位符依旧有效
                    sbuf.append(template, handledPosition, delimIndex - 1);
                    sbuf.append(StrUtil.utf8Str(params[argIndex]));
                    handledPosition = delimIndex + placeHolderLength;
                } else {
                    // 占位符被转义
                    argIndex--;
                    sbuf.append(template, handledPosition, delimIndex - 1);
                    sbuf.append(bePlace.charAt(0));
                    handledPosition = delimIndex + 1;
                }
            } else {// 正常占位符
                sbuf.append(template, handledPosition, delimIndex);
                sbuf.append(StrUtil.utf8Str(params[argIndex]));
                handledPosition = delimIndex + placeHolderLength;
            }
        }

        // 加入最后一个占位符后所有的字符
        sbuf.append(template, handledPosition, strPatternLength);

        return sbuf.toString();
    }
}