package com.netty.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 案例3 通过nio将字符串写到文件中
 */
public class NioTest3 {
    public static void main(String[] args) throws Exception {
        //获取文件输出流
        FileOutputStream outputStream = new FileOutputStream("bbb.txt");
        //通过流对象获取通道
        FileChannel channel = outputStream.getChannel();
        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] b = "hellou wored".getBytes();
        //将数据写到 buffer 中
        for (int i=0;i<b.length;i++){
            byteBuffer.put(b[i]);
        }
        //翻转
        byteBuffer.flip();
        //写到文件中
        channel.write(byteBuffer);
        outputStream.close();
    }
}
