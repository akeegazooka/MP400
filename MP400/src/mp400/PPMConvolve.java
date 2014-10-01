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
        
        
        for(int iY = 0; iY < height;iY++)
        {
            for(int iX = 0; iX < width; iX++)
            {
                //System.out.println("Visiting: "+iX + ", " + iY);
                PixRGB accPix = new PixRGB();
                for (int kY = 0; kY < kHeight; kY++) 
                {
                    for (int kX = 0; kX < kWidth; kX++)
                    {
                       // System.out.println("Visiting: "+iX + kX - inMask.kernel.length/2 + ", " + kY);
                       // System.out.println("Max dimensions are :" + imageOut.dimensions.getX() + " " + imageOut.dimensions.getY() + "\nFetching From: "  );
                        int fetchX = iX + kX - kWidth  /2;
                        int fetchY = iY + kY - kHeight /2;
                        
                        PixRGB mulPix = (PixRGB) inImage.getAt(fetchX, fetchY);
                       // System.out.println(String.format("setting to x: %d y: %d\nkX: %d kY %d\nMultiplying the pixel %f,%f,%f by: %f\nusing the pixel located at %d, %d", iX, iY, kX, kY,mulPix.getR(), mulPix.getG(), mulPix.getB(), inMask.kernel[kX][kY], (iX + kX - inMask.kernel.length/2), (iY + kY - inMask.kernel[0].length/2) ));
                        //accPix = mulPixel(accPix, mulPix, inMask.kernel[kX][kY]);
                        accPix.setR( (accPix.getR() + (mulPix.getR() * inMask.kernel[kX][kY]) ) );
                        accPix.setG( (accPix.getG() + (mulPix.getG() * inMask.kernel[kX][kY]) ) );
                        accPix.setB( (accPix.getB() + (mulPix.getB() * inMask.kernel[kX][kY]) ) );
                        
                        //imageOut.setAt(iX, iY, mulPixel( (PixRGB) inImage.getAt(iX, iY), inMask.kernel[kX][kY]));
                        //System.out.println("Image coords: " + iX + ", " + iY + "\nKernel Coords: " + kX +", " + kY);
                        
                    }      
          
                }
                accPix = pixAbs(accPix);
                imageOut.setAt(iX, iY, accPix);
               // System.out.println("Writing pixel: " + accPix.getR()+ " " +accPix.getG()+" "+ accPix.getB()+" to "+ iX + ", " + iY);
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
