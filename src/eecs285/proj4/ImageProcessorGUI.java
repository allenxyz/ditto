package eecs285.proj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.color.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.imageio.ImageIO;

import sun.java2d.loops.RenderCache;

import java.io.*;

import eecs285.proj4.Exceptions.EmptyTextFieldException;
import eecs285.proj4.ImageProcessor;
import eecs285.proj4.pixelTypes.rgb;
import eecs285.proj4.server.*;

import java.awt.event.*;
import java.awt.Robot;
import java.util.Vector;


public class ImageProcessorGUI extends JFrame
{
  private final ImageProcessingSocket socket;
  public static ImageProcessorGUI win;

  private JMenuItem Open;
  private JMenuItem Save;
  private JMenuItem Exit;
  private JMenuItem Undo;
  private JMenuItem Redo;
  private JMenuItem Reset;

  boolean isLoaded = false;

  JPanel ImageDisplay = new JPanel();
  JPanel DisplayImage = new JPanel();
  
  JPanel palettePanel;

  JDialog ColorPickerDialog;
  

  private JPanel ColorBottom;
  private JLabel A;
  private JLabel B;
  private JLabel C;
  private JLabel display = new JLabel();

  private JButton Apply;
  private JButton ClearFields;
  private JButton Enter;

  JTextField ColorA;
  JTextField ColorB;
  JTextField ColorC;
  JTextField ColorD;
  JTextField ColorE;
  JTextField ColorF;
  JTextField ColorG;
  
  private JTextField numColors;
  

  JRadioButton Color3;
  JRadioButton Color5;
  JRadioButton Color7;

  JComboBox<String> Filter;

  private BufferedImage mBufferedImage;
  private static BufferedImage curImage;
  private Color selectedColors[] = new Color[256];
  private int numBins = 0;
  private Graphics2D g2;
  
  private Vector<BufferedImage> queue = new Vector<BufferedImage>(5);

  private ImageProcessor image;
  public static void main(String[] arg)
  {
    win = new ImageProcessorGUI("Insta-Paint", null);
    win.pack();

    win.setVisible(true);
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

  }

