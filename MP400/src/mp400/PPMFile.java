/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;
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
import sun.awt.X11.XTimeCoord;

/**
 *
 * @author akeegazooka
 */
public class PPMFile {
    ArrayList<ArrayList<pixAbstract>> imageData;
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
        imageData = inFile.imageData;
        format = inFile.format;
        dimensions = inFile.dimensions;
        maxValue = inFile.maxValue;
        fileName = inFile.fileName;
    }
    
    private void setFileName(String inFileName)
    {
        fileName = inFileName;
    }
    
    public pixAbstract getAt(int xCoord, int yCoord)
    {
        pixAbstract outPix;
        if(xCoord < 0)
            xCoord = 0;
        else if(xCoord > dimensions.getX() -1)
            xCoord = dimensions.getX()-1;
        
        if(yCoord < 0)
            yCoord = 0;
        else if(yCoord > dimensions.getY()-1)
            yCoord = dimensions.getY()-1;
        
       // System.out.println(xCoord + ", " + yCoord);
        
        return this.imageData.get(xCoord).get(yCoord);
    }
    
    public void setAt(int xCoord, int yCoord, PixRGB inRGB)
    {
        this.imageData.get(xCoord).set(yCoord, new PixRGB(inRGB) );
    }
    
    private void loadPPM()
    {
        Scanner sc = null;
        if(! (fileName.equals("") ) )
        {
            try
            {
                sc  = new Scanner(new FileInputStream(fileName));     
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
                    format = dataElements[1];
                    dimensions = new MP2d(Integer.parseInt(dataElements[2]), Integer.parseInt(dataElements[3]) );
                    imageData = new ArrayList<>();
                    maxValue = Integer.parseInt(dataElements[4]);
                    PixRGB tempRGBData;

                    try 
                    {
                       for(int x = 0;x < dimensions.getX();x++)
                       {
                           imageData.add(new ArrayList<pixAbstract>());
                           for(int y = 0;y<dimensions.getY();y++)
                           {
                               tempRGBData = new PixRGB();
                               tempRGBData.setR(sc.nextInt());
                               tempRGBData.setG(sc.nextInt());
                               tempRGBData.setB(sc.nextInt());
                               imageData.get(x).add(tempRGBData);
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
                for(int xx= 0; xx < this.dimensions.getX();xx++)
                {
                    for(int yy = 0; yy < this.dimensions.getY();yy++)
                    {
                         if( (float)yy%5.0 == 0 )
                            pw.print("\n");
                        pw.print(this.imageData.get(xx).get(yy).toString());
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
