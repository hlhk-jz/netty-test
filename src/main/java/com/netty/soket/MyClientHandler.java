package com.netty.soket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 服务器端向客户端发送消息的时候该方法就会被触发
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
         System.out.println(ctx.channel().remoteAddress());
         System.out.println("服务器端的响应信息："+msg);
         ctx.writeAndFlush("来自客户端的消息");
    }

    /**
     * 在客户端连接服务器端成功后因为没有向服务器端发送消息导致什么也没输出
     * 所以通过该方法，第一节讲了在SimpleChannelInboundHandler的父类的父类
     * 中会有很多回调，不同的回调代表着不同的事件，而 channelActive 事件就是
     * 当通道已经连接了，已经处于活动状态，在该方法中该时机我们向服务器端发送
     * 数据，这样就会触发服务器端我们自定义的 handler 中向客户端发送数据，然后
     * 客户端 handler 也会向服务器端回信息，这样一直互相响应
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("与服务器建立连接后就发送消息");
    }

    /**
     * 出现异常要执行的事情
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
