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

public class ClientSideSocket extends ImageProcessingSocket {
   private Socket socket;
   private DataOutputStream outData;
   private DataInputStream inData;
   public ImageProcessorGUI win;
   public ClientSideSocket(String inIPAddr, int inPortNum)
   {
      super(inIPAddr, inPortNum);
      outData = null;
      inData = null;
      win = new ImageProcessorGUI("Client Side", this);
      win.setVisible(true);
   }
   
   public void start()
   {
      try
      {
System.out.println("ipaddr: " + ipAddr + " portNum: " + portNum);         
         socket = new Socket(ipAddr, portNum);
         outData = new DataOutputStream(socket.getOutputStream());
         inData = new DataInputStream(socket.getInputStream());
      }
      catch (IOException ioe)
      {
         System.out.println("ERROR: Unable to connect - " +
         "is the server running?");
         System.exit(10);
      }
   }
   
   
   public boolean eventOccurred(String e) 
   {
      boolean success = false;
      try
      {
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
      try 
      {
         ImageIO.write((RenderedImage) im,"JPG",outData);
         success = true;
      }
      catch (Exception except) 
      {
         System.out.println("Filed to load image TO the Server side");
         System.exit(-1);
      }
      return success;
   }

   
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
      else if ((int) recByte == 2)
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
      
      
   private void loadRecieve() {
System.out.println("Load recieved!");      
      BufferedImage img = null;
      try {
          img = ImageIO.read(ImageIO.createImageInputStream(inData));
          System.out.println("Image received!!!!");
      } catch (IOException ex) {
          System.out.println("Error al abrir el socket" + ex);
      }
      
//      JFrame frame = new JFrame();
//      frame.getContentPane().setLayout(new FlowLayout());
//      frame.getContentPane().add(new JLabel(new ImageIcon(img)));
//      frame.pack();
//      frame.setVisible(true);
      
      
      
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
   
   
   public void receiveInfo() {
      BufferedImage img = null;
      try {
          img = ImageIO.read(ImageIO.createImageInputStream(inData));
          System.out.println("Image received!!!!");
      } catch (IOException ex) {
          System.out.println("Error al abrir el socket" + ex);
      }
      win.loadImage(img);
      win.repaint();
   }
   
   public void sendInfo(BufferedImage im) 
   {
      if (im == null)
         System.out.println("well thats embarassing..");
      try 
      {
         outData.writeByte(3);
         ImageIO.write((RenderedImage) im, "JPG", outData);
      }
      catch (Exception except) 
      {
         System.out.println("Filed to load image TO the Server side");
         System.exit(-1);
      }
   }

}