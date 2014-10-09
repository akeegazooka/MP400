/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp400;

import static sun.org.mozilla.javascript.ScriptRuntime.typeof;

/**
 *
 * @author akeegazooka
 */
public class PixHSV extends pixAbstract
{

    private double hue;
    private double sat;
    private double val;

    public PixHSV()
    {
        hue = 0d;
        sat = 0d;
        val = 0d;
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
        double outR = 0;
        double outG = 0;
        double outB = 0;
        
        double h = inHSV.getHue();
        double s = inHSV.getSat();
        double v = inHSV.getVal();
        PixRGB outPix = new PixRGB();
        double c = v * s;
        double hPrime = h /60;
        
        double x = c * (1 - Math.abs(hPrime % 2d -1));
        if(hPrime >= 0d && hPrime < 1d)
        {
            outR = c;
            outG = x;
            outB = 0d;
        }
        else if (hPrime >= 1d && hPrime < 2d)
        {
            outR = x;
            outG = c;
            outB = 0d;           
        }
        else if (hPrime >= 2d && hPrime < 3d)
        {
            outR = 0d;
            outG = c;
            outB = x;
        }
        
        else if(hPrime >= 3d && hPrime < 4d)
        {
            outR = 0d;
            outG = x;
            outB = c;
        }
        else if(hPrime >= 4d && hPrime < 5d)
        {
            outR = x;
            outG = 0d;
            outB = c;
        }
         else if(hPrime >= 5d && hPrime < 6d)
         {
            outR = c;
            outG = 0d;
            outB = x;
         }
        double m = v - c;
        outR+=m;
        outG+=m;
        outB+=m;
            
/*        double r,g,b,c,x,m,rPrime = 0,gPrime = 0,bPrime = 0,outR,outG,outB = 0d;
        
        c = inHSV.getVal() * inHSV.getSat();
        x = c * (1 - Math.abs( (inHSV.getHue()/60d)  % 2d - 1));
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
            rPrime = 0d;
            gPrime = c;
            bPrime = x;
        }
        else if( (inHSV.getHue() <= 180) && (inHSV.getHue() < 240) )
        {
            //0,x,c
            rPrime = 0d;
            gPrime = c;
            bPrime = x;
        }
        else if( (inHSV.getHue() <= 240) && (inHSV.getHue() < 300) )
        {
            //x,0,c
            rPrime = x;
            gPrime = 0d;
            bPrime = c;
        }
        else if( (inHSV.getHue() <= 300) && (inHSV.getHue() < 360) )
        {
            //c,0,x
            rPrime = c;
            gPrime = 0d;
            bPrime = x;
        }
        
        outR = rPrime +m;
        outG = gPrime+m;
        outB = bPrime+m;
        */
        
        
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
