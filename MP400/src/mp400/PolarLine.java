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
public class PolarLine {
    
    private double r;
    private double theta;
    private int votes;
    
    public PolarLine() {
    }

    public PolarLine(double inR, double inTheta) {
        this.r = inR;
        this.theta = inTheta;
        this.votes = 0;
        //System.out.println("Theta in constructor : " + this.theta);
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getTheta() {
        return theta;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