  public ImageProcessorGUI(String title, ImageProcessingSocket inSocket)
  {
    // super(title);
    if( title == null )
    {
      title = "Insta-Paint";
    }
    setTitle(title);
    // setResizable(false);
    socket = inSocket;

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
        fd.setVisible(true);
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
    Save.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JFileChooser chooser = new JFileChooser();
        int chooserReturn;
        File chosenFile;
        chooserReturn = chooser.showSaveDialog(ImageProcessorGUI.this);
        if( chooserReturn == JFileChooser.APPROVE_OPTION )
        {
          try
          {
            chosenFile = chooser.getSelectedFile();
            saveImage(chooser.getSelectedFile());
          }
          catch( IOException e1 )
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }

      }
    });


    Exit = new JMenuItem("Exit Program");
    Undo = new JMenuItem("Undo");
    Undo.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        if(!queue.isEmpty())
        {
          ImageDisplay.removeAll();
          ImageDisplay.add(new JLabel(new ImageIcon((BufferedImage)queue.lastElement())));
          queue.removeElementAt(queue.size() - 1);
        }
        System.out.println(queue.size());
      }
    });
    Redo = new JMenuItem("Redo");
    Reset = new JMenuItem("Reset");
    Reset.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
      }
    });
    
    File.add(Open);
    File.add(Save);
    File.add(Exit);
    Edit.add(Undo);
    Edit.add(Redo);
    Edit.add(Reset);
    Menu.add(File);
    Menu.add(Edit);
    setJMenuBar(Menu);

    JPanel ColorTop = new JPanel();
    JPanel ColorMiddle = new JPanel();
    
    JLabel blockLabel = new JLabel("Enter Number of Colors:");
    ColorTop.add(blockLabel);
    
    numColors = new JTextField(4);

    Enter = new JButton("Enter");
    Enter.addActionListener(new EnterAction());
    Enter.setEnabled(false);
    ColorTop.add(numColors);
    ColorTop.add(Enter);


    TitledBorder ColorTitle = new TitledBorder("Color Block Palette");
    ColorBlock.setBorder(ColorTitle);
    ColorBlock.setLayout(new BoxLayout(ColorBlock, BoxLayout.PAGE_AXIS));
    ColorBlock.add(ColorTop);
    ColorBlock.add(ColorMiddle);
    // ColorBlock.add(ColorBottom);

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
    palettePanel = new JPanel();
    palettePanel.setLayout(new BorderLayout(100, 100));
    palettePanel.add(picLabel);

    JPanel paletteText = new JPanel();
    JLabel red = new JLabel("R: ");
    final JTextField redPal = new JTextField(5);
    JLabel green = new JLabel("G: ");
    final JTextField greenPal = new JTextField(5);
    JLabel blue = new JLabel("B: ");
    final JTextField bluePal = new JTextField(5);
    redPal.setEditable(false);
    greenPal.setEditable(false);
    bluePal.setEditable(false);
    paletteText.add(red);
    paletteText.add(redPal);
    paletteText.add(green);
    paletteText.add(greenPal);
    paletteText.add(blue);
    paletteText.add(bluePal);

    palettePanel.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();
        int y = (int) b.getY();
        int getRed = 0;
        int getGreen = 0;
        int getBlue = 0;
        try
        {
          Robot r = new Robot();
          Color color = r.getPixelColor(x, y);
          //private Color selectedColors[] = new Color[256];
          
          getRed = color.getRed();
          getGreen = color.getGreen();
          getBlue = color.getBlue();
        }
        catch( AWTException e1 )
        {
          System.out.println("You aren't supposed to be here, LEAVE!");
        }
        if( getRed != 234 && getGreen != 234 && getBlue != 234 )
        {
          redPal.setText(String.valueOf(getRed));
          greenPal.setText(String.valueOf(getGreen));
          bluePal.setText(String.valueOf(getBlue));
        }
      }
    });


    Filter = new JComboBox<String>();
    Filter.addItem("None");
    Filter.addItem("Sharpen");
    Filter.addItem("Edge Detector");
    Filter.addItem("Invert");
    Filter.addItem("Posterize");
    Filter.addItem("Blue Invert");
    Filter.addItem("Obama");
    Filter.addItem("Valencia");
    Filter.addItem("Greyscale");
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
    EditPalette.add(palettePanel);
    EditPalette.add(paletteText);
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


    setMinimumSize(new Dimension(1120, 650));
  }

  public void loadImage(String fileName)
  {
    // Use a MediaTracker to fully load the image.
    Filter.setEnabled(true);
    
    Enter.setEnabled(true);
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
                                       grabimage.getHeight(null), 
                                       BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = mBufferedImage.createGraphics();
    g2.drawImage(grabimage, null, ImageDisplay);
    ImageIcon disp = new ImageIcon(mBufferedImage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(disp));
    curImage = deepCopy(mBufferedImage);
    
    pack();
    // adjustToImageSize();
    // center();
    // ImageDisplay.validate();
    // ImageDisplay.repaint();

    // setTitle(kBanner + ": " + fileName);
    resizeToScale();
    image = new ImageProcessor();
    Filter.setSelectedItem("None");

    if( socket != null )
    {
      socket.loadOccurred(mBufferedImage);
    }
  }

  // IF YOU WANNA MESS AROUND WITH COLOR SELECT, USE THIS CODE!
  class binColorApply implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
        //mBufferedImage = image.getOriginal();
        colorBinTwoPointOh(curImage, numBins, selectedColors);
        ImageDisplay.removeAll();
        ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
        pack();
       // ColorPicker.this.dispose();
      }
  }

  // overloaded so that the other player can load the image directly from an
  // Image rather than a pathname
  public void loadImage(Image grabimage)
  {
    // Use a MediaTracker to fully load the image.
    Filter.setEnabled(true);
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
    g2 = mBufferedImage.createGraphics();
    g2.drawImage(grabimage, null, ImageDisplay);
    image = new ImageProcessor();
    ImageIcon disp = new ImageIcon(mBufferedImage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(disp));
    pack();
    isLoaded = true;
    resizeToScale();
    curImage = deepCopy(mBufferedImage);
  }


  public static void saveImage(File outputFile) throws IOException
  {
    if( outputFile == null )
    {
      return;
    }
    try
    {
      outputFile = new File(outputFile.getAbsolutePath() + ".jpg");
      ImageIO.write(curImage, "jpg", outputFile);
      JOptionPane.showMessageDialog(ImageProcessorGUI.win, "Output Has Been Saved",
          "Saved Output", JOptionPane.PLAIN_MESSAGE);
    }
    catch( IOException e )
    {
      throw new IOException(String.format("Error writing to file. Error: %s\n",
          e.getMessage()));
    }
  }


  public class isEmpty
  {
    public isEmpty(String s) throws EmptyTextFieldException
    {
      if( s.isEmpty() )
      {
        throw new EmptyTextFieldException();
      }
    }
  }
  
  class EnterAction implements ActionListener{

    public void actionPerformed(ActionEvent e)
    {
      int number;
      try{
        
        new isEmpty(numColors.getText());
        
      }catch(EmptyTextFieldException excep){
        JOptionPane.showMessageDialog(null,
            "Text Fields Cannot Be Empty!", "Error!",
            JOptionPane.ERROR_MESSAGE);
            return;
      }
      try{
        number = Integer.parseInt(numColors.getText());
        numBins = number;
      }catch(NumberFormatException excep1){
        JOptionPane.showMessageDialog(null,
            "Text Field Must be a number!", "Error!",
            JOptionPane.ERROR_MESSAGE);
            return;
      }
      ColorPickerDialog = new ColorPicker(number);
      //palettePanel.setEnabled(true); 
    }   
  }
  
  public class ColorPicker extends JDialog
  {

    int clicks;
    JTextField count;

    public ColorPicker(final int numColors)
    {
      super(ImageProcessorGUI.this, "Pick your Color Block colors", true);

      System.out.println("HERE");
      JPanel apply = new JPanel();

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
      palettePanel = new JPanel();
      palettePanel.setLayout(new BorderLayout(100, 100));
      palettePanel.add(picLabel);
      palettePanel.setEnabled(false);
      JPanel Textwrap = new JPanel();
      Textwrap.setLayout(new BoxLayout(Textwrap, BoxLayout.PAGE_AXIS));
      JPanel first = new JPanel();
      JPanel second = new JPanel();
      JLabel labelcount = new JLabel("Number of Colors Selected: ");
      first.add(labelcount);
      count = new JTextField(3);
      count.setText("0");
      count.setEditable(false);
      first.add(count);
      
      
      JPanel paletteText = new JPanel();
      JLabel red = new JLabel("R: ");
      final JTextField redPal = new JTextField(5);
      JLabel green = new JLabel("G: ");
      final JTextField greenPal = new JTextField(5);
      JLabel blue = new JLabel("B: ");
      final JTextField bluePal = new JTextField(5);
      JButton Apply = new JButton("Apply");
      Apply.addActionListener(new binColorApply());
      /*if(clicks  == numBins ){
        Apply.setEnabled(true);
      }
      else{
        Apply.setEnabled(false);
      }*/
      redPal.setEditable(false);
      greenPal.setEditable(false);
      bluePal.setEditable(false);
      paletteText.add(red);
      paletteText.add(redPal);
      paletteText.add(green);
      paletteText.add(greenPal);
      paletteText.add(blue);
      paletteText.add(bluePal);
      paletteText.add(Apply);
      second.add(paletteText);

      
    
      palettePanel.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e){
          //TODO: EDIT THIS LISTENER
          PointerInfo a = MouseInfo.getPointerInfo();
          Point b = a.getLocation();
          int x = (int) b.getX();
          int y = (int) b.getY();
          int getRed = 0;
          int getGreen = 0;
          int getBlue = 0;
          try
          {
            Robot r = new Robot();
            Color color = r.getPixelColor(x, y);
            if(clicks >= numBins){
              //THROW EXCEPTION
              return;
            }
            else
            {
            //private Color selectedColors[] = new Color[256];
              selectedColors[clicks] = color;
              System.out.println(String.valueOf(selectedColors[clicks].getRGB()));
              
            }
            count.setText(String.valueOf(clicks + 1));
            getRed = color.getRed();
            getGreen = color.getGreen();
            getBlue = color.getBlue();
            clicks = clicks + 1;
          }
          catch( AWTException e1 )
          {
            System.out.println("You aren't supposed to be here, LEAVE!");
          }
          if(getRed != 234 && getGreen != 234 && getBlue != 234)
          {
            redPal.setText(String.valueOf(getRed));
            greenPal.setText(String.valueOf(getGreen));
            bluePal.setText(String.valueOf(getBlue));
          }
        }
      });
      Textwrap.add(first);
      Textwrap.add(second);
      apply.add(palettePanel);
      apply.add(Textwrap);
      //apply.add(paletteText);
      add(apply);
      
      setModal(true);
      setLayout(new FlowLayout());
      pack();
      setVisible(true);
      
    }
  }


  // color binning code 2.0
  // add numbins param later
  // WILL FIX BUT IT WORKS OMG YAYYYYYYY
  void colorBinTwoPointOh(BufferedImage binimage, int numBins, Color colors[])
  {
    // TODO: make an exception for if numBins == 0
    deepCopyerino(curImage);
    int binEdges = 256 / numBins;
    for( int i = 0; i < binimage.getWidth(); ++i )
    {
      for( int j = 0; j < binimage.getHeight(); ++j )
      {
        int argb = binimage.getRGB(i, j);
        int red = (argb >> 16) & 0xff; // red
        int green = (argb >> 8) & 0xff; // green
        int blue = (argb) & 0xff; // blue
        int currentBrightness = calculateBrightness(red, green, blue);
        for( int k = 0; k < numBins; ++k )
        {
          if( currentBrightness < binEdges * (k + 1) )
          {
            binimage.setRGB(i, j, colors[k].getRGB());
            k = numBins;
          }
        }
      }  
    }
  }

  public void obama()
  {
    Color darkBlue = new Color(29, 82, 97);
    Color red = new Color(161, 30, 34);
    Color teal = new Color(86, 151, 163);
    Color beige = new Color(245, 255, 201);
    selectedColors[0] = darkBlue;
    selectedColors[1] = red;
    selectedColors[2] = teal;
    selectedColors[3] = beige;
    colorBinTwoPointOh(curImage, 4, selectedColors);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
  }

  class Filter implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      JComboBox<String> Filter = (JComboBox<String>) e.getSource();
      if( Filter.getSelectedItem().equals("None") )
      {
        noFilter();
      }
      else if( Filter.getSelectedItem().equals("Sharpen") )
      {
        sharpen();
      }
      else if( Filter.getSelectedItem().equals("Edge Detector") )
      {
        edgeDetector();
      }
      else if( Filter.getSelectedItem().equals("Invert") )
      {
        invert();
      }
      else if( Filter.getSelectedItem().equals("Posterize") )
      {
        posterize();
      }
      else if( Filter.getSelectedItem().equals("Blue Invert") )
      {
        blueInvert();
      }
      else if( Filter.getSelectedItem().equals("Obama") )
      {
        obama();
      }
      else if (Filter.getSelectedItem().equals("Valencia"))
      {
         valencia();
      }
      else if (Filter.getSelectedItem().equals("Greyscale"))
      {
         greyscale();
      }

      if( socket != null )
        socket.eventOccurred(Filter.getSelectedItem().toString());
    }
  }
  
  public void setNumBins(int num){
    numBins = num;
  }
  

  public void bins()
  {
    //mBufferedImage = image.getOriginal();
    BufferedImage binimage = deepCopy(mBufferedImage);
    // colorBin(binimage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(binimage)));
    pack();
  }


  static BufferedImage deepCopy(BufferedImage bi)
  {
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

  int calculateBrightness(int r, int g, int b)
  {
    return (int) Math.sqrt(.241 * r * r + .691 * g * g + .068 * b * b);

  }
  
  
  
  


  void deepCopyerino(BufferedImage bi)
  {
    BufferedImage save = deepCopy(bi);
    if(queue.size() != 5)
    {
      queue.add(save);
      System.out.println(queue.size());
    }
    else
    {
      queue.remove(0);
      queue.add(save);
      System.out.println(queue.size());
    }
  }
  
  
  

  // color binning code
  // add numbins param later
  // WILL FIX BUT IT WORKS OMG YAYYYYYYY
  void colorBin(BufferedImage binimage, Color colorA, Color colorB, Color colorC)
  {
    int binEdges[] = { 0, 0, 0 }; // based on brightness
    // for now, assume using 3 colors:
    // Color bColor = Color.decode("FF0096");
    // Color darkBlue = new Color(53, 3, 78);
    // Color limeGreen = new Color(43, 206, 96);
    // Color beige = new Color(251, 224, 155);
    binEdges[2] = calculateBrightness(colorA);
    binEdges[1] = calculateBrightness(colorB);
    binEdges[0] = calculateBrightness(colorC);

    for( int i = 0; i < binimage.getWidth(); ++i )
    {
      for( int j = 0; j < binimage.getHeight(); ++j )
      {
        int argb = binimage.getRGB(i, j);
        int red = (argb >> 16) & 0xff; // red
        int green = (argb >> 8) & 0xff; // green
        int blue = (argb) & 0xff; // blue
        int currentBrightness = calculateBrightness(red, green, blue);
        if( 0 <= currentBrightness && currentBrightness < 85 )
        {
          binimage.setRGB(i, j, colorA.getRGB());
        }
        else if( 85 <= currentBrightness && currentBrightness < 170 )
        {
          binimage.setRGB(i, j, colorB.getRGB());
        }
        else
        {
          binimage.setRGB(i, j, colorC.getRGB());

        }
      }
    }
  }

  public void valencia()
  {
     deepCopyerino(curImage);
     curImage = ImageProcessor.filterValencia(curImage);
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
     pack();
  }
  
  public void greyscale()
  {
     deepCopyerino(curImage);
     curImage = ImageProcessor.filterGreyscale(curImage);
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
     pack();
  }
  
  

  public void noFilter()
  {
     deepCopyerino(curImage);
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
     curImage = deepCopy(mBufferedImage);
     pack();
  }


  public void sharpen()
  {
    deepCopyerino(curImage);
    BufferedImageOp op = (BufferedImageOp) image.mOps.get("Sharpen");
    curImage = op.filter(curImage, null);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
  }
  
  public void edgeDetector() {
     deepCopyerino(curImage);
     BufferedImageOp op = (BufferedImageOp) image.mOps.get("Edge detector");
     curImage = op.filter(curImage, null);
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
     pack();
  }
  
  public void invert() {
     deepCopyerino(curImage);
     BufferedImageOp op = (BufferedImageOp) image.mOps.get("Invert");
     curImage = op.filter(curImage, null);
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
     pack();
  }
  
  public void posterize() {
     deepCopyerino(curImage);
     BufferedImageOp op = (BufferedImageOp) image.mOps.get("Posterize");
     curImage = op.filter(curImage, null);
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
     pack();
  }
  
  public void blueInvert() {
     deepCopyerino(curImage);
     BufferedImageOp op = (BufferedImageOp) image.mOps.get("Invert blue");
     curImage = op.filter(curImage, null);
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
     pack();
  }
  
  public final int displayImageWidth = 800;
  public final int displayImageHeight = 800;
  public void resizeToScale()
  {
     int height = mBufferedImage.getHeight();
     int width = mBufferedImage.getWidth();
     double hwRatio = (double)height/(double)width;
     
     if (height > displayImageHeight) 
     {
        height = displayImageHeight;
        width = (int) (height / hwRatio);
     }
     if (width > displayImageWidth)
     {
        width = displayImageWidth;
        height = (int) (width * hwRatio);
     }
  
     
     
     
     Image tmp = mBufferedImage.getScaledInstance(width, height, BufferedImage.SCALE_FAST);
     BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
     buffered.getGraphics().drawImage(tmp, 0, 0, null);
     mBufferedImage = buffered;
     
     
     ImageDisplay.removeAll();
     display = new JLabel(new ImageIcon(mBufferedImage));
     ImageDisplay.add(display);
     
     System.out.println(mBufferedImage.getHeight());
     System.out.println(mBufferedImage.getWidth());
     pack();
     image = new ImageProcessor();
  }
  
  
}