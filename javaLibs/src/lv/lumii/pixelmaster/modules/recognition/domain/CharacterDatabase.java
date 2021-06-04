package lv.lumii.pixelmaster.modules.recognition.domain;

/***
 * Klase attēlo parametrus no datu bāzes.
 * @author Sandra Rivare
 * @since 26.03.2009.
 */

public class CharacterDatabase {
    /*** Glabā taišņu garumus un atbilstošo teksta zīmi ***/
    public int[] V1;
    public int[] V2;
    public int[] V3;
    public int[] V4;
    public int[] V5;
    public int[] V6;
    public int[] V7;
    public int[] V8;

    //added 14.04.2009
    public int[] VV1;
    public int[] VV2;
    public int[] VV3;
    public int[] VV4;
    public int[] VV5;
    public int[] VV6;
    public int[] VV7;
    public int[] VV8;

    public int[] fromCenterV1;
    public int[] fromCenterV2;
    public int[] fromCenterV3;
    public int[] fromCenterV4;
    
    public String[] letter;
    /********************************************************/
    public double[] variance; //glabā korelācijas koeficientu
    public int rowCount; //glabā ierakstu skaitu
    //public boolean[] isValid;
}
