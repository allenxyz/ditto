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
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.imageio.ImageIO;

import java.io.*;

import eecs285.proj4.Exceptions.EmptyTextFieldException;
import eecs285.proj4.Exceptions.ZeroNumBinsException;
import eecs285.proj4.ImageProcessor;
import eecs285.proj4.pixelTypes.rgb;
import eecs285.proj4.server.*;

import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Vector;

public class ImageProcessorGUI extends JFrame
{
   
   //**initializing all variables that will be needed - this is long! :P
  private final ImageProcessingSocket socket;
  public static ImageProcessorGUI win;

  private JMenuItem Open;
  private JMenuItem Save;
  private JMenuItem Exit;
  private JMenuItem Undo;
  private JMenuItem Redo;
  private JMenuItem Reset;

  boolean isLoaded = false;

  private JPanel ImageDisplay = new JPanel();

  private JPanel palettePanel;

  private JDialog ColorPickerDialog;

  private JPanel ColorBottom;
  private JLabel A;
  private JLabel B;
  private JLabel C;
  private JLabel display = new JLabel();

  private JButton Apply;
  private JButton ClearFields;
  private JButton SaveSelection;
  private JButton Enter;
  // private JButton Apply;

  private JTextField ColorA;
  private JTextField ColorB;
  private JTextField ColorC;
  private JTextField ColorD;
  private JTextField ColorE;
  private JTextField ColorF;
  private JTextField ColorG;
  
  private JTextField redPal;
  private JTextField greenPal;
  private JTextField bluePal;

  private JTextField numColors;
  
  private JCheckBox stackFilter;

  private BufferedImage mBufferedImage;
  private static BufferedImage curImage;
  private Color selectedColors[] = new Color[256];
  private int numBins = 0;
  private Graphics2D g2;

  final private static int UNDO_MAX = 10;
  private Vector<BufferedImage> queue = new Vector<BufferedImage>(UNDO_MAX);
  private static int queueSize = -1;

  private ImageProcessor image;

  private JComboBox<String> UtilityFilters;
  private JComboBox<String> CustomFilters;
  private JComboBox<String> PresetFilters;
  private ArrayList<ColorScheme> customColorSchemes = new ArrayList<ColorScheme>();
  
  private Color paintColor = null;
  private Point oldPoint;
  private Point newPoint;
  private Image save;
  
  private JButton stickerSpeechLeft;
  private JButton stickerSpeechRight;
  private JButton stickerSkull;
  private JButton stickerHashtag;
  private JButton stickerPaperAirplane;
  private JButton stickerCrown;
  private JButton stickerDrop;
  private JButton stickerHeart;
  private String filePath = "";
  //****** end of variable initialization
  
  
  public int brushSize = 0;
  
  public static void main(String[] arg)
  {
    win = new ImageProcessorGUI("Insta-Paint", null);
    win.pack();

    win.setVisible(true);
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

  }

  
  
  
  
  //********** constructor begin**********************
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

    
    
    
    
