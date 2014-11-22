package eecs285.proj4.server;

import eecs285.proj4.ImageProcessorGUI;
import eecs285.proj4.server.ServerSideSocket;

public class ServerDemo
{
   ServerSideSocket socket;
   public static void main(String args[])
   {
      ServerSideSocket theServer = new ServerSideSocket("127.0.0.1", 45000);
      String recvdStr;
      theServer.start();
      

System.out.println("asdf");         
      ImageProcessorGUI ip = new ImageProcessorGUI();
      ip.setTitle("ServerSide");
      ip.setVisible(true);

System.out.println("asdf");   
      
   }
}