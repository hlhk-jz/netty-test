package com.netty.soket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
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
        //最后这个是我们自己创建的 handler
        pipeline.addLast(new MyServerHandler());
    }
}
