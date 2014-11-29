package eecs285.proj4.server;

import static java.lang.System.out;
import eecs285.proj4.*;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

   public ServerSideSocket(String inIPAddr, int inPortNum) {
      super(inIPAddr, inPortNum);
      socket = null;
      win = new ImageProcessorGUI("Server Side", this);
      win.setVisible(true);
   }

   public void start() {
      ServerSocket serverSock;
      try {
         System.out.println("ipaddr: " + ipAddr + " portNum: " + portNum);
         serverSock = new ServerSocket(portNum);
         out.println("Waiting for client to connect...");
         socket = serverSock.accept();
         outData = new DataOutputStream(socket.getOutputStream());
         inData = new DataInputStream(socket.getInputStream());
         out.println("Client connection accepted");
      } catch (IOException ioe) {
         ioe.printStackTrace();
         System.out.println("ERROR: Caught exception starting server");
         System.exit(7);
      }
   }
}
