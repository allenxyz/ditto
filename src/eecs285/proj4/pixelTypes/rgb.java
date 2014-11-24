package eecs285.proj4.pixelTypes;

import java.awt.Dimension;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.io.File;

public class rgb {
   private int r;
   private int g;
   private int b;

   public rgb() {
      r = 0;
      g = 0;
      b = 0;
   }

   public rgb(int pixelColor) {
      r = (int) pixelColor >> 16 & 0xff;
      g = (int) pixelColor >> 8 & 0xff;
      b = (int) pixelColor & 0xff;
   }

   public rgb(int rIn, int gIn, int bIn) {
      r = rIn;
      g = gIn;
      b = bIn;
   }

   public void changeR(int change) {
      r = change;
   }

   public void changeG(int change) {
      g = change;
   }

   public void changeB(int change) {
      b = change;
   }

   public int getR() {
      return r;
   }

   public int getG() {
      return g;
   }

   public int getB() {
      return b;
   }
   
   public String toString() {
      return ("R: " + r + " G: " + g + " B: " + b); 
   }

   public static rgb[][] toRGB(BufferedImage im) {
      int width = im.getWidth();
      int height = im.getHeight();
      System.out.println(height + " " + width);
      rgb[][] retval = new rgb[height][width];
      System.out.println(retval.length + " " + retval[0].length);
      for (int row = 0; row < height; row++) {
         for (int col = 0; col < width; col++) {
            retval[row][col] = new rgb(im.getRGB(col,row));
         }
      }
      return retval;
   }

   public static BufferedImage toImage(rgb[][] pixelVal) {
      int height = pixelVal.length;
      int width = pixelVal[0].length;
      BufferedImage image = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
      WritableRaster raster = image.getRaster();

      for (int i = 0; i < height; i += 1) {
         for (int j = 0; j < width; j += 1) {
            // System.out.println(i + "/" + height + " " + j + "/" + width);
            rgb argb = pixelVal[i][j];
            int[] pixels = { argb.getR(), argb.getG(), argb.getB() };
            raster.setPixel(j, i, pixels);
         }
      }
      return image;
   }

//   public static void main(String[] args) {
//      rgb[][] pixelVal = new rgb[20][15];
//
//      for (int i = 0; i < 20; i += 1) {
//         for (int j = 0; j < 15; j += 1) {
//            System.out.print(i * j / 2 + " ");
//            pixelVal[i][j] = new rgb(i * j, i * j / 2, i * j / 4);
//         }
//         System.out.println();
//      }
//
//      BufferedImage temp = rgb.toImage(pixelVal);
//
//      JFrame win = new JFrame();
//      ImageIcon img = new ImageIcon(temp);
//      JLabel add = new JLabel(img);
//      win.setSize(new Dimension(200, 200));
//      win.add(add);
//      win.setVisible(true);
//
//      System.out.println(temp.getHeight(win));
//
//      System.out.println("asdf");
//
//      JFileChooser chooser = new JFileChooser();
//      chooser.showOpenDialog(null);
//      File file = chooser.getSelectedFile();
//      BufferedImage image = null;
//      try {
//         image = ImageIO.read(file);
//      } catch (Exception e) {
//         System.out.println("ol nothing");
//      }
//
//      rgb[][] t = new rgb[image.getHeight()][image.getWidth()];
//      t = rgb.toRGB(image);
//      Image i = rgb.toImage(t);
//
//      JFrame win2 = new JFrame();
//      ImageIcon img2 = new ImageIcon(i);
//      JLabel add2 = new JLabel(img2);
//      win2.setSize(new Dimension(200, 200));
//      win2.add(add2);
//      win2.setVisible(true);
//
//   }
}