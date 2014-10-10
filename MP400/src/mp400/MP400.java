

package mp400;

import java.io.File;
import java.io.FileNotFoundException;

import static java.lang.Math.pow;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Keegan Ott
 */
public class MP400 {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        //Java 1.6 does not allow switching on strings so this is a workaround.
        int task = 0;
        if(args[0].equals("3.1"))
            task = 1;
        else if(args[0].equals("3.2"))
            task = 2;
        else if(args[0].equals("3.3"))
            throw new UnsupportedOperationException("Not implemented :(");
        
        //set working directories to ones supplied by the user
        String inDir = args[1];
        String outDir = args[2];
        
        File outDirectory = new File(outDir);
        //create them if they dont exist
        outDirectory.mkdirs();
        File[] inputFiles = new File(inDir).listFiles();
        if(inputFiles == null)
        {
            throw new FileNotFoundException("No files found");
        }
        //for every single file in the inputted directory.
        for(File file : inputFiles)
        {
            String name = file.getName();
            String path = file.getAbsolutePath();
            //if the file is ppm format 
            if(file.isFile() && name.substring(name.length()-3).toLowerCase().equals("ppm"))
            {
                //supply file to the given task
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

    /**
     *The over-arching controller for task one's operations.
     * @param inFile The file that this loop of task one will operate on
     * @param outFolder The folder that all the output is to be written to
     */
    
    public static void taskOne(PPMFile inFile, String outFolder)
    {
            outFolder = outFolder.concat("/");
            
            /**
             * Here i create both the horizontal and vertical kernels for the
             * sobel edge detection, the normalizing phase is important as it
             * allows multiplication to stay within the colour space range.
             */
            PixMask kSobelX = PPMConvolve.normalizeMask(new PixMask(Extra.sobelX));
            PixMask kSobelY = PPMConvolve.normalizeMask(new PixMask(Extra.sobelY));


            PPMFile newPpmData;
            PPMFile houghSpace;
            
            /**
             *The pipeline that task one follows begins with a median filter
             * to attempt to remove noise.
             */
            newPpmData = PPMConvolve.median(inFile, 5);
            /**
             * The a sobel edge detection operation is completed (adding
             * both halves together in a single line), initially i tried out
             * several other blurring kernels and algorithms such as a Gauss
             * Kernel in combination with a Mexican Hat kernel but that 
             * produced very thick and unformed lines; a median filter
             * in conjunction with a 3 x 3 Two Pass Sobel has delivered the best
             * results in my opinion.
             */
            newPpmData = MPUtility.imageAdd(PPMConvolve.convolve(kSobelX, newPpmData), PPMConvolve.convolve(kSobelY, newPpmData));
            
            /**
             * the edge image is then thresholded around the average RGB value
             * of 5, nothing special about this number it was found through trial
             * and error.
             */
            newPpmData.threshold(5d);
            /**
             * another Median filter is applied to further remove noise
             * (this time it is performed on the edge image).
             */
            newPpmData = PPMConvolve.median(newPpmData, 5);
            /**
             * the previous operations were all in preparation for the Hough Transform
             * stage, the function is provided with a pre-processed image file
             * along with the number of angle steps to be performed for the hough
             * transform.
             * 
             * the last parameter is the percentage of where to begin analysing
             * within the image, in this case .5 means it starts halfway down.
             */
            HoughTransform houghTransform = new HoughTransform(newPpmData, 180, 0.5);
            PolarLine[] lines = houghTransform.findViableLines(20, .2d);
            PolarLine[] goodLines = houghTransform.filterLines(lines, .3, .4 * Math.sqrt(pow(newPpmData.dimensions.getX(),2) + Math.pow(newPpmData.dimensions.getY(), 2)));
            /**
             * Write the lines that were found through the hough to file, this concludes the
             * line finding section.
             */
            inFile.drawLines(goodLines);
            
            /**
             * In order to detect signs within a road scene Connected component
             * labelling (CCL) is used to detect regions similar to signs.
             * 
             * I initially attempted to implement a circular Hough Transform
             * but i struggled with it so i left it and continued onto task
             * 2, realising the utility of CCL i came back to task 1 after 
             * completing task 2 and decided to try CCL on the signs and it 
             * worked quite well in the end - a few realistic scenes didnt
             * turn out due to wild lighting and blurring variations but 
             * overall i'm happy with my decision to use CCL over Circular
             * Hough Transforms for this scenario.
             */
            Map<Integer,Blob> signs = new HashMap<Integer,Blob>();
            
            /**
             * because of the circular nature of the Hue part of HSV
             * colour two ranges are needed for red, hence two files will be used.
             * 
             */
            PPMFile sign_red0 = new PPMFile(inFile);
            PPMFile sign_red1 = new PPMFile(inFile);
            PPMFile sign_red = new PPMFile(inFile);
            
            /**
             * isolate the end of the hue spectrum belonging to red.
             */
            sign_red0.removeColour(new PixHSV(345d, 0.5d, .1d),new PixHSV(365d, 1d, 1d), new PixRGB(255,255,255), false);
            /**
             * isolate the beginning of the hue spectrum belonging to red.
             */
            sign_red1.removeColour(new PixHSV(-1d, 0.5d, .1d),new PixHSV(15d, 1d, 1d), new PixRGB(255,255,255), false);
            /**
             * because of the binary nature of the colour isolated images two images
             * can be easily added together similar to an OR operation.
            */
            sign_red = MPUtility.imageAdd(sign_red0, sign_red1);
            /**
             * add all the found blobs to a preliminary list of un-analysed blobs.
             */
            signs.putAll( ConnectedComponents.blobImage(sign_red,inFile, 0.0001, "sign_red", inFile.getFileName()));
            
            for(Map.Entry<Integer,Blob> blob: signs.entrySet())
            {
                /**
                 * this stage loops through all the found blobs and tells
                 * each one to produce information about itself such as 
                 * density, size and aspect ratio.
                 */
                blob.getValue().classifyBlob(inFile);
            }
            /**
             * after each blob has produced its attributes it can be passed
             * to a function that fits it to a specific classification
             * for a sign.
             */
            Blob bestSign = FindBlob.findSign(signs);
            if(bestSign != null)
            {
                bestSign.writeOut(inFile, outFolder);
                bestSign.drawBounds(inFile);
            }
       
            
  /*          //if(inFile!=null)
            //{
                 //newFile.writePPM("original.ppm");
                 //newPpmData.writePPM(outFolder+"out.ppm");
                 inFile.writePPM(outFolder+inFile.getFileName()+ "-out.ppm");
                 houghTransform.manifestHoughSpace().writePPM(outFolder+"HOUGH.ppm");

            //}
       */
    }
            
    /**
     *Task Two performs Blobbing and classification on 8 images
     * that are found within a scene (any combination at any time)
     * To ease the process of classification blobs are detected on a per
     * colour basis on an image that has had its background removed (black)
     * @param inFile The current file to be worked on.
     * @param outFolder The output directory.
     */
    public static void taskTwo(PPMFile inFile, String outFolder)
    {
        /**
         * in order to output correctly to a folder a slash
         * is added to the folder path.
         */
        outFolder = outFolder.concat("/");
        PPMFile task2PPM = new PPMFile(inFile);
        PPMFile colourImage = new PPMFile(inFile);
        
        /**
         * Before anything else is performed a preprocessing stage of
         * a 5 x 5 median filter is performed on the given file.
         */
        task2PPM = PPMConvolve.median(task2PPM, 5);

        Map<Integer,Blob> blobs = new HashMap<Integer,Blob>();

        //background removal run
        task2PPM.removeColour(new PixHSV(10d, 0.05d, 0.2d),new PixHSV(52d, 0.25d, 0.95d), new PixRGB(0,0,0), true);

        //Yellow/Orange range run
        PPMFile orange_yellow = new PPMFile(task2PPM);
        orange_yellow.removeColour(new PixHSV(21d, .8d, .2d),new PixHSV(52d, 1d, .92d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(orange_yellow,colourImage, 0.001, "orange_yellow", inFile.getFileName()));

        //darker blue run
        PPMFile dark_blue = new PPMFile(task2PPM);
        dark_blue.removeColour(new PixHSV(180d, .85d, .15d),new PixHSV(235d, 1d, .58d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(dark_blue,colourImage, 0.001, "dark_blue", inFile.getFileName()));

        //lighter blue run
        PPMFile light_blue = new PPMFile(task2PPM);
        light_blue.removeColour(new PixHSV(170d, 0d, 0d),new PixHSV(250d, .5d, .5d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(light_blue,colourImage, 0.002, "light_blue", inFile.getFileName()));

        //purple run
        PPMFile purple = new PPMFile(task2PPM);
        purple.removeColour(new PixHSV(310, .42d, .05d),new PixHSV(355d, 1, .57d), new PixRGB(255,255,255), false);
        blobs.putAll( ConnectedComponents.blobImage(purple,colourImage, 0.001,"purple", inFile.getFileName()));
        
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
        /**
         * the mexican hat unlike the other objects is quite a complex object 
         * which means i had to do an operation on several colour ranges
         * stage one isolates blue, stage two isolates red and stage three
         * isolates green, they are then added together to form a larger blob.
         */
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

        /**every different colour range of object goes into the same
         * blob pool but are differentiated by a colour pass attribute
         * saying what run produced it.
         */
        for(Map.Entry<Integer,Blob> blob: blobs.entrySet())
        {
            blob.getValue().classifyBlob(colourImage);
        }

        //1. find the purple dome
        Blob bestPurpleBlob = FindBlob.findPurpleDisk(blobs);
        if(bestPurpleBlob != null)
            bestPurpleBlob.writeOut(inFile, outFolder);

        //2. find mr. amazon.
        Blob bestAmazon = FindBlob.findAmazon(blobs);
        if(bestAmazon != null)
            bestAmazon.writeOut(inFile, outFolder);

        //3. find blue toto
        Blob bestBlueToto = FindBlob.findBlueTotoro(blobs);
        if(bestBlueToto != null)
            bestBlueToto.writeOut(inFile, outFolder);

        //4. find grey toto
        Blob bestGreyToto = FindBlob.findGreyToto(blobs);
        if(bestGreyToto != null)
            bestGreyToto.writeOut(inFile, outFolder);

        //5. find keepon
        Blob bestKeepon = FindBlob.findKeepon(blobs);
        if(bestKeepon != null)
            bestKeepon.writeOut(inFile, outFolder);

        //6. find duck

        Blob bestDuck = FindBlob.findDuck(blobs);
        if(bestDuck != null)
            bestDuck.writeOut(inFile, outFolder);
        
        //7. find the wheel
        
        Blob bestWheel = FindBlob.findWheel(blobs);
        if(bestWheel != null)
            bestWheel.writeOut(inFile, outFolder);
        
        //8. find the mexican hat
        
        Blob bestHat = FindBlob.findMexicanHat(blobs);
        if(bestHat != null)
            bestHat.writeOut(inFile, outFolder);
    }
}
    

