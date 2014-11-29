package eecs285.proj4.server;

import static java.lang.System.out;

import java.awt.AWTEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import eecs285.proj4.ImageProcessorGUI;

public abstract class ImageProcessingSocket {
   protected String ipAddr;
   protected int portNum;
   protected ImageProcessorGUI win;

   protected DataOutputStream outData;
   protected DataInputStream inData;

   public ImageProcessingSocket(String inIPAddr, int inPortNum) {
      ipAddr = inIPAddr;
      portNum = inPortNum;
      outData = null;
      inData = null;
   }

   public abstract void start();

   // these are the functions that will write to the outData
   // if the first byte is :
   // 1 - it is an event that occured
   // 2 - a load image occured
   public boolean eventOccurred(String e) {
      boolean success = false;
      try {
         outData.writeInt(1);
         outData.writeChars(e);
         outData.writeChars("0"); // send 0 to signal the end of the string
         success = true;
      } catch (IOException except) {
         System.out.println("Caught IOException when sending even!");
         System.exit(-1);
      }
      return success;
   }

   public boolean loadOccurred(BufferedImage im) {
      boolean success = false;
      if (im == null) {
         System.out.println("tried to load an empty image...");
      }
      try {
         outData.writeInt(2);
         // convert buffered image to byte array
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ImageIO.write(im, "jpg", baos);
         byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();
         outData.write(size);
         outData.write(baos.toByteArray());
         success = true;
      } catch (Exception except) {
         System.out.println("Failed to load an image");
         System.exit(-1);
      }
      return success;
   }

   
// this function is the interface to recieve things from inData
   // essentially just needs to check the first byte, then call the
   // corresponding function to do the appropriate job
   public void receiveInput() {
      int read = 0;
      try {
         read = inData.readInt();
      } catch (Exception e) {
         System.out.println("Failed to read the input quantifier");
      }
      if (read == 1)
         eventRecieve();
      else if (read == 2)
         loadRecieve();
   }
   
   
   



   private void eventRecieve() {
      char recChar;
      String receivedString = "";

      try
      {
         recChar = inData.readChar();
         while (recChar != '0')
         {
            receivedString += recChar;
            recChar = inData.readChar();
         }
      }catch (IOException ioe) {
         out.println("ERROR: receiving string from socket");
         System.exit(8);
      }
      System.out.println("abcd");
      System.out.println("Received string is: " + receivedString);
      if (receivedString.trim().equals("Sharpen")) {
         System.out.println("hey it worked!");
         win.getUtilityFilterComboBox().setSelectedItem("Sharpen");
      }
      else if (receivedString.trim().equals("None"))
         win.getUtilityFilterComboBox().setSelectedItem("None");
      else if (receivedString.trim().equals("Edge Detector"))
         win.getUtilityFilterComboBox().setSelectedItem("Edge Detector");
      else if (receivedString.trim().equals("Invert"))
         win.getUtilityFilterComboBox().setSelectedItem("Invert");
      else if (receivedString.trim().equals("Posterize"))
         win.getUtilityFilterComboBox().setSelectedItem("Posterize");
      else if (receivedString.trim().equals("Blue Invert"))
         win.getUtilityFilterComboBox().setSelectedItem("Blue Invert");
      else if (receivedString.trim().equals("Obama"))
         win.getUtilityFilterComboBox().setSelectedItem("Obama");
      else if (receivedString.trim().equals("Morgana"))
         win.getUtilityFilterComboBox().setSelectedItem("Morgana");
      else if (receivedString.trim().equals("Fire"))
         win.getUtilityFilterComboBox().setSelectedItem("Fire");
      else if (receivedString.trim().equals("Rainbow"))
         win.getUtilityFilterComboBox().setSelectedItem("Rainbow");
      else if (receivedString.trim().equals("Neutral"))
         win.getUtilityFilterComboBox().setSelectedItem("Neutral");
      else if (receivedString.trim().equals("Coffee"))
         win.getUtilityFilterComboBox().setSelectedItem("Coffee");
      else if (receivedString.trim().equals("Greyscale"))
         win.getUtilityFilterComboBox().setSelectedItem("Greyscale");
      else if (receivedString.trim().equals("Vignette"))
         win.getUtilityFilterComboBox().setSelectedItem("Vignette");
      else if (receivedString.trim().equals("Circle Blue"))
         win.getUtilityFilterComboBox().setSelectedItem("Circle Blur");
      else if (receivedString.trim().equals("Tint"))
         win.getUtilityFilterComboBox().setSelectedItem("Tint");
      else if (receivedString.trim().equals("Valencia"))
         win.getUtilityFilterComboBox().setSelectedItem("Valencia");
      else
         System.out.println("Unrecognized command when recieving Event " );
      System.out.println(receivedString);
   }
   
   
   private void loadRecieve() {
      System.out.println("Load recieved!");
      BufferedImage img = null;
      try {
         byte[] sizeAr = new byte[4];
         inData.read(sizeAr);
         int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
         byte[] imageAr = new byte[size];
         inData.read(imageAr);
         img = ImageIO.read(new ByteArrayInputStream(imageAr));
         System.out.println("Image received!!!!");
      } catch (Exception ex) {
         System.out.println("Error al abrir el socket" + ex);
      }
      System.out.println("img loaded");
      win.loadImage(img);
   }
   
   //check input only checks if there is any input in the input data stream
   //will try to recieve input if there is
   public boolean checkInput() {
      try {
         if (inData.available() != 0) {
            receiveInput();
            return true;
         }
      } catch (Exception e) {
         System.out.println("Socket Error: checkInput");
      }
      return false;
   }

   public void receiveImage() {
      BufferedImage img = null;
      try {
         img = ImageIO.read(ImageIO.createImageInputStream(inData));
         System.out.println("Image received!!!!");
      } catch (IOException ex) {
         System.out.println("Error recieving image: " + ex);
      }
      win.loadImage(img);
   }

   public void sendImage(BufferedImage im) {
      if (im == null)
         System.out.println("Sending an empty image....");
      try {
         outData.writeByte(3);
         ImageIO.write((RenderedImage) im, "JPG", outData);
      } catch (Exception except) {
         System.out.println("Filed to load image TO the Server side");
         System.exit(-1);
      }
   }

}
