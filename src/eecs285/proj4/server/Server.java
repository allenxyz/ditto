package eecs285.proj4.server;

import eecs285.proj4.ImageProcessorGUI;
import eecs285.proj4.server.ServerSideSocket;

public class Server
{
   public static ServerSideSocket socket;
   
   public Server(String inIP) 
   {
      socket = new ServerSideSocket(inIP, 40000);
      socket.start();
      
//      try {
//      Thread.sleep(5000);
//      }
//      catch (Exception e)
//      {
//         System.out.println(2);
//      }
      
      
      
//      
//      while (true)
//      {
//         Server.socket.checkInput();
//      }
   }
}