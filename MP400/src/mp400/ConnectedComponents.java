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

    public static Map<Integer, Blob> blobImage(PPMFile bwImage, PPMFile colourImage, double minBlobRatio, String pass, String fileName)
    {
        int width = bwImage.dimensions.getX();
        int height = bwImage.dimensions.getY();
        
        //pass one
        int[][] blobLabels =  new int[width][height];
        //ArrayList<MP2d> equivalenceRelations = new ArrayList<>();
        Map<Integer, Integer> equivalenceRelations = new HashMap<Integer,Integer>();
        
        int currLabel = 1;
        for(int yy = 0; yy< height; yy++)
        {
            for (int xx = 0; xx < width; xx++)
            {
                PixRGB currPix = bwImage.getAt(xx, yy);
                
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
         Map<Integer, Blob> blobs = new HashMap<Integer, Blob>();
         int blobFileOffset = 0;
         for(int yy = 0;yy< height; yy++)
         {
             for(int xx = 0; xx < width; xx++)
             {
                 int workingLabel = blobLabels[xx][yy];
                 if( (workingLabel != 0) && (numBlobPixels[workingLabel] /  (double)(width * height) > minBlobRatio)  )
                 {
                    Blob newBlob;
                    if(blobs.containsKey(workingLabel))
                    {
                        newBlob = blobs.get(workingLabel);
                        newBlob.activePixels.add(new MP2d(xx,yy));
                    }
                    else
                    {
                        newBlob = new Blob(pass, blobFileOffset, fileName);
                        blobFileOffset++;
                        newBlob.activePixels.add( new MP2d(xx,yy) );
                        blobs.put(workingLabel, newBlob);
                    }
                 }
             }
         }
//         for(Map.Entry<Integer,Blob> blob: blobs.entrySet())
//         {
//             blob.getValue().classifyBlob(colourImage);
//         }
         //System.out.println("Finished blob processing, found " + blobs.size() +" Areas");
        
        return blobs;
    }
}
