/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp400;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author akeegazooka
 */
public class ConnectedComponents
{

    public static int [][] label(PPMFile inImage)
    {
        //pass one
        int[][] blobLabels =  new int[inImage.dimensions.getX()][inImage.dimensions.getY()];
        //ArrayList<MP2d> equivalenceRelations = new ArrayList<>();
        Map<Integer, Integer> equivalenceRelations = new HashMap<>();
        
        int currLabel = 1;
        for(int yy = 0; yy<inImage.dimensions.getY(); yy++)
        {
            for (int xx = 0; xx < inImage.dimensions.getX(); xx++)
            {
                PixRGB currPix = inImage.getAt(xx, yy);
                
                if( (int) (currPix.getR()) != 0)
                {
                    int northLabel = 0;
                    int westLabel = 0;
                    
                    //case 1
                    if(yy > 0)
                        northLabel = blobLabels[xx][yy-1];
                    
                    if(xx > 0)
                        westLabel = blobLabels[xx-1][yy];
                    
                    if( (northLabel == 0) && (westLabel == 0) )
                    {
                        blobLabels[xx][yy] = currLabel;
                        currLabel++;
                    }
                    else
                    {
                        int neighbourLabel = 0;
                        if( (northLabel == 0) && (westLabel > 0) )
                            neighbourLabel = westLabel;
                        else if( (northLabel > 0) && (westLabel == 0) )
                            neighbourLabel = northLabel;
                        else if(northLabel != westLabel)
                        {
                            neighbourLabel = Math.min(northLabel, westLabel);
                            equivalenceRelations.put(Math.min(northLabel, westLabel), Math.max(northLabel, westLabel));
                        }
                        else
                            neighbourLabel = northLabel;
                        
                        blobLabels[xx][yy] = neighbourLabel;
                        
                        
                    }
                        
                }
            }
        }
        //pass two
        int[] numBlobPixels = new int[currLabel];
        for(int yy = 1; yy < blobLabels[0].length;yy++)
        {
            for(int xx = 1; xx < blobLabels.length;xx++)
            {
                int refLabel = blobLabels[xx][yy];
                if(refLabel != 0)
                {
                    Integer label = blobLabels[xx][yy];
                    Integer next = label;
                    do 
                    {
                        label = next;
                        next = equivalenceRelations.get(label);
                    } 
                    while ( (next!=null) && (!next.equals(label) ) );
                    equivalenceRelations.put(refLabel, label);
                    blobLabels[xx][yy] = label;
                    numBlobPixels[label]++;
                    
                    
                }
            }
        }
         Map<Integer, Blob> blobs = new HashMap<>();
         
//        for(int ii = 0; ii < blobLabels[0].length;ii++)
//        {
//            for(int jj = 0; jj < blobLabels.length;jj++)
//            {
//                System.out.println("[" + jj + ", " + ii + "] " + blobLabels[jj][ii]);
//            }
//        }
        
        return blobLabels;
    }
}
