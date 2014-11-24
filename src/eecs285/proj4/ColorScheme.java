package eecs285.proj4;

import java.awt.Color;

public class ColorScheme
{
  static Color colors[] = new Color[256];
  static int numbercolors;
  static String name;
  
  public ColorScheme()
  {
    // TODO Auto-generated constructor stub
  }
  
  public ColorScheme(String s, Color colArr[], int numCols){
    System.arraycopy(colArr, 0, colors, 0, colArr.length);
    numbercolors = numCols;
    name = s;
  }
  
  public Color[] getColArr() {
    return colors;
  }
  
  public int getnumber(){
    return numbercolors;
  }
  
  public static void setColorScheme(String scheme, Color colArr[], int numCols){
    if(scheme.equals("Obama") ){
      Color darkBlue = new Color(29, 82, 97);
      Color red = new Color(161, 30, 34);
      Color teal = new Color(86, 151, 163);
      Color beige = new Color(245, 255, 201);
      colArr[0] = darkBlue;
      colArr[1] = red;
      colArr[2] = teal;
      colArr[3] = beige;
      numCols = 4;
    }
    else if(scheme.equals("Umich")){
      Color blue = new Color(0,39,76);
      Color maize = new Color(255, 203, 5);
      colArr[0] = blue;
      colArr[1] = maize;
      numCols = 3;
    }
    else if(scheme.equals("Morgana")){
      Color darkPurp = new Color(0,0,5);
      Color darkSkin = new Color(92,98,156);
      Color lightSkin = new Color(185, 191, 203);
      Color lightPurp = new Color(219,128,245);
      colArr[0] = darkPurp;
      colArr[1] = darkSkin;
      colArr[2] = lightPurp;
      colArr[3] = lightSkin;
      numCols = 4;      
    }
    else if(scheme.equals("Fire")){
      Color black = new Color(0,0,0);
      Color red = new Color(254,23,17);
      Color yellow = new Color(252, 221, 8);
      Color white = new Color(255,255,255);
      colArr[0] = black;
      colArr[1] = red;
      colArr[2] = yellow;
      colArr[3] = white;
      numCols = 4;      
    }
    else if (scheme.equals("Neutral")){
      Color darkest = new Color(57, 50, 42);
      Color brown = new Color(127,109,87);
      Color sand = new Color(227,209,173);
      Color beige = new Color(241, 235, 221);
      colArr[0] = darkest;
      colArr[1] = brown;
      colArr[2] = sand;
      colArr[3] = beige;
      numCols = 4;      
    }
    else if (scheme.equals("Coffee")){
      Color darkBrown = new Color(86, 11, 14);
      Color regBrown = new Color(132, 63, 29);
      Color lightBrown = new Color(173, 134, 86);
      Color highlight = new Color(239, 220, 198);
      colArr[0] = darkBrown;
      colArr[1] = regBrown;
      colArr[2] = lightBrown;
      colArr[3] = highlight;
      numCols = 4; 
    }
  
  }
  
}
