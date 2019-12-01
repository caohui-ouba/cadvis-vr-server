package com.cad.vr.server;

import com.cad.vr.bean.ClientEvent;
import com.cad.vr.util.SpringUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;

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
    private static final Gson gson = new Gson();

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
                String message = new String(msg, 0, len, StandardCharsets.UTF_8.name());
                log.info("read end , msg : " + message);
                // It's a  id,type,position.x,position.y
                String[] positions = message.split(",");
                int pLen = positions.length;
                ClientEvent event = new ClientEvent(sessionId, positions[pLen - 3], positions[pLen - 2], positions[pLen - 1]);
                String json  = gson.toJson(event);
                ChatThreadManager.getInstance().broadcast(json.getBytes());
                WebSocketChatManager.broadcast(json);
                log.info(json);
            }
            ChatThreadManager.getInstance().remove(this.sessionId);
        } catch (IOException e) {
            log.error(String.format("client '%s' is disconnected !", this.sessionId));
            interrupt();
            ChatThreadManager.getInstance().remove(this.sessionId);
        }
    }
}
