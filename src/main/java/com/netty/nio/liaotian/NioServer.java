package com.netty.nio.liaotian;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 聊天案例服务器端
 */
public class NioServer {
    public static Map<String,SocketChannel> clentMap = new HashMap<>();
    public static void main(String[] args)throws Exception{
        //模板代码，创建 serverSocket 并注册到 selector上 设置连接事件
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8899));
        Selector selector = Selector.open();
        //将 channel 注册到 selector 上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //处理客户端连接和读操作
        while (true){
            //当有事件发生的时候 select() 方法才会返回，返回的是一个数值为 事件的 数量
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            //遍历 selectionKeys
            selectionKeys.forEach(selectionKey -> {
                final SocketChannel socketChannel;
                try {
                    if(selectionKey.isAcceptable()){
                        //有连接进来了
                        ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel)selectionKey.channel();
                        socketChannel = serverSocketChannel1.accept();
                        //因为连接事件暂时不知道什么原因删除不掉，所以暂时加个非空判断
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                        //将该客户端连接保存到 map 中
                        String uuId = UUID.randomUUID()+"";
                        clentMap.put(uuId,socketChannel);
                    }else if(selectionKey.isReadable()){
                        //读事件发生，我们把数据打印即可
                        socketChannel = (SocketChannel)selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int read = socketChannel.read(byteBuffer);
                        if(0 < read){
                            byteBuffer.flip();
                            Charset charset = Charset.forName("utf-8");
                            String message = String.valueOf(charset.decode(byteBuffer).array());

                            //获取到客户端UUID
                            String clentKey="";
                            for(Map.Entry<String, SocketChannel> entry:clentMap.entrySet()){
                                if(socketChannel == entry.getValue()){
                                    clentKey = entry.getKey();
                                    break;
                                }
                            }
                            //进行分发
                            SocketChannel clent;
                            for(Map.Entry<String,SocketChannel> entry: clentMap.entrySet()){
                                ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
                                clent = entry.getValue();
                                byteBuffer1.put((clentKey+"："+message).getBytes());
                                byteBuffer1.flip();
                                clent.write(byteBuffer1);
                            }
                            System.out.println("服务器端接收客户端 "+socketChannel.getLocalAddress()+" 发送数据："+message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            //清除selectionKeys
            selectionKeys.clear();
        }
    }
}
