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
       PPMFile ppmData = null;
       try
       {
            ppmData = new PPMFile("/home/akeegazooka/Desktop/8.ppm");
       }
       catch(IOException e)
       {
           System.out.println(e.getMessage());
       }
       
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
       PolarLine[] goodLines = houghTransform.filterLines(lines, .26d, .15 * Math.sqrt(pow(newPpmData.dimensions.getX(),2) + Math.pow(newPpmData.dimensions.getY(), 2)));
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
    
}
