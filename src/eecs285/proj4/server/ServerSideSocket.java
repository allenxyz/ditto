package eecs285.proj4.server;


import static java.lang.System.out;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;



public class ServerSideSocket {
   
   private String ipAddr;
   private int portNum;
   private Socket socket;
   private DataOutputStream outData;
   private DataInputStream inData;
   
   
   public ServerSideSocket(String inIPAddr, int inPortNum)
   {
      ipAddr = inIPAddr;
      portNum = inPortNum;
      inData = null;
      outData = null;
      socket = null;
   }
   
   public void start()
   {
      ServerSocket serverSock;
      try
      {
         serverSock = new ServerSocket(portNum);
         System.out.println("Waiting for client to connect...");
         socket = serverSock.accept();
         outData = new DataOutputStream(socket.getOutputStream());
         inData = new DataInputStream(socket.getInputStream());
         System.out.println("Client connection accepted");
      }
      catch (IOException ioe)
      {
         System.out.println("ERROR: Caught exception starting server");
         System.exit(7);
      }
   }
   
   
   public boolean sendString(String strToSend)
   {
      boolean success = false;
      try
      {
         outData.writeBytes(strToSend);
         outData.writeByte(0); //send 0 to signal the end of the string
         success = true;
      }
      catch (IOException e)
      {
         System.out.println("Caught IOException Writing To Socket Stream!");
         System.exit(-1);
      }
      return (success);
   }
   
   public String recvString()
   {
      Vector< Byte > byteVec = new Vector< Byte >();
      byte [] byteAry;
      byte recByte;
      String receivedString = "";
      try
      {
         recByte = inData.readByte();
         while (recByte != 0)
         {
            byteVec.add(recByte);
            recByte = inData.readByte();
         }
         byteAry = new byte[byteVec.size()];
         for (int ind = 0; ind < byteVec.size(); ind++)
            byteAry[ind] = byteVec.elementAt(ind).byteValue();
         receivedString = new String(byteAry);
      }
      catch (IOException ioe)
      {
         out.println("ERROR: receiving string from socket");
         System.exit(8);
      }
      return (receivedString);
   }
}
