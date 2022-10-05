package com.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * buffer的 Scattering 和 Gathering 它们只是概念，并不是方法
 * 之前的案例我们不管读写都是对Buffer操作，并且读写都是对一个Buffer操作
 *
 * Scattering 的作用是我们不仅可以传递一个Buffer而且还可以传递一个Buffer的数组，就是
 * 说我们在读取的时候比如有N个Buffer数组，每个Buffer的capacity设置的都很小，
 * 读取的数据却很大，我们通过Scattering 传递这些数组，当第一个读满就用下一个
 * buffer数组。总结：就是可以将一个channel中的数据可以读到多个buffer中
 *
 * Gathering 的作用是在写的时候可以往多个buffer数组中写
 *
 * 应用场景：
 * Scattering 当我们自定义协议传输的时候，比如传过来一个请求，请求中的第一个
 * header是5个字节，第二个是10个字节，第三个是body数据，这种情况我们使用
 * Scattering可以针对不同长度的字节存储对应长度的 Buffer 当中，相当于
 * 天然的实现了对数据的分门别类，不必在只用一个Buffer来读数据，在解析的时候
 * 取前5个前10了，方便了很多
 * Gathering 道理和Scattering类似
 */
public class NioTest11 {
    public static void main(String[] args)throws Exception{
        /**
         * 打开服务器套接字通道,该对象必须绑定一个地址,这样就相当于服务器启动了
         * 并监听 8899 端口号上
         */
        ServerSocketChannel open = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        open.socket().bind(address);

        int messageLenth = 2 +3 + 4;
        //构建buffer数组，用于接收数据
        ByteBuffer[] byteBuffers = new ByteBuffer[3];
        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        //一直阻塞等待连接，当接收到客户端连接后该方法返回的套接字通道
        SocketChannel accept = open.accept();
        //循环读取
        while (true){
            int readNum = 0;
            //判断如果读取的字节小于我们定义的3个byteBuffer的capacity数，就一直继续读取
            while (readNum < messageLenth){
                //返回读到的字节数量
                long read = accept.read(byteBuffers);
                readNum += read;
                System.out.println("readNum == "+read);
                //每次读取我们都打印buffers中的每个buffer的 limit 和 postition
                Arrays.asList(byteBuffers).stream().forEach(buffer->{
                    System.out.println("limit："+buffer.limit()+"，postition："+buffer.position());
                });
            }
            //当读满后，我们将数据写会客户端，先 flip 下
            Arrays.asList(byteBuffers).stream().forEach(bu->{
                bu.flip();
            });
            //将数据写回客户端
            int witerNum = 0;
            while (witerNum < messageLenth){
                accept.write(byteBuffers);
                };

            //情空本次 buffers 中的几个buffer数据
            Arrays.asList(byteBuffers).forEach(sk->{
                sk.clear();
            });
            System.out.println("readNum："+readNum+"，witerNum："+witerNum+"，messageLenth："+messageLenth);
            }
     }
}
