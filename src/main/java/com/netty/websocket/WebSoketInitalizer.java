package com.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSoketInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //因为WebSocket是基于HTTP的所以需要 HTTP 的解码器
        pipeline.addLast(new HttpServerCodec());
        //以块的方式去写的一个处理器
        pipeline.addLast(new ChunkedWriteHandler());
        /**
         * HttpObjectAggregator 该类在 netty 中基于HTTP编程中使用的很多，
         * 它的主要作用是将 HTTP 的请求或响应将其分块聚合到一起形成一个
         * 完整的请求
         * netty 将请求实际上是分段或分块的一种方式，比如客户端向服务器端
         * 发送了一个请求，这个请求比如长度是1000，netty 会将这个请求分成
         * N个段，假如每100分成一个段，那么就是10段，每一段都会走一个完整的
         * 流程，这样的话我们自己的处理器实际上只是读到了一个段，在第一个案例
         * 中HTTP 服务器没有使用该处理器，我们发现 channelRead0 方法在一个
         * 请求的时候执行了多次，就是因为没有使用该处理器，所以该处理器就是将
         * 所有段整合到一起形成一个完成的HTTP 请求或者HTTP 响应
         * 8192 参数是可以设定的，是指定的长度，如果聚合的内容超过了该长度，
         * 那么 handleOversizedMessage 就会被调用
         */
        pipeline.addLast(new HttpObjectAggregator(8192));
        /**
         * WebSocketServerProtocolHandler 字面的意思是 websocket服务器协议处理器，
         * 这个处理器会帮助我们完成一系列的关于 Websocket 一些繁重的工作，比如
         * 负责 websocket 的握手，控制阵的一个处理，比如 ping pong close，心跳
         * 相关的内容，发起方称为 ping,返回方称为 pong，而文本的相关信息会交由下
         * 一个处理器去实现的，是我们自己定义的 handler
         * /ws 是 websocket 协议的形式，类似 HTTP 的形式是 http://而 websocket
         * 协议的形式是 ws://
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new WebSocketHandler());
    }
}
