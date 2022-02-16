# chat Project

一个端口同时提高http与websocket服务，基于vert.x

- [vert.x](https://github.com/eclipse-vertx/vert.x): 作为网络框架,是全异步的
- [solon](https://github.com/noear/solon): 作为`ioc`与`di`类似功能`Spring`

websocket测试网站：http://www.easyswoole.com/wstool.html

# http handler 与 websocket endpoint 挂载原理

**1.http挂载**
- 首先从容器中找到添加了`@Controller`的类
- 
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

todo

- 完善http封装
- reactive？
- 集群方案