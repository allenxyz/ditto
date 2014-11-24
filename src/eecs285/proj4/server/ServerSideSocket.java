package eecs285.proj4.server;


import static java.lang.System.out;
import eecs285.proj4.*;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.imageio.ImageIO;



public class ServerSideSocket extends ImageProcessingSocket {
   
   private Socket socket;
   private DataOutputStream outData;
   private DataInputStream inData;
   private ImageProcessorGUI win;
   
   
   public ServerSideSocket(String inIPAddr, int inPortNum)
   {
      super(inIPAddr, inPortNum);
      socket = null;
      outData = null;
      inData = null;
      win = new ImageProcessorGUI("Server Side", this);
      win.setVisible(true);
   }
   
   public void start()
   {
      ServerSocket serverSock;
      try
      {
System.out.println("ipaddr: " + ipAddr + " portNum: " + portNum);
         serverSock = new ServerSocket(portNum);
         out.println("Waiting for client to connect...");
         socket = serverSock.accept();
         outData = new DataOutputStream(socket.getOutputStream());
         inData = new DataInputStream(socket.getInputStream());
         out.println("Client connection accepted");
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
         System.out.println("ERROR: Caught exception starting server");
         System.exit(7);
      }
      
   }
   
   
   //these are the functions that will write to the outData
   //if the first byte is :
   // 1 - it is an event that occured
   // 2 - a load image occured
   public boolean eventOccurred(String e) 
   {  
      boolean success = false;
      try
      {
         outData.writeByte(1);
         outData.writeBytes(e.toString());
         outData.writeByte(0); //send 0 to signal the end of the string
         success = true;
      }
      catch (IOException except)
      {
         System.out.println("Caught IOException Writing To Client side!");
         System.exit(-1);
      }
      return success;
   }
   
   public boolean loadOccurred(BufferedImage im) 
   {
      boolean success = false;
      if (im == null)
         System.out.println("well thats embarassing..");
      try 
      {
         outData.writeByte(2);
         ImageIO.write((RenderedImage) im, "JPG", outData);
         success = true;
      }
      catch (Exception except) 
      {
         System.out.println("Filed to load image TO the Server side");
         System.exit(-1);
      }
      return success;
   }

   
   //this function is the interface to recieve things from inData
   // essentially just needs to check the first byte, then call the 
   // corresponding function to do the appropriate job
   public void receiveInput() 
   {
      Byte recByte = null;
      try 
      {
         recByte = inData.readByte();
      }
      catch (Exception e)
      {
         System.out.println("Failed to read the first Byte of input data");
      }
      
      if ((int)recByte == 1)
         eventRecieve();
      else if ((int)recByte == 2)
         loadRecieve();
   }
   
   private void eventRecieve() 
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
         {
            byteAry[ind] = byteVec.elementAt(ind).byteValue();
         }
         receivedString = new String(byteAry);
      }
      catch (IOException ioe)
      {
         out.println("ERROR: receiving string from socket");
         System.exit(8);
      }
      
      if (receivedString == "Sharpen")
         win.sharpen();
      else if (receivedString == "None")
         win.noFilter();
      else if (receivedString == "Edge Detector")
         win.edgeDetector();
      else if (receivedString == "Invert")
         win.invert();
      else if (receivedString == "Posterize")
         win.posterize();
      else if (receivedString == "Blue Invert")
         win.blueInvert();
      else
         System.out.println("Unrecognized command when recieving Event");
   }
      
      
   public void loadRecieve() {
System.out.println("Load recieved!");      
      BufferedImage img = null;
      try {
          img = ImageIO.read(ImageIO.createImageInputStream(inData));
          System.out.println("Image received!!!!");
      } catch (IOException ex) {
          System.out.println("Error reading loadImage datainput" + ex);
      }
      
      win.loadImage(img);
      win.repaint();
  }
   
   
   public boolean checkInput()
   {
      try {
         if (inData.available() != 0)
         {
            receiveInput();
            return true;
         }
         
      }
      catch (Exception e)
      {
         System.out.println("ClientSocket Error: checkInput");
      }
      return false;
   }
   
   public boolean sendInfo(BufferedImage im) 
   {
      boolean success = false;
      if (im == null)
         System.out.println("well thats embarassing..");
      try 
      {
         outData.writeByte(3);
         ImageIO.write((RenderedImage) im, "JPG", outData);
         success = true;
      }
      catch (Exception except) 
      {
         System.out.println("Filed to load image TO the Server side");
         System.exit(-1);
      }
      return success;
      
      
   }
}
   
