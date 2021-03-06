package chat.vertx.fzdwx.core.ws;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import vertx.fzdwx.cn.core.function.lang;
import vertx.fzdwx.cn.core.function.tuple.Tuple;
import vertx.fzdwx.cn.core.function.tuple.Tuple2;
import vertx.fzdwx.cn.core.util.Exc;

/**
 * websocket endpoint 必须实现该类
 *
 * @author <a href="mailto:likelovec@gmail.com">韦朕</a>
 * @date 2022/2/15 14:26
 */
public interface WebSocketListener {

    Tuple2<Boolean, Exception> handShakeSuccess = Tuple.of(Boolean.TRUE, null);
    Tuple2<Boolean, Exception> handShakeFail = Tuple.of(Boolean.FALSE, Exc.chat("握手失败"));

    /**
     * webSocket 握手请求
     *
     * @param headers 请求头
     * @return {@link Tuple2 <Boolean, String> }
     * @apiNote <pre>
     *     boolean: 表示当前请求是否成功
     *     exception: 表示失败要发送给客户端的信息(只有当失败时才会被处理)
     * </pre>
     */
    default Tuple2<Boolean, Exception> onHandShake(MultiMap headers) {
        // default impl
        return handShakeSuccess;
    }

    /**
     * 当websocket 通道打开时调用
     *
     * @param ws ws
     */
    void onOpen(final ServerWebSocket ws);

    /**
     * 当websocket 通道关闭时调用
     *
     * @param ws ws
     */
    void onclose(final ServerWebSocket ws);

    /**
     * 当websocket发生错误
     *
     * @param ws  websocket
     * @param thr thr
     */
    void onError(ServerWebSocket ws, Throwable thr);

    /**
     * 处理二进制数据
     *
     * @param ws  websocket
     * @param buf 二进制数据Buf
     */
    default void handleBinary(final ServerWebSocket ws, Buffer buf) {
        //
    }

    /**
     * 处理文本数据
     *
     * @param ws      websocket
     * @param message message
     */
    default void handleText(final ServerWebSocket ws, String message) {
        //
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

    default void doOnHandShake(MultiMap headers, Handler<AsyncResult<Integer>> resultHandler) {
        final var res = onHandShake(headers);
        if (res == null || res.A == Boolean.FALSE)
            resultHandler.handle(Future.failedFuture(lang.defVal(res, handShakeFail).B));
        else
            resultHandler.handle(Future.succeededFuture());
    }
}