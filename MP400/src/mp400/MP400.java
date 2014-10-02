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
            ppmData = new PPMFile("/home/akeegazooka/Desktop/2.ppm");
       }
       catch(IOException e)
       {
           System.out.println(e.getMessage());
       }
           
       

    Double[][] gauss = 
    { {1d, 2d, 1d},
      {2d, 4d, 2d},
      {1d, 2d, 1d}
    };
    Double[][] identity = 
    { {0d, 0d, 0d},
      {0d, 1d, 0d},
      {0d, 0d, 0d}
    };
    Double[][] box =
    { {1d, 1d, 1d},
      {1d, 1d, 1d},
      {1d, 1d, 1d}
    };
    Double[][] edge =
    { {-1d, -1d, -1d},
      {-1d, 8d, -1d},
      {-1d, -1d, -1d}
    };
    Double[][] test =
    { {0d, 0d, 0d},
      {0d, 2d, 0d},
      {0d, 0d, 0d}
    };
    Double[][] ident1 = 
    { {0d, 0d, 0d},
      {0d, 1d, 1d},
      {0d, 0d, 0d}
    };
    Double[][] ident2 = 
    { {0d, 1d, 0d},
      {0d, 1d, 0d},
      {0d, 0d, 0d}
    };
    Double[][] mexicanHat =
    { { 0d, 0d,-1d,-1d,-1d, 0d, 0d},
      { 0d,-1d,-3d,-3d,-3d,-1d, 0d},
      {-1d,-3d, 0d, 7d, 0d,-3d,-1d},
      {-1d,-3d, 7d,24d, 7d,-3d,-1d},
      {-1d,-3d, 0d, 7d, 0d,-3d,-1d},
      { 0d,-1d,-3d,-3d,-3d,-1d, 0d},
      { 0d, 0d,-1d,-1d,-1d, 0d, 0d}
    };
    
       PPMConvolve matrix = new PPMConvolve();
       Double[][] genGauss = matrix.generateGaussKernel(2.5d);
       PixMask newMask = new PixMask(mexicanHat);
       PixMask mexican = matrix.normalizeMask(newMask);
       
       PixMask newMask1 = new PixMask(genGauss);
       PixMask gaussian = matrix.normalizeMask(newMask1);
     
       
       PPMFile newPpmData;
       newPpmData = matrix.convolve(gaussian, ppmData);
       
       newPpmData= matrix.convolve(mexican, ppmData);
       newPpmData.threshold(8d);

       if(ppmData!=null)
       {
            //newFile.writePPM("original.ppm");
            newPpmData.writePPM("out.ppm");
            ppmData.writePPM("original.ppm");
            
       }

       
    }
    
}
