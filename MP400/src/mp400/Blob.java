/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mp400;

import java.util.ArrayList;

/**
 *
 * @author akeegazooka
 */
public class Blob 
{
    
    private double boundingBoxArea;
    private double blobDensity;
    private double aspectRatio;


    private int blobWidth;
    private int blobHeight;
    private int blobFileOffset;
    
    private int numPixels;
    
    private String pass;
    private String fileName;
    private String objectOfInterest;
    
    PixHSV avePix;
    MP2d topLeft;
    MP2d bottomRight;
    
    ArrayList<MP2d> activePixels;
    
    public Blob(String inPass, int inOffset, String inFileName) 
    {

        
        avePix  = new PixHSV();
        topLeft = new MP2d();
        bottomRight = new MP2d();
        activePixels = new ArrayList<MP2d>();
        
        boundingBoxArea = 0;
        aspectRatio = 0;
        blobDensity = 0;
        numPixels = 0;
        
        blobWidth = 0;
        blobHeight = 0;
        
        blobFileOffset = inOffset;
        
        pass = inPass;
        fileName = inFileName;
        objectOfInterest = "NaN";
        
    }

    public void setObjectOfInterest(String objectOfInterest) {
        this.objectOfInterest = objectOfInterest;
    }
    
    public void classifyBlob(PPMFile inImage)
    {
        //find average pixel
        
        double hRunningAve = 0;
        double sRunningAve = 0;
        double vRunningAve = 0;
        for(MP2d activePixel : activePixels)
        {
            PixHSV currPix = PixRGB.convertToHSV(inImage.getAt(activePixel.getX(), activePixel.getY()));
            hRunningAve += currPix.getHue()/activePixels.size();
            sRunningAve += currPix.getSat()/activePixels.size();
            vRunningAve += currPix.getVal()/activePixels.size();
        }
        avePix.setHue(hRunningAve);
        avePix.setSat(sRunningAve);
        avePix.setVal(vRunningAve);
        
        //find Top Left and Bottom Right of the given blob (relative to entire image)

        topLeft = new MP2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        bottomRight = new MP2d(-1,-1);
        
        for(MP2d activePixel : activePixels)
        {
            //find top left most pixel
            if(activePixel.getX() < topLeft.getX())
                topLeft.setX(activePixel.getX());
            if(activePixel.getY() < topLeft.getY())
                topLeft.setY(activePixel.getY());
            
            //find bottom right pixel
            if(activePixel.getX() > bottomRight.getX())
                bottomRight.setX(activePixel.getX());
            if(activePixel.getY() > bottomRight.getY())
                bottomRight.setY(activePixel.getY());
        }
        
        //find the dimensions of the bounding box
        blobWidth = (bottomRight.getX() - topLeft.getX() );
        blobHeight = (bottomRight.getY() - topLeft.getY());
        
        //finding the area of the bounding box
        boundingBoxArea = (blobWidth * blobHeight);
        
        //finding the density (fullness) of the blob in relation to its bounding box
        
        blobDensity = (activePixels.size() / boundingBoxArea);
        
        //finding the aspect ratio of the blob width/height
        
        aspectRatio = (double)blobWidth / (double)blobHeight;
        

        
        
    }
    
    public void writeOut(PPMFile inImage,String outFolder)
    {
        PPMFile blobOut = new PPMFile(blobWidth, blobHeight, 255, "P3");
        for(int xx = topLeft.getX(); xx < bottomRight.getX();xx++)
        {
            for(int yy = topLeft.getY(); yy < bottomRight.getY();yy++)
            {
                PixRGB settingPixel = new PixRGB(inImage.getAt(xx, yy));
                blobOut.setAt(xx-topLeft.getX(), yy-topLeft.getY(), settingPixel);
            }
        }
        for(MP2d active : activePixels)
        {
            PixRGB settingPixel = new PixRGB(inImage.getAt(active.getX(), active.getY()));
            int setPositionX = active.getX() - topLeft.getX();
            int setPositionY = active.getY() - topLeft.getY();
            blobOut.setAt(setPositionX, setPositionY, settingPixel);
        }
        String outName = fileName.substring(0, fileName.lastIndexOf('.'));
        blobOut.writePPM(outFolder+outName+"-"+objectOfInterest+".ppm");
    }
    public void drawBounds(PPMFile inImage)
    {
        inImage.bresenhamLine(topLeft.getX(), topLeft.getY(), topLeft.getX()+blobWidth , topLeft.getY(), new PixRGB(255,0,0)); //top left -> top right
        inImage.bresenhamLine(topLeft.getX(), topLeft.getY(), topLeft.getX() , topLeft.getY()+blobHeight, new PixRGB(255,0,0)); //top left -> bottom left
        inImage.bresenhamLine(topLeft.getX(), topLeft.getY()+blobHeight, topLeft.getX()+blobWidth , topLeft.getY()+blobHeight, new PixRGB(255,0,0)); //bottom left -> bottom right
        inImage.bresenhamLine(topLeft.getX()+blobWidth, topLeft.getY(), topLeft.getX()+blobWidth , topLeft.getY()+blobHeight, new PixRGB(255,0,0));  //top right -> bottom right
        
    }
    
    public double getBoundingBoxArea() {
        return boundingBoxArea;
    }

    public void setBoundingBoxArea(double boundingArea) {
        this.boundingBoxArea = boundingArea;
    }

    public double getDensity() {
        return blobDensity;
    }
    public String getPass(){
        return pass;
    }

    public void setDensity(double density) {
        this.blobDensity = density;
    }

    public int getNumPixels() {
        return numPixels;
    }

    public void setNumPixels(int numPixels) {
        this.numPixels = numPixels;
    }
    
    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
    
    
    
    
}
