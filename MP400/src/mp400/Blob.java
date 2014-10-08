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
    private double blobWidth;
    private double blobHeight;
    
    private int numPixels;
    
    private String pass;
    
    PixHSV avePix;
    MP2d topLeft;
    MP2d bottomRight;
    
    ArrayList<MP2d> activePixels;
    
    public Blob(String inPass) 
    {

        
        avePix  = new PixHSV();
        topLeft = new MP2d();
        bottomRight = new MP2d();
        activePixels = new ArrayList<MP2d>();
        
        boundingBoxArea = 0;
        blobDensity = 0;
        numPixels = 0;
        
        blobWidth = 0;
        blobHeight = 0;
        
        pass = inPass;
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
            hRunningAve += currPix.getHue();
            sRunningAve += currPix.getSat();
            vRunningAve += currPix.getVal();
        }
        hRunningAve/=activePixels.size();
        sRunningAve/=activePixels.size();
        vRunningAve/=activePixels.size();
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

    public void setDensity(double density) {
        this.blobDensity = density;
    }

    public int getNumPixels() {
        return numPixels;
    }

    public void setNumPixels(int numPixels) {
        this.numPixels = numPixels;
    }
    
    
    
    
}
