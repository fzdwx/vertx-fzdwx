package chat.like.cn.serv.core;

import chat.like.cn.core.function.tuple.Tuple;
import chat.like.cn.core.function.tuple.Tuple2;
import chat.like.cn.core.util.Exc;
import chat.like.cn.core.util.Func;
import chat.like.cn.core.util.Print;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;

/**
 * {@link org.noear.solon.annotation.ServerEndpoint} websocket endpoint 必须实现该类
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 14:26
 */
public interface WebSocketListener {

    Tuple2<Boolean, Exception> handShakeSuccess = Tuple.t2(Boolean.TRUE, null);
    Tuple2<Boolean, Exception> handShakeFail = Tuple.t2(Boolean.FALSE, Exc.chat("握手失败"));

    /**
     * webSocket 握手请求
     *
     * @param headers 请求头
     * @return {@link Tuple2<Boolean, String> }
     * @apiNote <pre>
     *     boolean: 表示当前请求是否成功
     *     exception: 表示失败要发送给客户端的信息(只有当失败时才会被处理)
     * </pre>
     */
    default Tuple2<Boolean, Exception> doOnHandShake(MultiMap headers) {
        // default impl
        return handShakeSuccess;
    }

    /**
     * 当websocket 通道打开时调用
     *
     * @param ws ws
     */
    void doOnOpen(final ServerWebSocket ws);

    /**
     * 当websocket 通道关闭时调用
     *
     * @param ws ws
     */
    void doOnClose(final ServerWebSocket ws);

    /**
     * 处理二进制数据
     *
     * @param ws  websocket
     * @param buf 二进制数据Buf
     */
    default void handleBinary(final ServerWebSocket ws, Buffer buf) {
        Print.print("websocket receive binary data", buf);
    }

    /**
     * 处理文本数据
     *
     * @param ws      websocket
     * @param message message
     */
    default void handleText(final ServerWebSocket ws, String message) {
        Print.print("websocket receive message data", message);
    }

    /**
     * 处理Pong
     *
     * @param ws  websocket
     * @param buf 数据帧
     */
    default void handlePong(final ServerWebSocket ws, Buffer buf) {
        ws.writeFrame(WebSocketFrame.pingFrame(Buffer.buffer("PING")));
    }

    /**
     * 处理Ping数据
     *
     * @param ws    websocket
     * @param frame 数据帧
     */
    default void handlePing(final ServerWebSocket ws, WebSocketFrame frame) {
        ws.writeFrame(WebSocketFrame.pongFrame(Buffer.buffer("PONG")));
    }

    default void onHandShake(MultiMap headers, Handler<AsyncResult<Integer>> resultHandler) {
        final var res = doOnHandShake(headers);

        if (res == null || res.t1 == Boolean.FALSE)
            resultHandler.handle(Future.failedFuture(Func.defVal(res, handShakeFail).t2));
        else
            resultHandler.handle(Future.succeededFuture());
    }

}