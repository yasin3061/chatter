package com.yasinbee.chatter.client.grpc.out;

import com.google.protobuf.GeneratedMessageV3;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.ClientCalls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.function.Supplier;

public class GrpcApiClient {

    private AbstractStub stub;
    private ManagedChannel channel;
    public GrpcApiClient(Class<?> grpc) {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress("localhost", 50000);
        channelBuilder.usePlaintext();

        channel = channelBuilder.build();
        try {
            Class<?> grpcClass = grpc.forName(grpc.getCanonicalName());
            Method method = findMethodOnClass("newBlockingStub", grpcClass);
            this.stub = (AbstractStub) method.invoke(grpcClass, channel);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create gRPC client", e);
        }
    }

    public Iterator invokeServerStreamingCall(Supplier<MethodDescriptor> method
            , GeneratedMessageV3 request) {

        return ClientCalls.blockingServerStreamingCall(
                this.getStub().getChannel(), method.get(), this.getStub().getCallOptions(),
                request);
    }

    public GeneratedMessageV3 invokeUnaryCall(Supplier<MethodDescriptor> method
            , GeneratedMessageV3 request) {

        return (GeneratedMessageV3) ClientCalls.blockingUnaryCall(
                this.getStub().getChannel(), method.get(), this.getStub().getCallOptions(),
                request);
    }

    public void disconnect() {
        this.channel.shutdown();
    }

    private static Method findMethodOnClass(String methodName, Class<?> grpcClass) {
        Method[] methods = grpcClass.getMethods();
        Method method = null;

        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }

        if (method == null) {
            throw new RuntimeException("Cannot find methodName method");
        }

        return method;
    }

    public AbstractStub getStub() {
        return this.stub;
    }
}
