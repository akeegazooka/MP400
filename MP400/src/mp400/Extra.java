/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp400;

/**
 *
 * @author akeegazooka
 */
public class Extra {
    
    static public Double[][] gauss = 
    { {1d, 2d, 1d},
      {2d, 4d, 2d},
      {1d, 2d, 1d}
    };
    static public Double[][] identity = 
    { {0d, 0d, 0d},
      {0d, 1d, 0d},
      {0d, 0d, 0d}
    };
    static public Double[][] box =
    { {1d, 1d, 1d},
      {1d, 1d, 1d},
      {1d, 1d, 1d}
    };
    static public Double[][] edge =
    { {-1d, -1d, -1d},
      {-1d, 8d, -1d},
      {-1d, -1d, -1d}
    };
    static public Double[][] test =
    { {0d, 0d, 0d},
      {0d, 2d, 0d},
      {0d, 0d, 0d}
    };
    static public Double[][] ident1 = 
    { {0d, 0d, 0d},
      {0d, 1d, 1d},
      {0d, 0d, 0d}
    };
    static public Double[][] ident2 = 
    { {0d, 1d, 0d},
      {0d, 1d, 0d},
      {0d, 0d, 0d}
    };
    static public Double[][] mexicanHat =
    { { 0d, 0d,-1d,-1d,-1d, 0d, 0d},
      { 0d,-1d,-3d,-3d,-3d,-1d, 0d},
      {-1d,-3d, 0d, 7d, 0d,-3d,-1d},
      {-1d,-3d, 7d,24d, 7d,-3d,-1d},
      {-1d,-3d, 0d, 7d, 0d,-3d,-1d},
      { 0d,-1d,-3d,-3d,-3d,-1d, 0d},
      { 0d, 0d,-1d,-1d,-1d, 0d, 0d}
    };
    
    static public Double[][] sobelX = 
    { {-1d,-2d,-1d},
      {0d, 0d, 0d },
      {1d, 2d, 1d },
    };
    
    static public Double[][] sobelY = 
    { {-1d, 0d, 1d},
      {-2d, 0d, 2d},
      {-1d, 0d, 1d}
    };
    
}
