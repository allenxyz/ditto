package eecs285.proj4.server;

import static java.lang.System.out;

import java.awt.AWTEvent;
import java.awt.Color;
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
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

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
   final public boolean eventOccurred(String e) {
      boolean success = false;
      System.out.println("event Occurred: " + e);
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

   final public boolean loadOccurred(BufferedImage im) {
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
   
   final public boolean binOccurred(int numColors, Color[] colors) {
      boolean success = false;
      
      try {
         outData.writeInt(3);
         outData.writeInt(numColors);
         for (int i = 0; i < numColors; i++) {
            outData.writeInt(colors[i].hashCode());
            System.out.println("Color: " + colors[i].hashCode() + "; " + colors[i].toString());
//            outData.writeChar('\0');
         }
         success = true;
      }
      catch (Exception except) {
         System.out.println("Failed to send a binned Image");
         except.printStackTrace();
         System.exit(-1);
      }
      
      return success;
   }

   
   //this function is the interface to recieve things from inData
   // essentially just needs to check the first byte, then call the
   // corresponding function to do the appropriate job
   final public void receiveInput() {
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
      else if (read == 3)
         binRecieve();
   }
   
   

   final private void eventRecieve() {
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
//         win.getUtilityFilterComboBox().setSelectedItem("Sharpen");
         System.out.println("Sharpen was called");
         win.sharpen();
         System.out.println("Sharpen was ended");
      }
      else if (receivedString.trim().equals("None")) {
//         win.getUtilityFilterComboBox().setSelectedItem("None");
         win.noFilter();
      }
      else if (receivedString.trim().equals("Edge Detector")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Edge Detector");
         win.edgeDetector();
      }
      else if (receivedString.trim().equals("Invert")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Invert");
         win.invert();
      }
      else if (receivedString.trim().equals("Posterize")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Posterize");
         win.posterize();
      }
      else if (receivedString.trim().equals("Blue Invert")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Blue Invert");
         win.blueInvert();
      }
      else if (receivedString.trim().equals("Obama")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Obama");
         win.obama();
      }
      else if (receivedString.trim().equals("Morgana")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Morgana");
         win.colorScheme("Morgana");
      }
      else if (receivedString.trim().equals("Fire")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Fire");
         win.colorScheme("Fire");
      }
      else if (receivedString.trim().equals("Rainbow")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Rainbow");
         win.colorScheme("Rainbow");
      }
      else if (receivedString.trim().equals("Neutral")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Neutral");
         win.colorScheme("Neutral");
      }
      else if (receivedString.trim().equals("Coffee")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Coffee");
         win.colorScheme("Coffee");
      }
      else if (receivedString.trim().equals("Greyscale")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Greyscale");
         win.greyscale();
      }
      else if (receivedString.trim().equals("Vignette")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Vignette");
         win.vignette();
      }
      else if (receivedString.trim().equals("Circle Blue")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Circle Blur");
         win.circleBlur();
      }
      else if (receivedString.trim().equals("Tint")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Tint");
         win.tint();
      }
      else if (receivedString.trim().equals("Valencia")) {
//         win.getUtilityFilterComboBox().setSelectedItem("Valencia");
         win.valencia();
      }
      else if (receivedString.trim().equals("stack")) {
         JCheckBox temp = win.getStackCheckBox();
         if (temp.isSelected())
            temp.setSelected(false);
         else
            temp.setSelected(true);
      }
      else if (receivedString.trim().equals("Undo"))
         win.undo();
      else if (receivedString.trim().equals("Redo"))
         win.redo();
      else if (receivedString.trim().equals("Reset"))
         win.reset();
      else if (receivedString.trim().equals("Exit")) {
         JOptionPane.showMessageDialog(win,
               "Your friend disconnected", "Exit",
               JOptionPane.WARNING_MESSAGE);
         System.exit(0);
      }
         
      else
         System.out.println("Unrecognized command when recieving Event " );
      System.out.println(receivedString);
   }
   
   
   final private void loadRecieve() {
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
   
   final private void binRecieve() {
      System.out.println("Bin recieved!");
      int numCol = 0;
      Color[] colors = null;
      try {
         numCol = inData.readInt();
         colors = new Color[numCol];
         for (int i = 0; i < numCol; i++) {  
            colors[i] = new Color(inData.readInt());
            System.out.println(colors[i].toString());
         }
         
      }
      catch (Exception e) {
         System.out.println("Failed to read bin info");
         e.printStackTrace();
         System.exit(8);
      }
      win.colorBinTwoPointOh(win.getCurImage(), numCol, colors);
   }
   
   
   
   //check input only checks if there is any input in the input data stream
   //will try to recieve input if there is
   final public boolean checkInput() {
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

   final public void receiveImage() {
      BufferedImage img = null;
      try {
         img = ImageIO.read(ImageIO.createImageInputStream(inData));
         System.out.println("Image received!!!!");
      } catch (IOException ex) {
         System.out.println("Error recieving image: " + ex);
      }
      win.loadImage(img);
   }
   
   final public void sendImage(BufferedImage im) {
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
