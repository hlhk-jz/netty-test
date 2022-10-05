package com.netty.io;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/** 0拷贝案例
 * 传统方式IO   server端
 */
public class OldServer {
    public static void main(String[] args)throws Exception{
        ServerSocket serverSocket = new ServerSocket(8800);
        while (true){
            //等待客户端连接
            Socket accept = serverSocket.accept();
            //案例是客户端发送一个文件，因为是二进制的所以这里用的是 dataInputStream
            DataInputStream dataInputStream = new DataInputStream(accept.getInputStream());
            try {
                while (true){
                    byte[] bytes = new byte[4444];
                    int read = dataInputStream.read(bytes, 0, bytes.length);
                    if(read == -1){
                        //读取完就结束
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
