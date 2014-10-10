
package mp400;


/**
 *
 * @author Keegan Ott
 */
public class PixHSV extends pixAbstract
{

    private double hue;
    private double sat;
    private double val;

    /**
     *
     */
    public PixHSV()
    {
        hue = 0d;
        sat = 0d;
        val = 0d;
    }
    
    /**
     *
     * @param inPix
     */
    public PixHSV(PixHSV inPix)
    {
        hue = inPix.getHue();
        sat = inPix.getSat();
        val = inPix.getVal();

    }
    
    /**
     *
     * @param inHue
     * @param inSat
     * @param inVal
     */
    public PixHSV(double inHue, double inSat, double inVal)
    {
        hue = inHue;
        sat = inSat;
        val = inVal;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return (hue + ", " + sat + ", " + val );
    }

    /**
     *
     * @return
     */
    @Override
    public pixAbstract getPixData() {
        return new PixHSV(this);
    }

    /**
     *
     * @return
     */
    public double getHue() {
        return hue;
    }

    /**
     *
     * @param hue
     */
    public void setHue(double hue) {
        this.hue = hue;

    }

    /**
     *
     * @return
     */
    public double getSat() {
        return sat;
    }

    /**
     *
     * @param sat
     */
    public void setSat(double sat) {
        this.sat = sat;

    }

    /**
     *
     * @return
     */
    public double getVal() {
        return val;
    }

    /**
     *
     * @param val
     */
    public void setVal(double val) {
        this.val = val;
 
    }
    
    /**
     *A function that converts a HSV pixel to an RGB one.
     * implementation is referenced from the internet
     * @param inHSV
     * @return
     */
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
        
        return new PixRGB(outR, outG, outB);
    }
    
    /**
     *equality operation for a HSV Pixel
     * @param inPixel
     * @return
     */
    public boolean equals(PixHSV inPixel)
    {
        return(
            (inPixel.getHue() == this.hue) &&
            (inPixel.getSat() == this.sat) &&
            (inPixel.getVal() == this.val)
              );
    }
}
