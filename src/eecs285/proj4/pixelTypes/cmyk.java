package eecs285.proj4.pixelTypes;

public class cmyk{
  private int c;
  private int m;
  private int y;
  private int k;


  public cmyk(){
    c = 0;
    m = 0;
    y = 0;
    k = 0;
  };
  
  public cmyk(int cIn, int mIn, int yIn, int kIn){
    c = cIn;
    m = mIn;
    y = yIn;
    k = kIn;
  }
  
  public void changeC(int change){
    c = change;
  }
  
  public void changeM(int change){
    m = change;
  }
  
  public void changeY(int change){
    y = change;
  }
  
  public void changeK(int change){
    k = change;
  }
  
  public int getC(){
    return c;
  }
  
  public int getM(){
    return m;
  }
  
  public int getY(){
    return y;
  }
  
  public int getK(){
    return k;
  }
}