package com.netty.herbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 *  ChannelInboundHandlerAdapter 该类是一个适配器，里面有一个 userEventTriggered 方法
 *  翻译过来就是用户事件已触发，意思就是 触发了某一个事件后该方法就会触发，转发到下一个
 *  handler 管道，我们需要重写该方法，编写一些自己的逻辑
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 参数1是上下文对象，参数2是事件对象
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        /**
         * 判断如果事件是一个 闲置 事件
         * event.state() 返回的是一个枚举，里面有 读空闲，写空闲，读写都空闲 三
         * 种状态
         */
        if(evt instanceof IdleStateEvent){
            String status = "";
            IdleStateEvent event = (IdleStateEvent)evt;
            switch (event.state()){
                case READER_IDLE:
                    status = "读空闲";
                case WRITER_IDLE:
                    status = "写空闲";
                case ALL_IDLE:
                    status = "读写都空闲";
            }
            System.out.println(ctx.channel().remoteAddress() + " ："+status);
        }
        //将客户端的连接通道关闭，测试只输出了上面的打印效果即可
        ctx.channel().close();
    }
}
