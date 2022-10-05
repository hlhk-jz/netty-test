package com.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.SocketAddress;
import java.net.URI;

/**
 * 我们自己定义的处理器
 * SimpleChannelInboundHandler 该类中的单词 Inbound 是一个术语就是进来的意思，
 * 而除去则是 OutBound。也就是说 SimpleChannelInboundHandler 是对进来的请求
 * 的一个处理,从该类向上跟进父类又继承了一个 ChannelInboundHandlerAdapter 类
 * ，该类中有很多的回调关于事件相关的方法，比如 通道注册，通道解除，通道处于
 * 活动状态，不活动状态等等，也就是说在某一个时刻会调用对应的事件回调方法，我
 * 们重写这些方法进行测试观察执行的顺序
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     *channelRead0 方法是用来读取客户端发过来的请求，并且向客户端返回
     * 响应的方法
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest) {
            //获取远程的客户端地址
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            System.out.println("客户端地址："+socketAddress);
            HttpRequest request = (HttpRequest)msg;
            String name = request.method().name();
            System.out.println("请求方式为："+name);
            String uri = request.uri();
            URI uri1 = new URI(uri);
            System.out.println("uri = "+uri1);
            if(uri1.getPath().equals("/favicon.ico")){
                System.out.println("请求 favicon.ico 直接返回");
                return;
            }
            System.out.println("channel执行！！！！！");
            //返回的信息
            ByteBuf content = Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8);
            //FullHttpResponse 是 netty提供的支持响应的一个对象
            //HttpVersion.HTTP_1_1 指定的HTTP 协议的版本，状态码，和内容
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            //设置一下头信息，因为返回的是文本所以设置 text/plain
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            //设置返回内容的长度响应多少个字节
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //用该对象将 response 返回，注意的是如果只使用 write 方法是不会返回的而是放到了缓冲区所以使用writeAndFlush
            ctx.writeAndFlush(response);
            //在响应后关闭连接，其实我们可以加上判断，是 http1.0还是1.1
            ctx.channel().close();
        }
    }

    /** 3
     * 连接，处于一个活动的状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive 执行！！！");
        super.channelActive(ctx);
    }

    /** 2
     * 注册，让netty进行统一管理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered 执行！！！");
        super.channelRegistered(ctx);
    }

    /** 1
     * 该方法执行说明有一个新的通道出现了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded 执行！！！");
        super.handlerAdded(ctx);
    }

    /** 4
     * 处理完后，处于不活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive 执行！！！");
        super.channelInactive(ctx);
    }

    /** 5
     * 最后取消注册
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered 执行！！！");
        super.channelUnregistered(ctx);
    }

    /**
     * 在出异常的时候调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //通常出异常需要把连接关闭掉
        ctx.close();
    }
}
