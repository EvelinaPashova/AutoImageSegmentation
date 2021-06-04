
package lv.lumii.pixelmaster.modules.ridge.domain;

/**
 *
 * @author Kumpa
 */
public class Monochrome {
    public int cGreen;
    public int cRed;
    public int cBlue;
    public int cSumm;

    public Monochrome () {
        cGreen = 59;
        cRed = 30;
        cBlue = 11;
        cSumm = 100;
    }

    public void set (int coefficientRed, int coefficientGreen, int coefficientBlue) {
        this.cGreen = coefficientGreen;
        this.cBlue = coefficientBlue;
        this.cRed = coefficientRed;
        cSumm = cGreen + cRed + cBlue;
    }
}
