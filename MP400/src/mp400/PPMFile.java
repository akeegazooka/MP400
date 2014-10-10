

package mp400;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Keegan Ott
 */
public class PPMFile {
    PixRGB[][] imageData;
    String format;
    MP2d dimensions;
    Integer maxValue;
    String fileName;
    
    /**
     *Alternate constructor that takes in a filename and then 
     * calls a loading function with that filename to populate it's 
     * pixel data.
     * @param inFileName the name of the file to be populated from
     * @throws IOException
     */
    public PPMFile(String inFileName) throws IOException
    {
        setFileName(inFileName);
        loadPPM();
    }
    
    /**
     *Copy Constructor
     * @param inFile
     */
    public PPMFile(PPMFile inFile)
    {
        imageData = inFile.getImageData();
        format = inFile.getFormat();
        dimensions = inFile.getDimensions();
        maxValue = inFile.getMaxValue();
        fileName = inFile.getFileName();
    }
    
    /**
     *Alternate constructor #2 creates an empty image the size of the
     * input Basic file information is set and containers are initialized.
     * @param inWidth
     * @param inHeight
     * @param max Maximum pixel value
     * @param inFormat PPM specific metadata eg "P3"
     */
    public PPMFile(int inWidth, int inHeight, int max, String inFormat)
    {
        imageData = new PixRGB[inWidth][inHeight];
        dimensions = new MP2d(inWidth, inHeight);
        format = inFormat;
        maxValue = max;
        
    }
    
    /**
     *
     * @return Images inner pixel array
     */
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
    
    /**
     *
     * @return
     */
    public String getFormat()
    {
        String outFormat = this.format;
        
        return outFormat;
    }
    
    /**
     *
     * @return
     */
    public MP2d getDimensions()
    {
        MP2d outDimensions = new MP2d(dimensions);
        
        return outDimensions;
    }
    
    /**
     *
     * @return
     */
    public int getMaxValue()
    {
        return maxValue;
    }
    
    /**
     *
     * @return
     */
    public String getFileName()
    {
        return fileName;
    }
    
    private void setFileName(String inFileName)
    {
        fileName = inFileName;
    }
    
    /**
     *Fetch an RGB pixel from the image
     * @param inX
     * @param inY
     * @return copy of image pixel
     */
    public PixRGB getAt(int inX, int inY)
    {
        PixRGB outPixel = new PixRGB();
        outPixel.r = imageData[inX][inY].r;
        outPixel.g = imageData[inX][inY].g;
        outPixel.b = imageData[inX][inY].b;
 
        return outPixel;
    }
    
    /**
     *
     * @param xCoord
     * @param yCoord
     * @param inRGB
     */
    public void setAt(int xCoord, int yCoord, PixRGB inRGB)
    {
        PixRGB newPixel = new PixRGB(inRGB);
        if ( ( (xCoord <= dimensions.getX()-1) && (yCoord <= dimensions.getY()-1) ) &&( (xCoord >= 0) && (yCoord >= 0) ) )
            imageData[xCoord][yCoord] = newPixel;   
    }
    
    /**
     *A function that thresholds an image to b/w around a pivot average
     * RGB value
     * @param thresh the average component of a pixel to threshold around
     */
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
    
    /**
     *A function that isolates a colour range and changes it to a single colour
     * that is provided. if bg (background) is true it only changes the colour range 
     * given to a new colour, if bg is false then the rest of the image that is not the 
     * given range is turned to black.
     * @param minHSV
     * @param maxHSV
     * @param inNewColour
     * @param bg
     */
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
    
    /**
     *
     * @param inLines Polar lines in the form of r, theta
     */
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
            
            
            //find x,y at left edge of image
            lineBegin.setX(0);
            lineBegin.setY( (int) ( (  ( inLines[i].getR() -(-centreX * tCos) ) / tSin) + centreY ) );
            
            //find x,y at right of image
            lineEnd.setX(dimensions.getX());
            lineEnd.setY( (int) ( ( ( inLines[i].getR() - ( (dimensions.getX()-1 - centreX) * tCos) ) / tSin) + centreY ));
            
            //feed the line beginning at line end to a bresenham line drawing algorithm
            bresenhamLine(lineBegin.getX(), lineBegin.getY(), lineEnd.getX(), lineEnd.getY(), new PixRGB(0,0,255));
        }
    }
    
    /**
     *A function that utilizes Bresenham's line drawing algorithm that allows
     * reasonable single pixel width lines to be drawn across a 2d image
     * given a beginning and an end coordinate.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param colour
     */
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
                setAt(x1, y1, colour);
            }
            
        }
    }
    
    /**
     * an internal function utilized and accessible only via the alternate
     * constructor that is provided a filename to populate its data from.
     */
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
                /**
                 * metaFull in this context refers to whether or not all
                 * the required metadata to classify a PPM image file has been
                 * obtained.
                 */
                boolean metaFull = false;
                String dataString = "";
                String[] dataElements = null;
                String[] lineResult;
                while(sc.hasNextLine() && (!metaFull) )
                {
                    s = sc.nextLine();
                    /*split around the comment identifier, this allows for
                    comments to occur at the end of a line of pixel data or
                    at the beginning of a file.*/
                    lineResult = s.split("#"); 
                    if(! lineResult[0].equals("") )
                    {
                        //append a new peice of metadata 
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
                    imageData = new PixRGB[dimensions.getX()][dimensions.getY()];
                    maxValue = Integer.parseInt(dataElements[4]);
                    PixRGB tempRGBData;

                    try 
                    {
                       for(int y = 0;y<dimensions.getY();y++)
                       {
                           for(int x = 0;x < dimensions.getX();x++)
                           {
                               tempRGBData = new PixRGB();
                               tempRGBData.setR(sc.nextInt());
                               tempRGBData.setG(sc.nextInt());
                               tempRGBData.setB(sc.nextInt());
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
    
    /**
     *The function that output the PPM image data to a file.
     * @param outFile the file to write the image data to
     */
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
                for(int yy= 0; yy < this.dimensions.getY();yy++)
                {
                    for(int xx = 0; xx < this.dimensions.getX();xx++)
                    {
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
