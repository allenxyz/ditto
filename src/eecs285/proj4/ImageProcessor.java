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

import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;


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

  // /**
  // * Center this window in the user's desktop.
  // **/
  // private void center()
  // {
  // Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
  // Dimension d = getSize();
  // int x = (screen.width - d.width) / 2;
  // int y = (screen.height - d.height) / 2;
  // setLocation(x, y);
  // }
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
  public static BufferedImage filterValencia(BufferedImage orig)
  {/*
    * rgb[][] rgbs = rgb.toRGB(img);
    * 
    * // for (int i = 0 ; i < rgbs.length ; i += 1) // for (int j = 0 ; j <
    * rgbs[0].length ; j += 1) // System.out.println(rgbs[i][j].toString()); //
    * // for( int i = 0; i < rgbs.length; i += 1 ) for( int j = 0; j <
    * rgbs[0].length; j += 1 ) filters.valencia(rgbs[i][j]);
    * 
    * // for (int i = 0 ; i < rgbs.length ; i += 1) // for (int j = 0 ; j <
    * rgbs[0].length ; j += 1) // System.out.println(rgbs[i][j].toString()); //
    * return rgb.toImage(rgbs)
    */

    BufferedImage img = ImageProcessorGUI.deepCopy(orig);
    for( int i = 0; i < img.getWidth(); i++ )
    {
      for( int j = 0; j < img.getHeight(); j++ )
      {

        Color color = new Color(img.getRGB(i, j));
        // Here's how to mess with hsb/hsv
        float[] hsb = new float[3];
        // this guy modifies the float[]
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        // decrease saturation
        if( hsb[1] > .7 )
        {
          hsb[1] -= .3;
        }
        // increase brightness
        if( hsb[2] < .88 )
        {
          hsb[2] += .05;
        }
        Color myRGBColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);

        int newRed = ImageProcessorGUI.putInRange(myRGBColor.getRed());
        int newGreen = ImageProcessorGUI.putInRange(myRGBColor.getGreen());
        int newBlue = ImageProcessorGUI.putInRange(myRGBColor.getBlue() - 10);
        myRGBColor = new Color(newRed, newGreen, newBlue);
        img.setRGB(i, j, myRGBColor.getRGB());
      }
    }
    return img;
  }

  public static BufferedImage filterGreyscale(BufferedImage img)
  {
    BufferedImage dstImage = null;
    ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
    ColorConvertOp op = new ColorConvertOp(colorSpace, null);
    dstImage = op.filter(img, null);

    return dstImage;
  }

  public static BufferedImage filterTint(BufferedImage loadImg)
  {
    BufferedImage img = ImageProcessorGUI.deepCopy(loadImg);
    for( int i = 0; i < img.getWidth(); i++ )
    {
      for( int j = 0; j < img.getHeight(); j++ )
      {


        Color color = new Color(img.getRGB(i, j));
        // This makes a warm filter
        int newRed = ImageProcessorGUI.putInRange(color.getRed() + 17);
        int newGreen = ImageProcessorGUI.putInRange(color.getGreen() - 17);
        int newBlue = ImageProcessorGUI.putInRange(color.getBlue() - 20);
        color = new Color(newRed, newGreen, newBlue);
        Color brighter = color.brighter();
        img.setRGB(i, j, brighter.getRGB());
      }
    }
    return img;
  }

  public static BufferedImage FilterVignette(BufferedImage orig)
  {
    BufferedImage binimage = ImageProcessorGUI.deepCopy(orig);
    double halflength = (binimage.getHeight()) / 2;
    double halfwidth = (binimage.getWidth()) / 2;
    double radius;
    if( halflength < halfwidth )
      radius = halflength;
    else
      radius = halfwidth;
    // System.out.println("radius: " + radius);

    for( int i = 0; i < binimage.getWidth(); ++i )
    {
      for( int j = 0; j < binimage.getHeight(); ++j )
      {
        int argb = binimage.getRGB(i, j);
        // System.out.println("old ARGB =" + argb);
        int alpha = (argb >> 24) & 0xff;
        int red = (argb >> 16) & 0xff; // red
        int green = (argb >> 8) & 0xff; // green
        int blue = (argb) & 0xff; // blue
        // int currentBrightness = calculateBrightness(red, green, blue);
        double x = (double) i - halfwidth;
        double y = (double) j - halflength;
        double radi = Math.sqrt((x * x) + (y * y));
        // System.out.println(radi);
        if( radi <= radius )
        {
          // Nothing
        }
        else if( radi > radius )
        {
          double darkness = radi / radius;
          red = (int) Math.round(red / darkness);
          // System.out.println(red);
          green = (int) Math.round(green / darkness);
          blue = (int) Math.round(blue / darkness);
          argb = (alpha << 24) | (red << 16) | (green << 8) | blue;
          binimage.setRGB(i, j, argb);
          // System.out.println("new ARGB = " + argb);
        }

      }
    }
    return binimage;
  }

  public static BufferedImage CircleBlurFilter(BufferedImage orig,
      BufferedImage op)
  {
    BufferedImage binimage = ImageProcessorGUI.deepCopy(orig);
    double halflength = (binimage.getHeight()) / 2;
    double halfwidth = (binimage.getWidth()) / 2;
    double radius;
    if( halflength < halfwidth )
      radius = halflength;
    else
      radius = halfwidth;
    // System.out.println("radius: " + radius);

    for( int i = 0; i < binimage.getWidth(); ++i )
    {
      for( int j = 0; j < binimage.getHeight(); ++j )
      {
        double x = (double) i - halfwidth;
        double y = (double) j - halflength;

        double radi = Math.sqrt((x * x) + (y * y));
        // System.out.println(radi);
        if( radi <= radius )
        {
          // Nothing
        }
        else if( radi > radius )
        {
          int blurred = op.getRGB(i, j);
          binimage.setRGB(i, j, blurred);

        }

      }
    }
    return binimage;
  }

  // /**
  // * All paint() has to do is show the current image.
  // **/
  // public void paint(Graphics g)
  // {
  // if( mBufferedImage == null )
  // return;
  // Insets insets = getInsets();
  // g.drawImage(mBufferedImage, insets.left, insets.top, null);
  // }
}
