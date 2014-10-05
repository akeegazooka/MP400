
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

import java.util.ArrayList;

/**
 *
 * @author akeegazooka
 */
public class HoughTransform {
    private double angleStep;
    private double[] angles;
    private int[][] acc;
    int mostBinVotes;
    //private MP2d [] pointMap;
    
    
    public HoughTransform(PPMFile inImFile, int inPrecision)
    {
        //dimensionality initializers/declarations
        MP2d dimensions = inImFile.getDimensions();
        //System.out.println("Well, i got here: " + dimensions.getX() + " " + dimensions.getY());
        int width = dimensions.getX();
        int height = dimensions.getY();
        MP2d centre = new MP2d(width/2,height/2);
        
        //angle related variables
        double thetaPrecision = Math.PI / inPrecision;
        double[] sinCache = new double[inPrecision];
        double[] cosCache = new double[inPrecision];
        
        //accumulator variables
        int accHeight = (int) ( (Math.sqrt(2) * Math.max(height, width)) /2);
        acc = new int[inPrecision][2 * accHeight];
        
        for(int i = 0;i<inPrecision;i++)
        {
            double theta = i * thetaPrecision;
            sinCache[i] = Math.sin(theta);
            cosCache[i] = Math.cos(theta);
        }
        //pointMap = new MP2d[height * width];
        mostBinVotes = 0;
        for(int yy = 0;yy<height;yy++)
        {
            for(int xx = 0; xx < width;xx++)
            {
                //System.out.println("here too: " + inImFile.getAt(xx, yy).r);
                if(inImFile.getAt(xx, yy).r == 255)
                {
                    //System.out.println("Got here");
                    for(int jj=0;jj<inPrecision;jj++)
                    {
                        int r = (int)  (((xx - centre.getX()) * cosCache[jj]) + ((yy - centre.getY()) * sinCache[jj]));
                        r+= accHeight;
                        if( (r>=0) && (r<accHeight*2) )
                            acc[jj][r]++;
                        //System.out.println("Accessing: " + jj + ", "+ r + "max is: " + inPrecision +", " + 2*accHeight);
                        if(acc[jj][r] >= mostBinVotes)
                            mostBinVotes = acc[jj][r];
                    }
                }
                
            }
        }
    }
    
    public PPMFile manifestHoughSpace()
    {
        int thetaSteps = acc.length;
        int accHeight = acc[0].length;
        
        PPMFile outHoughSpaceImage = new PPMFile(thetaSteps, accHeight, 255, "P3");
        for(int i = 0;i<accHeight;i++)
        {
            for(int j = 0;j<thetaSteps;j++)
            {
               // System.out.println(acc[j][i]);
                //System.out.println( (double) (acc[j][i]) /mostBinVotes * 255) ;
                Double v = new Double(acc[j][i]) / mostBinVotes*255.d;
                PixRGB pixel = new PixRGB(v,v,v);
                //System.out.println(pixel.r +", "+pixel.g+", "+pixel.b);
                outHoughSpaceImage.setAt(j, i, pixel);
            }
        }
        return outHoughSpaceImage;
    }
}
