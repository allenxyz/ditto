package eecs285.proj4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.lang.Math;

public class stickers{
  private String path;
  private Color[][] col;
  private BufferedImage mBufferedImage;
  
  stickers(String p){
    path = p;
    Image grabimage = Toolkit.getDefaultToolkit().getImage(p);

    // Make a BufferedImage from the Image.
    mBufferedImage = new BufferedImage(grabimage.getWidth(null),
        grabimage.getHeight(null), BufferedImage.TYPE_INT_RGB);
    
    col = toRGB(mBufferedImage);
  }
  
  stickers(BufferedImage bi){
    path = null;
    mBufferedImage = bi;
    col = toRGB(bi);
  }
  
  
  public String getPath(){
    return path;
  }
  
  public BufferedImage getBI(){
    return mBufferedImage;
  }
  
  public Color[][] col(){
    return col;
  }
  
  void addSticker(stickers s, int x, int y){
    int w = Math.max(s.getBI().getWidth(), this.getBI().getWidth());
    int h = Math.max(s.getBI().getHeight(), this.getBI().getHeight());
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(s.getBI(), x, y, null);
    g.drawImage(this.getBI(), 0, 0, null);
  }
  
  public static Color[][] toRGB(BufferedImage im) {
    int width = im.getWidth();
    int height = im.getHeight();
    System.out.println(height + " " + width);
    Color[][] retval = new Color[height][width];
    System.out.println(retval.length + " " + retval[0].length);
    for (int row = 0; row < height; row++) {
       for (int col = 0; col < width; col++) {
          retval[row][col] = new Color(im.getRGB(col,row));
       }
    }
    return retval;
 }
   
  

  
  
  
  
  
  
  
}