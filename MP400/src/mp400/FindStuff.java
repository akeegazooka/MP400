/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp400;

import java.util.Map;

/**
 *
 * @author akeegazooka
 */
public class FindStuff 
{
    public static Blob findPurpleDisk(Map<Integer,Blob> blobs)
    {
        Blob bestBlob = new Blob("blob-candidate", 3);
        Blob[] blobCandidates;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            System.out.println("Average pixel Components: "+blob.avePix.toString() +
                    "\nBounding Box Area: "+blob.getBoundingBoxArea() +
                    "\nBlob Density: "+ (1-blob.getDensity()) );
            bestBlob = blob;
        }
        return bestBlob;
    }
    
}
