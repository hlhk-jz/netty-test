package com.netty.io;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * 0拷贝案例
 * 传统方式 客户端
 */
public class OldClent {
    public static void main(String[] args)throws Exception{
        Socket socket = new Socket("localhost",8800);
        String path = "D:/install/commonly/vmvare/kudu/kudu-s001.vmdk";
        FileInputStream inputStream = new FileInputStream(path);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[4444];
        //实际读到的字节数
        long readCount = 0;
        //总的字节数
        long totle = 0;
        long time = System.currentTimeMillis();
        while ((readCount = inputStream.read(bytes))>=0){
           totle += readCount;
           outputStream.write(bytes);
        }
        System.out.println("总字节数："+totle+"，耗时："+(System.currentTimeMillis()-time));
        outputStream.close();
        socket.close();
        inputStream.close();
    }
}
