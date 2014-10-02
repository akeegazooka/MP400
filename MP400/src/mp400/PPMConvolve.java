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
    
    public PPMFile convolve(PixMask inMask, PPMFile inImage)
    {
        PPMFile imageOut = new PPMFile(inImage);
        int width = imageOut.dimensions.getX();
        int height = imageOut.dimensions.getY();
        
        int kWidth = inMask.kernel.length;
        int kHeight = inMask.kernel[0].length;
        
        for(int iY = 0;iY<imageOut.dimensions.getY();iY++)
        {
            for(int iX = 0;iX<imageOut.dimensions.getX();iX++)
            {
                PixRGB newPix = new PixRGB();
                for(int kY = 0;kY<kHeight;kY++)
                {
                    for(int kX = 0;kX<kWidth;kX++)
                    {
                        int fetchX = clamp( (iX+kX - kWidth/2), 0, inImage.dimensions.getX() );
                        int fetchY = clamp( (iY+kY - kHeight/2), 0, inImage.dimensions.getY() );
                        PixRGB  mulPixel = inImage.getAt(fetchX,fetchY);
                        
                        newPix.r += mulPixel.r * inMask.kernel[kX][kY];
                        newPix.g += mulPixel.g * inMask.kernel[kX][kY];
                        newPix.b += mulPixel.b * inMask.kernel[kX][kY];
                    }
                }
                newPix = pixAbs(newPix);
                imageOut.setAt(iX, iY, newPix);
            }
        }
        
        return imageOut;
    }
    
    private static int clamp(int inVal, int minVal, int maxVal)
    {
         return Math.max(minVal, Math.min(maxVal, inVal));

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
    
    
    private PixRGB pixAbs(PixRGB inPix)
    {
        inPix.setR(Math.abs(inPix.getR()));
        inPix.setG(Math.abs(inPix.getG()));
        inPix.setB(Math.abs(inPix.getB()));
        
        return inPix;
    }
}
