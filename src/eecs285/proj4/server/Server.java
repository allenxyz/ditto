package eecs285.proj4.server;

import eecs285.proj4.ImageProcessorGUI;
import eecs285.proj4.server.ServerSideSocket;

public class Server
{
   public static ServerSideSocket socket;
   
   public Server(String inIP) 
   {
      socket = new ServerSideSocket(inIP, 45000);
      socket.start();
      
      
      ImageProcessingThread thread1 = new ImageProcessingThread(socket);
      thread1.start();
   }
}