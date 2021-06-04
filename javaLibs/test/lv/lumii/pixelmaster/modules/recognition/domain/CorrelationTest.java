/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lv.lumii.pixelmaster.modules.recognition.domain;

import lv.lumii.pixelmaster.modules.recognition.domain.Correlation;
import junit.framework.TestCase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterDatabase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;

/**
 *
 * @author Sandra Rivare
 * @since 23.04.2009
 * @modiefy 14.05.2009
 */
public class CorrelationTest extends TestCase {
    
    public CorrelationTest(String testName) {
        super(testName);
    }

    /**
     * Test of Correlation method, of class Correlation.
     */
    public void testCorrelation() {
        System.out.println("alg: Correlation");
        CharacterInfo table = new CharacterInfo();
        CharacterInfo table1 = new CharacterInfo();
        CharacterInfo table2 = new CharacterInfo();
        CharacterInfo table3 = new CharacterInfo();
        CharacterInfo table4 = new CharacterInfo();
        CharacterInfo table5 = new CharacterInfo();
        CharacterDatabase cDB = new CharacterDatabase();

        int i = 9;
        cDB.letter = new String[i];
        cDB.V1 = new int[i];cDB.V2 = new int[i];cDB.V3 = new int[i];cDB.V4 = new int[i];
        cDB.V5 = new int[i];cDB.V6 = new int[i];cDB.V7 = new int[i];cDB.V8 = new int[i];
        cDB.VV1 = new int[i];cDB.VV2 = new int[i];cDB.VV3 = new int[i];cDB.VV4 = new int[i];
        cDB.VV5 = new int[i];cDB.VV6 = new int[i];cDB.VV7 = new int[i];cDB.VV8 = new int[i];
        cDB.fromCenterV1 = new int[i];cDB.fromCenterV2 = new int[i];cDB.fromCenterV3 = new int[i];cDB.fromCenterV4 = new int[i];
        
        cDB.letter[1] = "A";cDB.letter[2] = "B"; cDB.letter[3] ="C";cDB.letter[4] ="J";cDB.letter[5] ="L";cDB.letter[6] ="h";cDB.letter[7]="i";cDB.letter[8]="I";
        cDB.V1[1] = 28; cDB.V1[2] = 38; cDB.V1[3] = 43; cDB.V1[4] = 0; cDB.V1[5] = 31; cDB.V1[6] = 30; cDB.V1[7] = 7; cDB.V1[8] = 7;
        cDB.V2[1] = 50; cDB.V2[2] = 50; cDB.V2[3] = 50; cDB.V2[4] = 0; cDB.V2[5] = 0; cDB.V2[6] = 27; cDB.V2[7] = 50; cDB.V2[8] = 50;
        cDB.V3[1] = 26; cDB.V3[2] = 27; cDB.V3[3] = 0; cDB.V3[4] = 26; cDB.V3[5] = 0; cDB.V3[6] = 29; cDB.V3[7] = 5; cDB.V3[8] = 5;
        cDB.V4[1] = 18; cDB.V4[2] = 49; cDB.V4[3] = 49; cDB.V4[4] = 49; cDB.V4[5] = 49; cDB.V4[6] = 0; cDB.V4[7] = 49; cDB.V4[8] = 49;
        cDB.V5[1] = 64; cDB.V5[2] = 53; cDB.V5[3] = 47; cDB.V5[4] = 37; cDB.V5[5] = 43; cDB.V5[6] = 41; cDB.V5[7] = 9; cDB.V5[8] = 9;
        cDB.V6[1] = 62; cDB.V6[2] = 46; cDB.V6[3] = 48; cDB.V6[4] = 35; cDB.V6[5] = 0; cDB.V6[6] = 41; cDB.V6[7] = 7; cDB.V6[8] = 7;
        cDB.V7[1] = 25; cDB.V7[2] = 43; cDB.V7[3] = 48; cDB.V7[4] = 36; cDB.V7[5] = 0; cDB.V7[6] = 29; cDB.V7[7] = 7; cDB.V7[8] = 7;
        cDB.V8[1] = 28; cDB.V8[2] = 53; cDB.V8[3] = 48; cDB.V8[4] = 0; cDB.V8[5] = 43; cDB.V8[6] = 42; cDB.V8[7] = 9; cDB.V8[8] = 9;
        cDB.VV1[1] = 42; cDB.VV1[2] = 43; cDB.VV1[3] = 44; cDB.VV1[4] = 0;cDB.VV1[5] = 36; cDB.VV1[6] = 35;cDB.VV1[7] = 13;cDB.VV1[8] = 13;
        cDB.VV2[1] = 23; cDB.VV2[2] = 55; cDB.VV2[3] = 48; cDB.VV2[4] = 45;cDB.VV2[5] = 55; cDB.VV2[6] = 55;cDB.VV2[7] = 0;cDB.VV2[8] = 0;
        cDB.VV3[1] = 23; cDB.VV3[2] = 50; cDB.VV3[3] = 50; cDB.VV3[4] = 44;cDB.VV3[5] = 55; cDB.VV3[6] = 55;cDB.VV3[7] = 0;cDB.VV3[8] = 0;
        cDB.VV4[1] = 40; cDB.VV4[2] = 41; cDB.VV4[3] = 45; cDB.VV4[4] = 31;cDB.VV4[5] = 0; cDB.VV4[6] = 34;cDB.VV4[7] = 12;cDB.VV4[8] = 12;
        cDB.VV5[1] = 25; cDB.VV5[2] = 36; cDB.VV5[3] = 45; cDB.VV5[4] = 31;cDB.VV5[5] = 0; cDB.VV5[6] = 30;cDB.VV5[7] = 11;cDB.VV5[8] = 11;
        cDB.VV6[1] = 28; cDB.VV6[2] = 49; cDB.VV6[3] = 51; cDB.VV6[4] = 55;cDB.VV6[5] = 0; cDB.VV6[6] = 30;cDB.VV6[7] = 0;cDB.VV6[8] = 0;
        cDB.VV7[1] = 31; cDB.VV7[2] = 55; cDB.VV7[3] = 49; cDB.VV7[4] = 0;cDB.VV7[5] = 55; cDB.VV7[6] = 55;cDB.VV7[7] = 0;cDB.VV7[8] = 0;
        cDB.VV8[1] = 27; cDB.VV8[2] = 43; cDB.VV8[3] = 45; cDB.VV8[4] = 0;cDB.VV8[5] = 36; cDB.VV8[6] = 35;cDB.VV8[7] = 13;cDB.VV8[8] = 13;
        cDB.fromCenterV1[1] = 15; cDB.fromCenterV1[2] = 0; cDB.fromCenterV1[3] = 30; cDB.fromCenterV1[4] = 0; cDB.fromCenterV1[5] = 18; cDB.fromCenterV1[6] = 18;cDB.fromCenterV1[7] = 0;cDB.fromCenterV1[8] = 0;
        cDB.fromCenterV2[1] = 36; cDB.fromCenterV2[2] = 39; cDB.fromCenterV2[3] = 39; cDB.fromCenterV2[4] = 0; cDB.fromCenterV2[5] = 0; cDB.fromCenterV2[6] = 16;cDB.fromCenterV2[7] = 37;cDB.fromCenterV2[8] = 0;
        cDB.fromCenterV3[1] = 13; cDB.fromCenterV3[2] = 0; cDB.fromCenterV3[3] = 0; cDB.fromCenterV3[4] = 13; cDB.fromCenterV3[5] = 0; cDB.fromCenterV3[6] = 17;cDB.fromCenterV3[7] = 0;cDB.fromCenterV3[8] = 0;
        cDB.fromCenterV4[1] = 7; cDB.fromCenterV4[2] = 38; cDB.fromCenterV4[3] = 38; cDB.fromCenterV4[4] = 38; cDB.fromCenterV4[5] = 38; cDB.fromCenterV4[6] = 0;cDB.fromCenterV4[7] = 0;cDB.fromCenterV4[8] = 0;

        table.V1 = 28; table.V2 = 50; table.V3 = 26; table.V4 = 18;
        table.V5 = 64; table.V6 = 62; table.V7 = 25; table.V8 = 28;
        table.VV1 = 42; table.VV2 = 23; table.VV3 = 23; table.VV4 = 40;
        table.VV5 = 25; table.VV6 = 28; table.VV7 = 31; table.VV8 = 27;
        table.fromCenterV1 = 15; table.fromCenterV2 = 36; table.fromCenterV3 = 13;
        table.fromCenterV4 = 7;

        table1.V1 = 0; table1.V2 = 0; table1.V3 = 26; table1.V4 = 49;
        table1.V5 = 37; table1.V6 = 35; table1.V7 = 36; table1.V8 = 0;
        table1.VV1 = 0; table1.VV2 = 45; table1.VV3 = 44; table1.VV4 = 31;
        table1.VV5 = 31; table1.VV6 = 55; table1.VV7 = 0; table1.VV8 = 0;
        table1.fromCenterV1 = 0; table1.fromCenterV2 = 0; table1.fromCenterV3 = 13;
        table1.fromCenterV4 = 38;

        table2.V1 = 31; table2.V2 = 0; table2.V3 = 0; table2.V4 = 49;
        table2.V5 = 43; table2.V6 = 0; table2.V7 = 0; table2.V8 = 43;
        table2.VV1 = 36; table2.VV2 = 55; table2.VV3 = 55; table2.VV4 = 0;
        table2.VV5 = 0; table2.VV6 = 0; table2.VV7 = 55; table2.VV8 = 36;
        table2.fromCenterV1 = 18; table2.fromCenterV2 = 0; table2.fromCenterV3 = 0;
        table2.fromCenterV4 = 38;

        table3.V1 = 43; table3.V2 = 50; table3.V3 = 0; table3.V4 = 49;
        table3.V5 = 47; table3.V6 = 48; table3.V7 = 48; table3.V8 = 48;
        table3.VV1 = 44; table3.VV2 = 48; table3.VV3 = 50; table3.VV4 = 45;
        table3.VV5 = 45; table3.VV6 = 51; table3.VV7 = 49; table3.VV8 = 45;
        table3.fromCenterV1 = 30; table3.fromCenterV2 = 39; table3.fromCenterV3 = 0;
        table3.fromCenterV4 = 38;

        table4.V1 = 30; table4.V2 = 27; table4.V3 = 29; table4.V4 = 0;
        table4.V5 = 41; table4.V6 = 41; table4.V7 = 29; table4.V8 = 42;
        table4.VV1 = 35; table4.VV2 = 55; table4.VV3 = 55; table4.VV4 = 34;
        table4.VV5 = 30; table4.VV6 = 30; table4.VV7 = 55; table4.VV8 = 35;
        table4.fromCenterV1 = 18; table4.fromCenterV2 = 16; table4.fromCenterV3 = 17;
        table4.fromCenterV4 = 0;

        table5.V1 = 7; table5.V2 = 50; table5.V3 = 5; table5.V4 = 49;
        table5.V5 = 9; table5.V6 = 7; table5.V7 = 7; table5.V8 = 9;
        table5.VV1 = 13; table5.VV2 = 0; table5.VV3 = 0; table5.VV4 = 12;
        table5.VV5 = 11; table5.VV6 = 0; table5.VV7 = 0; table5.VV8 = 13;
        table5.fromCenterV1 = 0; table5.fromCenterV2 = 37; table5.fromCenterV3 = 0;
        table5.fromCenterV4 = 0;

        cDB.rowCount = 8;
        cDB.variance = new double[8];

        cDB.variance[1] = -1;cDB.variance[2] = -1;cDB.variance[3] = -1;
        cDB.variance[4] = -1;cDB.variance[5] = -1;cDB.variance[6] = -1;
        cDB.variance[7] = -1;
        int result = Correlation.Correlation(table, cDB, 0);
        
        cDB.variance[1] = -1;cDB.variance[2] = -1;cDB.variance[3] = -1;
        cDB.variance[4] = -1;cDB.variance[5] = -1;cDB.variance[6] = -1;
        cDB.variance[7] = -1;
        int result1 = Correlation.Correlation(table1, cDB, 1);

        cDB.variance[1] = -1;cDB.variance[2] = -1;cDB.variance[3] = -1;
        cDB.variance[4] = -1;cDB.variance[5] = -1;cDB.variance[6] = -1;
        cDB.variance[7] = -1;
        int result2 = Correlation.Correlation(table2, cDB, 2);

        cDB.variance[1] = -1;cDB.variance[2] = -1;cDB.variance[3] = -1;
        cDB.variance[4] = -1;cDB.variance[5] = -1;cDB.variance[6] = -1;
        cDB.variance[7] = -1;
        int result3 = Correlation.Correlation(table3, cDB, 3);

        cDB.variance[1] = -1;cDB.variance[2] = -1;cDB.variance[3] = -1;
        cDB.variance[4] = -1;cDB.variance[5] = -1;cDB.variance[6] = -1;
        cDB.variance[7] = -1;
        int result4 = Correlation.Correlation(table4, cDB, 4);

        cDB.variance[1] = -1;cDB.variance[2] = -1;cDB.variance[3] = -1;
        cDB.variance[4] = -1;cDB.variance[5] = -1;cDB.variance[6] = -1;
        cDB.variance[7] = -1;
        int result5 = Correlation.Correlation(table5, cDB, 0);

        assertEquals(1, result);
        assertEquals(4, result1);
        assertEquals(5, result2);
        assertEquals(3, result3);
        assertEquals(6, result4);
        assertEquals(7, result5);
    }
}
