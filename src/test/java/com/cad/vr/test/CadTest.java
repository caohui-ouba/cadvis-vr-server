package com.cad.vr.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author: Knox
 * @Date: 2019/11/20 7:57 PM
 * @Description: You Know
 * @Version 1.0
 */
public class CadTest {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 5555);
        OutputStream outputStream = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("please enter:");
            String str = scanner.nextLine();
            outputStream.write(str.getBytes());
            outputStream.flush();
        }
    }
}
