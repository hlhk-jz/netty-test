package com.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * WebSocketFrame 用于处理文本的，在 websocket 中使用，
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到消息："+msg.text());
        /**
         * 我们将收到的内容在返回给客户端
         * 需要注意的是 writeAndFlush 是一个通用的方法接收的是 Obj 类型，
         * 虽然我们在这里可以传字符串，但是在 WebSocket 中需要按照它的
         * 方式进行返回，也就是 WebSocketFrame 的六个子类中的哪个都可以
         */
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg.text()));
    }

    /**
     * 当一个连接建立后调用的通知，我们在这里打印下客户端的ID
     * asShortText() 是ID的简写，asLongText() 是全称是全局唯一的ID
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded 客户端id="+ctx.channel().id().asShortText());
    }

    /**
     * 当连接关闭时
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 客户端id="+ctx.channel().id().asShortText());
    }

    //发生异常我们关闭连接
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
