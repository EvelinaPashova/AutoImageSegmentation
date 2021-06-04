package lv.lumii.pixelmaster.modules.recognition.domain;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 * Klase atrod attelā redzamās teksta zīmes vektorus
 * @author Sandra Rivare
 * @since 11.04.2009
 */
public class FindVectors {
    /**
     * Atrod vektora garumus no attēla centra līdz tālākam teksta zīmes pikselim
    **/
    public static CharacterInfo findVectorLength(RasterImage rImage,CharacterInfo table) {
        table.centerX = rImage.getWidth()/2;
        table.centerY = rImage.getHeight()/2;

        //atrod V1
        table.V1 = 0;
        boolean cont = true;
        int[][] vecAB = new int [1][2];
        for(int x=0;x<=table.centerX;x++){
            int r = RGB.getR(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                //vecAB[0][1] = 0; //table.centerY - table.centerY
                //double sum = vecAB[0][0]*vecAB[0][0];//+vecAB[0][1]*vecAB[0][1];
                table.V1 = vecAB[0][0];
                //table.V1 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.V1 = "+table.V1);

        //atrod V2
        table.V2 = 0;
        cont = true;
        for(int y=0;y<=table.centerY;y++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            if(r==0 && g==0 && b==0){
                //vecAB[0][0] = table.centerX - table.centerX;
                vecAB[0][1] = table.centerY - y;
                //double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                //table.V2 = (int)Math.sqrt(sum);
                table.V2 = vecAB[0][1];
                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.V2 = "+table.V2);

        //atrod V3
        table.V3 = 0;
        cont = true;
        for(int x=rImage.getWidth();x>=table.centerX;x--){
            int r = RGB.getR(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                //vecAB[0][1] = table.centerY - table.centerY;
                //double sum = vecAB[0][0]*vecAB[0][0];//+vecAB[0][1]*vecAB[0][1];
                table.V3 = vecAB[0][0];//(int)Math.sqrt(sum);

                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.V3 = "+table.V3);

        //finding V4
        table.V4 = 0;
        cont = true;
        for(int y=rImage.getHeight()-1;y>=table.centerY;y--){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            if(r==0 && g==0 && b==0){
                //vecAB[0][0] = table.centerX - table.centerX;
                vecAB[0][1] = y - table.centerY;
                //double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                //table.V4 = (int)Math.sqrt(sum);
                table.V4 = vecAB[0][1];
                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.V4 = "+table.V4);

        //atrod V5
        table.V5 = 0;
        cont = true;
        int x,y;
        x = 0;
        y = rImage.getHeight()-1;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                vecAB[0][1] = y - table.centerY;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.V5 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            x++;
            y--;
        }
        System.out.print(" result.V5 = "+table.V5);

        //atrod V6
        table.V6 = 0;
        x = rImage.getWidth()-1;
        y = rImage.getHeight()-1;
        cont = true;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                vecAB[0][1] = y - table.centerY;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.V6 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            x--;
            y--;
        }
        System.out.print(" result.V6 = "+table.V6);

        //atrod V7
        table.V7 = 0;
        x = rImage.getWidth();
        y = 0;
        cont = true;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                vecAB[0][1] = table.centerY - y;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.V7 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            x--;
            y++;
        }
        System.out.print(" result.V7 = "+table.V7);

        //atrod V8
        table.V8 = 0;
        x = 0;
        y = 0;
        cont = true;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                vecAB[0][1] = table.centerY - y;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.V8 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            x++;
            y++;
        }
        System.out.print(" result.V8 = "+table.V8);
        /**********************************************************************/
        //atrod VV1
        table.VV1 = 0;
        cont = true;
        int count = 0;
        x = 0;
        y = rImage.getHeight()/4;
        y = y*3;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                vecAB[0][1] = y - table.centerY;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV1 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){y--; count = 0;}
            x++;
        }
        System.out.print(" result.VV1 = "+table.VV1);

        //atrod VV2
        table.VV2 = 0;
        cont = true;
        count = 0;
        x = rImage.getWidth()/4;
        y = rImage.getHeight()-1;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                vecAB[0][1] = y - table.centerY;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV2 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){x++; count=0;}
            y--;
        }
        System.out.print(" result.VV2 = "+table.VV2);

        //atrod VV3
        table.VV3 = 0;
        x = rImage.getWidth()/4;
        x = x*3;
        y = rImage.getHeight()-1;
        cont = true;
        count = 0;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                vecAB[0][1] = y - table.centerY;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV3 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){x--; count=0;}
            y--;
        }
        System.out.print(" result.VV3 = "+table.VV3);

        //atrod VV4
        table.VV4 = 0;
        x = rImage.getWidth()-1;
        y = rImage.getHeight()/4;
        y = y*3;
        cont = true;
        count = 0;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                vecAB[0][1] = y - table.centerY;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV4 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){y--; count=0;}
            x--;
        }
        System.out.print(" result.VV4 = "+table.VV4);

        //atrod VV5
        table.VV5 = 0;
        x = rImage.getWidth();
        y = rImage.getHeight()/4;
        cont = true;
        count=0;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                vecAB[0][1] = table.centerY - y;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV5 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){y++; count=0;}
            x--;
        }
        System.out.print(" result.VV5 = "+table.VV5);

        //atrod VV6
        table.VV6 = 0;
        x = rImage.getWidth()/4;
        x=x*3;
        y = 0;
        cont = true;
        count = 0;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                vecAB[0][1] = table.centerY - y;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV6 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){x--; count=0;}
            y++;
        }
        System.out.print(" result.VV6 = "+table.VV6);

        //atrod VV7
        table.VV7 = 0;
        x = rImage.getWidth()/4;
        y = 0;
        cont = true;
        count = 0;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                vecAB[0][1] = table.centerY - y;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV7 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){x++; count=0;}
            y++;
        }
        System.out.print(" result.VV7 = "+table.VV7);

        //atrod VV8
        table.VV8 = 0;
        x = 0;
        y = rImage.getHeight()/4;
        cont = true;
        count = 0;

        for(int i=0;i<=table.centerX;i++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+x));
            if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                vecAB[0][1] = table.centerY - y;
                double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                table.VV8 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
            count++;
            if(count==3){y++; count=0;}
            x++;
        }
        System.out.print(" result.VV8 = "+table.VV8);

        //meklē attālumu no centra līdz melnam pikselim
        table.fromCenterV1 = 0;
        cont = true;
        for(x=table.centerX;x>=0;x--){
            int r = RGB.getR(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            if(x==table.centerX && r==0 && g==0 && b==0){
                while(r==0 && g==0 && b==0 && x!=0){
                    r = RGB.getR(rImage.getRGB(table.centerY*rImage.getWidth()+x));
                    g = RGB.getG(rImage.getRGB(table.centerY*rImage.getWidth()+x));
                    b = RGB.getB(rImage.getRGB(table.centerY*rImage.getWidth()+x));
                    x--;
                }
            }
            else if(r==0 && g==0 && b==0){
                vecAB[0][0] = table.centerX - x;
                //vecAB[0][1] = 0; //table.centerY - table.centerY
                //double sum = vecAB[0][0]*vecAB[0][0];//+vecAB[0][1]*vecAB[0][1];
                table.fromCenterV1 = vecAB[0][0];
                //table.V1 = (int)Math.sqrt(sum);
                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.fromCenterV1 "+table.fromCenterV1);

        //atrod V2
        table.fromCenterV2 = 0;
        cont = true;
        for(y=table.centerY;y>=0;y--){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            if(y==table.centerY && r==0 && g==0 && b==0){
                while(r==0 && g==0 && b==0 && y>=0){
                    r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+table.centerX));
                    g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+table.centerX));
                    b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+table.centerX));
                    y--;
                }
            }
            else if(r==0 && g==0 && b==0){
                //System.out.println(" check " + y );
                //vecAB[0][0] = table.centerX - table.centerX;
                vecAB[0][1] = table.centerY - y;
                //double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                //table.V2 = (int)Math.sqrt(sum);
                table.fromCenterV2 = vecAB[0][1];
                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.fromCenterV2 "+table.fromCenterV2);

        //atrod V3
        table.fromCenterV3 = 0;
        cont = true;
        for(x=table.centerX;x<=rImage.getWidth();x++){
            int r = RGB.getR(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int g = RGB.getG(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            int b = RGB.getB(rImage.getRGB(table.centerY*rImage.getWidth()+x));
            if(x==table.centerX && r==0 && g==0 && b==0){
                while(r==0 && g==0 && b==0 && x!=rImage.getWidth()){
                    r = RGB.getR(rImage.getRGB(table.centerY*rImage.getWidth()+x));
                    g = RGB.getG(rImage.getRGB(table.centerY*rImage.getWidth()+x));
                    b = RGB.getB(rImage.getRGB(table.centerY*rImage.getWidth()+x));
                    x++;
                }
            }
            else if(r==0 && g==0 && b==0){
                vecAB[0][0] = x - table.centerX;
                //vecAB[0][1] = table.centerY - table.centerY;
                //double sum = vecAB[0][0]*vecAB[0][0];//+vecAB[0][1]*vecAB[0][1];
                table.fromCenterV3 = vecAB[0][0];//(int)Math.sqrt(sum);

                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.fromCenterV3 "+table.fromCenterV3);

        //finding V4
        table.fromCenterV4 = 0;
        cont = true;
        for(y=table.centerY;y<rImage.getHeight();y++){
            int r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            int b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+table.centerX));
            if(y==table.centerY && r==0 && g==0 && b==0){
                while(r==0 && g==0 && b==0 && y!=rImage.getHeight()){
                    r = RGB.getR(rImage.getRGB(y*rImage.getWidth()+table.centerX));
                    g = RGB.getG(rImage.getRGB(y*rImage.getWidth()+table.centerX));
                    b = RGB.getB(rImage.getRGB(y*rImage.getWidth()+table.centerX));
                    y++;
                }
            }
            else if(r==0 && g==0 && b==0){
                //vecAB[0][0] = table.centerX - table.centerX;
                vecAB[0][1] = y - table.centerY;
                //double sum = vecAB[0][0]*vecAB[0][0]+vecAB[0][1]*vecAB[0][1];
                //table.V4 = (int)Math.sqrt(sum);
                table.fromCenterV4 = vecAB[0][1];
                cont = false;
            }
            if(cont==false) break;
        }
        System.out.print(" result.fromCenterV4 "+table.fromCenterV4);
        return table;
    }
}
