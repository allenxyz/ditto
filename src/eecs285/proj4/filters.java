package eecs285.proj4;
import eecs285.proj4.pixelTypes.*;
import java.lang.Math.*;

public class filters
{

  static void valencia(rgb val)
  {
    hsl hslLayer = rgb2hsl(val);
    hslLayer.changeL(hslLayer.getL() + 12);
    //should I do a soft light blend? I could :^) but it'd be hella hard
    //then I have to somehow convert the midtones @__@?
    rgb rgbLayer = hsl2rgb(hslLayer);
    rgbLayer.changeR(rgbLayer.getR() + 17);
    rgbLayer.changeG(rgbLayer.getG() - 17);
    rgbLayer.changeB(rgbLayer.getB() - 20);
    hslLayer = rgb2hsl(rgbLayer);
    hslLayer.changeS(hslLayer.getS() - 20);
    //then somehow add noise, what the fuk
    rgb temp = hsl2rgb(hslLayer);
    val.changeR(temp.getR());
    val.changeG(temp.getG());
    val.changeB(temp.getB());
    
  }
  
  static void greyscale(rgb val)
  {
    cmyk cmykLayer = rgb2cmyk(val);
    cmykLayer.changeC(0);
    cmykLayer.changeM(0);
    cmykLayer.changeY(0);
    rgb temp = cmyk2rgb(cmykLayer);
    val.changeR(temp.getR());
    val.changeG(temp.getG());
    val.changeB(temp.getB());
  }
  
  static public hsl rgb2hsl(rgb val)
  {
    int rPrime = val.getR()/255;
    int gPrime = val.getG()/255;
    int bPrime = val.getB()/255;
    int max = Math.max(rPrime, Math.max(gPrime, bPrime));
    int min = Math.min(rPrime, Math.min(gPrime, bPrime));
    int delta = max - min;
    
    if(delta == 0)
    {
       return new hsl(0, 0, 0);
    }
    
    
    int hue = 0;
    int saturation = 0;
    int lightness = (max + min)/2;
    
    if(lightness == 0){
       return new hsl(0, 0, 0);
    }
    
    if(max == rPrime)
    {
      hue = 60*((gPrime - bPrime)/delta) % 6;
    }
    else if(max == gPrime)
    {
      hue = 60*((bPrime - rPrime)/delta + 2);
    }
    else if(max == bPrime)
    {
      hue = 60*((rPrime - gPrime)/delta + 4); 
    }
    
    if(delta == 0)
    {
      saturation = 0;
    }
    else
    {
      saturation = delta/(1 - Math.abs(2 * lightness - 1));
    }
    
      return new hsl(hue, saturation, lightness);
    }
  
  static public rgb hsl2rgb(hsl val)
  {
    int c = (1 - Math.abs(2 * val.getL())) * val.getS();
    int x = c * (1 - Math.abs((val.getH()/60) % 2 - 1));
    int m = val.getL() - c/2;
    
    int red = 0;
    int green = 0;
    int blue = 0;
    
    int whatTheHBobby = val.getH();
    
    if(whatTheHBobby >= 0 && whatTheHBobby < 60)
    {
      red = c + m;
      green = x + m;
      blue = m;
    }
    else if(whatTheHBobby >= 60 && whatTheHBobby < 120)
    {
      red = x + m;
      green = c + m;
      blue = m;
    }
    else if(whatTheHBobby >= 120 && whatTheHBobby < 180)
    {
      red = m;
      green = c + m;
      blue = x + m;
    }
    else if(whatTheHBobby >= 180 && whatTheHBobby < 240)
    {
      red = m;
      green = x + m;
      blue = c + m;
    }
    else if(whatTheHBobby >= 240 && whatTheHBobby < 300)
    {
      red = x + m;
      green = m;
      blue = c + m;
    }
    else if(whatTheHBobby >= 300 && whatTheHBobby < 360)
    {
      red = c + m;
      green = m;
      blue = x + m;
    }
  
    return new rgb(red, green, blue);
  }
  
  static public cmyk rgb2cmyk(rgb val)
  {
    int rPrime = val.getR()/255;
    int gPrime = val.getG()/255;
    int bPrime = val.getB()/255;
    
    int key = 1 - Math.max(rPrime, Math.max(gPrime, bPrime));
    
    int cyan = (1 - rPrime - key)/(1 - key);
    int magneta = (1 - gPrime - key)/(1 - key);
    int yellow = (1 - bPrime - key)/(1 - key);
    
    return new cmyk(cyan, magneta, yellow, key);
  }


  static public rgb cmyk2rgb(cmyk val)
  {
    int c = val.getC();
    int m = val.getM();
    int y = val.getY();
    int k = val.getK();
    
    int red = 255 * (1 - c) * (1 - k);
    int green = 255 * (1 - m) * (1 - k);
    int blue = 255 * (1 - y) * (1 - k);
    
    return new rgb(red, green, blue);
  }
}