/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

import java.io.IOException;
import static java.lang.Math.pow;
import java.util.ArrayList;


/**
 *
 * @author akeegazooka
 */
public class MP400 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
       int task = 2;
       PPMFile ppmData = null;
       try
       {
            ppmData = new PPMFile("/home/akeegazooka/Desktop/task2/Combos-016.ppm");
       }
       catch(IOException e)
       {
           System.out.println(e.getMessage());
       }
       
       if(task == 1)
       {
            PPMConvolve matrix = new PPMConvolve();
            //Double[][] genGauss = PPMConvolve.generateGaussKernel(2.5d);

            //mexican hat creation.
            //PixMask newMask = new PixMask(Extra.mexicanHat);
            //PixMask mexican = matrix.normalizeMask(newMask);

            //gaussian creation
            //PixMask newMask1 = new PixMask(genGauss);
            //PixMask gaussian = matrix.normalizeMask(newMask1);

            //sobel creation.

            PixMask kSobelX = matrix.normalizeMask(new PixMask(Extra.sobelX));
            PixMask kSobelY = matrix.normalizeMask(new PixMask(Extra.sobelY));


            PPMFile newPpmData;
            PPMFile houghSpace;

            newPpmData = PPMConvolve.median(ppmData, 5);
            //newPpmData = PPMConvolve.median(ppmData, 5);
            newPpmData = MPUtility.imageAdd(matrix.convolve(kSobelX, newPpmData), matrix.convolve(kSobelY, newPpmData));
            newPpmData.threshold(5d);

            newPpmData = PPMConvolve.median(newPpmData, 5);
            HoughTransform houghTransform = new HoughTransform(newPpmData, 180, 0.5);
            PolarLine[] lines = houghTransform.findViableLines(20, .2d);
            PolarLine[] goodLines = houghTransform.filterLines(lines, .3, .4 * Math.sqrt(pow(newPpmData.dimensions.getX(),2) + Math.pow(newPpmData.dimensions.getY(), 2)));
            //newPpmData.drawLines(lines);
            ppmData.drawLines(goodLines);

            //newPpmData= matrix.convolve(mexican, ppmData);
       

            if(ppmData!=null)
            {
                 //newFile.writePPM("original.ppm");
                 newPpmData.writePPM("out.ppm");
                 ppmData.writePPM("original.ppm");
                 houghTransform.manifestHoughSpace().writePPM("HOUGH.ppm");

            }
       }
       else if (task == 2)
       {
           PPMFile task2PPM = new PPMFile(ppmData);
           task2PPM.writePPM("OriginalTask2.ppm");
           task2PPM = PPMConvolve.median(task2PPM, 5);
           task2PPM.removeColour(new PixHSV(10d, 0.05d, 0.2d),new PixHSV(52d, 0.25d, 0.95d), new PixRGB(0,0,0), true);
           task2PPM.removeColour(new PixHSV(30d, .82d, .42d),new PixHSV(52d, 1d, .92d), new PixRGB(255,255,255), false);
           int[][] labelSpace = ConnectedComponents.label(task2PPM);
           for(int ii = 0; ii < labelSpace[0].length;ii++)
           {
               for(int jj = 0; jj < labelSpace.length;jj++)
               {
                   System.out.println("Label: " + labelSpace[jj][ii]);
                   //task2PPM.setAt(jj, ii, PixHSV.convertToRGB( new PixHSV ( (360 * labelSpace[jj][ii]/ labelSpace.length * labelSpace[0].length) ,5,.5) ));
               }
           }
           //360 * label / labelSpace.size
           
           
           task2PPM.writePPM("task2.ppm");
       }

       
    }
    
}