    //******* Menu stuff and their bajillion mouse listeners
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
        if( socket != null )
           socket.loadOccurred(mBufferedImage);
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
    Exit.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        socket.eventOccurred("Exit");
        System.exit(0);
      }


    });

    Undo = new JMenuItem("Undo");
    Undo.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
         undo();
         if (socket != null)
            socket.eventOccurred("Undo");
      }
    });
    Redo = new JMenuItem("Redo");
    Redo.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
         redo();
         if (socket != null)
            socket.eventOccurred("Redo");
      }
    });
    Reset = new JMenuItem("Reset");
    Reset.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
         reset();
         if (socket != null)
            socket.eventOccurred("Reset");
      }
    });
    Reset.setEnabled(false);

    File.add(Open);
    File.add(Save);
    File.add(Exit);
    Edit.add(Undo);
    Edit.add(Redo);
    Edit.add(Reset);
    Menu.add(File);
    Menu.add(Edit);
    setJMenuBar(Menu);

    Redo.setEnabled(false);
    Undo.setEnabled(false);
    //end Menu
    
    
    
    
    
    //******* palletePanel
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
    redPal = new JTextField(5);
    JLabel green = new JLabel("G: ");
    greenPal = new JTextField(5);
    JLabel blue = new JLabel("B: ");
    bluePal = new JTextField(5);
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
          // private Color selectedColors[] = new Color[256];
          paintColor = color;
          getRed = color.getRed();
          getGreen = color.getGreen();
          getBlue = color.getBlue();
        }
        catch( AWTException e1 )
        {
          System.out.println("You aren't supposed to be here, LEAVE!");
        }
        if( getRed != 234 && getGreen != 234 && getBlue != 234
              )
        {
          redPal.setText(String.valueOf(getRed));
          greenPal.setText(String.valueOf(getGreen));
          bluePal.setText(String.valueOf(getBlue));
        }
      }
    }); //end palletePanel

    //***** paintSizePanel
    
    JPanel paintSizePanel = new JPanel();
    final JTextField paintSize = new JTextField(5);
    final JLabel paintSizeLabel = new JLabel("Brush Size: ");
    JButton submitSize =  new JButton("Submit");
    submitSize.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try{
          brushSize = Integer.parseInt(paintSize.getText());
        }
        catch (Exception eeee){
          JOptionPane.showMessageDialog(getParent(), "ERROR! INVALID FIELD");
        }
        if(brushSize < 0 || brushSize > 500){
          JOptionPane.showMessageDialog(getParent(), "ERROR! INVALID BRUSH SIZE");
          brushSize = 0;
        }
      }
    });
    
    paintSizePanel.add(paintSizeLabel);
    paintSizePanel.add(paintSize);
    paintSizePanel.add(submitSize);
    add(paintSizePanel);
    
    
    
    
    //***** utility filters
    UtilityFilters = new JComboBox<String>();
    UtilityFilters.addItem("None");
    UtilityFilters.addItem("Sharpen");
    UtilityFilters.addItem("Edge Detector");
    UtilityFilters.addItem("Invert");
    UtilityFilters.addItem("Posterize");
    UtilityFilters.addItem("Blue Invert");
    UtilityFilters.addItem("Greyscale");
    UtilityFilters.addItem("Vignette");
    UtilityFilters.addItem("Circle Blur");
    UtilityFilters.setEnabled(false);
    UtilityFilters.addActionListener(new Filter());
    //end utilityfilters
    

    //***** preset filters
    PresetFilters = new JComboBox<String>();
    PresetFilters.addItem("None");
    PresetFilters.addItem("Obama");
    PresetFilters.addItem("Fire");
    PresetFilters.addItem("Morgana");
    PresetFilters.addItem("Rainbow");
    PresetFilters.addItem("Neutral");
    PresetFilters.addItem("Coffee");
    PresetFilters.addItem("Tint");
    PresetFilters.addItem("Valencia");
    PresetFilters.setEnabled(false);
    PresetFilters.addActionListener(new Filter());
    //end presetfilters
    
    
    
    //*** stacking filter mouse listener
    stackFilter = new JCheckBox("Stack Filters");
    stackFilter.addMouseListener(new MouseAdapter() {
       public void mouseClicked(MouseEvent e)
       {
          if (socket != null) socket.eventOccurred("stack");
       }
    });
    //end stacking filters mouse listener

    
    
    
    
    JPanel Instawrap = new JPanel();
    TitledBorder FilterTitle = new TitledBorder("Filter Palette");
    InstaFilter.setBorder(FilterTitle);
    Instawrap.add(UtilityFilters);
    Instawrap.add(stackFilter);

    JPanel presets = new JPanel();
    TitledBorder presetTitle = new TitledBorder("Preset Filters");
    presets.setBorder(presetTitle);
    presets.add(PresetFilters);
    presets.add(stackFilter);

    
    
    
    //***** sticker button stuff
    JPanel stickers = new JPanel(new GridLayout(1, 8));
    stickerSpeechLeft = new JButton(new ImageIcon("src/img/speech_left.gif"));
    stickerSpeechLeft.setPreferredSize(new Dimension(50,50));
    stickerSpeechRight = new JButton(new ImageIcon("src/img/speech_right.gif"));
    stickerSpeechRight.setPreferredSize(new Dimension(50,50));
    stickerSkull = new JButton(new ImageIcon("src/img/skull.gif"));
    stickerSkull.setPreferredSize(new Dimension(50,50));
    stickerHashtag = new JButton(new ImageIcon("src/img/hashtag.gif"));
    stickerHashtag.setPreferredSize(new Dimension(50,50));
    stickerPaperAirplane = new JButton(new ImageIcon("src/img/paper_airplane.gif"));
    stickerPaperAirplane.setPreferredSize(new Dimension(50,50));
    stickerCrown = new JButton(new ImageIcon("src/img/crown.gif"));
    stickerCrown.setPreferredSize(new Dimension(50,50));
    stickerDrop = new JButton(new ImageIcon("src/img/drop.png"));
    stickerDrop.setPreferredSize(new Dimension(50,50));
    stickerHeart= new JButton(new ImageIcon("src/img/heart.png"));
    stickerHeart.setPreferredSize(new Dimension(50,50));
    stickers.add(stickerSpeechLeft);
    stickers.add(stickerSpeechRight);
    stickers.add(stickerSkull);
    stickers.add(stickerHashtag);
    stickers.add(stickerPaperAirplane);
    stickers.add(stickerCrown);
    stickers.add(stickerDrop);
    stickers.add(stickerHeart);
    stickerSpeechLeft.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "speech_left_big.gif";
       }
    });
    stickerSpeechRight.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "speech_right_big.gif";
       }
    });
    stickerSkull.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "skull.gif";
       }
    });
    stickerHashtag.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "hashtag.gif";
       }
    });
    stickerPaperAirplane.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "paper_airplane.gif";
       }
    });
    stickerCrown.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "crown.gif";
       }
    });
    stickerDrop.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "drop.png";
       }
    });
    stickerHeart.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          filePath = "heart.png";
       }
    }); //end sticker buttons
    
    
    
    
    
    InstaFilter.add(Instawrap);
    
    
    
    
    
    //**** custom filter stuff
    TitledBorder CustomFilter = new TitledBorder("Custom Filter");
    Custom.setBorder(CustomFilter);
    CustomFilters = new JComboBox<String>();
    CustomFilters.addItem("Pick one: ");

    CustomFilters.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        String s = (String) ((JComboBox<String>) e.getSource())
            .getSelectedItem();
        if( s == "Pick one: " ) {
          noFilter();
          if (socket != null)
             socket.eventOccurred("None");
          return;
        }

        // find it in the array of customColorSchemes
        ColorScheme found = null;
        for( ColorScheme c : customColorSchemes ) {
          if( c.getName() == s )
            found = c;
        }

        if( found == null )
        {
          JOptionPane.showMessageDialog(null, "Selected Field not found!",
              "Error!", JOptionPane.ERROR_MESSAGE);
          return;
        }
        else
        {
          for( int i = 0; i < found.getnumber(); ++i )
          {
            System.out.println("first color: Red:"
                + found.getColArr()[i].getRed() + "Green:"
                + found.getColArr()[i].getGreen() + "Blue:"
                + found.getColArr()[i].getBlue());
          }
          colorBinTwoPointOh(curImage, found.getnumber(), found.getColArr());
          if (socket != null) socket.binOccurred(found.getnumber(), found.getColArr());
          // make the utilities default to none
          // FIX THIS - issue is that it can set default but then won't apply
          // filter
          // UtilityFilters.setSelectedItem("None");
        }


      }
    }); //end custom filter stuff

    
    
    
    Custom.add(CustomFilters);
    // Custom.setVisible(false);
    // CustomFilters.addActionListener(new CustomFilter());

    EditPalette.setLayout(new BoxLayout(EditPalette, BoxLayout.PAGE_AXIS));
    EditPalette.add(ColorBlock);
    EditPalette.add(palettePanel);
    EditPalette.add(paletteText);
    EditPalette.add(paintSizePanel);
    EditPalette.add(InstaFilter);
    EditPalette.add(presets);
    EditPalette.add(Custom);
    EditPalette.add(stickers);

    JPanel EditWrap = new JPanel();
    EditWrap.add(EditPalette);

    add(EditWrap, BorderLayout.WEST);
    // ImageDisplay.setMinimumSize(new Dimension(300,300));
    // ImageDisplay.setLayout(new BoxLayout(ImageDisplay,
    // BoxLayout.LINE_AXIS));
    add(ImageDisplay);   
    ImageDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
    
    
