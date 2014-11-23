package eecs285.proj4.server;

import eecs285.proj4.ImageProcessorGUI;
import eecs285.proj4.server.ClientSideSocket;

public class Client
{
   public static ClientSideSocket socket;
   
   public Client(String inIP)
   {
      socket = new ClientSideSocket(inIP, 40000);
      socket.start();
      while (true)
      {
         System.out.println(1);
         Client.socket.checkInput();
      }
   }
}