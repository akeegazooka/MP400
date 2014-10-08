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
public class PixRGB extends pixAbstract {
    public double r;
    public double g;
    public double b;
    
    public PixRGB(double inR, double inG, double inB)
    {
        r = inR;
        g = inG;
        b = inB;
    }
    
    public PixRGB()
    {
        r = 0d;
        g = 0d;
        b = 0d;
    }
    
    public PixRGB(PixRGB inRGB)
    {
        r = inRGB.r;
        g = inRGB.g;
        b = inRGB.b;
    }
    
    public double getR()
    {
        return r;
    }
    
    public double getG()
    {
        return g;
    }
    
    public double getB()
    {
        return b;
    }
    
    public void setR(double inR)
    {
        r = inR;
    }
    
    public void setG(double inG)
    {
        g = inG;
    }
    
    public void setB(double inB)
    {
        b = inB;
    }
    
    public static PixHSV convertToHSV(PixRGB inRGB)
    {
        double rPrime = (inRGB.r/255d);
        double gPrime = (inRGB.g/255d);
        double bPrime = (inRGB.b/255d);
        
        double cMax = Math.max(Math.max(rPrime, gPrime),bPrime);
        double cMin = Math.min(Math.min(rPrime, gPrime),bPrime);
        double deltaC = cMax - cMin;
        
        double h = 0;
        if(cMax == rPrime)
        {
            h = ( ((gPrime - bPrime)/deltaC) % 6 );
        }
        else if(cMax == gPrime)
        {
            h =  ( (bPrime - rPrime)/deltaC) + 2d;
        }
        else if(cMax == bPrime)
        {
            h = ((rPrime - gPrime) / deltaC) + 4d;
        }
        h*=60d;
        
        double s = 0;
        
        if(deltaC == 0d)
            s = 0;
        else
            s = deltaC / cMax;
        
        double v = cMax;
        
        
        PixHSV outHSV = new PixHSV(h,s,v);
        
        return outHSV;
    }
    
    public boolean equals(PixRGB inPixel)
    {
        return( (inPixel.getR() == this.r) &&
            (inPixel.getG() == this.g) &&
            (inPixel.getB() == this.b)
          );
    }
    
    @Override
    public String toString()
    {
        return ((int) Math.floor(r)+" "+ (int) Math.floor(g)+" "+ (int) Math.floor(b)+" ");
          
    }
    
    @Override
    public pixAbstract getPixData()
    {
        return new PixRGB(this);
    }
    
    
    
}
