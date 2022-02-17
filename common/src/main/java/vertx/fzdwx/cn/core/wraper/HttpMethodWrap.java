package vertx.fzdwx.cn.core.wraper;


import io.vertx.core.http.HttpMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/16 15:00
 */
public class HttpMethodWrap {

    private static Map<Method, HttpMethodWrap> cached = new HashMap<>();

    public HttpMethodWrap(final Method m, final String rootPath, final String subPath, final HttpMethod httpMethod) {
        this.method = m;
        this.rootPath = rootPath;
        this.subPath = subPath;
        this.httpMethod = httpMethod;
        this.parameters = paramsWrap(m.getParameters());
        this.annotations = m.getAnnotations();
    }

    public static HttpMethodWrap init(Method method, final String rootPath, final String subPath, final HttpMethod httpMethod) {
        HttpMethodWrap mw = cached.get(method);
        if (mw == null) {
            synchronized (method) {
                mw = cached.get(method);
                if (mw == null) {
                    mw = new HttpMethodWrap(method, rootPath, subPath, httpMethod);
                    cached.put(method, mw);
                }
            }
        }

        return mw;
    }

    /**
     * 函数
     */
    private final Method method;
    /**
     * 当前http method 的root 访问路径
     */
    private final String rootPath;
    /**
     * 子路径
     */
    private final String subPath;
    /**
     * http 请求类型
     */
    private final HttpMethod httpMethod;
    //函数参数
    private final HttpParamWrap[] parameters;
    //函数注解
    private final Annotation[] annotations;


    /**
     * 获取函数某种注解
     */
    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return method.getAnnotation(type);
    }

    public String getName() {
        return method.getName();
    }

    public String getRootPath() {
        return this.rootPath;
    }

    public String getSubPath() { return this.subPath; }

    public HttpMethod getHttpType() {
        return this.httpMethod;
    }

    /**
     * 获取函数本身
     */
    public Method getMethod() {
        return method;
    }

    /**
     * 获取函数反回类型
     */
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    /**
     * 获取函数泛型类型
     */
    public Type getGenericReturnType() {
        return method.getGenericReturnType();
    }

    /**
     * 获取函数参数
     */
    public HttpParamWrap[] getParamWraps() {
        return parameters;
    }

    /**
     * 获取函数所有注解
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }

    /**
     * 执行
     */
    public Object invoke(Object obj, Object... args) throws Exception {
        try {
            return method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            Throwable ex = e.getTargetException();
            if (ex instanceof Error) {
                throw (Error) ex;
            } else {
                throw (Exception) ex;
            }
        }
    }

    private HttpParamWrap[] paramsWrap(Parameter[] pAry) {
        HttpParamWrap[] tmp = new HttpParamWrap[pAry.length];
        for (int i = 0, len = pAry.length; i < len; i++) {
            tmp[i] = new HttpParamWrap(pAry[i]);
        }
        return tmp;
    }
}