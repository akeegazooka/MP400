
package mp400;

/**
 *
 * @author Keegan Ott
 */
public class PolarLine {
    
    private double r;
    private double theta;
    private int votes;
    
    /**
     *
     */
    public PolarLine() {
    }

    /**
     *
     * @param inR
     * @param inTheta
     */
    public PolarLine(double inR, double inTheta) {
        this.r = inR;
        this.theta = inTheta;
        this.votes = 0;
    }

    /**
     *
     * @return
     */
    public double getR() {
        return r;
    }

    /**
     *
     * @param r
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     *
     * @return
     */
    public double getTheta() {
        return theta;
    }

    /**
     *
     * @return
     */
    public int getVotes() {
        return votes;
    }

    /**
     *
     * @param votes
     */
    public void setVotes(int votes) {
        this.votes = votes;
    }

    /**
     *
     * @param theta
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }
}
