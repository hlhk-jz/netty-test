package com.netty.nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 对文件加锁，不能进行读写，用的不多简单了解
 */
public class NioTest10 {
    public static void main(String[] args) throws  Exception{
        RandomAccessFile file = new RandomAccessFile("aaa.txt","rw");
        FileChannel channel = file.getChannel();
        //对第三索引到第六索引进行锁定 如果设置true就是共享锁，如果设置false就是排它锁
        FileLock lock = channel.lock(3,6,true);
        //锁的类型，是不是共享锁
        boolean shared = lock.isShared();
        //是否是有效的
        boolean valid = lock.isValid();
        System.out.println("是否有效的："+valid+" ，是否是共享锁："+shared);
        //把锁释放掉
        lock.release();
        file.close();
    }
}
