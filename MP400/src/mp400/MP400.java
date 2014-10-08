/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

import java.io.IOException;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
           
           Map<Integer,Blob> blobs = new HashMap<Integer,Blob>();
           
           //background removal run
           task2PPM.removeColour(new PixHSV(10d, 0.05d, 0.2d),new PixHSV(52d, 0.25d, 0.95d), new PixRGB(0,0,0), true);
           task2PPM.writePPM("No_BG.ppm");
           
           //Yellow/Orange range run
           PPMFile orange_yellow = new PPMFile(task2PPM);
           orange_yellow.removeColour(new PixHSV(30d, .82d, .42d),new PixHSV(52d, 1d, .92d), new PixRGB(255,255,255), false);
           blobs.putAll( ConnectedComponents.blobImage(orange_yellow, 0.001, "orange_yellow"));
           orange_yellow.writePPM("Yellow_Orange.ppm");
           
           //darker blue run
           PPMFile dark_blue = new PPMFile(task2PPM);
           dark_blue.removeColour(new PixHSV(180d, .85d, .15d),new PixHSV(235d, 1d, .58d), new PixRGB(255,255,255), false);
           blobs.putAll( ConnectedComponents.blobImage(dark_blue, 0.001, "dark_blue"));
           dark_blue.writePPM("Blue.ppm");
           
           //lighter blue run
           PPMFile light_blue = new PPMFile(task2PPM);
           light_blue.removeColour(new PixHSV(150d, 0d, 0.21d),new PixHSV(250d, 0.5d, .4d), new PixRGB(255,255,255), false);
           blobs.putAll( ConnectedComponents.blobImage(light_blue, 0.001, "light_blue"));
           light_blue.writePPM("Light_Blue.ppm");
           
           //purple
           PPMFile purple = new PPMFile(task2PPM);
           purple.removeColour(new PixHSV(319, .42d, .1d),new PixHSV(355d, .92, .57d), new PixRGB(255,255,255), false);
           blobs.putAll( ConnectedComponents.blobImage(purple, 0.001,"purple"));
           purple.writePPM("Purple.ppm");

           //brown
           PPMFile brown = new PPMFile(task2PPM);
           brown.removeColour(new PixHSV(29, .6d, .18d),new PixHSV(43d, .95, .72d), new PixRGB(255,255,255), false);
           blobs.putAll( ConnectedComponents.blobImage(brown, 0.001, "brown"));
           brown.writePPM("Brown.ppm");
           
           
           
       }

       
    }
    
}
