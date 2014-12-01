package eecs285.proj4.server;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


public class MainFile {
   static JFrame win;
   public static int port = 45000;;
   
   public static void center(JFrame win) {

      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension d = win.getSize();
      int x = (screen.width - d.width) / 2;
      int y = (screen.height - d.height) / 2;
      win.setLocation(x, y);
   }
   public static void main (String [] args)
   {
      win = new JFrame("Welcome");
      win.setLayout(new FlowLayout());
      win.setVisible(true);
      win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      JButton hostButton = new JButton("Host");
      JButton joinButton = new JButton("Join");
      win.add(hostButton);
      win.add(joinButton);
      win.pack();
      
      center(win);
      
      hostButton.addMouseListener(new MouseAdapter() 
      {
         public void mouseClicked(MouseEvent e)
         {
            String ip = "";
            try {
               Server serverWindow = new Server(Inet4Address.getLocalHost().getHostAddress());
               win.dispose();
            }
            catch (Exception except) {
               System.out.println("exception occurred");
               except.printStackTrace();
            }
               
         }
      });
      
      joinButton.addMouseListener(new MouseAdapter()
      {
         public void mouseClicked(MouseEvent e)
         {
            try {
               String ip = "";
               Enumeration en = NetworkInterface.getNetworkInterfaces();
               NetworkInterface ni=(NetworkInterface) en.nextElement();
               Enumeration ee = ni.getInetAddresses();
               while(ee.hasMoreElements()) {
               InetAddress ia= (InetAddress) ee.nextElement();
                    ip = ia.getHostAddress();
                    System.out.println(ia.getHostAddress());
                }
//               ip = Inet4Address.getLocalHost().getHostAddress();
               Client clientWindow = new Client(ip);
               win.dispose();
            }
            catch (Exception except) {
               System.out.println("exception occurred");
               except.printStackTrace();
            }
         }
      });
   }
}
