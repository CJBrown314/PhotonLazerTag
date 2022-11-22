import Entities.PlayerTaggedEvent;
import javafx.concurrent.Task;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server extends Task<Integer> {
    private final int BYTE_SIZE = 65535;
    private DatagramSocket SOCKET;

    public Server(int port){
        try {
            SOCKET = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println("Failed to create socket for server on port " +  port);
            e.printStackTrace();
        }
    }

    @Override
    public Integer call() {
        DatagramPacket receivePacket = null;
        byte[] receiveBuffer = new byte[BYTE_SIZE];
        System.out.println("Server started!");
        while(true) {
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

            try {
                SOCKET.receive(receivePacket);
            } catch (IOException e) {
                System.err.println("Failed to receive data");
                e.printStackTrace();
            }

            String received = data(receiveBuffer);

            if(!received.equals("bye")) {
                handleEvent(getEvent(received));
            } else {
                return 0;
            }

            receiveBuffer = new byte[BYTE_SIZE];
        }
    }

    public boolean handleEvent(PlayerTaggedEvent event) {
        //do something
        System.out.println(event);
        return true;
    }

    public static String data(byte[] a)
    {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }

        return ret.toString();
    }

    private PlayerTaggedEvent getEvent(String received) {
        String[] split = received.split(":");
        int transmitPlayer = Integer.parseInt(split[0]);
        int hitPlayer = Integer.parseInt(split[1]);
        return new PlayerTaggedEvent(transmitPlayer, hitPlayer);
    }
}