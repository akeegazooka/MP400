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
    
    private double boundingArea;
    private double density;
    
    private int numPixels;
    
    PixHSV avePix;
    MP2d minCoords;
    MP2d maxCoords;
    
    ArrayList<MP2d> activePixels;
    
    public Blob() 
    {
        avePix  = new PixHSV();
        minCoords = new MP2d();
        maxCoords = new MP2d();
        activePixels = new ArrayList<MP2d>();
        
        boundingArea = 0;
        density = 0;
        numPixels = 0;
    }
    
    public double getBoundingArea() {
        return boundingArea;
    }

    public void setBoundingArea(double boundingArea) {
        this.boundingArea = boundingArea;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public int getNumPixels() {
        return numPixels;
    }

    public void setNumPixels(int numPixels) {
        this.numPixels = numPixels;
    }
    
    
    
    
}
