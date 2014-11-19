package eecs285.proj4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ImageProcessorGUI extends JFrame
{
  public static ImageProcessorGUI win;
  
  private JMenuItem Open;
  private JMenuItem Save;
  private JMenuItem Exit;
  private JMenuItem Undo;
  private JMenuItem Redo;
  
 
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
  
  public static void main(String[] arg){
    win = new ImageProcessorGUI();
   win.setMinimumSize(new Dimension(1120, 650));
    win.pack();

    win.setVisible(true);
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
  }

  public ImageProcessorGUI(){
    
    super("Insta-Paint");
    //setResizable(false);
    
    JPanel EditPalette = new JPanel();
    JPanel ColorBlock = new JPanel();
    JPanel InstaFilter = new JPanel();
    JPanel Custom = new JPanel();
    JPanel ImageDisplay = new JPanel();
    
    JMenuBar Menu = new JMenuBar();
    JMenu File = new JMenu("File");
    JMenu Edit = new JMenu("Edit");
    Open = new JMenuItem("Open Image");
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
    
    //JButton FilterA = new JButton("Filter A");
    //JButton FilterB = new JButton("Filter B");
    //EditPalette.add(FilterA);
    //EditPalette.add(FilterB);
    JPanel ColorTop = new JPanel();
    //ColorTop.setLayout(new BoxLayout(ColorTop, BoxLayout.PAGE_AXIS));
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
    //ColorD.setVisible(false);
    ColorE = new JTextField(7);
    //ColorE.setVisible(false);
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
    
    JButton NoFilter = new JButton( new ImageIcon("../../Cube.jpg"));
    NoFilter.setMargin(new Insets(0, 0,0,0));
    JButton FilterA = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterA.setMargin(new Insets(0,0,0,0));
    JButton FilterB = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterB.setMargin(new Insets(0,0,0,0));
    JButton FilterC = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterC.setMargin(new Insets(0,0,0,0));
    JButton FilterD = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterD.setMargin(new Insets(0,0,0,0));
    JButton FilterE = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterE.setMargin(new Insets(0,0,0,0));
    JButton FilterF = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterF.setMargin(new Insets(0,0,0,0));
    JButton FilterG = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterG.setMargin(new Insets(0,0,0,0));
    JButton FilterH = new JButton(new ImageIcon("../../CubeA.jpg"));
    FilterH.setMargin(new Insets(0,0,0,0));
    
    JPanel Instawrap = new JPanel(new GridLayout(3,3));
    TitledBorder FilterTitle = new TitledBorder("Filter Palette");
    InstaFilter.setBorder(FilterTitle);
    Instawrap.add(NoFilter);
    Instawrap.add(FilterA);
    Instawrap.add(FilterB);
    Instawrap.add(FilterC);
    Instawrap.add(FilterD);
    Instawrap.add(FilterE);
    Instawrap.add(FilterF);
    Instawrap.add(FilterG);
    Instawrap.add(FilterH);
    InstaFilter.add(Instawrap);
    
    TitledBorder CustomTitle = new TitledBorder("Custom Settings");
    JButton CustomBlock = new JButton("Custom Color Block");
    JButton CustomFilter = new JButton("Custom Filter");
    Custom.add(CustomBlock);
    Custom.add(CustomFilter);
    Custom.setBorder(CustomTitle);
    
    
    EditPalette.setLayout(new BoxLayout(EditPalette, BoxLayout.PAGE_AXIS));
    EditPalette.add(ColorBlock);
    EditPalette.add(InstaFilter);
    EditPalette.add(Custom);
    
    JPanel EditWrap = new JPanel();
    EditWrap.add(EditPalette);
    
    
    
    add(EditWrap, BorderLayout.WEST);
    //ImageDisplay.setMinimumSize(new Dimension(300,300));
    add(ImageDisplay, BorderLayout.EAST);
    
    //apply.add(Menu, BorderLayout.NORTH);
    
    //add(apply); 
    
    
  }
  
  class ColorNumSelect implements ActionListener{

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
      
      if (e.getActionCommand() == "three"){
        ColorMiddleR2.setVisible(false);
        ColorMiddleR3.setVisible(false);
        
      }else if( e.getActionCommand() == "five"){
        ColorMiddleR2.setVisible(true);
        ColorMiddleR3.setVisible(false);
        
      }else if(e.getActionCommand()=="seven"){
        ColorMiddleR2.setVisible(true);
        ColorMiddleR3.setVisible(true);
        
      }
      
    }
    
  }
  
}
