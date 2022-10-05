package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 案例4 从 aaa.txt 中读取数据然后输出到 bbb.txt中
 */
public class NioTest4 {
    public static void main(String[] args)throws Exception{
        FileInputStream inputStream = new FileInputStream("aaa.txt");
        FileOutputStream outputStream = new FileOutputStream("bbb.txt");
        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(33);

        while (true){
            byteBuffer.clear();
            //读取多少字节就会返回，如果读取 -1 说明读取完了
            int read = inputChannel.read(byteBuffer);
            System.out.println("read == "+read);
            if(-1 == read){
                break;
            }
            byteBuffer.flip();
            System.out.println("limit == "+byteBuffer.limit());
            outputChannel.write(byteBuffer);
        }
        inputChannel.close();
        outputChannel.close();

    }
}
