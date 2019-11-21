package com.cad.vr;

import com.cad.vr.server.SocketChatServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @Author: Knox
 * @Date: 2019/11/20 10:40 AM
 * @Description: You Know
 * @Version 1.0
 */
@EnableWebSocket
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SocketChatServer.getInstance().start();
        SpringApplication.run(Main.class, args);
    }
}
