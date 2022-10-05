package com.netty.grpc;

import com.grpcpk.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.Random;

/**
 * 客户端
 */
public class GrpcClient {
    public static void main(String[] args){
        ManagedChannel localhost = ManagedChannelBuilder.forAddress("localhost", 8899).usePlaintext().build();
        //案例1和2的stub，客户端用来和服务器端真正交互的对象，它的底层会帮我们发送tcp 的请求，案例1简单请求简单响应
        StudentServiceGrpc.StudentServiceBlockingStub studentServiceBlockingStub = StudentServiceGrpc.newBlockingStub(localhost);
        //案例3 客户端调用传输为 流式 数据的 stub，因为是异步的所以需要使用 newStub 来定义
        StudentServiceGrpc.StudentServiceStub studentServiceStub = StudentServiceGrpc.newStub(localhost);

        MyResponse response = studentServiceBlockingStub.getUserName(MyRequest.newBuilder().setUsername("王五sss ").build());
        System.out.println("服务器端返回结果："+response.getRealname());

        System.out.println("---------------分割线-----------------");

        //案例2，普通请求，流式数据响应
        Iterator<StudentResponse> stuById = studentServiceBlockingStub.getStuById(StudentRequest.newBuilder().setId("5").build());
        while (stuById.hasNext()){
            StudentResponse next = stuById.next();
            System.out.println("姓名："+next.getName()+",age："+next.getAge());
        }
        System.out.println("---------------分割线-----------------");


        /**
         * 案例3 该调用方式和案例1和2也同样有着区别,客户端是由两个部分构成的：
         * 3.1 首先我们需要构造StreamObserver 该对象，它
         * 代表着服务器端的响应对象
         */
        StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
            //服务器在返回数据的时候该 onNext方法就会被调用
            @Override
            public void onNext(StudentResponseList o) {
                //把返回的list进行打印
                o.getStudentResponseList().forEach(stu->{
                    System.out.println("客户端案例3：服务端响应 name："+stu.getName()+"，age："+stu.getAge());
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
               System.out.println("客户端案例3:onCompleted()方法执行");
             }
        };
        //3.2
        StreamObserver<MyRequest> streamObserver = studentServiceStub.getStudnetList(studentResponseListStreamObserver);
        streamObserver.onNext(MyRequest.newBuilder().setUsername("难").build());
        streamObserver.onNext(MyRequest.newBuilder().setUsername("中").build());
        streamObserver.onNext(MyRequest.newBuilder().setUsername("析").build());
        streamObserver.onNext(MyRequest.newBuilder().setUsername("被").build());
        //调用onCompleted 代表客户端发送流式数据结束
        streamObserver.onCompleted();


        //案例4
        StreamObserver<StreamRequest> strserver = studentServiceStub.streamInfoById(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse value) {
                System.out.println("案例4客户端接收到服务器端响应："+value.getResponseId());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
               System.out.println("客户端打印服务器端关闭时间："+System.currentTimeMillis());
            }
        });
        for (int i = 1;i<10;i++){
            strserver.onNext(StreamRequest.newBuilder().setRequestId("来自客户端请求"+i+"").build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /**
         * 案例3因为是异步发送，还没来得及发出就程序已经结束了，所以这里让线程等会结束，其实
         * 在真实环境下程序肯定是一直运行着的
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
