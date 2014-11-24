package eecs285.proj4;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import eecs285.proj4.*;

public class ColorPicker extends JDialog
{

  int clicks;
  JPanel palettePanel;
  JTextField redPal;
  JTextField greenPal;
  JTextField bluePal;

  public ColorPicker(final int numColors)
  {

    setTitle("Pick your Color Block colors");
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

    JPanel paletteText = new JPanel();
    JLabel red = new JLabel("R: ");
    redPal = new JTextField(5);
    JLabel green = new JLabel("G: ");
    greenPal = new JTextField(5);
    JLabel blue = new JLabel("B: ");
    bluePal = new JTextField(5);
    JButton Apply = new JButton("Apply");
    Apply.addActionListener(new ImageProcessorGUI.binColorApply());
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

    palettePanel.addMouseListener(new PalletClick());
    apply.add(palettePanel);
    apply.add(paletteText);
    add(apply);

    setModal(true);
    setLayout(new FlowLayout());
    pack();
    setVisible(true);


  }

  class PalletClick implements MouseListener
  {
    @Override
    public void mouseClicked(MouseEvent e)
    {
      // TODO: EDIT THIS LISTENER
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
        if( clicks >= ImageProcessorGUI.getNumBins() )
        {
          // THROW EXCEPTION
          return;
        }
        else
        {
          // private Color selectedColors[] = new Color[256];
          ImageProcessorGUI.getSelectedColors()[clicks] = color;
          System.out.println(String.valueOf(ImageProcessorGUI
              .getSelectedColors()[clicks].getRGB()));

        }
        getRed = color.getRed();
        getGreen = color.getGreen();
        getBlue = color.getBlue();
        clicks = clicks + 1;
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

    @Override
    public void mousePressed(MouseEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
      // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
      // TODO Auto-generated method stub

    }
  };


}
