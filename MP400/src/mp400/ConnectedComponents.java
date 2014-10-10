
package mp400;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Keegan Ott
 */
public class ConnectedComponents
{

    /**
     *
     * @param bwImage
     * @param colourImage
     * @param minBlobRatio minimum %age of the image size that the blob is allowed to be
     * @param pass what colour range triggered this function call
     * @param fileName
     * @return map of all blobs discovered through CCL
     */
    public static Map<Integer, Blob> blobImage(PPMFile bwImage, PPMFile colourImage, double minBlobRatio, String pass, String fileName)
    {
        int width = bwImage.dimensions.getX();
        int height = bwImage.dimensions.getY();
        
        //pass one
        
        //a label array corresponding to each pixel in the image
        int[][] blobLabels =  new int[width][height];
        
        //a map of equivalence relations (pixel regions that have been labelled differently but
        //belong to the same region)
        Map<Integer, Integer> equivalenceRelations = new HashMap<Integer,Integer>();
        
        //label 0 is reserved for black (Background)
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
                    
                    /*if there is a pixel above the current pixel*/
                    if(yy > 0)
                        northLabel = blobLabels[xx][yy-1];
                    
                    /*if there is a pixel to the west of the current pixel*/
                    if(xx > 0)
                        westLabel = blobLabels[xx-1][yy];
                    
                    /*if both north and west pixels are labelled as background*/
                    if( (northLabel == 0) && (westLabel == 0) )
                    {
                        /*assign pixel new label*/
                        blobLabels[xx][yy] = currLabel;
                        /*increase the next unique label*/
                        currLabel++;
                    }
                    /*if there is an existing label to the north or west*/
                    else
                    {
                        int neighbourLabel = 0;
                        /*if north label is background and west is not assign
                          the current pixel the west pixel's label*/
                        if( (northLabel == 0) && (westLabel > 0) )
                            neighbourLabel = westLabel;
                        /*otherwise assign the northern pixel's label to the current one*/
                        else if( (northLabel > 0) && (westLabel == 0) )
                            neighbourLabel = northLabel;
                        /*if both west and north pixels have unqiue labels*/
                        else if(northLabel != westLabel)
                        {
                            /*assign the smallest of the two to the current pixel*/
                            neighbourLabel = Math.min(northLabel, westLabel);
                            /*and also take note that both regions should belong to the same area*/
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
        /*Pass two merges all equivalence relations and takes note of how
        many pixels exist in each region*/
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
         /*for each pixel in the image */
         for(int yy = 0;yy< height; yy++)
         {
             for(int xx = 0; xx < width; xx++)
             {
                 /*find the pixel's corresponding labe;*/
                 int workingLabel = blobLabels[xx][yy];
                 /*if its not background and it's number of assigned pixels is greater than the fraction provided to the function*/
                 if( (workingLabel != 0) && (numBlobPixels[workingLabel] /  (double)(width * height) > minBlobRatio)  )
                 {
                    /*create a temporary blob */
                    Blob newBlob;
                    /*if the current label has already been assigned to a blob*/
                    if(blobs.containsKey(workingLabel))
                    {
                        newBlob = blobs.get(workingLabel);
                        /*assign the current pixel to it's activePixels list*/
                        newBlob.activePixels.add(new MP2d(xx,yy));
                    }
                    else
                    {
                        /*otherwise, create a new blob with relevant file information
                        and add the current pixel to it's activePixels list
                        and then place the newly created blob into the */
                        newBlob = new Blob(pass, blobFileOffset, fileName);
                        blobFileOffset++;
                        newBlob.activePixels.add( new MP2d(xx,yy) );
                        blobs.put(workingLabel, newBlob);
                    }
                 }
             }
         }
        
        return blobs;
    }
}
