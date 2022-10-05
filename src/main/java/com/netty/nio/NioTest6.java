package com.netty.nio;

import java.nio.ByteBuffer;

/**
 *  我们可以指定 position 和 limit 然后通过 slice() 方法来获取
 *  期间的数据会返回一个 ByteBuffer ，该对象是上一个byteBuffer
 *  的子序列，它们底层的数据其实就是一份当我们改了子序列数据内容
 *  重新设置上一个的 position 和 limit 获取的数据期间是更改后的
 */
public class NioTest6 {
    public static void main(String[] args){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for (int i=1;i<10;i++){
            byteBuffer.put((byte)i);
        }
        byteBuffer.flip();
        byteBuffer.position(2);
        byteBuffer.limit(4);
        //返回子序列
        ByteBuffer slice = byteBuffer.slice();
        for (int i=0;i<slice.remaining();i++){
            byte b = slice.get();
            b*=2;
            slice.put(i,b);
        }

        byteBuffer.limit(byteBuffer.capacity());
        byteBuffer.position(0);
        while (byteBuffer.hasRemaining()){
          System.out.println("=== "+byteBuffer.get());
        }
    }
}
