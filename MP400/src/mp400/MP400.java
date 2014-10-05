/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

import java.io.IOException;
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
       Double[][] genGauss = PPMConvolve.generateGaussKernel(2.5d);
       
       //mexican hat creation.
       PixMask newMask = new PixMask(Extra.mexicanHat);
       PixMask mexican = matrix.normalizeMask(newMask);
       
       //gaussian creation
       PixMask newMask1 = new PixMask(genGauss);
       PixMask gaussian = matrix.normalizeMask(newMask1);
       
       //sobel creation.
       
       PixMask kSobelX = matrix.normalizeMask(new PixMask(Extra.sobelX));
       PixMask kSobelY = matrix.normalizeMask(new PixMask(Extra.sobelY));
     
       
       PPMFile newPpmData;
       newPpmData = matrix.convolve(gaussian, ppmData);
       newPpmData = PPMConvolve.median(ppmData, 5);
       
       newPpmData = MPUtility.imageAdd(matrix.convolve(kSobelX, newPpmData), matrix.convolve(kSobelY, newPpmData));
       //newPpmData= matrix.convolve(mexican, ppmData);
       newPpmData.threshold(5d);

       if(ppmData!=null)
       {
            //newFile.writePPM("original.ppm");
            newPpmData.writePPM("out.ppm");
            ppmData.writePPM("original.ppm");
            
       }

       
    }
    
}
