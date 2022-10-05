package com.netty.io;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * 0拷贝案例
 * 0拷贝方式 客户端
 */
public class NioClent {
    public static void main(String[] args)throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",6666));
        socketChannel.configureBlocking(true);
        String path = "D:/install/commonly/vmvare/kudu/kudu-s001.vmdk";

        FileChannel fileChannel = new FileInputStream(path).getChannel();
        long time = System.currentTimeMillis();
        /**
         * transferTo 是写到一个通道上
         * transferFrom 方法是从一个通道上读
         *
         * 这种方法可能比简单的循环更有效
         * *从该通道读取并写入目标通道的。许多的
         * *操作系统可以直接从文件系统缓存传输字节
         * *复制到目标通道，而不实际复制它们
         */
        long l = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总字节数："+l+"，耗时："+(System.currentTimeMillis()-time));
    }
}
