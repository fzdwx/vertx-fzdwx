package vertx.fzdwx.cn.core.wraper;


import vertx.fzdwx.cn.core.annotation.Body;
import vertx.fzdwx.cn.core.annotation.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author noear 2020/12/20 created
 */
public class HttpParamWrap {

    private final Parameter parameter;
    private String name;
    private boolean required;
    private boolean requireBody;
    private ParameterizedType genericType;
    private Annotation[] annotations;

    public HttpParamWrap(Parameter parameter) {
        this.parameter = parameter;

        Param paramAnno = parameter.getAnnotation(Param.class);
        Body bodyAnno = parameter.getAnnotation(Body.class);

        if (paramAnno != null) {
            this.name = paramAnno.value();
            required = paramAnno.required();
        }

        if (bodyAnno != null) {
            requireBody = true;
        }

        Type tmp = parameter.getParameterizedType();
        if (tmp instanceof ParameterizedType) {
            genericType = (ParameterizedType) tmp;
        } else {
            genericType = null;
        }
        this.annotations = parameter.getAnnotations();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Annotation[] annotations() {
        return annotations;
    }

    public String getName() {
        return name;
    }

    public ParameterizedType getGenericType() {
        return genericType;
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public boolean required() {
        return required;
    }

    public boolean requireBody() {
        return requireBody;
    }
}