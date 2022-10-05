package com.netty.nio;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**案例：这里的服务端开了4端口，然后用一个线程去监听这些端口，
 * 当客户端触发 连接，可读数据，的事件时这里也进行了回调处理
 * 事件的概念是很重要的，客户端和服务器端都是通过一个个的事件
 * 来进行交互的
 *
 *  ServerSocketChannel.open 方法和 Selector的open方法差不多
 *  底层都是通过提供器来实现的
 */
public class NioTest12 {
    public static void main(String[] args)throws Exception{
        int[] port = new int[4];
        port[0] = 3333;
        port[1] = 4444;
        port[2] = 5555;
        port[3] = 6666;
        Selector selector = Selector.open();
        /**
         * 将一个 selector 注册到这些端口号上，使得一个 selector
         * 可以监听这么多的端口号
         */
        for (int i = 0;i<port.length;i++){
            /**
             * ServerSocketChannel 它也是一个抽象类，是一个面向流的
             * 可选择的 通道，我们可以通过它的 open() 方法来创建，一个
             * 新创建的 ServerSocketChannel 是打开的，但是是未绑定的，不能直接
             * 调用它的 accept 方法，所以这里需要进行绑定，可以通过 bind 方法进行绑定
             */
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //调整channel的阻塞模式，false是不阻塞反之阻塞
            serverSocketChannel.configureBlocking(false);
            //通过它的socket方法获取与这个通道相关联的 ServerSocket 对象
            ServerSocket socket = serverSocketChannel.socket();
            //封装IP端口
            InetSocketAddress address = new InetSocketAddress(port[i]);
            System.out.println("监听端口："+port[i]);
            //通过 bind 方法进行绑定
            socket.bind(address);
            /**
             * 将selector注册到所有通道上，并设置感兴趣的key 参数1是我们定义的 selector
             * 参数2是感兴趣的selectionkey，这里的常量为 可以接受的key
             * 这里我们必须得设置该常量 连接
             * 补充：常量一共有四种  接受；连接；读；写
             */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        /**
         * 补充：其实在 NIO 这里也是有一个死循环的，之后调用select方法，它是阻塞的，当
         * 客户端连接来之后然后这里判断这是一个连接的动作，进行连接的处理，连接建立好之后
         * 就开始等待客户端传数据
         */
        while (true){
            //select() 方法返回的是一个 key 的数量
            int select = selector.select();
            //该方法返回的是我们感兴趣的 key set
           // Set<SelectionKey> keys = selector.keys();
            //使用set时调用remove() 方法报错，所以转成list了，ArrayList重新了这些方法, 其它默认都是throw UnsupportedOperationException而且不作任何操作
            //selector.keys() 为什么要返回来一个SelectionKey集合呢？因为这是监听多个通道，假如同时有多个通道同时返回所以这里是返回的集合
            List<SelectionKey> keys = new ArrayList<>(selector.keys());
            System.out.println("当前key数量："+select+"，keys："+keys);
            //获取该set的迭代器
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                //isAcceptable 方法是判断是否有连接进来了的事件我们在这里首先判断该事件
                if(next.isAcceptable()){
                    //如果有连接事件，我们就可以通过该 selectionKey 的 channel 方法，获取到该通道
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) next.channel();
                    SocketChannel accept = serverSocketChannel.accept();
                    if(null == accept){continue;}
                    //设置为不阻塞
                    accept.configureBlocking(false);
                    /**
                     * 此时连接已经建立好了，我们应该也要把当前连接执行一次注册
                     * 并且我们此时应该关注的是 读
                     */
                    accept.register(selector,SelectionKey.OP_READ);
                    //最后我们需要将当前key从 iterator中删除掉，否则还会在执行一遍这里的逻辑
                    iterator.remove();
                    System.out.println("获取到当前客户端连接："+serverSocketChannel);
                }else if(next.isReadable()){
                    //判断是不是可读的
                    int readNum = 0;
                    SocketChannel socketChannel = (SocketChannel)next.channel();
                    while (true){
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);
                        if(0 == read){
                            break;
                        }
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        readNum += read;
                    }
                    System.out.println("读取："+readNum+"，来自于："+socketChannel);
                    iterator.remove();
                }
            }
        }
    }
}
