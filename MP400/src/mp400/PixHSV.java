/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp400;

/**
 *
 * @author akeegazooka
 */
public class PixHSV extends pixAbstract
{

    private  double hue;
    private double sat;
    private double val;

    public PixHSV()
    {
        hue = 0;
        sat = 0;
        val = 0;
    }
    
    public PixHSV(PixHSV inPix)
    {
        hue = inPix.getHue();
        sat = inPix.getSat();
        val = inPix.getVal();
    }
    
    public PixHSV(double inHue, double inSat, double inVal)
    {
        hue = inHue;
        sat = inSat;
        val = inVal;
    }
    
    @Override
    public String toString() {
        return (hue + ", " + sat + ", " + val );
    }

    
    public pixAbstract getPixData() {
        return new PixHSV(this);
    }

    public double getHue() {
        return hue;
    }

    public void setHue(double hue) {
        this.hue = hue;
    }

    public double getSat() {
        return sat;
    }

    public void setSat(double sat) {
        this.sat = sat;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
    
    public static PixRGB convertToRGB(PixHSV inHSV)
    {
        double r,g,b,c,x,m,rPrime = 0,gPrime = 0,bPrime = 0,outR,outG,outB = 0d;
        
        c = inHSV.getVal() * inHSV.getSat();
        x = c * (1 - Math.abs( (inHSV.getHue()/60d)  % 2 - 1));
        m = inHSV.getVal() - c;
        
        
        if( (inHSV.getHue() <= 0) && (inHSV.getHue() < 60) )
        {
            //c,x,0
            rPrime = c;
            gPrime = x;
            bPrime = 0d;
        }
        else if( (inHSV.getHue() <= 60) && (inHSV.getHue() < 120) )
        {
            //x, c, 0
            rPrime = x;
            gPrime = c;
            bPrime = 0d;
        }
        else if( (inHSV.getHue() <= 120) && (inHSV.getHue() < 180) )
        {
            //0,c,x
            rPrime = 0;
            gPrime = c;
            bPrime = x;
        }
        else if( (inHSV.getHue() <= 180) && (inHSV.getHue() < 240) )
        {
            //0,x,c
            rPrime = 0;
            gPrime = c;
            bPrime = x;
        }
        else if( (inHSV.getHue() <= 240) && (inHSV.getHue() < 300) )
        {
            //x,0,c
            rPrime = x;
            gPrime = 0;
            bPrime = c;
        }
        else if( (inHSV.getHue() <= 300) && (inHSV.getHue() < 360) )
        {
            //c,0,x
            rPrime = c;
            gPrime = 0;
            bPrime = x;
        }
        
        outR = rPrime +m;
        outG = gPrime+m;
        outB = bPrime+m;
        
        
        return new PixRGB(outR, outG, outB);
    }
    
    public boolean equals(PixHSV inPixel)
    {
        return(
            (inPixel.getHue() == this.hue) &&
            (inPixel.getSat() == this.sat) &&
            (inPixel.getVal() == this.val)
              );
    }
}
