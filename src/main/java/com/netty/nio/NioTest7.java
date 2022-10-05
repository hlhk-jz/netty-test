package com.netty.nio;

import java.nio.ByteBuffer;

/**
 * 通过 asReadOnlyBuffer 方法返回一个只读的 byteBuffer 
 */
public class NioTest7 {
    public static void main(String[] args){
        ByteBuffer byteBuffer = ByteBuffer.allocate(433);
        ByteBuffer byteBuffer1 = byteBuffer.asReadOnlyBuffer();
    }
}
