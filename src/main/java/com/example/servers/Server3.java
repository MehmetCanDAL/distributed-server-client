package com.example.servers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import com.example.proto.ConfigurationOuterClass;
import com.example.proto.SubscriberOuterClass;

public class Server3 {
    private static final int ADMIN_PORT = 5003; 
    private static final int CLIENT_PORT = 5013; 
    private static final int SERVER_PORT = 5023; 
    private static final List<Integer> OTHER_SERVER_PORTS = Arrays.asList(5021, 5022); 
    private static boolean isClientPortActive = false;

    public static void main(String[] args) {
        new Thread(Server3::listenToAdmin).start(); // Admin için dinleyici başlat
    }

    private static void listenToAdmin() {
        try (ServerSocket serverSocket = new ServerSocket(ADMIN_PORT)) {
            System.out.println("Server3 Admin için dinliyor (Port: " + ADMIN_PORT + ")");
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    InputStream input = socket.getInputStream();
                    ConfigurationOuterClass.Configuration config = ConfigurationOuterClass.Configuration.parseFrom(input);

                    if (config.getMethod() == ConfigurationOuterClass.MethodType.STRT && !isClientPortActive) {
                        System.out.println("Server3 başlatıldı!");
                        isClientPortActive = true;
                        new Thread(Server3::listenToClients).start(); // Client portunu dinlemeye başla
                        new Thread(Server3::listenToServers).start(); // Sunucular için portu dinlemeye başla
                    } else if (config.getMethod() == ConfigurationOuterClass.MethodType.STOP && isClientPortActive) {
                        System.out.println("Server3 durduruldu!");
                        isClientPortActive = false;
                        break; // Admin portunu dinlemeye devam eder
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listenToClients() {
        try (ServerSocket serverSocket = new ServerSocket(CLIENT_PORT)) {
            System.out.println("Server3 Client için dinliyor (Port: " + CLIENT_PORT + ")");
            while (isClientPortActive) {
                try (Socket socket = serverSocket.accept()) {
                    InputStream input = socket.getInputStream();
                    SubscriberOuterClass.Subscriber subscriber = SubscriberOuterClass.Subscriber.parseFrom(input);
                    System.out.println("Yeni Abone: " + subscriber);

                    // Abone bilgisini diğer sunuculara gönder
                    sendSubscriberToOtherServers(subscriber);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listenToServers() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server3 Diğer sunucular için dinliyor (Port: " + SERVER_PORT + ")");
            while (isClientPortActive) {
                try (Socket socket = serverSocket.accept()) {
                    InputStream input = socket.getInputStream();
                    SubscriberOuterClass.Subscriber subscriber = SubscriberOuterClass.Subscriber.parseFrom(input);
                    System.out.println("Diğer sunucudan abone bilgisi alındı: " + subscriber);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendSubscriberToOtherServers(SubscriberOuterClass.Subscriber subscriber) {
        for (int port : OTHER_SERVER_PORTS) {
            try (Socket socket = new Socket("localhost", port)) {
                OutputStream output = socket.getOutputStream();
                subscriber.writeTo(output);
                output.close();
                System.out.println("Abone bilgisi diğer sunucuya gönderildi (Port: " + port + ")");
            } catch (IOException e) {
                System.out.println("Hata: Diğer sunucuya bağlanılamadı (Port: " + port + ").");
            }
        }
    }
}
