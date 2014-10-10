

package mp400;

/**
 *
 * @author Keegan Ott
 */
public class PPMConvolve {
    PixMask mask;
    PixMask normalMask;
    
    /**
     *
     * @param inMask A 2d array of any size to be applied to the supplied image.
     * @param inImage the image that will be convolved upon with the supplied mask
     * @return imageOut is a copy of the supplied image modified by the convolution mask
     */
    public static PPMFile convolve(PixMask inMask, PPMFile inImage)
    {
        /**
         * create copy of given image
        */
        PPMFile imageOut = new PPMFile(inImage.dimensions.getX(), inImage.dimensions.getY(), 255, "P3");
        
        int width = imageOut.dimensions.getX();
        int height = imageOut.dimensions.getY();
        
        int kWidth = inMask.kernel.length;
        int kHeight = inMask.kernel[0].length;
        /**
         * for every pixel in the image loop through the surrounding
         * neighbourhood of pixels calculating the current pixel's new
         * value based around them.
         */
        for(int iY = 0;iY<height;iY++)
        {
            for(int iX = 0;iX<width;iX++)
            {
                PixRGB newPix = new PixRGB();
                for(int kY = 0;kY<kHeight;kY++)
                {
                    for(int kX = 0;kX<kWidth;kX++)
                    {
                        int fetchX = Extra.clampInt( (iX+kX - kWidth/2), 0, width-1 );
                        int fetchY = Extra.clampInt( (iY+kY - kHeight/2), 0, height-1);
                        PixRGB  mulPixel = inImage.getAt(fetchX,fetchY);
                        newPix.r += mulPixel.r * inMask.kernel[kX][kY];
                        newPix.g += mulPixel.g * inMask.kernel[kX][kY];
                        newPix.b += mulPixel.b * inMask.kernel[kX][kY];
                    }
                }
                newPix = pixAbs(newPix);
                imageOut.setAt(iX,iY, newPix);
            }
        }
        
        return imageOut;
    }
  
    /**
     *A normalized mask is useful because it allows the image pixels that are
     * being operated on to stay within the colour range (in this case 0..255
     * @param inMask a convolution mask to be normalized
     * @return A normalized mask  is useful because it allows the image pixels
     */
    public static PixMask normalizeMask(PixMask inMask)
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
    
    /**
     * changes any negative components to their positive counterparts
     * @param inPix a possibly negative pixel
     * @return a positive pixel
     */
    private static PixRGB pixAbs(PixRGB inPix)
    {
        inPix.setR(Math.abs(inPix.getR()));
        inPix.setG(Math.abs(inPix.getG()));
        inPix.setB(Math.abs(inPix.getB()));
        
        return inPix;
    }
    
    /**
     *a function that can generate multi-sized gauss kernels useful for
     * multiple image operations
     * @param sigma an exponent useful for defining the size and intensity of a gauss kernel
     * @return a variably sized gauss kernel
     */
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
    
    /**
     *The median filter collects a neighbourhood of pixels and sorts each
     * component of those pixels, then find the middle value and uses that
     * as the value for the pixel being operated on, useful for removing speckle
     * noise.
     * @param inFile an image files to be used as a baseline for the median filter
     * @param size the dimensions of the neighbourhood surrounding each pixel
     * to utilize
     * @return a modified copy of the provided image file
     */
    public static PPMFile median(PPMFile inFile, int size)
    {
        int mHeight = size;
        int mWidth = size;
        
        int height =  inFile.dimensions.getY();
        int width =  inFile.dimensions.getX();
        
        int blackTotal = 0;
        int whiteTotal = 0;
        double rMedian = 0d;
        double gMedian = 0d;
        double bMedian = 0d;
        
        Double[] rSet;
        Double[] gSet;
        Double[] bSet;
        
        PPMFile imageOut = new PPMFile(width,height,255,"P3");
        
        for(int y = 0;y<height;y++)
        {
            for(int x = 0;x<width;x++)
            {
                blackTotal = 0;
                whiteTotal = 0;
                
                rSet = new Double[size*size];
                gSet = new Double[size*size];
                bSet = new Double[size*size];
                
                int index = 0;
                for(int j=0;j<mHeight;j++)
                {
                    for(int i = 0;i<mWidth;i++)
                    {
                        PixRGB tempPix = new PixRGB();
                        int fetchX = Extra.clampInt( (x+i - mWidth/2), 0, width-1 );
                        int fetchY = Extra.clampInt( (y+j - mHeight/2), 0, height-1);
                        tempPix = inFile.getAt(fetchX, fetchY);
                        rSet[index] = tempPix.r;
                        gSet[index] = tempPix.g;
                        bSet[index] = tempPix.b;
                        index++;
                    }
                }
                MPUtility.sort(rSet);
                MPUtility.sort(gSet);
                MPUtility.sort(bSet);
                
                if( (rSet.length) % 2 == 0 )
                {
                    rMedian = (rSet[rSet.length/2] + rSet[rSet.length/2] / 2);
                    gMedian = (gSet[rSet.length/2] + gSet[gSet.length/2] / 2);  
                    bMedian = (bSet[rSet.length/2] + bSet[bSet.length/2] / 2);  
                }
                else
                {
                    rMedian = rSet[rSet.length/2];
                    gMedian = gSet[gSet.length/2];
                    bMedian = bSet[bSet.length/2];
                }
                imageOut.setAt(x, y, new PixRGB(rMedian,gMedian,bMedian));
            }
        }
        return imageOut;
        
    }
    
    /**
     *
     * @param inImage baseline image for operations
     * @param neighbourHoodSize number of surrounding pixels to be used
     * @return a copy of the baseline image modified so that each pixel is the maximum
     * value of its neighbours
     */
    public static PPMFile maxFilter(PPMFile inImage, int neighbourHoodSize)
    {
        int height = inImage.dimensions.getY();
        int width = inImage.dimensions.getX();
        
        int nHeight = neighbourHoodSize;
        int nWidth = neighbourHoodSize;
        
        PPMFile outResult = new PPMFile(inImage);
        for (int yy = 0; yy < height;yy++)
        {
            for(int xx = 0; xx < width;xx++)
            {
                double maxHue = 0;
                double maxSat = 0;
                double maxVal = 0;
                PixHSV maxPixel;
                boolean black = true;
                for(int y = 0; y < nHeight;y++)
                {
                    for(int x = 0; x < nWidth;x++)
                    {
                        //System.out.println("uh.");
                        //int fetchX = Extra.clampInt( (x+i - mWidth/2), 0, width-1 );
                        int fetchX = Extra.clampInt(xx + x - nWidth/2, 0, width-1);
                        int fetchY = Extra.clampInt(yy+ y - nHeight/2, 0, height-1);
                        PixHSV tempPix = PixRGB.convertToHSV(inImage.getAt(fetchX, fetchY));
                        if(tempPix.getVal() == 0)
                            black = true;
                        
                    }
                }
                if(black)
                    maxPixel = new PixHSV(0,0,0);
                else
                    maxPixel = new PixHSV(0,0,1);
                outResult.setAt(xx, yy, PixHSV.convertToRGB(maxPixel));
            }
        }
        return outResult;
    }
    
}
