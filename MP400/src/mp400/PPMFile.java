/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author akeegazooka
 */
public class PPMFile {
    PixRGB[][] imageData;
    String format;
    MP2d dimensions;
    Integer maxValue;
    String fileName;
    
    public PPMFile(String inFileName) throws IOException
    {
        setFileName(inFileName);
        loadPPM();
    }
    
    public PPMFile(PPMFile inFile)
    {
        imageData = inFile.getImageData();
        format = inFile.getFormat();
        dimensions = inFile.getDimensions();
        maxValue = inFile.getMaxValue();
        fileName = inFile.getFileName();
    }
    
    public PPMFile(int inWidth, int inHeight, int max, String inFormat)
    {
        imageData = new PixRGB[inWidth][inHeight];
        dimensions = new MP2d(inWidth, inHeight);
        format = inFormat;
        maxValue = max;
        
    }
    
    public PixRGB[][] getImageData()
    {
        PixRGB[][] outData = new PixRGB[dimensions.getX()][dimensions.getY()];
        for(int i = 0;i<dimensions.getY();i++)
        {
            for(int j = 0;j<dimensions.getX();j++)
            {
                outData[j][i] = this.getAt(j, i);
                //System.out.println("Copied pixel: " + outData[j][i]);
            }
        }
        return outData;
    }
    
    public String getFormat()
    {
        String outFormat = this.format;
        
        return outFormat;
    }
    
    public MP2d getDimensions()
    {
        MP2d outDimensions = new MP2d(dimensions);
        
        return outDimensions;
    }
    
    public int getMaxValue()
    {
        return maxValue;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    private void setFileName(String inFileName)
    {
        fileName = inFileName;
    }
    
    public PixRGB getAt(int inX, int inY)
    {
        PixRGB outPixel = new PixRGB();
        outPixel.r = imageData[inX][inY].r;
        outPixel.g = imageData[inX][inY].g;
        outPixel.b = imageData[inX][inY].b;
 
        return outPixel;
    }
    
    public void setAt(int xCoord, int yCoord, PixRGB inRGB)
    {
        PixRGB newPixel = new PixRGB(inRGB);
        if ( ( (xCoord <= dimensions.getX()-1) && (yCoord <= dimensions.getY()-1) ) &&( (xCoord >= 0) && (yCoord >= 0) ) )
            imageData[xCoord][yCoord] = newPixel;   
    }
    
    public void threshold(double thresh)
    {
        for(int i = 0; i < dimensions.getY();i++)
        {
            for(int j = 0; j < dimensions.getX();j++)
            {
                PixRGB workingPixel = getAt(j, i);
                double pixSum = workingPixel.getR() + workingPixel.getG() + workingPixel.getB();
                pixSum /=3;
                
                if(pixSum >= thresh)
                {
                    workingPixel.setR(maxValue);
                    workingPixel.setG(maxValue);
                    workingPixel.setB(maxValue);
                }
                else
                {
                    workingPixel.setR(0);
                    workingPixel.setG(0);
                    workingPixel.setB(0);
                }
                setAt(j, i, workingPixel);
            }
        }
    }
    
    public void removeColour(PixHSV minHSV ,PixHSV maxHSV, PixRGB inNewColour, boolean bg)
    {
        if(bg)
        {
            for(int y = 0; y<dimensions.getY();y++)
            {
                for(int x = 0;x<dimensions.getX();x++)
                {
                    PixHSV workingPix = PixRGB.convertToHSV(getAt(x, y) );


                    if ( (workingPix.getHue() >= minHSV.getHue()) && (workingPix.getHue() <= maxHSV.getHue())  &&
                        (workingPix.getSat() >= minHSV.getSat()) && (workingPix.getSat() <= maxHSV.getSat())  &&
                        (workingPix.getVal() >= minHSV.getVal()) && (workingPix.getVal() <= maxHSV.getVal()) 
                       )
                    {
                        setAt(x, y, inNewColour);
                    }
                }
            }
        }
        else
        {
            for(int y = 0; y<dimensions.getY();y++)
            {
                for(int x = 0;x<dimensions.getX();x++)
                {
                    PixHSV workingPix = PixRGB.convertToHSV(getAt(x, y) );
                    if ( (workingPix.getHue() >= minHSV.getHue()) && (workingPix.getHue() <= maxHSV.getHue())  &&
                        (workingPix.getSat() >= minHSV.getSat()) && (workingPix.getSat() <= maxHSV.getSat())  &&
                        (workingPix.getVal() >= minHSV.getVal()) && (workingPix.getVal() <= maxHSV.getVal()) 
                       )
                    {
                        setAt(x, y, inNewColour);
                    }
                    else
                    {
                        setAt(x, y, new PixRGB(0,0,0));
                    }
                }
            }
        }
            
    }
    
    public void drawLines(PolarLine[] inLines)
    {
        int centreX = dimensions.getX()/2;
        int centreY = dimensions.getY()/2;
        
        
        for(int i = 0;i<inLines.length; i++)
        {
            MP2d lineBegin = new MP2d();
            MP2d lineEnd = new MP2d();
            double tCos = Math.cos(inLines[i].getTheta());
            double tSin = Math.sin(inLines[i].getTheta());
            
            //System.out.println("cos / sin val "+tCos +", " + tSin);
            
            
            //find x,y at left edge of image
            lineBegin.setX(0);
            lineBegin.setY( (int) ( (  ( inLines[i].getR() -(-centreX * tCos) ) / tSin) + centreY ) );
            
            lineEnd.setX(dimensions.getX());
            lineEnd.setY( (int) ( ( ( inLines[i].getR() - ( (dimensions.getX()-1 - centreX) * tCos) ) / tSin) + centreY ));

            bresenhamLine(lineBegin.getX(), lineBegin.getY(), lineEnd.getX(), lineEnd.getY(), new PixRGB(0,0,255));
        }
    }
    
    
    public void bresenhamLine(int x1,int y1, int x2, int y2, PixRGB colour)
    {
        if((x1==x2) && (y1==y2))
            setAt(x1, y1, colour);
        else
        {
            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);
            int diff = dx - dy;
            int shiftX,shiftY;
            
            if(x1 < x2) 
                shiftX = 1;
            else
                shiftX = -1;
            if(y1 < y2)
                shiftY = 1;
            else
                shiftY = -1;
            
            while((x1 != x2 ) || (y1 != y2))
            {
                int p = 2 * diff;
                
                if(p > -dy)
                {
                    diff-=dy;
                    x1+=shiftX;
                }
                if(p < dx)
                {
                    diff+=dx;
                    y1+=shiftY;
                }
                //System.out.println("Setting at " + x1+", " + y1);
                setAt(x1, y1, colour);
            }
            
        }
    }
    
