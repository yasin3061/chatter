package com.yasinbee.chatter.client;

import com.yasinbee.chatter.client.connect.MessageReceiverThread;
import com.yasinbee.chatter.client.connect.ShutdownHook;
import com.yasinbee.chatter.client.grpc.out.ConnectorServiceProxy;
import com.yasinbee.connector.api.dto.Message;
import com.yasinbee.connector.api.dto.User;
import com.yasinbee.connector.api.service.ConnectorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter your username");
        String currentUser = in.nextLine();
        User user = User.newBuilder().setId(currentUser).build();

        ConnectorService connector = new ConnectorServiceProxy();
        MessageReceiverThread messageReceiver = new MessageReceiverThread(connector, user);
        messageReceiver.startThread();
        ShutdownHook shutdownHook = new ShutdownHook(connector, user);
        shutdownHook.register();

        String receiver = "";
        while (!receiver.equals("quit")) {
            System.out.println("Enter receiver user (type 'quit' to quit application)");
            receiver = in.nextLine();

            String message = "";

            while (!message.equals("close")) {
                System.out.println("Enter message (type 'close' to select another user)");
                message = in.nextLine();
                if (!"close".equals(message)) {
                    connector.sendMessage(Message.newBuilder().setFrom(currentUser).setTo(receiver)
                            .setText(message).build());
                }
            }
        }

        System.exit(0);
    }
}
