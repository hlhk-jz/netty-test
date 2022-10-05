package com.netty.soket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyClieantInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 客户端的编解码和服务器端的都一样，就是最后一个处理器我们自己定义
     * 的与服务器端定义的有少许不同
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //以下部分都是 netty 提供的一些处理器handler
        //解码器，作用就是把二进制的一些数据解析成真正的数据
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
        //也是编解码的一部分
        pipeline.addLast(new LengthFieldPrepender(4));
        //字符串的解码
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //字符串的编码
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //自己定义的处理器
        pipeline.addLast(new MyClientHandler());
    }
}
