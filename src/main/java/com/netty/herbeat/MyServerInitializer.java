package com.netty.herbeat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 补充：在之前以及当下的一些案例中可以看到每一个样例的处理器都离不开一个个
 * 的 handler，在 netty 中的这种 handler 是类似于过滤器拦截器的概念，叫做
 * 责任链模式，和 springmvc 的过责任链模式概念很像，从头一个 handler 开始
 * 处理一直到最后一个 handler，当处理完成后再原路返回
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /**
         * IdleStateHandler 空闲状态事件触发的handler，当在一定的时间间隔
         * 内如果没有执行 读，写 ,或者即没有读也没有写操作就会触发该事件
         * 时间设置是通过该类的构造方法指定
         * 此时指定的是 读空闲是 5 秒，写空闲是 7 秒，读写都空闲是 10 秒
         */
        pipeline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));
        pipeline.addLast(new MyServerHandler());
    }
}
