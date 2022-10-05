package com.netty.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTest1 {
    public static void main(String[] args){
        IntBuffer allocate = IntBuffer.allocate(10);
        for (int i = 0;i<allocate.capacity();i++){
            int i1 = new SecureRandom().nextInt(20);
            allocate.put(i1);
        }

        allocate.flip();

        while (allocate.hasRemaining()){
            System.out.println(allocate.get());
        }


    }
}
