# vertx-fzdwx

一个端口同时提高http与websocket服务，基于vert.x

- [vert.x](https://github.com/eclipse-vertx/vert.x): 作为网络框架,是全异步的

websocket测试网站：http://www.easyswoole.com/wstool.html

**http handler 与 websocket endpoint 挂载原理**
具体实现可以看=>[ChatServerVertx](https://github.com/fzdwx/chat/blob/3c3ed609b330bee8e818bb4808eb1c9017ac0602/serv/src/main/java/vertx/fzdwx/cn/serv/core/verticle/ChatServerVertx.java)
<br>

<br>

**http handler demo**

```java

@Controller
@Mapping("/qweqwe")
@Slf4j
public class TestController {

    @SneakyThrows
    @Get
    @Mapping("/hello/:id/:name")
    public void hello(RoutingContext context, @Param("id") int id, @Param("name") String name) {
        log.info("invoke");
        context.redirect("http://localhost:8080/#/homePage");
        // return Map.of("hello", "world");
    }
}
```
<br>

**websocket end point demo**

```java

@ServerEndpoint("/123123")
public class TestServerEndpoint implements WebSocketListener {

    @Override
    public void onOpen(final ServerWebSocket ws) {
        ws.writeTextMessage("你好");
    }

    @Override
    public void onclose(final ServerWebSocket ws) {
        System.out.println("web socket 关闭");
    }

    @Override
    public void onError(final ServerWebSocket ws, final Throwable thr) {

    }
}
```
<br>

### 启动

```java

@Slf4j
public class ChatServ {

    public static void main(String[] args) {
        // 加载controller
        final List<Object> controllers = List.of(new TestController());
        // 加载ws
        final List<WebSocketListener> webSocketListeners = List.of(new TestServerEndpoint());
        // 加载http注解参数解析器
        Map<String, HttpArgumentParser> parsers = lang.listOf(new ParamParser(), new RoutingContextParser())
                .stream().collect(Collectors.toMap(HttpArgumentParser::type, Function.identity()));
        // 加载配置
        final var chatServerProps = new ChatServerProps();

        // 部署
        VerticleStarter.create(chatServerProps)
                .addDeploy("vertx.fzdwx.cn.serv.core.verticle.ChatServerVertx",
                        // ChatServerVertx 生命周期实现类，用于初始化数据
                        new ChatServerVertx.ChatInit().preDeploy(() -> listOf(chatServerProps,
                                controllers,
                                webSocketListeners,
                                parsers)
                        ))
                .start();

    }
}
```

- 集群方案