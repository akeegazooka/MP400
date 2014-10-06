
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author akeegazooka
 */
public class HoughTransform {
    private double angleStep;
    private double[] angles;
    private int[][] acc;
    int mostBinVotes;
    //private MP2d [] pointMap;
    
    
    public HoughTransform(PPMFile inImFile, int inPrecision, double startPercentage)
    {
        //dimensionality initializers/declarations
        MP2d dimensions = inImFile.getDimensions();
        //System.out.println("Well, i got here: " + dimensions.getX() + " " + dimensions.getY());
        int width = dimensions.getX();
        int height = dimensions.getY();
        MP2d centre = new MP2d(width/2,height/2);
        
        //angle related variables
        angleStep = Math.PI / inPrecision;
        double[] sinCache = new double[inPrecision];
        double[] cosCache = new double[inPrecision];
        
        //accumulator variables
        int accHeight = (int) ( (Math.sqrt(2) * Math.max(height, width)) /2);
        acc = new int[inPrecision][2 * accHeight];
        
        for(int i = 0;i<inPrecision;i++)
        {
            double theta = i * angleStep;
            sinCache[i] = Math.sin(theta);
            cosCache[i] = Math.cos(theta);
        }
        //pointMap = new MP2d[height * width];
        mostBinVotes = 0;
        for(int yy = (int) 0; yy<height;yy++)
        {
            for(int xx = 0; xx < width;xx++)
            {
                //System.out.println("here too: " + inImFile.getAt(xx, yy).r);
                if(inImFile.getAt(xx, yy).r == 255)
                {
                    //System.out.println("Got here");
                    for(int jj=0;jj<inPrecision;jj++)
                    {
                        int r = (int)  (((xx - centre.getX()) * cosCache[jj]) + ((yy - centre.getY()) * sinCache[jj]));
                        r+= accHeight;
                        if( (r>=0) && (r<accHeight*2) )
                            acc[jj][r]++;
                        //System.out.println("Accessing: " + jj + ", "+ r + "max is: " + inPrecision +", " + 2*accHeight);
                        if(acc[jj][r] >= mostBinVotes)
                            mostBinVotes = acc[jj][r];
                    }
                }
                
            }
        }
    }
    
    public PolarLine[] findViableLines(int neighbourhoodSize, Double threshold)
    {
        PolarLine[] outLines;
        int accWidth = acc.length;
        int accHeight = acc[0].length;
        
        int[][] tempAcc = new int[accWidth][accHeight];
        for(int ii = 0; ii < accWidth;ii++)
        {
            tempAcc[ii] = acc[ii].clone();
        }
        
        //System.arraycopy(acc, 0, tempAcc, 0, acc.length);
        int neighbourhoodCentre = neighbourhoodSize /2;
        double minVotes = threshold * mostBinVotes;
        
        Map<Integer,MP2d> bestLines = new TreeMap<>(Collections.reverseOrder());
        
        for(int r = 0;r<accHeight;r++)
        {
            for(int t = 0;t<accWidth;t++)
            {
                int currBinVotes = tempAcc[t][r];
                if(currBinVotes >= minVotes)
                {
                    int localMaximumVotes = 0;
                    for(int i = 0;i<neighbourhoodSize;i++)
                    {
                        for(int j = 0;j<neighbourhoodSize;j++)
                        {
                            int fetchT = Extra.clampInt(t+i - neighbourhoodCentre, 0, accWidth-1);
                            int fetchR = Extra.clampInt(r+j - neighbourhoodCentre, 0, accHeight-1);
                            
                            if(localMaximumVotes < tempAcc[fetchT][fetchR])
                                localMaximumVotes = tempAcc[fetchT][fetchR];
                        }
                    }
                    //not local max
                    if(currBinVotes < localMaximumVotes)
                        tempAcc[t][r] = 0;
                    else
                    {
                        bestLines.put(currBinVotes, new MP2d(t,r));
                        //insert into outlines
                        //outLines = new PolarLine(t,r);
                    }
                }
                else
                    tempAcc[t][r] = 0;
            }
        }
        
        outLines = new PolarLine[bestLines.size()];
        int idx = 0;
        for(Map.Entry<Integer, MP2d> viableLine : bestLines.entrySet())
        {
            //System.out.println("Help me: "+ viableLine.getValue().toString());
            MP2d val = new MP2d(viableLine.getValue());
            outLines[idx] = getPolarLine(val.getX(), val.getY());
            idx++;
        }
        
        return outLines;
    }
    
    public PolarLine getPolarLine(int inTheta, int inR)
    {
        int accHeight = acc[0].length;
        double r = (double) (inR - accHeight/2);
        //System.out.println("---"+r);
        double theta = (double) (inTheta * angleStep);
        //System.out.println("Viable polar line: " + r + ", " + theta);
        return new PolarLine(r, theta+0.001);
    }
    //r 15%
    //theta 15 .27
    public PolarLine[] filterLines(PolarLine[] inLines, double thetaSimilarity, double rSimilarity )
    {
        //                  if(Math.abs(thetaDiff) < restrainingOrderTheta
        //                  && Math.abs(distDiff) < restrainingOrderDist) {
        //                  isRestrained = true;
        //                  double thetaDiff = theta - bestLineThetaList[bestI];
        //                  double distDiff = dist - bestLineDistList[bestI];
        PolarLine[] goodLines;
        ArrayList<PolarLine> lineArray = new ArrayList<>();
        for (PolarLine line : inLines) 
        {
            //System.out.println("---"+Math.abs(Math.abs(line.getTheta()) - Math.PI/2)+"---");
            if (Math.abs(Math.abs(line.getTheta()) - Math.PI/2) > .18)  //10 degrees from horizontal
            {
                if(lineArray.isEmpty())
                    lineArray.add(line);
                else
                {
                    boolean isSimilar = false;
                    for(PolarLine newLine : lineArray)
                    {
                        double thetaDiff = Math.abs( newLine.getTheta() - line.getTheta() );
                        double distDiff = Math.abs ( newLine.getR() - newLine.getR() );

                        System.out.println("R1: " + newLine.getR() + " R2: " + line.getR() );

                        if( (thetaDiff < thetaSimilarity) && (distDiff < rSimilarity) )
                            isSimilar = true;
                    }
                    if(!isSimilar)
                        lineArray.add(line);
                }
                
                //System.out.println(line.getR());
            }        
        }
        
        goodLines = new PolarLine[lineArray.size()];
        for(int ii = 0;ii<goodLines.length;ii++)
        {
            goodLines[ii] = lineArray.get(ii);
        }
        return goodLines;
        
    }
    
    public PPMFile manifestHoughSpace()
    {
        int thetaSteps = acc.length;
        int accHeight = acc[0].length;
        
        PPMFile outHoughSpaceImage = new PPMFile(thetaSteps, accHeight, 255, "P3");
        for(int i = 0;i<accHeight;i++)
        {
            for(int j = 0;j<thetaSteps;j++)
            {
               // System.out.println(acc[j][i]);
                //System.out.println( (double) (acc[j][i]) /mostBinVotes * 255) ;
                Double v = new Double(acc[j][i]) / mostBinVotes*255.d;
                PixRGB pixel = new PixRGB(v,v,v);
                //System.out.println(pixel.r +", "+pixel.g+", "+pixel.b);
                outHoughSpaceImage.setAt(j, i, pixel);
            }
        }
        return outHoughSpaceImage;
    }
}
