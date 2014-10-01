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
    private double r;
    private double g;
    private double b;
    
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
    
    
    
    @Override
    public String toString()
    {
        return ((int) Math.floor(r)+" "+ (int) Math.floor(g)+" "+ (int) Math.floor(b)+" ");
          
    }
    
    @Override
    public pixAbstract getPixData()
    {
        return this;
    }
    
}
