package lv.lumii.pixelmaster.modules.recognition.domain;

import lv.lumii.pixelmaster.modules.recognition.domain.CharacterDatabase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;

/**
 * Klase satur metodes, kas darbojās ar informāciju par teksta zīmi attēlā un datu bāzē.
 **/
public class Correlation {
     /**
     * Aprēķina datu bāzē dotās informācijas korelācijas koeficientu ar attēlā
     * dotās teksta zīmes informāciju.
     * @author Sandra Rivare
     * @since 03.04.2009
     * @modified 14.04.2009 pievienoti vēl 8 vektori
     * @modified 16.04.2009 pievienota papildus pārbaude uz vektoru garumā 0
     * @param table satur attēla attēlotās teksta zīmes parametrus
     * @param cDB satur datu bāzē dotos teksta zīmju parametrus
     * @return 
     **/

    public static int Correlation(CharacterInfo table, CharacterDatabase cDB, int isNull){
        /**
         * isNull:
         * V1 == 0 == 1
         * V2 == 0 == 2
         * V3 == 0 == 3
         * V4 == 0 == 4
         * none == 0 == 0
         **/

        double x0=0,y0=0;

        x0 = (table.V1+table.V2+table.V3+table.V4+table.V5+table.V6+table.V7+table.V8
                +table.VV1+table.VV2+table.VV3+table.VV4+table.VV5+table.VV6+table.VV7
                +table.VV8)/16;
        double x1,x2,x3,x4,x5,x6,x7,x8,x21,x22,x23,x24,x25,x26,x27,x28;
        x1 = table.V1 - x0;
        x2 = table.V2 - x0;
        x3 = table.V3 - x0;
        x4 = table.V4 - x0;
        x5 = table.V5 - x0;
        x6 = table.V6 - x0;
        x7 = table.V7 - x0;
        x8 = table.V8 - x0;

        x21 = table.VV1 - x0;
        x22 = table.VV2 - x0;
        x23 = table.VV3 - x0;
        x24 = table.VV4 - x0;
        x25 = table.VV5 - x0;
        x26 = table.VV6 - x0;
        x27 = table.VV7 - x0;
        x28 = table.VV8 - x0;

        if(isNull==0){
            //System.out.println("    isNull == 0");
            for(int i=0;i<cDB.rowCount;i++){
                cDB.variance[i] = CorrelationY(i,x1,x2,x3,x4,x5,x6,x7,x8,x21,x22,x23,x24,x25,x26,x27,x28,cDB);
                //System.out.println(cDB.letter[i]+"  R = "+cDB.variance[i]);
            }
        }//end of if isNull == 0
        else if(isNull==1){
            //System.out.println("    isNull == 1");
            for(int i=0;i<cDB.rowCount;i++){
                if(cDB.V1[i] == 0){
                    cDB.variance[i] = CorrelationY(i,x1,x2,x3,x4,x5,x6,x7,x8,x21,x22,x23,x24,x25,x26,x27,x28,cDB);
                //System.out.println(cDB.letter[i]+"  R = "+cDB.variance[i]);
                }
            }
        }//end of if isNull == 1
        else if(isNull==2){
            //System.out.println("    isNull == 2");
            for(int i=1;i<cDB.rowCount;i++){
                if(cDB.V2[i] == 0){
                    cDB.variance[i] = CorrelationY(i,x1,x2,x3,x4,x5,x6,x7,x8,x21,x22,x23,x24,x25,x26,x27,x28,cDB);
                //System.out.println(cDB.letter[i]+"  R = "+cDB.variance[i]);
                }
            }
        }//end of if isNull == 2
        else if(isNull==3){
            //System.out.println("    isNull == 3");
            for(int i=0;i<cDB.rowCount;i++){
                if(cDB.V3[i] == 0){
                    cDB.variance[i] = CorrelationY(i,x1,x2,x3,x4,x5,x6,x7,x8,x21,x22,x23,x24,x25,x26,x27,x28,cDB);
                //System.out.println(cDB.letter[i]+"  R = "+cDB.variance[i]);
                }
            }
        }//end of if isNull == 3
        else if(isNull==4){
            //System.out.println("    isNull == 4");
            for(int i=0;i<cDB.rowCount;i++){
                if(cDB.V4[i] == 0){
                    cDB.variance[i] = CorrelationY(i,x1,x2,x3,x4,x5,x6,x7,x8,x21,x22,x23,x24,x25,x26,x27,x28,cDB);
                //System.out.println(cDB.letter[i]+"  R = "+cDB.variance[i]);
                }
            }
        }//end of if isNull == 4
        return searchTheBestVar(cDB,table);
    }