///******* attempted to draw...     
   
    ImageDisplay.addMouseMotionListener(new MouseMotionAdapter() {
      
      
       public void mouseDragged(MouseEvent e) {
          if (paintColor == null || !isLoaded || !filePath.equals("")) return;
          
          newPoint = e.getPoint();
          if(oldPoint.equals(null)){
            oldPoint = newPoint;
          }
          Stroke stroke = new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
          Graphics2D g = (Graphics2D)ImageDisplay.getGraphics();
          g.setColor(paintColor);
          g.setStroke(stroke);
         
          //g.fillOval(e.getX() - brushSize/2, e.getY() - brushSize/2, brushSize, brushSize);
          g.drawLine(oldPoint.x, oldPoint.y, newPoint.x, newPoint.y);
          Graphics2D g2 = curImage.createGraphics();
          g2.setColor(paintColor);
          g2.setStroke(stroke);
          g2.drawLine(oldPoint.x, oldPoint.y, newPoint.x, newPoint.y);
          //g2.fill(new Ellipse2D.Float(e.getX() - brushSize/2, e.getY() - brushSize/2, brushSize, brushSize));
          oldPoint = newPoint;
          g2.dispose();           
       }
       
    });
    
    ImageDisplay.addMouseListener(new MouseAdapter() {
    
    public void mousePressed(MouseEvent e)
      {
        
        oldPoint = e.getPoint();
        newPoint = oldPoint;
        
        if (paintColor == null || !isLoaded || !filePath.equals("")) return;
        
        newPoint = e.getPoint();
        if(oldPoint.equals(null)){
          oldPoint = newPoint;
        }
        //repeat code of previous to be able to draw dots
        Stroke stroke = new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        Graphics2D g = (Graphics2D)ImageDisplay.getGraphics();
        g.setColor(paintColor);
        g.setStroke(stroke);
       
        //g.fillOval(e.getX() - brushSize/2, e.getY() - brushSize/2, brushSize, brushSize);
        g.drawLine(oldPoint.x, oldPoint.y, newPoint.x, newPoint.y);
        Graphics2D g2 = curImage.createGraphics();
        g2.setColor(paintColor);
        g2.setStroke(stroke);
        g2.drawLine(oldPoint.x, oldPoint.y, newPoint.x, newPoint.y);
        //g2.fill(new Ellipse2D.Float(e.getX() - brushSize/2, e.getY() - brushSize/2, brushSize, brushSize));
        oldPoint = newPoint;
        g2.dispose();  
        
      }

      public void mouseReleased(MouseEvent me) {
          if (paintColor == null || !isLoaded || !filePath.equals("")) return;
          deepCopyerino(curImage);
          if (socket != null)
             socket.sendImage(curImage);
       }
    });
    
