package com.netty.chat;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MyChatClient {
   public static void main(String[] args)throws Exception{
       //客户端只需要一个事件循环组即可，因为它只负责连即可
       EventLoopGroup loopGroup = new NioEventLoopGroup();
       try {
           //以下和服务器端用到的对象都有少许区别，但大体上都差不多
           Bootstrap bootstrap = new Bootstrap();
           bootstrap.group(loopGroup).channel(NioSocketChannel.class);
           bootstrap.handler(new MyChatClientInitlalizer());
           Channel localhost = bootstrap.connect("localhost", 8899).sync().channel();
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
           for (;;){
               localhost.writeAndFlush(bufferedReader.readLine()+" \n");
           }
       }finally {
           loopGroup.shutdownGracefully();
       }
   }
}
