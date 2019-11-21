package com.cad.vr.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author: Knox
 * @Date: 2019/11/20 8:20 PM
 * @Description: You Know
 * @Version 1.0
 */
public class ReceiveTest {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 5555);
        InputStream inputStream = socket.getInputStream();

        byte[] msg = new byte[1024];
        while (true) {
            int read = inputStream.read(msg);
            System.out.println(new String(msg, 0, read, StandardCharsets.UTF_8.name()));
        }
    }
}
