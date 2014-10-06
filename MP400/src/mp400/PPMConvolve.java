/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
        
        //List<Integer> neighbourhood = new ArrayList<>();
        
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
                
//                System.out.println("---Begin Set---");
//                for (Double rSet1 : rSet) {
//                    System.out.println(rSet1);
//                }
//                System.out.println("---End Set---");
                
                //double outR = rSet.
                imageOut.setAt(x, y, new PixRGB(rMedian,gMedian,bMedian));
            }
        }
        

            
        
        
        
        return imageOut;
        
    }
}
