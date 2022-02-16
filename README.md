# chat Project

一个端口同时提高http与websocket服务，基于vert.x

- [vert.x](https://github.com/eclipse-vertx/vert.x): 作为网络框架,是全异步的
- [solon](https://github.com/noear/solon): 作为`ioc`与`di`类似功能`Spring`

websocket测试网站：http://www.easyswoole.com/wstool.html

# http handler 与 websocket endpoint 挂载原理
具体实现可以看=>[ChatServSolonStarter](https://github.com/fzdwx/chat/blob/4d5440768b6e3fbaebad0cd72af1076f5a9e513a/chat-serv/src/main/java/chat/like/cn/serv/solon/ChatServSolonStarter.java)

**1.http挂载**
- 首先从容器中找到添加了`@Controller`的类
```java
    public Multi<HttpHandlerMapping> collectHttp() {
        return Multi.createFrom().iterable((Aop.beanFind((ChatServSolonStarter::httpCondition))
                .stream()
                .flatMap(beanWrap -> {
                    final var parentMapping = beanWrap.annotationGet(Mapping.class);
                    final var rootPath = defVal(defVal(parentMapping.value(), parentMapping.path()), "/");
    
                    log.info("Http Controller Find: " + format("[ {}, rootPath: {} ]", beanWrap.clz(), rootPath));
    
                    return Utils.collectMethod(beanWrap.clz().getDeclaredMethods(), Utils.allHttpType()).stream()
                            .map(method -> initMethod(rootPath, method))
                            .map(methodWrap -> HttpHandlerMapping.create(beanWrap.get(), methodWrap));
                }).toList()));
    }
```
- 挂载到vert.x的路由上
```java
    /**
     * 将当前http method handler挂载到router上去
     *
     * @param router 路由器
     */
    public void attach(final Router router) {
        log.info("Http Handler Registered: {}", this.path);
        router.route(HttpMethod.valueOf(this.httpMethodType.name), this.path)
                .handler(rCtx -> {
                    rCtx.json(this.invoke())
                            .subscribe().with(v -> {
                                rCtx.end().subscribe();
                            });
                });
    }
```

**2.websocket挂载同理**

todo

- 完善http封装
- 集群方案
- 日志打印问题
