package com.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MyServer {
    public static void main(String[] args)throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup wokeGroup = new NioEventLoopGroup();

       try {
           ServerBootstrap bootstrap = new ServerBootstrap();
           bootstrap.group(bossGroup,wokeGroup).channel(NioServerSocketChannel.class)
                   .handler(new LoggingHandler(LogLevel.INFO))
                   .childHandler(new WebSoketInitalizer());
           ChannelFuture sync = bootstrap.bind(8899).sync();
           sync.channel().closeFuture().sync();
       }finally {
           bossGroup.shutdownGracefully();
           wokeGroup.shutdownGracefully();
       }
    }
}
