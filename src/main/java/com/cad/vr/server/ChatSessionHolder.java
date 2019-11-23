package com.cad.vr.server;

import com.cad.vr.util.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Knox
 * @Date: 2019/11/20 10:57 AM
 * @Description: You Know
 * @Version 1.0
 */
@Data
@Slf4j
public class ChatSessionHolder extends Thread {
    private Socket socket;
    private BufferedInputStream bufferInput;
    private String sessionId;
    // max bytes from clients
    private static final int MAX_BYTES = 1024;

    public ChatSessionHolder(String sessionId, Socket socket) {
        this.socket = socket;
        this.sessionId = sessionId;
        try {
            this.bufferInput = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] msg = new byte[MAX_BYTES];
        try {
            while (!isInterrupted()) {
                log.info("wait receive...");
                int len = getBufferInput().read(msg);
                //TODO send to all websocket clients and socket clients
                String message = new String(msg, 0, len, StandardCharsets.UTF_8.name());
                log.info("read end , msg : " + message);
                // It's a position
                String[] positions = message.split(",");
                String send = positions[positions.length - 2] + "," + positions[positions.length - 1];
                ChatThreadManager.getInstance().broadcast(send.getBytes());
                WebSocketChatManager.broadcast(message);
                log.info(message);
            }
            ChatThreadManager.getInstance().remove(this.sessionId);
        } catch (IOException e) {
            e.printStackTrace();
            interrupt();
        }
    }
}
