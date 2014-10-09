/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.pow;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author akeegazooka
 */
public class MP400 {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        int task = 0;
        if(args[0].equals("3.1"))
            task = 1;
        else if(args[0].equals("3.2"))
            task = 2;
        else if(args[0].equals("3.3"))
            throw new UnsupportedOperationException("Not implemented :(");
        
        String inDir = args[1];
        String outDir = args[2];
        
        File outDirectory = new File(outDir);
        outDirectory.mkdirs();
        File[] inputFiles = new File(inDir).listFiles();
        if(inputFiles == null)
        {
            throw new FileNotFoundException("No files found");
        }
        for(File file : inputFiles)
        {
            String name = file.getName();
            String path = file.getAbsolutePath();
            if(file.isFile() && name.substring(name.length()-3).toLowerCase().equals("ppm"))
            {
                if(task == 1)
                {
                    try
                    {
                        taskOne(new PPMFile(path), outDir);    
                    }
                    catch (Exception e) 
                    {
                        System.out.println(e.getMessage());
                    }
                    
                }
                else if(task == 2)
                {
                    try 
                    {
                        taskTwo(new PPMFile(path), outDir);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
                    
            }
        }
    }
       
    public static void taskOne(PPMFile inFile, String outFolder)
    {
            outFolder = outFolder.concat("/");
            //PPMConvolve matrix = new PPMConvolve();
            //Double[][] genGauss = PPMConvolve.generateGaussKernel(2.5d);

            //mexican hat creation.
            //PixMask newMask = new PixMask(Extra.mexicanHat);
            //PixMask mexican = matrix.normalizeMask(newMask);

            //gaussian creation
            //PixMask newMask1 = new PixMask(genGauss);
            //PixMask gaussian = matrix.normalizeMask(newMask1);

            //sobel creation.

            PixMask kSobelX = PPMConvolve.normalizeMask(new PixMask(Extra.sobelX));
            PixMask kSobelY = PPMConvolve.normalizeMask(new PixMask(Extra.sobelY));


            PPMFile newPpmData;
            PPMFile houghSpace;

            newPpmData = PPMConvolve.median(inFile, 5);
            //newPpmData = PPMConvolve.median(ppmData, 5);
            newPpmData = MPUtility.imageAdd(PPMConvolve.convolve(kSobelX, newPpmData), PPMConvolve.convolve(kSobelY, newPpmData));
            newPpmData.threshold(5d);

            newPpmData = PPMConvolve.median(newPpmData, 5);
            HoughTransform houghTransform = new HoughTransform(newPpmData, 180, 0.5);
            PolarLine[] lines = houghTransform.findViableLines(20, .2d);
            PolarLine[] goodLines = houghTransform.filterLines(lines, .3, .4 * Math.sqrt(pow(newPpmData.dimensions.getX(),2) + Math.pow(newPpmData.dimensions.getY(), 2)));
            //newPpmData.drawLines(lines);
            inFile.drawLines(goodLines);
            
            //blob to find signs
            Map<Integer,Blob> signs = new HashMap<Integer,Blob>();
            PPMFile sign_red0 = new PPMFile(inFile);
            PPMFile sign_red1 = new PPMFile(inFile);
            PPMFile sign_red = new PPMFile(inFile);
            
            sign_red0.removeColour(new PixHSV(345d, 0.5d, .1d),new PixHSV(365d, 1d, 1d), new PixRGB(255,255,255), false);
            //sign_red0.writePPM(outFolder+"0Ok"+inFile.getFileName());
            sign_red1.removeColour(new PixHSV(-1d, 0.5d, .1d),new PixHSV(15d, 1d, 1d), new PixRGB(255,255,255), false);
            //sign_red1.writePPM(outFolder+"1Ok"+inFile.getFileName());
            sign_red = MPUtility.imageAdd(sign_red0, sign_red1);
            //PPMConvolve.median(sign_red, 3);
            //sign_red.writePPM(outFolder+"Ok"+inFile.getFileName());
            
            
            signs.putAll( ConnectedComponents.blobImage(sign_red,inFile, 0.0001, "sign_red", inFile.getFileName()));
            
            for(Map.Entry<Integer,Blob> blob: signs.entrySet())
            {
                blob.getValue().classifyBlob(inFile);
                //blob.getValue().writeOut(inFile, outFolder);
            }
            Blob bestSign = FindStuff.findSign(signs);
            if(bestSign != null)
            {
                bestSign.writeOut(inFile, outFolder);
                bestSign.drawBounds(inFile);
            }
            //lines_signs.writePPM(outFolder+inFile.getFileName()+"uhh.ppm");
            //newPpmData= matrix.convolve(mexican, ppmData);
       

            if(inFile!=null)
            {
                 //newFile.writePPM("original.ppm");
                 //newPpmData.writePPM(outFolder+"out.ppm");
                 inFile.writePPM(outFolder+inFile.getFileName()+ "-out.ppm");
                 houghTransform.manifestHoughSpace().writePPM(outFolder+"HOUGH.ppm");

            }
       
    }
       
    public static void taskTwo(PPMFile inFile, String outFolder)
    {
        outFolder = outFolder.concat("/");
        PPMFile task2PPM = new PPMFile(inFile);
        PPMFile colourImage = new PPMFile(inFile);
        task2PPM.writePPM(outFolder+"OriginalTask2.ppm");
        task2PPM = PPMConvolve.median(task2PPM, 5);

        Map<Integer,Blob> blobs = new HashMap<Integer,Blob>();

        //background removal run
        task2PPM.removeColour(new PixHSV(10d, 0.05d, 0.2d),new PixHSV(52d, 0.25d, 0.95d), new PixRGB(0,0,0), true);
        //task2PPM.writePPM(outFolder+"No_BG.ppm");

        //Yellow/Orange range run
        PPMFile orange_yellow = new PPMFile(task2PPM);
        orange_yellow.removeColour(new PixHSV(21d, .8d, .2d),new PixHSV(52d, 1d, .92d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(orange_yellow,colourImage, 0.001, "orange_yellow", inFile.getFileName()));
       // orange_yellow.writePPM(outFolder+"Yellow_Orange.ppm");

        //darker blue run
        PPMFile dark_blue = new PPMFile(task2PPM);
        dark_blue.removeColour(new PixHSV(180d, .85d, .15d),new PixHSV(235d, 1d, .58d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(dark_blue,colourImage, 0.001, "dark_blue", inFile.getFileName()));
        //dark_blue.writePPM(outFolder+"Blue.ppm");

        //lighter blue run
        PPMFile light_blue = new PPMFile(task2PPM);
        light_blue.removeColour(new PixHSV(170d, 0d, 0d),new PixHSV(250d, .5d, .5d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(light_blue,colourImage, 0.002, "light_blue", inFile.getFileName()));
        //light_blue.writePPM(outFolder+"Light_Blue.ppm");

        //purple run
        PPMFile purple = new PPMFile(task2PPM);
        purple.removeColour(new PixHSV(310, .42d, .05d),new PixHSV(355d, 1, .57d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(purple,colourImage, 0.001,"purple", inFile.getFileName()));
        //purple.writePPM(outFolder+"Purple.ppm");



        //amazon_brown
        PPMFile brown = new PPMFile(task2PPM);
        brown.removeColour(new PixHSV(22, .6d, .07d),new PixHSV(47d, 1, .33), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(brown,colourImage, 0.001, "amazon_brown", inFile.getFileName()));
        //brown.writePPM(outFolder+"Amazon_Brown.ppm");

        //totoro brown

        PPMFile totoro_brown = new PPMFile(task2PPM);
        //new PixHSV(28, .28d, .15d),new PixHSV(45d, .55, .55d)
        totoro_brown.removeColour(new PixHSV(24, .30d, 0d),new PixHSV(45d, .7, .6d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(totoro_brown,colourImage, 0.001, "totoro_brown", inFile.getFileName()));
        //totoro_brown.writePPM(outFolder+"Totoro_Brown.ppm");

        //beige run
        PPMFile wheel_beige = new PPMFile(task2PPM);
        wheel_beige.removeColour(new PixHSV(32, .23d, .3d),new PixHSV(53d, .70, .8d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(wheel_beige,colourImage, 0.001, "wheel_beige", inFile.getFileName()));
        //wheel_beige.writePPM(outFolder+"Wheel_Beige.ppm");

        //Mexican hat attempt
        //PPMConvolve.maxFilter(task2PPM, 5);
        PPMFile mexican_blue = new PPMFile(task2PPM);
        PPMFile mexican_red = new PPMFile(task2PPM);
        PPMFile mexican_green = new PPMFile(task2PPM);
        PPMFile mexican_hat;

        mexican_blue.removeColour(new PixHSV(180d, .85d, .15d),new PixHSV(235d, 1d, .58d), new PixRGB(255,255,255), false);
        mexican_red.removeColour(new PixHSV(0d, .55d, .06d), new PixHSV(21d, .95d, .33d), new PixRGB(255,255,255), true);
        mexican_green.removeColour(new PixHSV(119d, .90d, .10d),new PixHSV(138d, 1d, .35d), new PixRGB(255,255,255), false);

        mexican_hat = MPUtility.imageAdd(mexican_blue, mexican_red);
        mexican_hat = MPUtility.imageAdd(mexican_hat, mexican_green);

        blobs.putAll( ConnectedComponents.blobImage(mexican_hat,colourImage, 0.001, "mexican_hat", inFile.getFileName()));
        //mexican_hat.writePPM(outFolder+"Mexican_Hat.ppm");



        //System.out.println("HELLO WORLD:::::::: "+outFolder);
        for(Map.Entry<Integer,Blob> blob: blobs.entrySet())
        {
            blob.getValue().classifyBlob(colourImage);
            //blob.getValue().writeOut(inFile, outFolder);
        }

        //1. find the purple dome
        Blob bestPurpleBlob = FindStuff.findPurpleDisk(blobs);
        if(bestPurpleBlob != null)
            bestPurpleBlob.writeOut(inFile, outFolder);

        //2. find mr. amazon.
        Blob bestAmazon = FindStuff.findAmazon(blobs);
        if(bestAmazon != null)
            bestAmazon.writeOut(inFile, outFolder);

        //3. find blue toto
        Blob bestBlueToto = FindStuff.findBlueTotoro(blobs);
        if(bestBlueToto != null)
            bestBlueToto.writeOut(inFile, outFolder);

        //4. find grey toto
        Blob bestGreyToto = FindStuff.findGreyToto(blobs);
        if(bestGreyToto != null)
            bestGreyToto.writeOut(inFile, outFolder);

        //5. find keepon
        Blob bestKeepon = FindStuff.findKeepon(blobs);
        if(bestKeepon != null)
            bestKeepon.writeOut(inFile, outFolder);

        //6. find duck

        Blob bestDuck = FindStuff.findDuck(blobs);
        if(bestDuck != null)
            bestDuck.writeOut(inFile, outFolder);

        Blob bestWheel = FindStuff.findWheel(blobs);
        if(bestWheel != null)
            bestWheel.writeOut(inFile, outFolder);

        Blob bestHat = FindStuff.findMexicanHat(blobs);
        if(bestHat != null)
            bestHat.writeOut(inFile, outFolder);
    }
}
    

