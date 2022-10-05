package com.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * byteBuffer通过 allocateDirect方法来获取，它底层是
 * new出来的一个DirectByteBuffer 对象，我们可以称之为
 * 直接缓冲
 * 之前我们通过 .allocate(22) 方法获取的 ByteBuffer 底层和
 * java 的创建对象没啥区别，该对象是在堆中的。而这里我们通过
 * allocateDirect(22) 获取的 ByteBuffer 对象，该对象
 * 也是位于堆中，但是它底层会通过某个成员直接引用堆外内存
 * 的数据，当我们通过放方法获取的时候底层的顶层接口中
 * 的 Buffer中的 address 就会被使用，该属性在使用 直接缓冲
 * 的时候会被使用到用来增加调用 JNI(本地接口) 的效率速度
 * 因为是堆外内存，所以在堆中需要有个成员来引用堆外内存
 * 的ByteBuffer数据，该成员就是 address
 *
 * 通过 allocate(22) 方法底层多了一个拷贝的过程，堆中会有一个
 * 数组来存放数据，然后当进行读写的时候底层需要将该数组原封
 * 不动的拷贝到堆外内存进行读写，而使用 allocateDirect方法
 * 堆中就不会有数组了，我们通过address 来直接对堆外内存中的
 * 数组直接读写
 *
 * 只要涉及IO肯定要和外设打交道，通过 allocateDirect 获取的
 * BUffer 我们在进行读写的时候省略了拷贝的一个过程，达到了
 * 0拷贝的意义
 *
 */
public class NioTest8 {
    public static void main(String[] args)throws Exception{
        //创建文件输入输出流
        FileInputStream inputStream = new FileInputStream("mmm.txt");
        FileOutputStream outputStream = new FileOutputStream("xxx.txt");
        //get通道
        FileChannel inputChanner = inputStream.getChannel();
        FileChannel outputChanner = outputStream.getChannel();
        //创建buffer 这里是通过 allocateDirect方法创建
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(521);

        while (true){
            //清空上一次读取的数据
            byteBuffer.clear();
            int read = inputChanner.read(byteBuffer);
            if(-1 == read){
                break;
            }
            //翻转
            byteBuffer.flip();
            //写到输出文件中
            outputChanner.write(byteBuffer);
        }
        inputStream.close();
        outputStream.close();
    }
}
