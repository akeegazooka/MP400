

package mp400;

/**
 *
 * @author Keegan Ott
 */
public class PixRGB extends pixAbstract {

    public double r;
    public double g;
    public double b;
    
    /**
     *
     * @param inR
     * @param inG
     * @param inB
     */
    public PixRGB(double inR, double inG, double inB)
    {
        r = inR;
        g = inG;
        b = inB;
    }
    
    /**
     *
     */
    public PixRGB()
    {
        r = 0d;
        g = 0d;
        b = 0d;
    }
    
    /**
     *
     * @param inRGB
     */
    public PixRGB(PixRGB inRGB)
    {
        r = inRGB.r;
        g = inRGB.g;
        b = inRGB.b;
    }
    
    /**
     *
     * @return
     */
    public double getR()
    {
        return r;
    }
    
    /**
     *
     * @return
     */
    public double getG()
    {
        return g;
    }
    
    /**
     *
     * @return
     */
    public double getB()
    {
        return b;
    }
    
    /**
     *
     * @param inR
     */
    public void setR(double inR)
    {
        r = inR;
    }
    
    /**
     *
     * @param inG
     */
    public void setG(double inG)
    {
        g = inG;
    }
    
    /**
     *
     * @param inB
     */
    public void setB(double inB)
    {
        b = inB;
    }
    
    /**
     *A function to convert from an RGB Pixel to a HSV one
     * referenced ffrom the internet
     * @param inRGB
     * @return
     */
    public static PixHSV convertToHSV(PixRGB inRGB)
    {
        double rPrime = (inRGB.r/255d);
        double gPrime = (inRGB.g/255d);
        double bPrime = (inRGB.b/255d);
        
        double cMax = Math.max(Math.max(rPrime, gPrime),bPrime);
        double cMin = Math.min(Math.min(rPrime, gPrime),bPrime);
        double deltaC = cMax - cMin;
        
        double h = 0;
        if(deltaC == 0)
        {
            h = 0d;
        }
        else if(cMax == rPrime)
        {
            h = ( ( (gPrime - bPrime)/deltaC) % 6 );
            //System.out.println("calc [" + h + " delta: " +deltaC + "] " + "\ngprime "+ gPrime + " bPrime "+bPrime + "gPrime - bPrime: ["+(gPrime-bPrime)+"]" );
        }
        else if(cMax == gPrime)
        {
            h =  ( ( (bPrime - rPrime)/deltaC) + 2d);
            //System.out.println("Calulating at cMax = gPrime");
        }
        else if(cMax == bPrime)
        {
            h = (((rPrime - gPrime) / deltaC) + 4d);
            //System.out.println("Calulating at cMax = bPrime");
        }
        
        h*=60d;
        if(h <= 0)
            h+= 360;
        
        double s = 0;
        
        if(deltaC == 0d)
            s = 0;
        else
            s = deltaC / cMax;
        
        double v = cMax;
        
        //System.out.println("In value: " + inRGB.getR() + ", " + inRGB.getG() +", " + inRGB.getB() + "Out hue: " + h);
        PixHSV outHSV = new PixHSV(h,s,v);
        
        return outHSV;
    }
    
    /**
     *
     * @param inPixel
     * @return
     */
    public boolean equals(PixRGB inPixel)
    {
        return( (inPixel.getR() == this.r) &&
            (inPixel.getG() == this.g) &&
            (inPixel.getB() == this.b)
          );
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return ((int) Math.floor(r)+" "+ (int) Math.floor(g)+" "+ (int) Math.floor(b)+" ");
          
    }
    
    /**
     *
     * @return
     */
    @Override
    public pixAbstract getPixData()
    {
        return new PixRGB(this);
    }
    
    
    
}
