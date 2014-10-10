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
    
    /**
     *
     */
    public MP2d()
    {
        x = 0;
        y = 0;
    }

    /**
     *
     * @param inX
     * @param inY
     */
    public MP2d(int inX, int inY)
    {
        x = inX;
        y = inY;
    }
    
    /**
     *
     * @param inDim
     */
    public MP2d(MP2d inDim)
    {
        
        x = inDim.getX();
        y = inDim.getY();
    }
    
    /**
     *
     * @param inX
     */
    public void setX(int inX)
    {
        x = inX;
    }
    
    /**
     *
     * @param inY
     */
    public void setY(int inY)
    {
        y = inY;
    }

    /**
     *
     * @return
     */
    public int getX()
    {
        return x;
    }
    
    /**
     *
     * @return
     */
    public int getY()
    {
        return y;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return (""+ x + " " + y);
    }
    
}
