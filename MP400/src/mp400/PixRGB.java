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
    private int r;
    private int g;
    private int b;
    
    public PixRGB(int inR, int inG, int inB)
    {
        r = inR;
        g = inG;
        b = inB;
    }
    
    public PixRGB()
    {
        r = 0;
        g = 0;
        b = 0;
    }
    
    public PixRGB(PixRGB inRGB)
    {
        r = inRGB.r;
        g = inRGB.g;
        b = inRGB.b;
    }
    
    public int getR()
    {
        return r;
    }
    
    public int getG()
    {
        return g;
    }
    
    public int getB()
    {
        return b;
    }
    
    public void setR(int inR)
    {
        r = inR;
    }
    
    public void setG(int inG)
    {
        g = inG;
    }
    
    public void setB(int inB)
    {
        b = inB;
    }
    
    
    
    @Override
    public String toString()
    {
        return (""+r+" "+ g+" "+b+" ");
          
    }
    
    @Override
    public pixAbstract getPixData()
    {
        return this;
    }
    
}
