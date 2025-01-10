package com.example.client;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.example.proto.SubscriberOuterClass;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hangi sunucuya bağlanmak istiyorsunuz?");
        System.out.println("1. Server1 (Port: 5011)");
        System.out.println("2. Server2 (Port: 5012)");
        System.out.println("3. Server3 (Port: 5013)");
        System.out.print("Seçim: ");
        int serverChoice = scanner.nextInt();

        int port = 0;
        switch (serverChoice) {
            case 1:
                port = 5011;
                break;
            case 2:
                port = 5012;
                break;
            case 3:
                port = 5013;
                break;
            default:
                System.out.println("Geçersiz seçim. Çıkılıyor.");
                System.exit(0);
        }

        System.out.println("Abone işlemi seçin:");
        System.out.println("1. Abone Ol (SUBS)");
        System.out.println("2. Abone Sil (DEL)");
        System.out.println("3. Abone Güncelle (UPDT)");
        System.out.print("Seçim: ");
        int actionChoice = scanner.nextInt();

        SubscriberOuterClass.DemandType demandType = null;
        switch (actionChoice) {
            case 1:
                demandType = SubscriberOuterClass.DemandType.SUBS;
                break;
            case 2:
                demandType = SubscriberOuterClass.DemandType.DEL;
                break;
            case 3:
                demandType = SubscriberOuterClass.DemandType.UPDT;
                break;
            default:
                System.out.println("Geçersiz seçim. Çıkılıyor.");
                System.exit(0);
        }

        try {
            Socket socket = new Socket("localhost", port);
            OutputStream output = socket.getOutputStream();

            SubscriberOuterClass.Subscriber subscriber = SubscriberOuterClass.Subscriber.newBuilder()
                    .setID(1)
                    .setNameSurname("Ali Veli")
                    .setStartDate(System.currentTimeMillis() / 1000)
                    .setLastAccessed(System.currentTimeMillis() / 1000)
                    .setIsOnline(true)
                    .setDemand(demandType)
                    .build();

            subscriber.writeTo(output);
            output.close();
            socket.close();
            System.out.println("İşlem başarıyla gönderildi.");
        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }
}
