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
public class MPUtility {
    
    public static void sort(Double[] inArray)
    {
        //System.out.println(inArray[0]);
        int idx = 0;
        int j;
        double x;
        for(int i = 1;i< inArray.length;i++)
        {
            j = i;
            x = inArray[i];
            while( (j>0) && (inArray[j-1] > x) )
            {
                inArray[j] = inArray[j-1];
                j--;
            }
            inArray[j] = x;
            
        }
    }
    
    public static PPMFile imageAdd(PPMFile inImage1, PPMFile inImage2)
    {
        PPMFile imageOut = new PPMFile(inImage1.dimensions.getX(), inImage1.dimensions.getY(), 255, "P3");
        
        for(int yy = 0;yy<imageOut.dimensions.getY();yy++)
        {
            for(int xx = 0;xx<imageOut.dimensions.getX();xx++)
            {
                PixRGB tempPixel = new PixRGB();
                tempPixel.r = ( inImage1.getAt(xx, yy).r + inImage2.getAt(xx, yy).r );
                tempPixel.g = ( inImage1.getAt(xx, yy).g + inImage2.getAt(xx, yy).g );
                tempPixel.b = ( inImage1.getAt(xx, yy).b + inImage2.getAt(xx, yy).b );
                imageOut.setAt(xx, yy, tempPixel);
            }
        }
        
        return imageOut;
    }
    
}
