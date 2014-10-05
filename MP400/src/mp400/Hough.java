
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
public class Hough {
    private double angleStep;
    private double[] angles;
    private MP2d [] pointMap;
    
    
    public void populatePoints(PPMFile inImFile)
    {
        MP2d dimensions = inImFile.getDimensions();
        int width = dimensions.getX();
        int height = dimensions.getY();
        int index = 0;
        
        pointMap = new MP2d[height * width];
        for(int yy = 0;yy<height;yy++)
        {
            for(int xx = 0; xx < width;xx++)
            {
                if(inImFile.getAt(xx, yy).r == 255)
                {
                    pointMap[index] = new MP2d(xx,yy);
                }
            }
        }
        
    }
    
}
