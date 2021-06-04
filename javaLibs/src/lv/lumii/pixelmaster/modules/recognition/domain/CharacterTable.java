package lv.lumii.pixelmaster.modules.recognition.domain;

/**
 * Klase attēlo parametrus, kuri palīdz aprakstīt teksta zīmi attēlā.
 * @author Sandra Rivare
 * @since 26.03.2009
 */
public class CharacterTable {
    // coordinates of symbol pixels
    public int[] X;
    public int[] Y;

    public int place;//recording count
    //character center coordinates
    public int centerX;
    public int centerY;

    //taišņu garumi
    public int V1;
    public int V2;
    public int V3;
    public int V4;
    public int V5;
    public int V6;
    public int V7;
    public int V8;

    public int VV1;
    public int VV2;
    public int VV3;
    public int VV4;
    public int VV5;
    public int VV6;
    public int VV7;
    public int VV8;

    public int fromCenterV1;
    public int fromCenterV2;
    public int fromCenterV3;
    public int fromCenterV4;
}
