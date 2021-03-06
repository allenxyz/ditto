package eecs285.proj4.server;


import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import static java.lang.System.out;
import java.awt.AWTEvent;



public class ClientServerSocket
{
   private String ipAddr;
   private int portNum;
   private Socket socket;
   private DataOutputStream outData;
   private DataInputStream inData;
   
   
   public ClientServerSocket(String inIPAddr, int inPortNum)
   {
      ipAddr = inIPAddr;
      portNum = inPortNum;
      inData = null;
      outData = null;
      socket = null;
   }
   
   public void startClient()
   {
      try
      {
         socket = new Socket(ipAddr, portNum);
         outData = new DataOutputStream(socket.getOutputStream());
         inData = new DataInputStream(socket.getInputStream());
      }
      catch (IOException ioe)
      {
         out.println("ERROR: Unable to connect - " +
         "is the server running?");
         System.exit(10);
      }
   }
   
   public void startServer()
   {
      ServerSocket serverSock;
      try
      {
         serverSock = new ServerSocket(portNum);
         out.println("Waiting for client to connect...");
         socket = serverSock.accept();
         outData = new DataOutputStream(socket.getOutputStream());
         inData = new DataInputStream(socket.getInputStream());
         out.println("Client connection accepted");
      }
      catch (IOException ioe)
      {
         out.println("ERROR: Caught exception starting server");
         System.exit(7);
      }
   }
   
   
   static public void eventHappened(AWTEvent event, InetAddress IP)
   {
      System.out.println("Something happenedaa: " + event.toString());
      System.out.println("alskdjfsad");
      System.out.println("IP Address: " + IP.toString());
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
