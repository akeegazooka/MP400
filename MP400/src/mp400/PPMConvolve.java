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
                PixRGB accPix = new PixRGB();
                for (int kX = 0; kX<inMask.kernel.length;kX++) 
                {
                    for (int kY = 0; kY<inMask.kernel[0].length;kY++)
                    {
                       // System.out.println("Max dimensions are :" + imageOut.dimensions.getX() + " " + imageOut.dimensions.getY() + "\nFetching From: "  );
                        PixRGB mulPix = (PixRGB) imageData.getAt(iX + kX - inMask.kernel.length/2, iY + kY - inMask.kernel[0].length);
                        accPix = mulPixel(accPix, mulPix, inMask.kernel[kX][kY]);
                        //imageOut.setAt(iX, iY, mulPixel( (PixRGB) inImage.getAt(iX, iY), inMask.kernel[kX][kY]));
                        //System.out.println("Image coords: " + iX + ", " + iY + "\nKernel Coords: " + kX +", " + kY);
                        
                    }      
          
                }
                accPix = pixAbs(accPix);
                imageOut.setAt(iX, iY, accPix);
                //set output pixel to acc here
            }
        }
        return imageOut;
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
    
    private PixRGB mulPixel(PixRGB accPix, PixRGB inPixel, double mul)
    {
        PixRGB outPixel = new PixRGB(inPixel);
        outPixel.setR(accPix.getR() + (int) Math.floor(inPixel.getR() * mul));
        outPixel.setG(accPix.getG() + (int) Math.floor(inPixel.getG() * mul));
        outPixel.setB(accPix.getB() + (int) Math.floor(inPixel.getB() * mul));
        return outPixel;
    }
    
    private PixRGB pixAbs(PixRGB inPix)
    {
        inPix.setR(Math.abs(inPix.getR()));
        inPix.setG(Math.abs(inPix.getG()));
        inPix.setB(Math.abs(inPix.getB()));
        
        return inPix;
    }
//    
//    private PixRGB rangeCheck(int inX, int inY)
//    {
//        retX = 0;
//        if(inX < 0)
//        {
//            retX = 0;
//        }
//        else if(x > i)
//        PixRGB returnPixel;
//    }
}
