package com.netty.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 案例2 将文件中的数据通过nio 方式读取到程序中
 */
public class NioTest2 {
    public static void main(String[] args)throws Exception{
        //创建文件输入流
        FileInputStream stream = new FileInputStream("text.txt");
        //通过文件输入流获取一个通道
        FileChannel channel = stream.getChannel();
        //创建一个 buffer
        ByteBuffer allocate = ByteBuffer.allocate(512);
        //将数据写到 buffer当中
        channel.read(allocate);

        //翻转
        allocate.flip();

        while (allocate.remaining()>0){
            byte b = allocate.get();
            System.out.println("=== "+(char)b);
        }
        //关闭流
        stream.close();
    }
}
