package eecs285.proj4.server;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import eecs285.proj4.ImageProcessorGUI;
import eecs285.proj4.server.ClientSideSocket;

public class Client
{
   public static ClientSideSocket socket;
   
   public Client(String inIP)
   {
      socket = new ClientSideSocket(inIP, 45000);
      socket.start();
      
      ImageProcessingThread thread1 = new ImageProcessingThread(socket);
      thread1.start();
   }
}