

package mp400;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Keegan Ott
 */
public class HoughTransform {
    private final double angleStep;
    private double[] angles;
    private final int[][] acc;
    int mostBinVotes;
    //private MP2d [] pointMap;
    
    /**
     *
     * @param inImFile
     * @param inPrecision
     * @param startPercentage
     */
    public HoughTransform(PPMFile inImFile, int inPrecision, double startPercentage)
    {
        //dimensionality initializers/declarations
        MP2d dimensions = inImFile.getDimensions();
        int width = dimensions.getX();
        int height = dimensions.getY();
        MP2d centre = new MP2d(width/2,height/2);
        
        //angle related variables
        /*angle step is the number of iterations through angles needed
        to reach the level of sampling precision provided to the function*/
        angleStep = Math.PI / inPrecision;
        double[] sinCache = new double[inPrecision];
        double[] cosCache = new double[inPrecision];
        
        //accumulator variables
        int accHeight = (int) ( (Math.sqrt(2) * Math.max(height, width)) /2);
        acc = new int[inPrecision][2 * accHeight];
        
        for(int i = 0;i<inPrecision;i++)
        {
            /*calculate and then cache the sin and cos values for each step
            through the circle, saves a lot of computation later on */
            double theta = i * angleStep;
            sinCache[i] = Math.sin(theta);
            cosCache[i] = Math.cos(theta);
        }
        mostBinVotes = 0;
        for(int yy = (int) 0; yy<height;yy++)
        {
            for(int xx = 0; xx < width;xx++)
            {
                /*if the coordinate corresponds to an edge pixel*/
                if(inImFile.getAt(xx, yy).r == 255)
                {
                    /*for inPrecision intervals through PI radians*/
                    for(int jj=0;jj<inPrecision;jj++)
                    {
                        /*the value of R is offset by the center in order to
                        make it's origin at the centre of the image, much easier
                        for later calculations*/
                        int r = (int)  (((xx - centre.getX()) * cosCache[jj]) + ((yy - centre.getY()) * sinCache[jj]));
                        /*the value of R is then offset by the height of the hough space
                        so that the value of R falls between 0 and 2 * accHeight (No negatives)*/
                        r+= accHeight;
                        /*if the given value of R falls in legal range
                        increment the number of votes in that cell*/
                        if( (r>=0) && (r<accHeight*2) )
                            acc[jj][r]++;
                        /*if the number of votes in that cell are the greatest
                        assign it to MaxVotes*/
                        if(acc[jj][r] >= mostBinVotes)
                            mostBinVotes = acc[jj][r];
                    }
                }
                
            }
        }
    }
    
    /**
     *A local maxima search to find the best line in a local region and to make
     * sure that it is within a percentage of the strongest line
     * @param neighbourhoodSize
     * @param threshold
     * @return
     */
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
        int neighbourhoodCentre = neighbourhoodSize /2;
        double minVotes = threshold * mostBinVotes;
        
        Map<Integer,MP2d> bestLines = new TreeMap<Integer,MP2d>(Collections.reverseOrder());
        
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
                    //not local max, make it worthless
                    if(currBinVotes < localMaximumVotes)
                        tempAcc[t][r] = 0;
                    else
                    {
                        //insert into outlines
                        bestLines.put(currBinVotes, new MP2d(t,r));
                        
                    }
                }
                else
                    tempAcc[t][r] = 0;
            }
        }
        
        outLines = new PolarLine[bestLines.size()];
        int idx = 0;
        /*for every viable line create a polar line for it and add it to
        the array of viable lines*/
        
        for(Map.Entry<Integer, MP2d> viableLine : bestLines.entrySet())
        {
            MP2d val = new MP2d(viableLine.getValue());
            outLines[idx] = getPolarLine(val.getX(), val.getY());
            idx++;
        }
        
        return outLines;
    }
    
    /**
     *
     * @param inTheta
     * @param inR
     * @return
     */
    public PolarLine getPolarLine(int inTheta, int inR)
    {
        int accHeight = acc[0].length;
        /*reverse previous offset to return it within normal ranges*/
        double r = (double) (inR - accHeight/2);
        double theta = (double) (inTheta * angleStep);
        /*offset the theta value by a tiny margin to make sure there are no
        true 0 values otherwise the line has an infinite slope*/
        return new PolarLine(r, theta+0.001);
    }

    /**
     *Make sure no lines are too similar to each other and keep the strongest line
     * @param inLines
     * @param thetaSimilarity
     * @param rSimilarity
     * @return
     */
        public PolarLine[] filterLines(PolarLine[] inLines, double thetaSimilarity, double rSimilarity )
    {

        PolarLine[] goodLines;
        ArrayList<PolarLine> lineArray = new ArrayList<PolarLine>();
        /* for every candidate line loop through */
        for (PolarLine line : inLines) 
        {
            /**
             * In order to filter out almost perfectly horizontal lines this function 
             * Math.PI/2 being 90deg therefore if we that away from the positive
             * angle it should not lie near flat.
             */
            if (Math.abs(Math.abs(line.getTheta()) - Math.PI/2) > .18)  //10 degrees from horizontal
            {
                /*if the list of new candidate lines is empty there is nothing to compare to
                therefore add first entry*/
                if(lineArray.isEmpty())
                    lineArray.add(line);
                else
                {
                    /*if there are candidate lines to compare to*/
                    boolean isSimilar = false;
                    for(PolarLine newLine : lineArray)
                    {
                        /*find the difference between both the R and
                        theta components*/
                        double thetaDiff = Math.abs( newLine.getTheta() - line.getTheta() );
                        double distDiff = Math.abs ( newLine.getR() - line.getR() );
                        
                        /*only if they are BOTH within the threshold, determine 
                          that they are too similar.
                        */
                        if( (thetaDiff <= thetaSimilarity) && (distDiff <= rSimilarity) )
                        {
                            isSimilar = true;
                        }
                    }
                    /*if the line being compared is not too similar, add it to
                    the list of lines to compare to*/
                    if( (!isSimilar) && (lineArray.size() < 3))
                        lineArray.add(line);
                }
            }        
        }
        
        /*create a static array of all the good candidate lines, populate it
        then return it*/
        goodLines = new PolarLine[lineArray.size()];
        for(int ii = 0;ii<goodLines.length;ii++)
        {
            goodLines[ii] = lineArray.get(ii);
        }
        return goodLines;
        
    }
    
    /**
     *Create an image of the hough space and write it to disk.
     * @return
     */
    public PPMFile manifestHoughSpace()
    {
        int thetaSteps = acc.length;
        int accHeight = acc[0].length;
        
        PPMFile outHoughSpaceImage = new PPMFile(thetaSteps, accHeight, 255, "P3");
        for(int i = 0;i<accHeight;i++)
        {
            for(int j = 0;j<thetaSteps;j++)
            {
                /*normalize the vote value in order to get a decent 
                RGB output from the calculation*/
                Double v = (double) acc[j][i] / mostBinVotes*255.d;
                PixRGB pixel = new PixRGB(v,v,v);
                outHoughSpaceImage.setAt(j, i, pixel);
            }
        }
        return outHoughSpaceImage;
    }
}
