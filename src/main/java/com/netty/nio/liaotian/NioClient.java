package com.netty.nio.liaotian;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**聊天程序java客户端  连接后通过键盘输入发送数据*/
public class NioClient {
    public static void main(String[] args)throws IOException {
        try {
            //客户端建立通道
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            //客户端时发起连接，所以是 connect
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            //绑定连接地址
            socketChannel.connect(new InetSocketAddress("127.0.0.1",8899));

            while (true){
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey:selectionKeys){
                    //如果建立连接上了就进行处理
                    if(selectionKey.isConnectable()){
                        SocketChannel socketChannel1 = (SocketChannel)selectionKey.channel();
                        //判断这个连接是否正常进行的状态，相当于是否有效
                        if(socketChannel1.isConnectionPending()){
                            //声明已经完成连接
                            socketChannel1.finishConnect();
                            //然后向服务器端发送一个建立好连接的消息
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            byteBuffer.put((LocalDateTime.now()+"客户端建立连接成功").getBytes());
                            byteBuffer.flip();
                            socketChannel1.write(byteBuffer);

                            /**
                             * 此时已经和服务器端处于一个双向绑定的状态，我们
                             * 案例是需要用键盘输入来发送消息，所以我们起一个线程
                             * 不断的等待键盘输入然后发送消息
                             */
                            ExecutorService executorService = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
                            executorService.submit(()->{
                                while (true){
                                    byteBuffer.clear();
                                    InputStreamReader inputStream = new InputStreamReader(System.in);
                                    BufferedReader bufferedReader = new BufferedReader(inputStream);
                                    //一次读一行
                                    String line = bufferedReader.readLine();
                                    byteBuffer.put(line.getBytes());
                                    byteBuffer.flip();
                                    socketChannel1.write(byteBuffer);
                                }
                            });
                            //建立好连接后，注册为可读事件
                            socketChannel1.register(selector,SelectionKey.OP_READ);
                        }
                    }else if(selectionKey.isReadable()){
                        SocketChannel socketChannel1 = (SocketChannel)selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1023);
                        int read = socketChannel1.read(byteBuffer);
                        if(read>0){
                            String msg = new String(byteBuffer.array(),0,read);
                            System.out.println("服务器端响应："+msg);
                        }
                    }
                }
                //清除selectionKeys
                selectionKeys.clear();
            }
        }catch (Exception e){
          e.printStackTrace();
        }
    }
}
