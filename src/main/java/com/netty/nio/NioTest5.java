package com.netty.nio;

import java.nio.ByteBuffer;

/**
 * 手动添加byteBuffer数据，注意的是添加是什么顺序，
 * 取出的时候就是什么顺序
 */
public class NioTest5 {
    public static void main(String[] args){
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byteBuffer.putChar('你');
        byteBuffer.putDouble(22.22);
        byteBuffer.putLong(22L);
        byteBuffer.putFloat(22.2223f);
        byteBuffer.flip();

        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getDouble());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getFloat());
    }
}
