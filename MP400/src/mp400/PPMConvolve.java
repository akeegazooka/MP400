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
        //PPMFile imageOut = new PPMFile(inImage);
        System.out.println("Dimensions " + inImage.dimensions.getX() + " " + inImage.dimensions.getY());
        PPMFile imageOut = new PPMFile(inImage.dimensions.getX(), inImage.dimensions.getY(), 255, "P3");
        
        int width = imageOut.dimensions.getX();
        int height = imageOut.dimensions.getY();
        
        int kWidth = inMask.kernel.length;
        int kHeight = inMask.kernel[0].length;
        
        for(int iY = 0;iY<height;iY++)
        {
            for(int iX = 0;iX<width;iX++)
            {
                boolean wtf = (iX > 120 && iX < 125 && iY == 10);
                PixRGB newPix = new PixRGB();
                for(int kY = 0;kY<kHeight;kY++)
                {
                    for(int kX = 0;kX<kWidth;kX++)
                    {
                        int fetchX = clamp( (iX+kX - kWidth/2), 0, width-1 );
                        int fetchY = clamp( (iY+kY - kHeight/2), 0, height-1);
                       // fetchX = clamp(iX, 50, 100);
                       // fetchY = clamp(iY, 50, 100);
                        
                        PixRGB  mulPixel = inImage.getAt(fetchX,fetchY);
                        //if(iX == 123 && iY == 123) {
                        //    System.out.println("kX: "+kX+", kY: "+kY +"\nfetchX "+ fetchX + " FetchY "+fetchY+ "\nMulpix R " + mulPixel.r + "\nkernel[kx][ky] " + inMask.kernel[kX][kY] + "\nNewpix r " + newPix.r);
                            
                       // }
                        
                        newPix.r += mulPixel.r * inMask.kernel[kX][kY];
                        newPix.g += mulPixel.g * inMask.kernel[kX][kY];
                        newPix.b += mulPixel.b * inMask.kernel[kX][kY];
                    }
                }
                newPix = pixAbs(newPix);
                imageOut.setAt(iX,iY, newPix,wtf);
            }
        }
        
        return imageOut;
    }
  
    
    private static int clamp(int val, int min, int max)
    {
        if(val < min) { return min; }
        if(val > max) { return max; }
        return val;
}
//    private static int clamp(int inVal, int minVal, int maxVal)
//    {
//         return Math.max(minVal, Math.min(maxVal, inVal));
//
//    }
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
    
    public static Double[][] generateGaussKernel(Double sigma)
    {
        Integer radius = (int)Math.round(sigma)*3;
        Double[][] kernel = new Double[radius][radius];
        Double sum = 0d;
        for (int y = 0; y < radius; y++)
        {
            for (int x = 0; x < radius; x++)
            {
                int xx = x - radius / 2;
                int yy = y - radius / 2;
                kernel[x][y] = Math.pow(Math.E, -(xx * xx + yy * yy) / (2 * (sigma*sigma)));
                sum += kernel[x][y];
            }
        }
        for (int y = 0; y < radius; y++) 
        {
            for (int x = 0; x < radius; x++)
            {
                kernel[x][y] /= sum;
            }
        }
        return kernel;
    }
}
