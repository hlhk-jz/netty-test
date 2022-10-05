package com.netty.io;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 0拷贝案例
 * nio 0拷贝方式 服务端
 */
public class NioServer {
    public static void main(String[] args)throws Exception{
        InetSocketAddress inetSocketAddress = new InetSocketAddress(6666);
        ServerSocketChannel open = ServerSocketChannel.open();
        ServerSocket socket = open.socket();
        /**
         * 该设置是指当tcp连接断开处于超时的状态下，其它客户端仍然可以连接到该通道，
         * 因为正常情况下tcp连接刚断开超时状态下其它客户端时不能连接该通道的，所以
         * 如果有需要我们可以设置该属性，代表可以在上一个tcp连接断开超时的时候可以
         * 进行连接，还有设置该值需要在 bind 方法前，否则是无效的
         */
        socket.setReuseAddress(true);
        socket.bind(inetSocketAddress);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4444);
        while (true){
            SocketChannel accept = open.accept();
            //配置成阻塞的，因为案例没用到selector，其实默认也是阻塞的，配不配置都可以
            accept.configureBlocking(true);
            int readCount = 0;
            while (-1 != readCount){
                readCount = accept.read(byteBuffer);
                //每读一次就将 byteBuffer 归为
                byteBuffer.rewind();
            }
        }
    }
}