//***********end attempt to draw
    
    
    //*** Sticker mouse Listener
    ImageDisplay.addMouseListener(new MouseAdapter() {
       public void mousePressed(MouseEvent me) {
          //Some kind of check that it is not trying to paint
          if (filePath.equals("") || !isLoaded) return;
          Image sticker = null;
          try {
             sticker = ImageIO.read(new File("src/img/"+filePath));
          }
          catch (Exception e) {
             System.out.println("Error reading file");
          }
          curImage.getGraphics().drawImage(sticker, me.getX() - 25, me.getY() - 25, null);
          ImageDisplay.removeAll();
          ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
          pack();
          deepCopyerino(curImage);
          filePath = "";
          if (socket != null)
             socket.sendImage(curImage);
          
       }
    });
    //end sticker mouse listener
    
    setMinimumSize(new Dimension(1120, 800));
  } //end of constructor

  
  
  
  
  
  //***** loadImage function
  public void loadImage(String fileName)
  {
    // Use a MediaTracker to fully load the image.
    UtilityFilters.setEnabled(true);
    PresetFilters.setEnabled(true);
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
        grabimage.getHeight(null), BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = mBufferedImage.createGraphics();
    g2.drawImage(grabimage, null, ImageDisplay);

    //resize the image
    resizeToScale();

    //display using JLabel
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
    pack();
    
    curImage = deepCopy(mBufferedImage);
    // these are making a call to noFilter -> screwing up UNDO
    // can't get them working with multi-player - srry gonna remove them :(!
//    UtilityFilters.setSelectedItem("None");
//    CustomFilters.setSelectedItem("Pick one: ");
    
    
    //to fix undo stuff
    deepCopyerino(curImage);
    if( !isLoaded ) {
       System.out.println("This is loaded now");
      isLoaded = true;
      Undo.setEnabled(false);
    }
    
  }

  // overloaded so that the other player can load the image directly from an
  // Image rather than a pathname
  public void loadImage(Image grabimage)
  {
    // Use a MediaTracker to fully load the image.
    UtilityFilters.setEnabled(true);
    Enter.setEnabled(true);
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
    mBufferedImage = new BufferedImage(grabimage.getWidth(null),
        grabimage.getHeight(null), BufferedImage.TYPE_INT_RGB);
    g2 = mBufferedImage.createGraphics();
    g2.drawImage(grabimage, null, ImageDisplay);
    
    resizeToScale();
    
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
    pack();
    
    curImage = deepCopy(mBufferedImage);
    
    deepCopyerino(curImage);
    if( !isLoaded ) {
      isLoaded = true;
      Undo.setEnabled(false);
    }
  } 

  //this one doesn't overwrite the original image - need it for networking
  public void setImage(Image grabimage)
  {
    // Use a MediaTracker to fully load the image.
    UtilityFilters.setEnabled(true);
    Enter.setEnabled(true);
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
    curImage = new BufferedImage(grabimage.getWidth(null),
        grabimage.getHeight(null), BufferedImage.TYPE_INT_RGB);
    g2 = curImage.createGraphics();
    g2.drawImage(grabimage, null, ImageDisplay);
    
    resizeToScale();
    
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    
    deepCopyerino(curImage);
    if( !isLoaded ) {
      isLoaded = true;
      Undo.setEnabled(false);
    }
  }  //end loadImages stuff

  
  
  
  
  //*******Save Image function
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
      JOptionPane.showMessageDialog(ImageProcessorGUI.win,
          "Output Has Been Saved", "Saved Output", JOptionPane.PLAIN_MESSAGE);
    }
    catch( IOException e )
    {
      throw new IOException(String.format("Error writing to file. Error: %s\n",
          e.getMessage()));
    }
  }//end saveImage

  
  
  //****helper functions
  //pallete modal helper functions
  public class isEmpty
  {
    public isEmpty(String s) throws EmptyTextFieldException
    {
      if( s.isEmpty() )
      {
        throw new EmptyTextFieldException("Text Fields Cannot Be Empty!");
      }
    }
  }

  class EnterAction implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      int number;
      try
      {
        new isEmpty(numColors.getText());
      }
      catch( EmptyTextFieldException excep )
      {
        JOptionPane.showMessageDialog(null, "Text Fields Cannot Be Empty!",
            "Error!", JOptionPane.ERROR_MESSAGE);
        return;
      }
      try
      {
        number = Integer.parseInt(numColors.getText());
        numBins = number;
        if( numBins == 0 )
        {
          throw (new ZeroNumBinsException("Number must be greater than zero!"));
        }
      }
      catch( NumberFormatException excep1 )
      {
        JOptionPane.showMessageDialog(null, "Text Field Must be a number!",
            "Error!", JOptionPane.ERROR_MESSAGE);
        return;
      }
      catch( ZeroNumBinsException zEx )
      {
        JOptionPane.showMessageDialog(null, zEx.getMessage(), "Error!",
            JOptionPane.ERROR_MESSAGE);
        return;
      }
      ColorPickerDialog = new ColorPicker(numBins);
      // palettePanel.setEnabled(true);
    }
  }
  
  //pallete colorPicker modal
  public class ColorPicker extends JDialog
  {
    int clicks;
    JTextField count;

    public ColorPicker(final int numColors)
    {
      setTitle("Pick your Color Block Colors");
      setModal(true);

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
      JPanel third = new JPanel();
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
      Apply = new JButton("Apply");
      Apply.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          colorBinTwoPointOh(curImage, numBins, selectedColors);
          if (socket != null) socket.binOccurred(numBins, selectedColors);
          dispose();
        }
      });

      SaveSelection = new JButton("Save Selection");
      SaveSelection.setEnabled(false);
      SaveSelection.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          /*
           * String s = (String)JOptionPane.showInputDialog( frame,
           * "Complete the sentence:\n" + "\"Green eggs and...\"",
           * "Customized Dialog", JOptionPane.PLAIN_MESSAGE, icon,
           * possibilities, "ham");
           */

          String s = (String) JOptionPane.showInputDialog(ColorPickerDialog,
              "Name your Color Scheme\n", "Custom Name",
              JOptionPane.PLAIN_MESSAGE, null, null, null);
          Color inColors[] = new Color[numBins];
          System.out.println("numBins: " + numBins);
          for( int i = 0; i < numBins; i++ )
          {
            inColors[i] = new Color(selectedColors[i].getRed(),
                selectedColors[i].getGreen(), selectedColors[i].getBlue());
            System.out.println("Color" + i + ": Red:" + inColors[i].getRed()
                + "Green:" + inColors[i].getGreen() + "Blue:"
                + inColors[i].getBlue());
          }
          ColorScheme customscheme = new ColorScheme(s, inColors, numBins);
          System.out.println(customscheme.getName());
          customColorSchemes.add(customscheme);
          CustomFilters.addItem(customscheme.getName());
          System.out.println(s + "  "
              + String.valueOf(customscheme.getnumber()));
        }

      });
      JButton Close = new JButton("Close");
      Close.addActionListener(new ActionListener()
      {

        public void actionPerformed(ActionEvent e)
        {
          if (socket != null) socket.eventOccurred("Exit");
          dispose();

        }

      });
      Apply.setEnabled(false);
      /*
       * if(clicks == numBins ){ Apply.setEnabled(true); } else{
       * Apply.setEnabled(false); }
       */
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
      third.add(SaveSelection);
      third.add(Close);

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
            getRed = color.getRed();
            getGreen = color.getGreen();
            getBlue = color.getBlue();
            if (getRed == 238 && getGreen == 238 && getBlue == 238 ) return;
            
            if( clicks >= numBins )
            {
              // THROW EXCEPTION
              JOptionPane.showMessageDialog(null, "You have already picked "
                  + numBins + " colors", "Error", JOptionPane.ERROR_MESSAGE);
              return;
            }
            else
            {
              // private Color selectedColors[] = new Color[256];
              if( clicks == numBins - 1 )
              {
                Apply.setEnabled(true);
                SaveSelection.setEnabled(true);
              }
              selectedColors[clicks] = color;
              // System.out.println(String.valueOf(selectedColors[clicks].getRGB()));

            }
            redPal.setText(String.valueOf(getRed));
            greenPal.setText(String.valueOf(getGreen));
            bluePal.setText(String.valueOf(getBlue));
            count.setText(String.valueOf(clicks + 1));
            clicks = clicks + 1;
            
          }
          catch( AWTException e1 )
          {
            System.out.println("You aren't supposed to be here, LEAVE!");
          }
        }
      });
      Textwrap.add(first);
      Textwrap.add(second);
      Textwrap.add(third);
      apply.add(palettePanel);
      apply.add(Textwrap);
      // apply.add(paletteText);
      add(apply);

      setModal(true);
      setLayout(new FlowLayout());
      pack();
      setVisible(true);

    }
  } //end helper functions

  
  
  
  
  //*******************BINNING ALGORITHM!***********
  // color binning code 2.0
  // add numbins param later
  // WILL FIX BUT IT WORKS OMG YAYYYYYYY
  public void colorBinTwoPointOh(BufferedImage binimage, int numBins, Color colors[])
  {
     if( !stackFilter.isSelected() )
       curImage = deepCopy(mBufferedImage);
     
    // TODO: make an exception for if numBins == 0
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
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(binimage)));
    pack();
    deepCopyerino(binimage);
  } //end binning functions

  
  //***************Main Filter ActionListener******************
  //main interface to lead to the corresponding filter choices
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
      else if( Filter.getSelectedItem().equals("Morgana") )
      {
        colorScheme("Morgana");
      }
      else if( Filter.getSelectedItem().equals("Fire") )
      {
        colorScheme("Fire");
      }
      else if( Filter.getSelectedItem().equals("Neutral") )
      {
        colorScheme("Neutral");
      }
      else if( Filter.getSelectedItem().equals("Coffee") )
      {
        colorScheme("Coffee");
      }
      else if( Filter.getSelectedItem().equals("Rainbow") )
      {
        colorScheme("Rainbow");
      }
      else if( Filter.getSelectedItem().equals("Greyscale") )
      {
        greyscale();
      }
      else if( Filter.getSelectedItem().equals("Vignette") )
      {
        vignette();
      }
      else if( Filter.getSelectedItem().equals("Circle Blur") )
      {
        circleBlur();
      }
      else if( Filter.getSelectedItem().equals("Valencia") )
      {
        valencia();
      }
      else if( Filter.getSelectedItem().equals("Tint") )
      {
        tint();
      }

      if( socket != null )
        socket.eventOccurred(Filter.getSelectedItem().toString());
      // make custom filter popup default back to orig
      // FIX THIS - issue is that it can set default but then won't apply filter
      // CustomFilters.setSelectedItem("Pick one: ");
    }
  }//end filter ActionListener
  
  //*******************filtering functions*************
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
  }//end obama

  public void colorScheme(String scheme)
  {
    numBins = ColorScheme.setColorScheme(scheme, selectedColors, numBins);
    colorBinTwoPointOh(curImage, numBins, selectedColors);
  }//end colorScheme

  
  
  
  //*******************MOAR helper functions**************
  //my OCD wants me to move these up with the other helper functions
  //but then you'll all have merge conflicts
  //your welcome :)
  public void setNumBins(int num)
  {
    numBins = num;
  } //end setNumBins

  public void bins()
  {
    // mBufferedImage = image.getOriginal();
    BufferedImage binimage = deepCopy(mBufferedImage);
    // colorBin(binimage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(binimage)));
    pack();
  } //end bins

  public static int putInRange(int rgbComponent)
  {

    if( rgbComponent < 0 )
    {
      rgbComponent = 0;
    }
    else if( rgbComponent > 255 )
    {
      rgbComponent = 255;
    }
    return rgbComponent;
  } //end putInRange


  static BufferedImage deepCopy(BufferedImage bi)
  {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  } //end deepCopy 

  int calculateBrightness(Color c1)
  {
    return (int) Math.sqrt(.241 * c1.getRed() * c1.getRed() + .691
        * c1.getGreen() * c1.getGreen() + .068 * c1.getBlue() * c1.getBlue());
  }//end calculateBrightness

  int calculateBrightness(int r, int g, int b)
  {
    return (int) Math.sqrt(.241 * r * r + .691 * g * g + .068 * b * b);
  }//end calculateBrightness

  
  //da-bomb-erino
  void deepCopyerino(BufferedImage bi)
  {
    queueSize++;
    // delete everything else in undo queue
    for( int i = queue.size() - 1; i >= queueSize; i-- )
    {
      queue.removeElementAt(i);
    }

    Undo.setEnabled(true);
    Redo.setEnabled(false);
    BufferedImage save = deepCopy(bi);
    if( queue.size() != UNDO_MAX )
    {
      queue.add(save);
      System.out.println(queue.size());
    }
    else
    {
      queueSize--;
      queue.removeElementAt(0);
      queue.add(save);
    }
    if (queueSize == 0)
       Undo.setEnabled(false);
  }//end deepCopyerino
  //end MOAR helper functions
  
  //*******************Binnin v1.0 - outdated...****
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
  }//end bin v1.0

  
  
  
  //***********MOAR filter functons!***********
  public void valencia()
  {
    if( stackFilter.isSelected() )
      curImage = ImageProcessor.filterValencia(curImage);
    else
      curImage = ImageProcessor.filterValencia(mBufferedImage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end valencia

  public void greyscale()
  {
    if( stackFilter.isSelected() )
      curImage = ImageProcessor.filterGreyscale(curImage);
    else
      curImage = ImageProcessor.filterGreyscale(mBufferedImage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end greyscale

  public void vignette()
  {
    if( stackFilter.isSelected() )
      curImage = ImageProcessor.FilterVignette(curImage);
    else
      curImage = ImageProcessor.FilterVignette(mBufferedImage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end vignette

  public void tint()
  {
    if( stackFilter.isSelected() )
      curImage = ImageProcessor.filterTint(curImage);
    else
      curImage = ImageProcessor.filterTint(mBufferedImage);
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();

    deepCopyerino(curImage);
  }//end tint

  public void noFilter()
  {
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
    curImage = deepCopy(mBufferedImage);
    pack();
    deepCopyerino(curImage);
  }//end noFilter

  public void sharpen()
  {
    BufferedImageOp op = (BufferedImageOp) image.mOps.get("Sharpen");

    if( stackFilter.isSelected() )
      curImage = op.filter(curImage, null);
    else
      curImage = op.filter(mBufferedImage, null);

    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end sharpen

  public void circleBlur()
  {
    BufferedImageOp op = (BufferedImageOp) image.mOps.get("Blur");
    BufferedImage BlurImage;

    if( stackFilter.isSelected() )
    {
      BlurImage = op.filter(curImage, null);
      curImage = ImageProcessor.CircleBlurFilter(curImage, BlurImage);
    }
    else
    {
      BlurImage = op.filter(mBufferedImage, null);
      curImage = ImageProcessor.CircleBlurFilter(mBufferedImage, BlurImage);
    }
    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end circleBlur

  public void edgeDetector()
  {
    BufferedImageOp op = (BufferedImageOp) image.mOps.get("Edge detector");

    if( stackFilter.isSelected() )
      curImage = op.filter(curImage, null);
    else
      curImage = op.filter(mBufferedImage, null);

    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end edgeDetector

  public void invert()
  {
    BufferedImageOp op = (BufferedImageOp) image.mOps.get("Invert");

    if( stackFilter.isSelected() )
      curImage = op.filter(curImage, null);
    else
      curImage = op.filter(mBufferedImage, null);

    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end invert

  public void posterize()
  {
    BufferedImageOp op = (BufferedImageOp) image.mOps.get("Posterize");

    if( stackFilter.isSelected() )
      curImage = op.filter(curImage, null);
    else
      curImage = op.filter(mBufferedImage, null);

    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end posterize

  public void blueInvert()
  {
    BufferedImageOp op = (BufferedImageOp) image.mOps.get("Invert blue");

    if( stackFilter.isSelected() )
      curImage = op.filter(curImage, null);
    else
      curImage = op.filter(mBufferedImage, null);

    ImageDisplay.removeAll();
    ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
    pack();
    deepCopyerino(curImage);
  }//end blueInvert

  //end MOAR filtering functions
  
  
  
  
  
  //*************and even more helper functions*******
  //OCD.... should actulaly be CDO, cause then the letters are in order :)
  public final int displayImageWidth = 800;
  public final int displayImageHeight = 800;

  public void resizeToScale()
  {
    int height = mBufferedImage.getHeight();
    int width = mBufferedImage.getWidth();
    double hwRatio = (double) height / (double) width;

    if( height > displayImageHeight )
    {
      height = displayImageHeight;
      width = (int) (height / hwRatio);
    }
    if( width > displayImageWidth )
    {
      width = displayImageWidth;
      height = (int) (width * hwRatio);
    }

    Image tmp = mBufferedImage.getScaledInstance(width, height,
        BufferedImage.SCALE_FAST);
    mBufferedImage = new BufferedImage(width, height,
        BufferedImage.TYPE_INT_RGB);
    mBufferedImage.getGraphics().drawImage(tmp, 0, 0, null);
    
//    JFrame win = new JFrame();
//    win.add(new JLabel(new ImageIcon(tmp)));
//    win.pack();
//    win.setVisible(true);
    
    ImageDisplay.setMinimumSize(new Dimension(width, height));
    setMinimumSize(new Dimension(width, height));
    
    System.out.println(mBufferedImage.getHeight());
    System.out.println(mBufferedImage.getWidth());

    
    image = new ImageProcessor();
    Reset.setEnabled(true);
  }//end resize to scale
  
  //undo helpers
  private void removeUndoHistory(int numTimes) {
     for (int i = 0 ; i < numTimes; i++) {
        queue.removeElementAt(queueSize);
        queueSize--;
        System.out.println(queueSize);
     }
  }//end undoHistory
  private void clearUndoHistory() {
     queue = new Vector<BufferedImage>(UNDO_MAX);
     queueSize = 0;
     Undo.setEnabled(false);
     Redo.setEnabled(false);
  }//end clearUndo
  
  
  public void undo() {
     if( !queue.isEmpty() )
     {
       queueSize--;
       curImage = deepCopy(queue.elementAt(queueSize));
       ImageDisplay.removeAll();
       ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
       pack();
     }
     if( queueSize == 0 )
       Undo.setEnabled(false);
     Redo.setEnabled(true);
  }//end undo
  
  public void redo() {
     queueSize++;
     curImage = deepCopy(queue.elementAt(queueSize));
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(curImage)));
     pack();
     if( queueSize == queue.size() )
       Redo.setEnabled(false);
     Undo.setEnabled(true);
  }//end redo
  
  public void reset() {
     ImageDisplay.removeAll();
     ImageDisplay.add(new JLabel(new ImageIcon(mBufferedImage)));
     curImage = deepCopy(mBufferedImage);
     deepCopyerino(curImage);
     pack();
  }//end reset
  //end even more helper functions
  
  
  //***************getter functions********************
  public JComboBox<String> getUtilityFilterComboBox() 
  {
     return UtilityFilters;
  }
  
  public JComboBox<String> getCustomFiltersComboBox()
  {
     return CustomFilters;
  }
  public JComboBox<String> getPresetFiltersComboBox()
  {
     return PresetFilters;
  }
  public JCheckBox getStackCheckBox() 
  {
     return stackFilter;
  }
  public BufferedImage getCurImage() {
     return curImage;
  }
  //end getter functions
}
