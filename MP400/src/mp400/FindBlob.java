
package mp400;

import java.util.Map;

/**
 *
 * @author Keegan Ott
 */

/**
 * every classification filter function in this file initialises a Blob
 * object to null and searches through blob candidates in order to find one
 * that fits the bill, if none is found, the blob object remains null and 
 * the check for null at the end of each function only allows an initialized
 * blob to be written out - this works well for combination images which might
 * not contain an object, the check for that object will still be completed
 * but no file will be written out if nothing is found.
 */
public class FindBlob 
{

    /**
     *Hardcoded classification criteria that consistently identifies the purple
     * dome
     * @param blobs
     * @return
     */
    public static Blob findPurpleDisk(Map<Integer,Blob> blobs)
    {
        Blob purpleDome = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();

            
            if(blob.getPass().equals("purple"))
            {
                if(blob.getDensity() < .81 && blob.getDensity() > .76)
                {
                        purpleDome = blob;
                }
            }
        }
        if(purpleDome != null)
            purpleDome.setObjectOfInterest("PurpleDome");
        return purpleDome;
    }
    
    /**
     *Hardcoded classification criteria that consistently identifies the Amazon figurine
     * @param blobs
     * @return
     */
    public static Blob findAmazon(Map<Integer,Blob> blobs)
    {
        Blob amazonMan = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();

            
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
        if(amazonMan != null)
            amazonMan.setObjectOfInterest("Amazon");
        return amazonMan;
        
    }
    
    /**
     *Hardcoded classification criteria that consistently identifies the Small Totoro
     * @param blobs
     * @return
     */
    public static Blob findBlueTotoro(Map<Integer,Blob> blobs)
    {
        Blob blueToto = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();

            
            if(blob.getPass().equals("light_blue"))
            {
                if(blob.getDensity() < .65 && blob.getDensity() > .30)
                {
                        blueToto = blob;
                }
            }
        }
        if(blueToto != null)
            blueToto.setObjectOfInterest("SmallTotoro");
        return  blueToto;
        
    }

    /**
     *Hardcoded classification criteria that consistently identifies the Large Totoro
     * @param blobs
     * @return
     */
    public static Blob findGreyToto(Map<Integer,Blob> blobs)
    {
        Blob greyToto = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();

            
            if(blob.getPass().equals("totoro_brown"))
            {
                if(Extra.PixelRange(blob.avePix, new PixHSV(32,.37,.15), new PixHSV(40,.55,.33)))
                {
                    if(blob.getAspectRatio() > .4 && blob.getAspectRatio() <= 1.2)
                        greyToto = blob;
                }
            }
        }
        if(greyToto != null)
            greyToto.setObjectOfInterest("LargeTotoro");
        return greyToto;
    }
    
    /**
     *Hardcoded classification criteria that consistently identifies the Keepon Toy
     * @param blobs
     * @return
     */
    public static Blob findKeepon(Map<Integer,Blob> blobs)
    {
        Blob keepon = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();

            
            if(blob.getPass().equals("orange_yellow"))
            {
                if(Extra.PixelRange(blob.avePix, new PixHSV(30,.85,.39), new PixHSV(40,1,.65)))
                {
                    if(blob.getAspectRatio() > .75 && blob.getAspectRatio() <= .91)
                        keepon = blob;
                }
            }
        }
        if(keepon != null)
            keepon.setObjectOfInterest("Keepon");
        return keepon;
    }

    /**
     *Hardcoded classification criteria that consistently identifies the Rubber duck 
     * @param blobs
     * @return
     */
    public static Blob findDuck(Map<Integer,Blob> blobs)
    {
        Blob duck = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            
            if(blob.getPass().equals("orange_yellow"))
            {
                if(Extra.PixelRange(blob.avePix, new PixHSV(32,.95,.38), new PixHSV(45, 1, .9)))
                {
                    if(blob.getDensity() > .63 && blob.getDensity() < .85)
                    {
                        if(blob.getAspectRatio() >= .9 && blob.getAspectRatio() <= 1.4)
                        {
                            duck = blob;
                        }
                    }
                }
            }
        
        
        }
        if(duck != null)
            duck.setObjectOfInterest("Duck");
        return duck;
    }

    /**
     *Hardcoded classification criteria that consistently identifies the Wheel Object
     * @param blobs
     * @return
     */
    public static Blob findWheel(Map<Integer,Blob> blobs)
    {
        Blob wheel = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            if(Extra.PixelRange(blob.avePix, new PixHSV(35,.39,.40), new PixHSV(47, .53,.70)))
            {
                if(blob.getDensity() > .25 && blob.getDensity() < 1.1)
                {
                    if(blob.getAspectRatio() >= .7 && blob.getAspectRatio() <= 1.3)
                        wheel = blob;
                }
            }
        }
        if(wheel != null)
            wheel.setObjectOfInterest("Wheel");
        return  wheel;
        
    }
    
    /**
     *Hardcoded classification criteria that attempts to identify the mexican hat
     * @param blobs
     * @return
     */
    public static Blob findMexicanHat(Map<Integer,Blob> blobs)
    {
        Blob mexicanHat = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            if(blob.getPass().equals("mexican_hat"))
            {
                if(Extra.PixelRange(blob.avePix, new PixHSV(125,.49,.15), new PixHSV(165, .62,.25)))
                {
                    if(blob.getDensity() > .20 && blob.getDensity() < .7)
                    {
                        if(blob.getAspectRatio() >= .8 && blob.getAspectRatio() <= 1.8)
                            mexicanHat = blob;
                    }
                }
            }
        }
        if(mexicanHat != null)
            mexicanHat.setObjectOfInterest("MexicanHat");
        return mexicanHat;
    }
    
    /**
     *Hardcoded classification criteria that consistently identifies sign elements
     * @param blobs
     * @return
     */
    public static Blob findSign(Map<Integer,Blob> blobs)
    {
        Blob sign = null;
        for(Map.Entry<Integer,Blob> blobEntry : blobs.entrySet())
        {
            Blob blob = blobEntry.getValue();
            if(blob.getAspectRatio() >= .8 && blob.getAspectRatio() <= 1.1)
                sign = blob;
        }
        return sign;
    }
}
