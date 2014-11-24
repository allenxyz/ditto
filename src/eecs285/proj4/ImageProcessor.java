package eecs285.proj4;


/**
 * ImageDicer provides a grab-bag of image processing operators.
 * This version is compliant with Java 1.2 Beta 4, Aug 1998.
 * Please refer to: <BR>
 * http://www.javaworld.com/javaworld/jw-09-1998/jw-09-media.html
 * <P>
 * @author Jonathan Knudsen <jonathan@oreilly.com>
 * @author Bill Day <bill.day@javaworld.com>
 * @version 1.0
 * @see java.awt.image.BufferedImage
 * @see java.awt.image.BufferedImageOp
 * @see java.awt.image.ConvolveOp
 * @see java.awt.image.LookupOp
 * @see java.awt.image.ThresholdOp
 * 
 * ---------
 * For the 285 project, we will use the code from the aforementioned source and work with it
 * All that code has been copy/pasted here. We will change around the gui stuff a lot, and we will change 
 * the functions given to fit our project
 * 
 **/

import java.awt.*;

import eecs285.proj4.pixelTypes.*;

import java.awt.color.ColorSpace;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class ImageProcessor extends Frame
{
  /**
   * kBanner holds the application title which is used in the window title.
   **/
  private static final String kBanner = "ImageDicer v1.0";
  
  public ImageProcessor()
  {
    // super(kBanner);
    createOps();
    // createUI();
    // loadImage(fileName);
    // setVisible(true);

  }

  /**
   * A Hashtable member variable holds the image processing operations, keyed by
   * their names.
   **/
  public static Hashtable mOps;
  
  /**
   * The createOps() method creates the image processing operations discussed in
   * the column.
   **/
  private void createOps()
  {
    // Create a brand new Hashtable to hold the operations.
    mOps = new Hashtable();

    // Blurring
    float ninth = 1.0f / 9.0f;
    float[] blurKernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth,
        ninth, ninth, };
    mOps.put("Blur", new ConvolveOp(new Kernel(3, 3, blurKernel)));

    // Edge detection
    float[] edgeKernel = { 0.0f, -1.0f, 0.0f, -1.0f, 4.0f, -1.0f, 0.0f, -1.0f,
        0.0f };
    mOps.put("Edge detector", new ConvolveOp(new Kernel(3, 3, edgeKernel)));

    // Sharpening
    float[] sharpKernel = { 0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f,
        0.0f };
    mOps.put("Sharpen", new ConvolveOp(new Kernel(3, 3, sharpKernel),
        ConvolveOp.EDGE_NO_OP, null));

    // Lookup table operations: posterizing and inversion.
    short[] posterize = new short[256];
    short[] invert = new short[256];
    short[] straight = new short[256];
    for( int i = 0; i < 256; i++ )
    {
      posterize[i] = (short) (i - (i % 32));
      invert[i] = (short) (255 - i);
      straight[i] = (short) i;
    }

    mOps.put("Posterize",
        new LookupOp(new ShortLookupTable(0, posterize), null));
    mOps.put("Invert", new LookupOp(new ShortLookupTable(0, invert), null));
    short[][] blueInvert = new short[][]{ straight, straight, invert };
    mOps.put("Invert blue", new LookupOp(new ShortLookupTable(0, blueInvert),
        null));

    // Thresholding
    mOps.put("Threshold 192", createThresholdOp(192, 0, 255));
    mOps.put("Threshold 128", createThresholdOp(128, 0, 255));
    mOps.put("Threshold 64", createThresholdOp(64, 0, 255));
  }


  /**
   * createThresholdOp() uses a LookupOp to simulate a thresholding operation.
   **/
  private BufferedImageOp createThresholdOp(int threshold, int minimum,
      int maximum)
  {
    short[] thresholdArray = new short[256];
    for( int i = 0; i < 256; i++ )
    {
      if( i < threshold )
        thresholdArray[i] = (short) minimum;
      else
        thresholdArray[i] = (short) maximum;
    }
    return new LookupOp(new ShortLookupTable(0, thresholdArray), null);
  }

//  /**
//   * Center this window in the user's desktop.
//   **/
//  private void center()
//  {
//    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
//    Dimension d = getSize();
//    int x = (screen.width - d.width) / 2;
//    int y = (screen.height - d.height) / 2;
//    setLocation(x, y);
//  }
  private void center()
  {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension d = getSize();
    int x = (screen.width - d.width) / 2;
    int y = (screen.height - d.height) / 2;
    setLocation(x, y);
  }

  /**
   * This member variable holds the currently displayed image.
   **/
  public static BufferedImage filterValencia(BufferedImage img) {
     rgb[][] rgbs = rgb.toRGB(img);
     
//     for (int i = 0 ; i < rgbs.length ; i += 1) 
//        for (int j = 0 ; j < rgbs[0].length ; j += 1)
//           System.out.println(rgbs[i][j].toString());
//
//     
     for (int i = 0 ; i < rgbs.length ; i += 1) 
        for (int j = 0 ; j < rgbs[0].length ; j += 1)
           filters.valencia(rgbs[i][j]);
     
//     for (int i = 0 ; i < rgbs.length ; i += 1) 
//        for (int j = 0 ; j < rgbs[0].length ; j += 1)
//           System.out.println(rgbs[i][j].toString());
//     
     return rgb.toImage(rgbs);
  }
  
  public static BufferedImage filterGreyscale(BufferedImage img) {
     rgb[][] rgbs = rgb.toRGB(img);

     for (int i = 0 ; i < rgbs.length ; i += 1) 
      for (int j = 0 ; j < rgbs[0].length ; j += 1)
         filters.greyscale(rgbs[i][j]);
     
     return rgb.toImage(rgbs);
  }
  
  
//  /**
//   * All paint() has to do is show the current image.
//   **/
//  public void paint(Graphics g)
//  {
//    if( mBufferedImage == null )
//      return;
//    Insets insets = getInsets();
//    g.drawImage(mBufferedImage, insets.left, insets.top, null);
//  }
}
