
package mp400;

/**
 *
 * @author Keegan Ott
 */
public class Extra {
    
    /**
     *a standard 3x3 gauss blur kernel
     */
    static public Double[][] gauss = 
    { {1d, 2d, 1d},
      {2d, 4d, 2d},
      {1d, 2d, 1d}
    };

    /**
     * the identity kernel
     */
    static public Double[][] identity = 
    { {0d, 0d, 0d},
      {0d, 1d, 0d},
      {0d, 0d, 0d}
    };

    /**
     *a box blur kernel
     */
    static public Double[][] box =
    { {1d, 1d, 1d},
      {1d, 1d, 1d},
      {1d, 1d, 1d}
    };

    /**
     *a standard edge enhancement kernel
     */
    static public Double[][] edge =
    { {-1d, -1d, -1d},
      {-1d, 8d, -1d},
      {-1d, -1d, -1d}
    };
    


    /**
     *the mexican hat 7 x 7 kernel
     */
    static public Double[][] mexicanHat =
    { { 0d, 0d,-1d,-1d,-1d, 0d, 0d},
      { 0d,-1d,-3d,-3d,-3d,-1d, 0d},
      {-1d,-3d, 0d, 7d, 0d,-3d,-1d},
      {-1d,-3d, 7d,24d, 7d,-3d,-1d},
      {-1d,-3d, 0d, 7d, 0d,-3d,-1d},
      { 0d,-1d,-3d,-3d,-3d,-1d, 0d},
      { 0d, 0d,-1d,-1d,-1d, 0d, 0d}
    };
    
    /**
     * Sobel's horizontal component kernel
     */
    static public Double[][] sobelX = 
    { {-1d,-2d,-1d},
      {0d, 0d, 0d },
      {1d, 2d, 1d },
    };
    
    /**
     *Sobel's verticalcomponent kernel
     */
    static public Double[][] sobelY = 
    { {-1d, 0d, 1d},
      {-2d, 0d, 2d},
      {-1d, 0d, 1d}
    };
    
    /**
     *A simple function to clamp an integer to a given range
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static int clampInt(int val, int min, int max)
    {
        if(val < min) { return min; }
        if(val > max) { return max; }
        return val;
    }
    
    /**
     *a function to clamp a double to within a specified range
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static double clampDouble(double val, double min, double max)
    {
        if(val < min) { return min; }
        if(val > max) { return max; }
        return val;
    }
    
    /**
     *A function that returns whether or not a given pixel is within a provided
     * range.
     * @param inPix
     * @param minPix
     * @param maxPix
     * @return
     */
    public static boolean PixelRange(PixHSV inPix, PixHSV minPix, PixHSV maxPix)
    {
        return ( (inPix.getHue() >= minPix.getHue()) && (inPix.getHue() <= maxPix.getHue())  &&
                 (inPix.getSat() >= minPix.getSat()) && (inPix.getSat() <= maxPix.getSat())  &&
                 (inPix.getVal() >= minPix.getVal()) && (inPix.getVal() <= maxPix.getVal()) );
    }
    
    
    
}
