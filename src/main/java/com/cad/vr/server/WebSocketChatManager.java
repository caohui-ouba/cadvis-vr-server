package com.cad.vr.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Knox
 * @Date: 2019/11/20 7:09 PM
 * @Description: You Know
 * @Version 1.0
 */
@ServerEndpoint("/cadvis")
@RestController
@Slf4j
public class WebSocketChatManager {
    private static final AtomicInteger cnt = new AtomicInteger(0);

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>(1);

    @OnOpen
    public void onOpen(Session session) throws Exception {
        log.info("coming a websocket connect !  id:" + session.getId());
        sessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("receive a msg :" + message + " from " + session.getId());
        broadcast(message);
        ChatThreadManager.getInstance().broadcast(message.getBytes());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Error while websocket. ", error);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("session " + session.getId() + " close...");
        sessions.remove(session.getId());
    }

    public static void broadcast(String msg) {
        log.info("on broadcast , size :" + sessions.size());
        sessions.forEach((k, v) -> {
            try {
                v.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
