package eecs285.proj4.pixelTypes;

public class hsl{
  private int h;
  private int s;
  private int l;


  public hsl(){
    h = 0;
    s = 0;
    l = 0;
  };
  
  public hsl(int hIn, int sIn, int lIn){
    h = hIn;
    s = sIn;
    l = lIn;
  }
  
  public void changeH(int change){
    h = change;
  }
  
  public void changeS(int change){
    s = change;
  }
  
  public void changeL(int change){
    l = change;
  }
  
  public int getH(){
    return h;
  }
  
  public int getS(){
    return s;
  }
  
  public int getL(){
    return l;
  }
}