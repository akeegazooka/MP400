/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author akeegazooka
 */
public class PPMFile {
    ArrayList<Integer[]> imageData;
    String format;
    MP2d dimensions;
    Integer maxValue;
    String fileName;
    
    public PPMFile(String inFileName) throws IOException
    {
        setFileName(inFileName);
        loadFile();
    }
    
    private void setFileName(String inFileName)
    {
        fileName = inFileName;
    }
    
    private void loadFile() throws IOException
    {
        BufferedReader inputStream = null;
        if(! (fileName.equals("") ) )
        {
        try
        {
            inputStream = new BufferedReader(new FileReader(fileName)) ;
            
            String s;
            boolean metaFull = false;
            String dataString = "";
            String[] dataElements;
            String[] lineResult;
            do {
            while( (s = inputStream.readLine()) !=null)
            {
                    lineResult = s.split("#");
                    if(! lineResult[0].equals("") )
                    {
                        dataString = dataString.concat(" " + lineResult[0]);
                        dataElements = dataString.split(" ");
                        if(dataElements.length == 5)
                        {
                            metaFull = true;
                            for(String sElement : dataElements)
                            {
                                System.out.println(sElement);
                            }
                        }
                        
                    }
                
                }
                
            }while (!metaFull);
            
        } 
        catch (IOException x)
        {
            System.out.println(x.getMessage());
        }
        finally
        {
            if(inputStream != null)
            {
                inputStream.close();
            }
        }
        }
    }
    
    
    
    
}
