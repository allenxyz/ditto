package eecs285.proj4.server;

import java.awt.AWTEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public abstract class ImageProcessingSocket {
   protected String ipAddr;
   protected int portNum;
   
   public ImageProcessingSocket(String inIPAddr, int inPortNum)
   {
      ipAddr = inIPAddr;
      portNum = inPortNum;
   }
      
   public abstract void start();
   public abstract boolean eventOccurred(String e);
   public abstract boolean loadOccurred(BufferedImage im);
   public abstract void receiveInput();
   
   
   
   

}
