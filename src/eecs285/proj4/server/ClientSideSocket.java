package eecs285.proj4.server;

import static java.lang.System.out;
import eecs285.proj4.*;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.nio.ByteBuffer;

public class ClientSideSocket extends ImageProcessingSocket {
   private Socket socket;

   public ClientSideSocket(String inIPAddr, int inPortNum) {
      super(inIPAddr, inPortNum);
      socket = null;
      win = new ImageProcessorGUI("Client Side", this);
      win.setVisible(true);
   }

   public void start() {
      try {
         System.out.println("ipaddr: " + ipAddr + " portNum: " + portNum);
         socket = new Socket(ipAddr, portNum);
         outData = new DataOutputStream(socket.getOutputStream());
         inData = new DataInputStream(socket.getInputStream());
      } catch (IOException ioe) {
         System.out.println("ERROR: Unable to connect - "
               + "is the server running?");
         System.exit(10);
      }
   }
}