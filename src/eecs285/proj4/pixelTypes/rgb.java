package eecs285.proj4.pixelTypes;

public class rgb{
  private int r;
  private int g;
  private int b;

  public rgb(){
    r = 0;
    g = 0;
    b = 0;
  }
  public rgb(int pixelColor) 
  {
     r = (int)pixelColor >> 16 & 0xfff;
     g = (int)pixelColor >> 8 & 0xfff;
     b = (int)pixelColor & 0xfff;
  }
  public rgb(int rIn, int gIn, int bIn){
    r = rIn;
    g = gIn;
    b = bIn;
  }
  
  public void changeR(int change){
    r = change;
  }
  
  public void changeG(int change){
    g = change;
  }
  
  public void changeB(int change){
    b = change;
  }
  
  public int getR(){
    return r;
  }
  
  public int getG(){
    return g;
  }
  
  public int getB(){
    return b;
  }
}