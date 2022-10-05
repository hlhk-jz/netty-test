package com.netty;

import com.google.protobuf.InvalidProtocolBufferException;
import com.test.protobuf.DataInfo;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

public class test {
    public static void main(String[] args){
       /* DataInfo.Student.Builder builder = DataInfo.Student.newBuilder();
        byte[] bytes = builder.setName("王五").setAge(22).setAddress("沈阳").build().toByteArray();

        try {
            DataInfo.Student student = DataInfo.Student.parseFrom(bytes);
            System.out.println("stu == "+student.getName());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }*/
        int max = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
   /*    final int sd;
       if(true){
           sd = 1;
       }else {
           sd = 2;
       }*/
       System.out.println("=="+max);
    }
}
