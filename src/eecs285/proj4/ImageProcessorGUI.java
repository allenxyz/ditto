package eecs285.proj4;

import java.awt.*;

import javax.swing.*;

public class ImageProcessorGUI extends JFrame
{
  public static ImageProcessorGUI win;
  
  private JMenuItem Open;
  private JMenuItem Save;
  private JMenuItem Exit;
  private JMenuItem EDIT;
  
  
  public static void main(String[] arg){
    win = new ImageProcessorGUI();
   win.setMinimumSize(new Dimension(1120, 600));
    win.pack();

    win.setVisible(true);
    win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    
  }

  public ImageProcessorGUI(){
    
    super("Insta-Paint");
    //setResizable(false);
    
    JPanel apply = new JPanel();
    JPanel EditPalette = new JPanel();
    JPanel ImageDisplay = new JPanel();
    
    JMenuBar Menu = new JMenuBar();
    JMenu File = new JMenu("File");
    JMenu Edit = new JMenu("Edit");
    Open = new JMenuItem("Open Image");
    Save = new JMenuItem("Save Image");
    Exit = new JMenuItem("Exit Program");
    EDIT = new JMenuItem("EDIT???");
    File.add(Open);
    File.add(Save);
    File.add(Exit);
    Edit.add(EDIT);
    Menu.add(File);
    Menu.add(Edit);
    setJMenuBar(Menu);
    
    JButton FilterA = new JButton("Filter A");
    JButton FilterB = new JButton("Filter B");
    EditPalette.add(FilterA);
    EditPalette.add(FilterB);
    add(EditPalette, BorderLayout.WEST);
    //ImageDisplay.setMinimumSize(new Dimension(300,300));
    add(ImageDisplay, BorderLayout.EAST);
    
    //apply.add(Menu, BorderLayout.NORTH);
    
    //add(apply); 
    
    
  }
  
}
