package com.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInit extends ChannelInitializer<SocketChannel> {
    /**
     * 初始化管道，一旦 channel 被注册，该方法就会被调用
     * ChannelPipeline 就是一个管道，管道中有很多 ChannelHandler，
     * 相当于一个个的拦截器，每个拦截器都会根据自身的业务进行处理
     * pipeline 中有很多 add.. 方法，不同的方法有不同的先后顺序
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //name 是可选的，如果我们不指定，netty会给我生成一个
        /**
         * HttpServerCodec 该组件的作用是相当于 HttpRequestDecoder和HttpResponseEncoder
         * 的组合，帮我们解决客户端请求的解码，与响应的编码， netty 提供了一些基础的实现
         */
        pipeline.addLast("httpServerCodec",new HttpServerCodec());
        pipeline.addLast("TestHttpServerHandler",new TestHttpServerHandler());
    }
}
