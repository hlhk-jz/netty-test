package com.netty.nio;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射文件 MappedByteBuffer，我们通过 allocateDirect(521)方法
 * 的时候底层的父类中其中有个 MappedByteBuffer，它是一个直接的缓冲区
 * 它是一个文件的内存映射区域，它和文件的映射会一直保持，直到 buffer
 * 被垃圾回收掉
 * 内存映射就是说我们不必根文件打交道，我们跟内存打交道，我们对内存的
 * 任何修改都会被写到磁盘上，总结来说 内存映射文件 就是允许java直接和
 * 内存访问的一种特殊的文件，我们直接操作内存，然后由操作系统相应的对
 * 文件进行IO，这样我们就可以实现高效率的IO操作。用于内存映射的内存实际
 * 是一个堆外内存，在 NioTest8中有做介绍
 */
public class NioTest9 {
    public static void main(String[] args)throws Exception{
        /**
         * 注意的是通过  FileInputStream.getChannel 获取的通道文件只能读不能写，使用它会报错
         * 而 RandomAccessFile.getChannel 然后通过设置 rw 是既能读又能写
         */
        //FileInputStream file = new FileInputStream("aaa.txt");
        // rw 代表既能读又能写
        RandomAccessFile file = new RandomAccessFile("aaa.txt","rw");
        FileChannel channel = file.getChannel();
        /**
         * 参数1 是要进行什么操作，这里设置的是既能读又能写
         * 参数2 映射的起始位置
         * 参数3 映射多少，这里设置了5个字节
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0,(byte)'a');
        map.put(2,(byte) 'c');
        file.close();

    }
}
