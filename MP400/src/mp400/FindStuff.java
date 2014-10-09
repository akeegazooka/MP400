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
        Blob purpleDome = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            System.out.println("Average pixel Components: "+blob.avePix.toString() +
                    "\nBounding Box Area: "+blob.getBoundingBoxArea() +
                    "\nBlob Density: "+ (blob.getDensity()) );
            
            if(blob.getPass().equals("purple"))
            {
                if(blob.getDensity() < .81 && blob.getDensity() > .76)
                {
                        purpleDome = blob;
                }
            }
        }
        return purpleDome;
    }
    
    public static Blob findAmazon(Map<Integer,Blob> blobs)
    {
        Blob amazonMan = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            System.out.println("Average pixel Components: "+blob.avePix.toString() +
                    "\nBounding Box Area: "+blob.getBoundingBoxArea() +
                    "\nBlob Density: "+ (blob.getDensity()) +
                    "\nAspect Ratio: " + blob.getAspectRatio());
            
            if(blob.getPass().equals("amazon_brown"))
            {
                if(blob.getDensity() > .48 && blob.getDensity() < .9)
                {
                    if(Extra.PixelRange(blob.avePix, new PixHSV(30,.7,.18), new PixHSV(36,.95,.33))) /*PixHSV(30,.7,18), new PixHSV(36,.95,.33)))*/
                    {
                        if(blob.getAspectRatio() > .38 && blob.getAspectRatio() < .70)
                            amazonMan = blob;
                    }
                        
                }
            }
        }
        return amazonMan;
        
    }
    
    public static Blob findBlueTotoro(Map<Integer,Blob> blobs)
    {
        Blob blueToto = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            System.out.println("Average pixel Components: "+blob.avePix.toString() +
                    "\nBounding Box Area: "+blob.getBoundingBoxArea() +
                    "\nBlob Density: "+ (blob.getDensity())  +
                    "\nAspect Ratio: " + blob.getAspectRatio());
            
            if(blob.getPass().equals("light_blue"))
            {
                if(blob.getDensity() < .65 && blob.getDensity() > .30)
                {
                        blueToto = blob;
                }
            }
        }

        return  blueToto;
        
    }
    public static Blob findGreyToto(Map<Integer,Blob> blobs)
    {
        Blob greyToto = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            System.out.println("Average pixel Components: "+blob.avePix.toString() +
                    "\nBounding Box Area: "+blob.getBoundingBoxArea() +
                    "\nBlob Density: "+ (blob.getDensity())  +
                    "\nAspect Ratio: " + blob.getAspectRatio());
            
            if(blob.getPass().equals("totoro_brown"))
            {
                if(Extra.PixelRange(blob.avePix, new PixHSV(32,.40,.15), new PixHSV(40,.55,.30)))
                {
                    if(blob.getAspectRatio() > .4 && blob.getAspectRatio() <= 1.2)
                        greyToto = blob;
                }
            }
        }
        
        return greyToto;
    }
    
    public static Blob findKeepon(Map<Integer,Blob> blobs)
    {
        Blob keepon = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            System.out.println("Average pixel Components: "+blob.avePix.toString() +
                    "\nBounding Box Area: "+blob.getBoundingBoxArea() +
                    "\nBlob Density: "+ (blob.getDensity())  +
                    "\nAspect Ratio: " + blob.getAspectRatio());
            
            if(blob.getPass().equals("orange_yellow"))
            {
                if(Extra.PixelRange(blob.avePix, new PixHSV(30,.85,.39), new PixHSV(40,1,.65)))
                {
                    if(blob.getAspectRatio() > .75 && blob.getAspectRatio() <= 1)
                        keepon = blob;
                }
            }
        }
        
        return keepon;
    }
    
    

    
}
