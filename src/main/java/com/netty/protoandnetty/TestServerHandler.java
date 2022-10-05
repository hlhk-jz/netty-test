package com.netty.protoandnetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * SimpleChannelInboundHandler 这里的泛型是在处理器中要解码的实例类型
 */
public class TestServerHandler extends SimpleChannelInboundHandler<SkDataPb.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SkDataPb.MyMessage msg) throws Exception {
       SkDataPb.MyMessage.DataType d  = msg.getType();
       if(d == SkDataPb.MyMessage.DataType.CatType){
           //传递的消息是 Cat
           SkDataPb.Cat cat = msg.getCat();
       }

        if(d == SkDataPb.MyMessage.DataType.DogType){
            //传递的消息是 Dog
        }

        if(d == SkDataPb.MyMessage.DataType.StudentType){
            //传递的消息是 Student
        }
    }
}
