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
public class PPMConvolve {
    PixMask mask;
    PixMask normalMask;
    PPMFile imageData;
    
    final private Double[][] gauss = { {1d,2d,1d},
                                 {2d,4d,2d},
                                 {1d,2d,1d}
                               };
    
    public PPMFile convolve(PixMask inMask, PPMFile inImage)
    {
        imageData = inImage;
        PPMFile imageOut = new PPMFile(imageData);
        int xOffset = 0;
        int yOffset = 0;
        if(inMask.dimensions.getX() % 2 != 0)
            xOffset = (int)Math.floor( (float)inMask.dimensions.getX()/2.0 );
        if(inMask.dimensions.getY() % 2 != 0)
            yOffset = (int)Math.floor( (float)inMask.dimensions.getY()/2.0 );
        
        for(int iX = 0; iX < imageData.dimensions.getX();iX++)
        {
            for(int iY = 0; iY < imageData.dimensions.getY();iY++)
            {
                for (Double[] kernel : inMask.kernel) 
                {
                    for (Double item : inMask.kernel[0])
                    {
                        
                    }      
          
                }
            }
        }
        return imageData;
    }
    
    public PixMask normalizeMask(PixMask inMask)
    {
        PixMask normMask = new PixMask(inMask.kernel);
        double runningTotal = 0;
        for(int mX = 0;mX<inMask.dimensions.getX();mX++)
        {
            for(int mY = 0;mY<inMask.dimensions.getY();mY++)
            {
                runningTotal+= Math.abs(inMask.kernel[mX][mY]);
            }
        }
       for(int nX=0;nX<inMask.dimensions.getX();nX++)
       {
           for(int nY=0;nY<inMask.dimensions.getY();nY++)
           {
               normMask.kernel[nX][nY] = inMask.kernel[nX][nY] / runningTotal;
           }
       }
               
        
        return normMask;
    }
    
    private PixRGB rangeCheck(int inX, int inY)
    {
        retX = 0;
        if(inX < 0)
        {
            retX = 0;
        }
        else if(x > i)
        PixRGB returnPixel;
    }
}