    public static double CorrelationY(int i,double x1,double x2,double x3,double x4,double x5,
            double x6,double x7,double x8,double x21,double x22,double x23,double x24,double x25,
            double x26,double x27,double x28, CharacterDatabase cDB){
            double y0 = (cDB.V1[i]+cDB.V2[i]+cDB.V3[i]+cDB.V4[i]+cDB.V5[i]+cDB.V6[i]
                            +cDB.V7[i]+cDB.V8[i]+cDB.VV1[i]+cDB.VV2[i]+cDB.VV3[i]+cDB.VV4[i]
                            +cDB.VV5[i]+cDB.VV6[i]+cDB.VV7[i]+cDB.VV8[i])/16;
                    double y1,y2,y3,y4,y5,y6,y7,y8,y21,y22,y23,y24,y25,y26,y27,y28;
                    y1 = cDB.V1[i]-y0;
                    y2 = cDB.V2[i]-y0;
                    y3 = cDB.V3[i]-y0;
                    y4 = cDB.V4[i]-y0;
                    y5 = cDB.V5[i]-y0;
                    y6 = cDB.V6[i]-y0;
                    y7 = cDB.V7[i]-y0;
                    y8 = cDB.V8[i]-y0;

                    y21 = cDB.VV1[i]-y0;
                    y22 = cDB.VV2[i]-y0;
                    y23 = cDB.VV3[i]-y0;
                    y24 = cDB.VV4[i]-y0;
                    y25 = cDB.VV5[i]-y0;
                    y26 = cDB.VV6[i]-y0;
                    y27 = cDB.VV7[i]-y0;
                    y28 = cDB.VV8[i]-y0;
                    double xy = (x1*y1+x2*y2+x3*y3+x4*y4+x5*y5+x6*y6+x7*y7+x8*y8
                            +x21*y21+x22*y22+x23*y23+x24*y24+x25*y25+x26*y26+x27*y27+x28*y28)/16;
                    double Dx,Dy;
                    Dx = (Math.pow(x1,2)+Math.pow(x2,2)+Math.pow(x3,2)+Math.pow(x4,2)+Math.pow(x5,2)
                            +Math.pow(x6,2)+Math.pow(x7,2)+Math.pow(x8,2)+Math.pow(x21,2)
                            +Math.pow(x22,2)+Math.pow(x23,2)+Math.pow(x24,2)+Math.pow(x25,2)
                            +Math.pow(x26,2)+Math.pow(x27,2)+Math.pow(x28,2))/16;
                    Dy = (Math.pow(y1,2)+Math.pow(y2,2)+Math.pow(y3,2)+Math.pow(y4,2)+Math.pow(y5,2)
                            +Math.pow(y6,2)+Math.pow(y7,2)+Math.pow(y8,2)+Math.pow(y21,2)
                            +Math.pow(y22,2)+Math.pow(y23,2)+Math.pow(y24,2)+Math.pow(y25,2)
                            +Math.pow(y26,2)+Math.pow(y27,2)+Math.pow(y28,2))/16;
                    double Dxx,Dyy;
                    Dxx = Math.sqrt(Dx);
                    Dyy = Math.sqrt(Dy);
                    double R;
                    R = xy/(Dxx*Dyy);
                    return R;
    }

   
    /**
     * Meklē teksta zīmi no datu bāzes ar visaugstāko korelācijas koeficientu
     * @param cDB satur datu bāzē dotos teksta zīmju parametrus un korelācijas koeficientu
     **/
    private static int searchTheBestVar(CharacterDatabase cDB, CharacterInfo table) {
        int record = 0;
        double max = 0;
        //System.out.println();
        for(int i=0;i<cDB.rowCount;i++){
            if(max < cDB.variance[i]){
                max = cDB.variance[i];
                record = i;
                //System.out.println(cDB.letter[i]+"  R = "+cDB.variance[i]);
            }
        }
        
        double max2=0;
        for(int i=0;i<cDB.rowCount;i++){
            if(max == cDB.variance[i] /*&& record!=i*/){
                //System.out.println(cDB.letter[i]+"  R2 = "+cDB.variance[i]);
                //max = cDB.variance[i];
                double x0=0,y0=0;
                x0 = (table.fromCenterV1+table.fromCenterV2+table.fromCenterV3
                        +table.fromCenterV4)/4;
                double x1,x2,x3,x4;
                x1 = table.fromCenterV1 - x0;
                x2 = table.fromCenterV2 - x0;
                x3 = table.fromCenterV3 - x0;
                x4 = table.fromCenterV4 - x0;

                y0 = (cDB.fromCenterV1[i]+cDB.fromCenterV2[i]+cDB.fromCenterV3[i]
                        +cDB.fromCenterV4[i])/4;
                    double y1,y2,y3,y4;
                    y1 = cDB.fromCenterV1[i]-y0;
                    y2 = cDB.fromCenterV2[i]-y0;
                    y3 = cDB.fromCenterV3[i]-y0;
                    y4 = cDB.fromCenterV4[i]-y0;

                    double xy = (x1*y1+x2*y2+x3*y3+x4*y4)/4;
                    double Dx,Dy;
                    Dx = (Math.pow(x1,2)+Math.pow(x2,2)+Math.pow(x3,2)+Math.pow(x4,2))/4;
                    Dy = (Math.pow(y1,2)+Math.pow(y2,2)+Math.pow(y3,2)+Math.pow(y4,2))/4;
                    double Dxx,Dyy;
                    Dxx = Math.sqrt(Dx);
                    Dyy = Math.sqrt(Dy);
                    double R2;
                    R2 = xy/(Dxx*Dyy);
                if(max2<R2){
                    record = i;
                    max2 = R2;
                    //System.out.println("max2 = "+R2);
                }
            }
        }
        return record;
    }
}//end of Correlation class 
