package com.netty.grpc;

import com.grpcpk.*;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
    /**
     * 案例 1
     * @param request
     * @param responseObserver
     */
    @Override
    public void getUserName(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("接收客户端请求数据："+request);
        //构造返回数据
        responseObserver.onNext(MyResponse.newBuilder().setRealname("张三").build());
        //标识调用方法已经结束
        responseObserver.onCompleted();
    }

    /**
     * 案例 2
     * 查询所有ID 为 5 的数据
     * @param request
     * @param responseObserver
     */
    @Override
    public void getStuById(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        String id = request.getId();
        if("5".equals(id)){
            responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge("22")
            .setHome("沈阳").build());
            responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge("22")
                    .setHome("海城").build());
            responseObserver.onNext(StudentResponse.newBuilder().setName("赵柳").setAge("22")
                    .setHome("大连").build());
            responseObserver.onCompleted();
        }
    }

    /**
     * 案例3 客户端请求是一个流，服务器端响应一个 list
     * 可以发现案例3与案例1和2有着明显的差别，请求参数，以及响应，前两个案例的
     * 参数1是客户端的请求，参数2是用来向调用者返回数据以及标注返回结束，并且是没有返回
     * 类型的 void，而这里的案例3有着明确的返回类型 StreamObserver，并且参数只有
     * 一个responseObserver，很明显 参数中的responseObserver还是用来处理响应的，而
     * 返回一个请求StreamObserver<MyRequest> 就有点难理解了，按理说请求不应该作为
     * 参数传进来么，怎么会作为响应返回呢？请求参数又该怎么拿到呢？
     */
    @Override
    public StreamObserver<MyRequest> getStudnetList(StreamObserver<StudentResponseList> responseObserver) {
        return new StreamObserver<MyRequest>() {
            /**
             * 因为发过来的请求是流式数据，所以发过来一个数据该方法就会执行一次
             */
            @Override
            public void onNext(MyRequest myRequest) {
              System.out.println("服务端案例3：接收客户端请求数据："+myRequest.getUsername());
            }

            //出错的时候会执行该方法
            @Override
            public void onError(Throwable throwable) {
              System.out.println("接收客户端流式数据发生异常！！！");
            }

            /**
             * 该方法是当客户端所有的数据都发送完成之后会执行该方法,这时候
             * 服务器端就会感知到这个事件，然后在该方法中将结果进行响应
             */
            @Override
            public void onCompleted() {
                StudentResponseList.Builder builder = StudentResponseList.newBuilder();
                builder.addStudentResponse(StudentResponse.newBuilder().setName("张三").setAge("22").setHome("沈阳"));
                builder.addStudentResponse(StudentResponse.newBuilder().setName("李四").setAge("33").setHome("大连"));
                builder.addStudentResponse(StudentResponse.newBuilder().setName("王五").setAge("44").setHome("鞍山"));
                responseObserver.onNext(builder.build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * 案例4 客户端发送流，服务器端响应流
     * 和案例3不同的是这里的响应是在 onNext 中进行响应的，
     * 意思是客户端发送一个流服务器端就响应一个流，其实放在
     * onCompleted中来响应也是可以的，但是只能响应一个流
     * onCompleted代表客户端的流式数据已经发送完毕了
     */
    @Override
    public StreamObserver<StreamRequest> streamInfoById(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest value) {
                System.out.println("案例4服务器端接收客户端参数："+value.getRequestId());
                responseObserver.onNext(StreamResponse.newBuilder().setResponseId(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
