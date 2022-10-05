package com.netty.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * 服务器端
 */
public class GrpcServer {
    private Server server;

    /**
     * 定义启动服务器的方法，并初始化 server 对象
     * addService 增加服务器端真正处理的我们自定义service来处理逻辑
     * 如果我们用的是 spring 我们在添加自定义的 service 就可以通过
     * 配置文件的方式加载进来
     */
    private void start()throws IOException {
        this.server = ServerBuilder.forPort(8899).addService(new StudentServiceImpl()).build().start();
        System.out.println("server start!");
    }

    /**
     * 定义关闭 server 的方法
     */
    private void stop(){
        if(null != this.server){
            this.server.shutdown();
        }
    }

    /**
     * 使用 grpc 提供的阻塞方法
     */
    private void block() throws InterruptedException {
        if(null != this.server){
            this.server.awaitTermination();
        }
    }

    /**
     * 定义 main 方法
     */
    public static void main(String[] args)throws Exception{
        //先实例化当前对象
        GrpcServer grpcServer = new GrpcServer();
        //调用初始化方法
        grpcServer.start();
        //调用阻塞方法，如果不加该方法，启动直接就停止了
        grpcServer.block();
    }
}

