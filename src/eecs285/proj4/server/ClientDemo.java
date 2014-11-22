package eecs285.proj4.server;

import eecs285.proj4.ImageProcessorGUI;
import eecs285.proj4.server.ClientSideSocket;

public class ClientDemo
{
   public ClientSideSocket socket;
   
   public static void main(String [] args)
   {
      ClientSideSocket theClient = new ClientSideSocket("127.0.0.1", 45000);
      String recvdStr;
      theClient.start();
      
System.out.println("asdf");      
      ImageProcessorGUI ip = new ImageProcessorGUI();
      ip.setTitle("Client Side");
      ip.setVisible(true);
System.out.println("asdf");
   }
}