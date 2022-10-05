package com.netty.protoandnetty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestClientHandler extends SimpleChannelInboundHandler<SkDataPb.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SkDataPb.MyMessage msg) throws Exception {

    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       // DataInfoPb.Student build = DataInfoPb.Student.newBuilder().setName("张三").setAge(22).setAddress("沈阳").build();
        //包含所有消息的proto，这里传递的是最外层的消息，包含了所有消息。这里设置的消息是 Cat
        SkDataPb.MyMessage.Builder builder = SkDataPb.MyMessage.newBuilder();
        builder.setType(SkDataPb.MyMessage.DataType.CatType);
        builder.setCat(SkDataPb.Cat.newBuilder().setHome("黑龙江").setSex("母").build());
        ctx.channel().writeAndFlush(builder);
    }

}
