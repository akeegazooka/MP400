/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mp400;

/**
 *
 * @author akeegazooka
 */
public class PixMask
{
    MP2d dimensions;
    Double[][] kernel;
    
    /**
     *
     * @param inKernel
     */
    public PixMask(Double[][] inKernel)
    {
        dimensions = new MP2d(inKernel.length, inKernel[0].length);
        kernel = inKernel;
    }
}
