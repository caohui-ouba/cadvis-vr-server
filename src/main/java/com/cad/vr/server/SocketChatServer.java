package com.cad.vr.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: Knox
 * @Date: 2019/11/20 10:41 AM
 * @Description: You Know
 * @Version 1.0
 */
@Slf4j
public class SocketChatServer extends Thread {

    private static final int PORT = 5555;

    private ServerSocket serverSocket;

    private SocketChatServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            log.error("failed to init server !");
            System.exit(1);
        }
        log.info("socket server init success !");
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Socket socket = serverSocket.accept();
                log.info("accept a socket client !");
                ChatSessionHolder holder = new ChatSessionHolder(String.valueOf(ChatThreadManager.getInstance().getCnt() + 1),socket);
                ChatThreadManager.getInstance().put(holder);
                holder.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SocketChatServer getInstance() {
        return Instance.INSTANCE.getInstance();
    }

    enum Instance {
        INSTANCE;
        private SocketChatServer chatServer;

        Instance() {
            this.chatServer = new SocketChatServer();
        }

        public SocketChatServer getInstance() {
            return chatServer;
        }
    }
}
