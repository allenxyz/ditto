package eecs285.proj4.pixelTypes;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class rgb{
  private int r;
  private int g;
  private int b;

  public rgb(){
    r = 0;
    g = 0;
    b = 0;
  }
  public rgb(int pixelColor) 
  {
     r = (int)pixelColor >> 16 & 0xfff;
     g = (int)pixelColor >> 8 & 0xfff;
     b = (int)pixelColor & 0xfff;
  }
  public rgb(int rIn, int gIn, int bIn){
    r = rIn;
    g = gIn;
    b = bIn;
  }
  
  public void changeR(int change){
    r = change;
  }
  
  public void changeG(int change){
    g = change;
  }
  
  public void changeB(int change){
    b = change;
  }
  
  public int getR(){
    return r;
  }
  
  public int getG(){
    return g;
  }
  
  public int getB(){
    return b;
  }
  
  public static rgb[][] toRGB(BufferedImage im) {
     int width = im.getWidth();
     int height = im.getHeight();
     rgb[][] retval = new rgb[height][width];
     
     for (int row = 0; row < Math.min(width, height); row++) {
        for (int col = 0; col < Math.min(width, height); col++) {
//           System.out.println(row + "/" + im.getHeight() + " " + col + "/" + im.getWidth());
           retval[row][col] = new rgb(im.getRGB(row, col));
        }
     }
     return retval;
  }
  
//  public static BufferedImage toImage(rgb[][] pixelVal)
//  {
////     
////     BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
////     WritableRaster raster = (WritableRaster) image.getData();
////     raster.setPixels(0,0,width,height,pixels);
////     return image;
////     return new BufferedImage();
//  }
}