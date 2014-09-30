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
import java.util.ArrayList;

/**
 *
 * @author akeegazooka
 */
public class PPMFile {
    ArrayList<Integer[]> imageData;
    MP2d dimensions;
    Integer maxValue;
    String fileName;
    
    public PPMFile(String inFileName)
    {
        setFileName(inFileName);
    }
    
    private void setFileName(String inFileName)
    {
        BufferedReader inputStream = null;
        try
        {
            inputStream = new BufferedReader(new FileReader(inFileName)) ;
            String s;
            while( (s = inputStream.readLine()) != null )
            {
                
            }
        } 
        catch (IOException x)
        {
            System.out.println(x.getMessage());
        }
    }
    
    
    
    
}
