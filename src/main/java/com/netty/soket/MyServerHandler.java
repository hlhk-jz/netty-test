package com.netty.soket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 我们约定好客户端与服务器端 soket 连接传输的格式是 string，所以
 * 继承的类的泛型为 string,我们也可以自己定义类型作为数据的传输格式
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    /**
     * ChannelHandlerContext 上下文对象，可以通过它获得客户端的一些信息
     * msg 该内容就是客户端真正传递过来的数据，该类型和继承类的泛型类型一致
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("地址："+ctx.channel().remoteAddress()+"，内容："+msg);
        /**
         * writeAndFlush 该方法包含了 write 写和 flush 是把缓冲的内容清出去
         */
        ctx.channel().writeAndFlush("Hellow Word!!!!!!!!!!");
    }

    /**
     * 出现异常要做的事情，通常我们都会把这个连接关闭掉
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
