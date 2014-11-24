package eecs285.proj4.server;

import eecs285.proj4.ImageProcessorGUI;

public class ImageProcessingThread extends Thread {
   private ImageProcessingSocket socket;
   
   public ImageProcessingThread(ImageProcessingSocket inSocket)
   {
      socket = inSocket;
   }
   
   
   public void run()
   {
      while (true) 
         socket.checkInput();
   }
   
   
   
}
