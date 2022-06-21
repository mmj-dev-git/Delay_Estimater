package com.example.webrtcdelayestimator;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDP {
    private String TAG1= "MainClass";
    private int port ;
    private InetAddress inetAddress;
    private DatagramSocket socket;
    private int receivedPacketSize = 0;
    private String address;


    UDP(int port, String address)
    {
        this.port = port;
        try {
            this.inetAddress = InetAddress.getByName(address);
            socket = new DatagramSocket(port);
            Log.d(TAG1,"DatagramSocket Socket set");
//            socket.setTrafficClass(0x04);
//            solcket.setReuseAddress(true);
        }catch (Exception e)
        {
            Log.d(TAG1,"DatagramSocket Socket Error");
            e.printStackTrace();
        }

    }

    UDP(int port)
    {
        try {
            socket = new DatagramSocket(port);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] receiver(byte[] buffer)
    {
        try {

            DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(datagramPacket);
            receivedPacketSize = datagramPacket.getLength();
            address = datagramPacket.getAddress().toString();
            Log.d(TAG1,"Packet Receive :"+ address);
        }

        catch ( Exception e)
        {
            Log.d(TAG1,"receiver Error");
            e.printStackTrace();
        }
        return buffer;
    }


    public void send(byte[] data)
    {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, inetAddress, port);
            socket.send(datagramPacket);
            Log.d(TAG1,"Packet Sent" + Arrays.toString(data));
        }catch (Exception e)
        {
            Log.d(TAG1,"Sending Error"+e.getMessage());
            e.printStackTrace();
        }
    }


//    public void send(ProtoModelOuterClass.ProtoModel data)
//    {
//        try {
//            DatagramPacket datagramPacket = new DatagramPacket(data.toByteArray(), data.toByteArray().length, inetAddress, port);
//            socket.send(datagramPacket);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }


    public void setTimeOut(int timeOut)
    {
        try {
            socket.setSoTimeout(timeOut);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getReceivedPacketSize()
    {
        return receivedPacketSize;
    }

    public void closeSocket()
    {
        try {
            socket.close();
            Log.d(TAG1,"Socket Closed");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public String getAddress() {
        return address;
    }
}
