package lv.lumii.pixelmaster.modules.recognition.domain.database;

import lv.lumii.pixelmaster.modules.recognition.domain.ResizeImage;
import lv.lumii.pixelmaster.modules.recognition.domain.CutCharacter;
import lv.lumii.pixelmaster.modules.recognition.domain.FindVectors;
import lv.lumii.pixelmaster.modules.recognition.domain.ImageModification;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;
import lv.lumii.pixelmaster.modules.binarization.domain.Binarization;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

/**
 *
 * @author Sandra Rivare
 * @since 22.04.2009
 */
public class NewDatabase {
    public static void createNewDatabase() throws IOException {
        CharacterInfo table = new CharacterInfo();
        String path = null;
        BufferedImage tmpBufImage = null;
        String letter = null;

		File dir = new File(System.getProperty("user.home") + ("/.pixelmaster/modules/recognition/"));
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// output
		File output = new File(dir.getAbsolutePath() + "/charDB.txt");
		FileOutputStream fos = new FileOutputStream(output);
		PrintWriter out = new PrintWriter(fos);

        for(int i=0;i<62;i++){
            if(i==0){path = "A.bmp"; letter = "A";}
            else if(i==1){path = "B.bmp"; letter = "B";}
            else if(i==2){path = "C.bmp";letter = "C";}
            else if(i==3){path = "D.bmp";letter = "D";}
            else if(i==4){path = "E.bmp";letter = "E";}
            else if(i==5){path = "F.bmp";letter = "F";}
            else if(i==6){path = "G.bmp";letter = "G";}
            else if(i==7){path = "H.bmp";letter = "H";}
            else if(i==8){path = "I.bmp";letter = "I";}
            else if(i==9){path = "J.bmp";letter = "J";}
            else if(i==10){path = "K.bmp";letter = "K";}
            else if(i==11){path = "L.bmp";letter = "L";}
            else if(i==12){path = "M.bmp";letter = "M";}
            else if(i==13){path = "N.bmp";letter = "N";}
            else if(i==14){path = "O.bmp";letter = "O";}
            else if(i==15){path = "P.bmp";letter = "P";}
            else if(i==16){path = "Q.bmp";letter = "Q";}
            else if(i==17){path = "R.bmp";letter = "R";}
            else if(i==18){path = "S.bmp";letter = "S";}
            else if(i==19){path = "T.bmp";letter = "T";}
            else if(i==20){path = "U.bmp";letter = "U";}
            else if(i==21){path = "V.bmp";letter = "V";}
            else if(i==22){path = "W.bmp";letter = "W";}
            else if(i==23){path = "X.bmp";letter = "X";}
            else if(i==24){path = "Y.bmp";letter = "Y";}
            else if(i==25){path = "Z.bmp";letter = "Z";}
            else if(i==26){path = "low case/a.bmp";letter = "a";}
            else if(i==27){path = "low case/b.bmp";letter = "b";}
            else if(i==28){path = "low case/c.bmp";letter = "c";}
            else if(i==29){path = "low case/d.bmp";letter = "d";}
            else if(i==30){path = "low case/e.bmp";letter = "e";}
            else if(i==31){path = "low case/f.bmp";letter = "f";}
            else if(i==32){path = "low case/g.bmp";letter = "g";}
            else if(i==33){path = "low case/h.bmp";letter = "h";}
            else if(i==34){path = "low case/i.bmp";letter = "i";}
            else if(i==35){path = "low case/j.bmp";letter = "j";}
            else if(i==36){path = "low case/k.bmp";letter = "k";}
            else if(i==37){path = "low case/l.bmp";letter = "l";}
            else if(i==38){path = "low case/m.bmp";letter = "m";}
            else if(i==39){path = "low case/n.bmp";letter = "n";}
            else if(i==40){path = "low case/o.bmp";letter = "o";}
            else if(i==41){path = "low case/p.bmp";letter = "p";}
            else if(i==42){path = "low case/q.bmp";letter = "q";}
            else if(i==43){path = "low case/r.bmp";letter = "r";}
            else if(i==44){path = "low case/s.bmp";letter = "s";}
            else if(i==45){path = "low case/t.bmp";letter = "t";}
            else if(i==46){path = "low case/u.bmp";letter = "u";}
            else if(i==47){path = "low case/v.bmp";letter = "v";}
            else if(i==48){path = "low case/w.bmp";letter = "w";}
            else if(i==49){path = "low case/x.bmp";letter = "x";}
            else if(i==50){path = "low case/y.bmp";letter = "y";}
            else if(i==51){path = "low case/z.bmp";letter = "z";}
            else if(i==52){path = "numbers/0.bmp";letter = "0";}
            else if(i==53){path = "numbers/1.bmp";letter = "1";}
            else if(i==54){path = "numbers/2.bmp";letter = "2";}
            else if(i==55){path = "numbers/3.bmp";letter = "3";}
            else if(i==56){path = "numbers/4.bmp";letter = "4";}
            else if(i==57){path = "numbers/5.bmp";letter = "5";}
            else if(i==58){path = "numbers/6.bmp";letter = "6";}
            else if(i==59){path = "numbers/7.bmp";letter = "7";}
            else if(i==60){path = "numbers/8.bmp";letter = "8";}
            else if(i==61){path = "numbers/9.bmp";letter = "9";}

			tmpBufImage = ImageIO.read(new NewDatabase().getClass().getResourceAsStream(
				"/lv/lumii/pixelmaster/modules/recognition/domain/Characters/" + path)
			);

            RasterImage tmpImage = new RasterImage(tmpBufImage.getWidth(), tmpBufImage.getHeight());
            ImageConverter.convertBufImage(tmpBufImage, tmpImage);
            int threshold = Binarization.OtsuThresholding(tmpImage, 10, 30, 60);
            Binarization.binary(tmpImage, tmpImage, threshold, 10, 30, 60);
            tmpImage = CutCharacter.findBorders(tmpImage);
            //skatās, kurš lielums ir lielāks - augstums vai garums
            int templateSize = 100;
            int newW, newH;
            if(tmpImage.getHeight()>=tmpImage.getWidth()){
                newH = templateSize;
                newW = ResizeImage.resizePropertiesNewW(tmpImage, templateSize);
            }else{
                newW = templateSize;
                newH = ResizeImage.resizePropertiesNewH(tmpImage, templateSize);
            }
            //BufferedImage bufOrigImage = new BufferedImage(tmpImage.getWidth(), tmpImage.getHeight(),BufferedImage.TYPE_INT_RGB);
            BufferedImage bufOrigImage = ImageConverter.toBuffered(tmpImage);
            bufOrigImage=ResizeImage.resizeImage(bufOrigImage, newW, newH,bufOrigImage.getWidth(),bufOrigImage.getHeight());
            tmpImage.resize(newW, newH);
            ImageConverter.convertBufImage(bufOrigImage, tmpImage);

            tmpImage = ImageModification.drawWhiteBack(tmpImage,templateSize);
            threshold = Binarization.OtsuThresholding(tmpImage, 10, 30, 60);
            Binarization.binary(tmpImage, tmpImage, threshold, 10, 30, 60);
            table = FindVectors.findVectorLength(tmpImage,table);

            out.println(letter+"\t"+table.V1+"\t"+table.V2+"\t"+table.V3+
                "\t"+table.V4+"\t"+table.V5+"\t"+table.V6+"\t"+table.V7+"\t"+table.V8+"\t"
                 +table.VV1+"\t"+table.VV2+"\t"+table.VV3+"\t"+table.VV4+"\t"+table.VV5+"\t"+table.VV6
                 +"\t"+table.VV7+"\t"+table.VV8+"\t"+table.fromCenterV1+"\t"+table.fromCenterV2
                 +"\t"+table.fromCenterV3+"\t"+table.fromCenterV4);

        }
        //in.close();
        //inFile.delete();
        //outFile.renameTo(inFile);
        //fis.close();
        //out.flush();
        out.close();
    }
}
