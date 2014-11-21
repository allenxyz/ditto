package eecs285.proj4;

void valencia(rgb val){
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
}

void greyscale(rgb val){
  cmyk cmykLayer = rgb2cmyk(val);
  cmykLayer.changeC(0);
  cmykLayer.changeM(0);
  cmykLayer.changeY(0);
}

public hsl rgb2hsl(rgb val){
  int rPrime = val.getR()/255;
  int gPrime = val.getG()/255;
  int bPrime = val.getB()/255;
  int max = max(rPrime, max(gPrime, bPrime));
  int min = min(rPrime, min(gPrime, bPrime));
  int delta = max - min;
  
  int hue;
  int saturation;
  int lightness = (max + min)/2;
  
  if(max == rPrime){
    hue = 60*((gPrime - bPrime)/delta)%6);
  }
  else if(max == gPrime){
    hue = 60*((bPrime - rPrime)/delta + 2);
  }
  else if(max == bPrime){
    hue = 60*((rPrime - gPrime)/delta + 4); 
  }
  
  if(delta == 0){
    saturation = 0;
  }
  else{
    saturation = delta/(1 - abs(2 * lightness - 1));

  return hsl(hue, saturation, lightness);
}

public rgb hsl2rgb(hsl val){





}

public cmyk rgb2cmyk(rgb val){





}



