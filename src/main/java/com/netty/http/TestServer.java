package com.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args)throws  Exception{
        /** 1
         * 首先定义两个线程组,为事件循环组，其实用一个线程组也可以，
         * 既接收请求，也处理请求，但是这并不是一种好的编程方式，
         * netty 推荐使用两个线程组来完成
         *
         * NioEventLoopGroup 我们可以把它理解成死循环，就好像 tomcat
         * 一样，tomcat 里面也是存在一个死循环，用来不断的接收客户端
         * 发起的连接并进行处理
         *
         * 这里为什么定义两个死循环呢？
         * bossGroup 翻译过来就是老板的意思，它用来不断的接收客户端的
         * 连接，但是它不对这个连接进行处理而是交给 workerGroup
         * workerGroup 充当工人的角色，用来处理连接，比如获取参数，
         * 进行一些业务处理等等
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        /** 2
         *  ServerBootstrap 可以看做服务端启动，它是 netty 给我们
         *  提供的一个可以很方便的启动服务端的一个类，封装了启动相关
         *  的一些逻辑。相当于一个帮助类简化netty的创建工作，不必我们自己封装了
         *
         *  group 方法传入定义的两个线程组
         *  channel 方法设置管道NioServerSocketChannel
         *  childHandler 子处理器，参数对象是由我们自己编写的初始化器用来进行处理请求，
         *  初始化由我们自己定义的一些 childHandler和系统的一些handler
         */
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new TestServerInit());

            //绑定一个端口，然后调用 sync() 方法进行等待连接
            ChannelFuture channelFuture = serverBootstrap.bind(9998).sync();
            //关闭监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //使用netty的优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
