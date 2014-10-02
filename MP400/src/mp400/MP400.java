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
       if(ppmData!=null)
           ppmData.writePPM("out.ppm");
       

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
    
       PixMask newMask = new PixMask(gauss);
       PPMConvolve matrix = new PPMConvolve();
       PixMask normalMask = matrix.normalizeMask(newMask);
       ppmData = matrix.convolve(normalMask, ppmData);
       if(ppmData!=null)
            ppmData.writePPM("out1.ppm");

       
    }
    
}
