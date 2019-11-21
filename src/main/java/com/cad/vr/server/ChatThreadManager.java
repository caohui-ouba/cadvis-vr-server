package com.cad.vr.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Knox
 * @Date: 2019/11/20 10:56 AM
 * @Description: You Know
 * @Version 1.0
 */
@Slf4j
public class ChatThreadManager {

    private Map<String, ChatSessionHolder> sessions = new ConcurrentHashMap<>(1);
    private AtomicInteger cnt = new AtomicInteger(0);

    private ChatThreadManager() {
    }

    enum Instance {
        INSTANCE;
        private ChatThreadManager manager;

        Instance() {
            this.manager = new ChatThreadManager();
        }

        public ChatThreadManager getInstance() {
            return manager;
        }
    }

    public static ChatThreadManager getInstance() {
        return Instance.INSTANCE.getInstance();
    }

    public Map<String, ChatSessionHolder> sessions() {
        return this.sessions;
    }

    public int getCnt() {
        return cnt.get();
    }

    public synchronized int put(ChatSessionHolder session) {
        sessions.put(String.valueOf(cnt.getAndIncrement()), session);
        return cnt.get();
    }

    public synchronized void remove(String id) {
        sessions.remove(id);
    }

    public synchronized void broadcast(byte[] msg) {
        sessions.forEach((k, v) -> {
            try {
                OutputStream os = v.getSocket().getOutputStream();
                os.write(msg);
            } catch (IOException e) {
                log.error(String.format("the client '%s' send msg failed!", k));
                e.printStackTrace();
            }
        });
    }
}