    private void loadPPM()
    {
        Scanner sc = null;
        if(! (fileName.equals("") ) )
        {
            try
            {
                File f = new File(fileName);
                FileInputStream fis = new FileInputStream(f);
                sc  = new Scanner(fis);     
                fileName = f.getName();
                String s;
                boolean metaFull = false;
                String dataString = "";
                String[] dataElements = null;
                String[] lineResult;
                while(sc.hasNextLine() && (!metaFull) )
                {
                    s = sc.nextLine();
                    lineResult = s.split("#");
                    if(! lineResult[0].equals("") )
                    {
                        dataString = dataString.concat(" " + lineResult[0]);
                        dataElements = dataString.split(" ");
                        if(dataElements.length == 5)
                        {
                            metaFull = true;
                        }
                    }
                }

                if(dataElements != null)
                {
                    for(int jj = 0;jj<5;jj++)
                    {
                        System.out.println(dataElements[jj]);
                    }
                    format = dataElements[1];
                    dimensions = new MP2d(Integer.parseInt(dataElements[2]), Integer.parseInt(dataElements[3]) );
                    imageData = new PixRGB[dimensions.getX()][dimensions.getY()];
                    maxValue = Integer.parseInt(dataElements[4]);
                    PixRGB tempRGBData;

                    try 
                    {
                       for(int y = 0;y<dimensions.getY();y++)
                       //for(int x = 0;x < dimensions.getX();x++)
                       {
                           //imageData.add(new ArrayList<PixRGB>());
                           for(int x = 0;x < dimensions.getX();x++)
                           //for(int y = 0;y<dimensions.getY();y++)
                           {
                               tempRGBData = new PixRGB();
                               tempRGBData.setR(sc.nextInt());
                               tempRGBData.setG(sc.nextInt());
                               tempRGBData.setB(sc.nextInt());
                               //imageData.get(x).add(tempRGBData);
                               imageData[x][y] = tempRGBData;
                           }
                       }
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        System.out.println(e.getMessage());
                    }  
                }
                else
                {
                    throw new IOException();
                }
            } 
            catch (IOException x)
            {
                System.out.println(x.getMessage());
            }
            finally
            {
                if(sc!=null)
                    sc.close();    
            }
        }
    }
    
    public void writePPM(String outFile)
    {
        PrintWriter pw = null;
        try
        {
            pw = new PrintWriter(new File(outFile));    
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        try
        {
            if(pw != null)
            {
                pw.print("P3\n"+this.dimensions.getX() + " " + this.dimensions.getY() + "\n"+ maxValue+"\n");
                //for(int xx = 0; xx < this.dimensions.getX();xx++)
                for(int yy= 0; yy < this.dimensions.getY();yy++)
                {
                    //for(int yy= 0; yy < this.dimensions.getY();yy++)
                    for(int xx = 0; xx < this.dimensions.getX();xx++)
                    {
//                         if( (float)yy%5.0 == 0 )
//                            pw.print("\n");
                        //pw.print(this.imageData.get(xx).get(yy).toString());
                         pw.print(this.getAt(xx, yy));
                         pw.print("\n");
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            if(pw!=null)
            {
                pw.close();
            }
        }
    } 
}
