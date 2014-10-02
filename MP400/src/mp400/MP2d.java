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
public class MP2d {
    private int x;
    private int y;
    
    public MP2d()
    {
        x = 0;
        y = 0;
    }
    public MP2d(int inX, int inY)
    {
        x = inX;
        y = inY;
    }
    
    public MP2d(MP2d inDim)
    {
        
        x = inDim.getX();
        y = inDim.getY();
    }
    
    public void setX(int inX)
    {
        x = inX;
    }
    
    public void setY(int inY)
    {
        y = inY;
    }
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    @Override
    public String toString()
    {
        return (""+ x + " " + y);
    }
    
}
