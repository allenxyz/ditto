package eecs285.proj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.imageio.ImageIO;

import java.io.*;

import eecs285.proj4.ImageProcessor;


public class ImageProcessorGUI extends JFrame
{
  public static ImageProcessorGUI win;

  private JMenuItem Open;
  private JMenuItem Save;
  private JMenuItem Exit;
  private JMenuItem Undo;
  private JMenuItem Redo;

  JPanel ImageDisplay = new JPanel();
  JPanel DisplayImage = new JPanel();

  private JPanel ColorMiddleR2;
  private JPanel ColorMiddleR3;
  private JPanel ColorMiddle;
  private JLabel A;
  private JLabel B;
  private JLabel C;

  private JButton Apply;
  private JButton ClearFields;

  JTextField ColorA;
  JTextField ColorB;
  JTextField ColorC;
  JTextField ColorD;
  JTextField ColorE;
  JTextField ColorF;
  JTextField ColorG;

  JComboBox<String> Filter;

  private BufferedImage mBufferedImage;
  private Graphics2D g2;

  ImageProcessor image;

  public static void main(String[] arg)
  {
    win = new ImageProcessorGUI();
    win.setMinimumSize(new Dimension(1120, 650));
    win.pack();

    win.setVisible(true);
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

  }

  public ImageProcessorGUI()
  {

    super("Insta-Paint");
    // setResizable(false);


    JPanel EditPalette = new JPanel();
    JPanel ColorBlock = new JPanel();
    JPanel InstaFilter = new JPanel();

    JPanel Custom = new JPanel();

    JMenuBar Menu = new JMenuBar();
    JMenu File = new JMenu("File");
    JMenu Edit = new JMenu("Edit");
    Open = new JMenuItem("Open Image");
    Open.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent ae)
      {
        FileDialog fd = new FileDialog(ImageProcessorGUI.this);
        fd.show();
        if( fd.getFile() == null )
          return;
        String path = fd.getDirectory() + fd.getFile();
        ImageDisplay.removeAll();
        loadImage(path);
        // repaint();
        // ImageDisplay.add(DisplayImage);
        // ImageDisplay.add()
      }
    });
    Save = new JMenuItem("Save Image");
    Exit = new JMenuItem("Exit Program");
    Undo = new JMenuItem("Undo");
    Redo = new JMenuItem("Redo");
    File.add(Open);
    File.add(Save);
    File.add(Exit);
    Edit.add(Undo);
    Edit.add(Redo);
    Menu.add(File);
    Menu.add(Edit);
    setJMenuBar(Menu);

    // JButton FilterA = new JButton("Filter A");
    // JButton FilterB = new JButton("Filter B");
    // EditPalette.add(FilterA);
    // EditPalette.add(FilterB);
    JPanel ColorTop = new JPanel();
    JRadioButton Color3 = new JRadioButton("3 Color Image");
    JRadioButton Color5 = new JRadioButton("5 Color Image");
    JRadioButton Color7 = new JRadioButton("7 Color Image");
    ButtonGroup ColorNumber = new ButtonGroup();


    ColorNumber.add(Color3);
    Color3.setActionCommand("three");
    ColorNumber.add(Color5);
    Color5.setActionCommand("five");
    ColorNumber.add(Color7);
    Color7.setActionCommand("seven");
    Color3.addActionListener(new ColorNumSelect());
    Color5.addActionListener(new ColorNumSelect());
    Color7.addActionListener(new ColorNumSelect());
    ColorTop.add(Color3);
    ColorTop.add(Color5);
    ColorTop.add(Color7);
    ColorMiddle = new JPanel();
    ColorMiddle.setLayout(new BoxLayout(ColorMiddle, BoxLayout.PAGE_AXIS));
    JPanel ColorMiddleR1 = new JPanel();
    ColorMiddleR2 = new JPanel();
    ColorMiddleR3 = new JPanel();
    TitledBorder Hex = new TitledBorder("Enter Hex Color Values");
    A = new JLabel("A: ");
    A.setEnabled(false);
    B = new JLabel("B: ");
    B.setEnabled(false);
    C = new JLabel("C: ");
    C.setEnabled(false);
    JLabel D = new JLabel("D: ");
    JLabel E = new JLabel("E: ");
    JLabel F = new JLabel("F: ");
    JLabel G = new JLabel("G: ");
    ColorA = new JTextField(7);
    ColorA.setEnabled(false);
    ColorB = new JTextField(7);
    ColorB.setEnabled(false);
    ColorC = new JTextField(7);
    ColorC.setEnabled(false);
    ColorD = new JTextField(7);
    // ColorD.setVisible(false);
    ColorE = new JTextField(7);
    // ColorE.setVisible(false);
    ColorF = new JTextField(7);
    ColorG = new JTextField(7);
    ColorMiddleR1.add(A);
    ColorMiddleR1.add(ColorA);
    ColorMiddleR1.add(B);
    ColorMiddleR1.add(ColorB);
    ColorMiddleR1.add(C);
    ColorMiddleR1.add(ColorC);
    ColorMiddleR2.add(D);
    ColorMiddleR2.add(ColorD);
    ColorMiddleR2.add(E);
    ColorMiddleR2.add(ColorE);
    ColorMiddleR2.setVisible(false);
    ColorMiddleR3.add(F);
    ColorMiddleR3.add(ColorF);
    ColorMiddleR3.add(G);
    ColorMiddleR3.add(ColorG);
    ColorMiddleR3.setVisible(false);
    ColorMiddle.setBorder(Hex);
    ColorMiddle.add(ColorMiddleR1);
    ColorMiddle.add(ColorMiddleR2);
    ColorMiddle.add(ColorMiddleR3);
    ColorMiddle.setEnabled(false);
    JPanel ColorBottom = new JPanel();
    Apply = new JButton("Apply");
    ClearFields = new JButton("Clear Fields");
    Apply.setEnabled(false);
    ClearFields.setEnabled(false);
    ColorBottom.add(Apply);
    ColorBottom.add(ClearFields);

    TitledBorder ColorTitle = new TitledBorder("Color Block Palette");
    ColorBlock.setBorder(ColorTitle);
    ColorBlock.setLayout(new BoxLayout(ColorBlock, BoxLayout.PAGE_AXIS));
    ColorBlock.add(ColorTop);
    ColorBlock.add(ColorMiddle);
    ColorBlock.add(ColorBottom);

    // FUCKKKK
    BufferedImage myPicture = null;
    try
    {
      myPicture = ImageIO.read(new File("wheel.png"));
    }
    catch( IOException e )
    {
      System.out.println("rip\n");
    }
    JLabel picLabel = new JLabel(new ImageIcon(myPicture));
    JPanel newPanel = new JPanel();
    newPanel.setLayout(new BorderLayout(100, 100));
    newPanel.add(picLabel);


    Filter = new JComboBox<String>();
    Filter.addItem("None");
    Filter.addItem("Sharpen");
    Filter.addItem("Edge Detector");
    Filter.addItem("Invert");
    Filter.addItem("Posterize");
    Filter.addItem("Blue Invert");
    Filter.addItem("Bins");
    Filter.setEnabled(false);
    Filter.addActionListener(new Filter());


    JPanel Instawrap = new JPanel();
    TitledBorder FilterTitle = new TitledBorder("Filter Palette");
    InstaFilter.setBorder(FilterTitle);
    Instawrap.add(Filter);

    InstaFilter.add(Instawrap);

    TitledBorder CustomTitle = new TitledBorder("Custom Settings");
    JButton CustomBlock = new JButton("Custom Color Block");
    JButton CustomFilter = new JButton("Custom Filter");
    Custom.add(CustomBlock);
    Custom.add(CustomFilter);
    Custom.setBorder(CustomTitle);

    EditPalette.setLayout(new BoxLayout(EditPalette, BoxLayout.PAGE_AXIS));
    EditPalette.add(ColorBlock);
    EditPalette.add(newPanel);
    EditPalette.add(InstaFilter);
    EditPalette.add(Custom);

    JPanel EditWrap = new JPanel();
    EditWrap.add(EditPalette);


    add(EditWrap, BorderLayout.WEST);
    // ImageDisplay.setMinimumSize(new Dimension(300,300));
    // ImageDisplay.setLayout(new BoxLayout(ImageDisplay, BoxLayout.LINE_AXIS));
    add(ImageDisplay);

    // apply.add(Menu, BorderLayout.NORTH);

    // add(apply);


  }

  public void loadImage(String fileName)
  {
    // Use a MediaTracker to fully load the image.
    Filter.setEnabled(true);
    Image grabimage = Toolkit.getDefaultToolkit().getImage(fileName);
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(grabimage, 0);
    try
    {
      mt.waitForID(0);
    }
    catch( InterruptedException ie )
    {
      return;
    }
    if( mt.isErrorID(0) )
      return;


    // Make a BufferedImage from the Image.
    mBufferedImage = new BufferedImage(grabimage.getWidth(null),
        grabimage.getHeight(null), BufferedImage.TYPE_INT_RGB);


    image = new ImageProcessor();
    image.saveOriginal(mBufferedImage);


    g2 = mBufferedImage.createGraphics();
    // resize grabimage if it's too big
   /* if( mBufferedImage.getWidth() > ImageDisplay.getWidth()
        || mBufferedImage.getHeight() > ImageDisplay.getWidth() )
    {
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(mBufferedImage, 0, 0, mBufferedImage.getWidth() / 2,
          mBufferedImage.getHeight() / 2, ImageDisplay);

    }
    else
    {*/
      g2.drawImage(grabimage, null, ImageDisplay);
    //}
    ImageIcon disp = new ImageIcon(mBufferedImage);
    JLabel display = new JLabel(disp);
    // display.setIcon(new ImageIcon(mBufferedImage));

    ImageDisplay.add(display);
    pack();
    // adjustToImageSize();
    // center();
    // ImageDisplay.validate();
    // ImageDisplay.repaint();

    // setTitle(kBanner + ": " + fileName);
  }

  class Filter implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {


      JComboBox<String> Filter = (JComboBox<String>) e.getSource();
      if( Filter.getSelectedItem().equals("None") )
      {
        mBufferedImage = image.getOriginal();
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
        pack();
      }
      else if( Filter.getSelectedItem().equals("Sharpen") )
      {
        mBufferedImage = image.getOriginal();
        BufferedImageOp op = (BufferedImageOp) image.mOps.get("Sharpen");
        mBufferedImage = op.filter(mBufferedImage, null);
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
        pack();
      }
      else if( Filter.getSelectedItem().equals("Edge Detector") )
      {
        mBufferedImage = image.getOriginal();
        BufferedImageOp op = (BufferedImageOp) image.mOps.get("Edge detector");
        mBufferedImage = op.filter(mBufferedImage, null);
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
        pack();
      }
      else if( Filter.getSelectedItem().equals("Invert") )
      {
        mBufferedImage = image.getOriginal();
        BufferedImageOp op = (BufferedImageOp) image.mOps.get("Invert");
        mBufferedImage = op.filter(mBufferedImage, null);
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
        pack();
      }
      else if( Filter.getSelectedItem().equals("Posterize") )
      {
        mBufferedImage = image.getOriginal();
        BufferedImageOp op = (BufferedImageOp) image.mOps.get("Posterize");
        mBufferedImage = op.filter(mBufferedImage, null);
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
        pack();
      }
      else if( Filter.getSelectedItem().equals("Blue Invert") )
      {
        mBufferedImage = image.getOriginal();
        BufferedImageOp op = (BufferedImageOp) image.mOps.get("Invert blue");
        mBufferedImage = op.filter(mBufferedImage, null);
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
        pack();
      }
      else if (Filter.getSelectedItem().equals("Bins")){
        mBufferedImage = image.getOriginal();
        BufferedImage binimage = deepCopy(mBufferedImage);
        colorBin(binimage);
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(binimage)));
        pack();
        
      }
    }

  }
  
  static BufferedImage deepCopy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
   }

  int calculateBrightness(Color c1)
  {
    return (int) Math.sqrt(.241 * c1.getRed() * c1.getRed() + .691
        * c1.getGreen() * c1.getGreen() + .068 * c1.getBlue() * c1.getBlue());
  }
  
  int calculateBrightness(int r, int g, int b){
    return (int) Math.sqrt(.241 * r*r + .691
        * g*g + .068 * b*b);
    
  }

  // color binning code
  //add numbins param later
  //WILL FIX BUT IT WORKS OMG YAYYYYYYY
  void colorBin(BufferedImage binimage){
    int binEdges[] = {0,0,0}; //based on brightness
    //for now, assume using 3 colors:
    Color darkBlue = new Color(53, 3, 78);
    Color limeGreen = new Color(43, 206, 96);
    Color beige = new Color(251, 224, 155); 
      binEdges[2] = calculateBrightness(darkBlue);
      binEdges[1] = calculateBrightness(limeGreen);
      binEdges[0] = calculateBrightness(beige);
      
      for(int i = 0; i <binimage.getWidth(); ++i){
        for(int j = 0; j < binimage.getHeight(); ++j){
          int argb = binimage.getRGB(i, j);
          int red = (argb >> 16) & 0xff; //red
          int green = (argb >>  8) & 0xff; //green
          int blue = (argb      ) & 0xff;  //blue
          int currentBrightness = calculateBrightness(red, green, blue);
          if(0 <= currentBrightness && currentBrightness < 85){
            binimage.setRGB(i, j, darkBlue.getRGB());
          }
          else if ( 85 <= currentBrightness && currentBrightness <170){
            binimage.setRGB(i, j, limeGreen.getRGB());
          }
          else{
            binimage.setRGB(i, j, beige.getRGB());
            
          }
        }
      }
  }

  class ColorNumSelect implements ActionListener
  {

    public void actionPerformed(ActionEvent e)
    {
      Apply.setEnabled(true);
      ClearFields.setEnabled(true);
      ColorMiddle.setEnabled(true);
      ColorA.setEnabled(true);
      ColorB.setEnabled(true);
      ColorC.setEnabled(true);
      A.setEnabled(true);
      B.setEnabled(true);
      C.setEnabled(true);

      if( e.getActionCommand() == "three" )
      {
        ColorMiddleR2.setVisible(false);
        ColorMiddleR3.setVisible(false);

      }
      else if( e.getActionCommand() == "five" )
      {
        ColorMiddleR2.setVisible(true);
        ColorMiddleR3.setVisible(false);

      }
      else if( e.getActionCommand() == "seven" )
      {
        ColorMiddleR2.setVisible(true);
        ColorMiddleR3.setVisible(true);

      }

    }

  }

}
