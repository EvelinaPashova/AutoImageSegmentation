package lv.lumii.pixelmaster.modules.ridge.domain;


import java.util.List;      
import java.util.ArrayList; 
import java.util.Collections;
import java.awt.Point;


/**
 * Klase glabā Ridge pakotnes apstrādājamo attēlu un satur metodes attēla
 * izpludināšanai, koru atrašanai, līniju izvešanai, līniju sadalīšanai
 * nogriežņos un to apvienošanu nogriežņos un grupās.
 * @author Ainārs Kumpiņš
 */
public class Ridge {
    //Attēls ar  ko strādā Ridge pakotne.
    public Image workingImage;
    //Mainīgais, kurā glabājas pikseļu intensitātes vērtības.
    //Koru meklēšanas rezultāti.
    private int[][] strength;
    //Glabā ridge pakotnes metodēm padodamos parametrus
    private RidgeSettings settings;

    /**
     * Klase kas nodrošina boolean vērtības padošanu un atgriezenisko saiti
     * starp Ridge klases metodēm.
     * @author Ainārs Kumpiņš
     */
    private class IsRidge {
        public boolean isRidge;
    }

    /**
     * Metode uzstāda Klases entītijas settings vērtību
     * @param settings
     * @return vienmēr true
     * @author Ainārs Kumpiņš
     */
    public boolean setSettings(RidgeSettings settings) {
        this.settings = settings;
        return true;
    }

    /**
     * Metode atgriež pikseļa intensitātes vērtību norādītajās koordinātēs
     * @param width pikseļa X koordināte.
     * @param height pikseļa Y koordināte.
     * @return intensitātes vērtība norādītajas koordinātēs.
     * @author Ainārs Kumpiņš
     */
    public int getStength (int width, int height) {
        if (width > workingImage.getWidth() || height > workingImage.getHeight()
                || width < 0 || height < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (strength==null) {
            return 0;
        }
        return strength[width][height];
    }

/**
 * Metode veic attēla priekšapstrādi un sagatavošanu koru meklēšanai.
 * Priekšamstrādē ietilps pelēkuma skalas (greyscale) attēla iegūšana no
 * izejas attēla, Attēla izpludināšana un jauna strenght datu objekta
 * izveidošana ar izmēriem, kas ir vienādi ar ieejas attēla augstumu un
 * platumu.
 * @param inputImage ieejas attēls
 * @param settings ridge pakotnes metodēm padodamie parametri.
 * @author Ainārs Kumpiņš
 */
public  void preprocess (Image inputImage, RidgeSettings settings) {
    this.settings = settings;
    preprocess(inputImage);
}

/**
 * Metode veic attēla priekšapstrādi un sagatavošanu koru meklēšanai.
 * Priekšamstrādē ietilps pelēkuma skalas (greyscale) attēla iegūšana no
 * izejas attēla, Attēla izpludināšana un jauna strenght datu objekta
 * izveidošana ar izmēriem, kas ir vienādi ar ieejas attēla augstumu un
 * platumu.
 * @param inputImage ieejas attēls
 * @author Ainārs Kumpiņš
 */
public void preprocess ( Image inputImage) {
    workingImage = new Image(inputImage.getWidth(), inputImage.getHeight());
    inputImage.greyImage();
    smoothImageGB (settings.smoothingRadius, inputImage, workingImage);
    strength = new int[inputImage.getWidth()][inputImage.getHeight()];
}

/**
 * Metode veic attēla izpludināšanu izmantojot Gausa lineārās pludināšanas.
 * @param smoothingRadius attēla izpludināšanas rādiuss.
 * @param inputImage apstrādājamais attēls.
 * @param outputImage objekts kurā tiek saglabāts izpludināšanas rezultāts.
 * @author Ainārs Kumpiņš
 */
private void smoothImageGB2 (int smoothingRadius,  Image inputImage, Image outputImage) {
    int lenght = 2 * smoothingRadius + 1;
    // calculation of gaussian coefficients
    long[][] gaussFact = new long[lenght][lenght];
    gaussFact[0][0] = 1;
    for (int depth = 1; depth < lenght; depth ++) {
        gaussFact[depth][0] = 1;
        gaussFact[depth][depth] = 1;
        for (int i=1; i<=depth; i++) {
            gaussFact[depth][i] = gaussFact[depth-1][i-1] + gaussFact[depth-1][i];
        }
    }
    int gaussSumm = 0;
    for (int i = 0; i < lenght; i++) {
        gaussSumm += gaussFact[lenght - 1][i];
    }
    lenght -= 1; // used for bitshift
    Image tempImage = new Image(outputImage.getWidth(), outputImage.getHeight());
    for (int height = 0; height < tempImage.getHeight(); height++) {
        // columns near borders of image (border <-> smoothingRadius)
        for (int width = 0; width < smoothingRadius; width++) {
            long pixelValueFirst = 0;
            long pixelValueLast = 0;
             for (int k = -smoothingRadius; k <= smoothingRadius; k++) {
                pixelValueFirst +=
                          inputImage.pixels[Math.abs(width + k)][height]
                        * gaussFact[lenght][smoothingRadius + k];
                pixelValueLast +=
                          inputImage.pixels[tempImage.getWidth() - Math.abs(width + k) - 1][height]
                        * gaussFact[lenght][smoothingRadius + k];
             }
            tempImage.pixels[width][height] = (int)(pixelValueFirst >> lenght); // / gaussSumm;
            tempImage.pixels[tempImage.getWidth() - width - 1][height] = (int)(pixelValueLast >> lenght);// / gaussSumm;
        }
        // rest columns
        for (int width = smoothingRadius; width < tempImage.getWidth() - smoothingRadius; width++) {
            long pixelValue = 0;
            for (int k = -smoothingRadius; k <= smoothingRadius; k++) {
                pixelValue += inputImage.pixels[width + k][height] * gaussFact[lenght][smoothingRadius + k];
            }
            tempImage.pixels[width][height] = (int)(pixelValue >> lenght);// / gaussSumm;
        }
    }
    for (int width = 0; width < outputImage.getWidth(); width++) {
        // rows near borders of image (border <-> smoothingRadius)
        for (int height = 0; height < smoothingRadius; height++) {
            long pixelValueFirst = 0;
            long pixelValueLast = 0;
            for (int k = -smoothingRadius; k <= smoothingRadius; k++) {
                pixelValueFirst +=
                        tempImage.pixels[width][Math.abs(height + k)]
                     * gaussFact[lenght][smoothingRadius + k];
                pixelValueLast +=
                        tempImage.pixels[width][tempImage.getHeight() - Math.abs(height + k) - 1]
                     * gaussFact[lenght][smoothingRadius + k];
            }
            outputImage.pixels[width][height] = (int)(pixelValueFirst >> lenght); // / gaussSumm;
            outputImage.pixels[width][outputImage.getHeight() - height - 1] = (int)(pixelValueLast >> lenght); // / gaussSumm;
        }
        // rest rows
        for (int height = smoothingRadius; height < outputImage.getHeight() - smoothingRadius; height++) {
            long pixelValue = 0;
            for (int k = -smoothingRadius; k <= smoothingRadius; k++) {
                pixelValue += tempImage.pixels[width][height + k] * gaussFact[lenght][smoothingRadius + k];
            }
            outputImage.pixels[width][height] = (int)(pixelValue >> lenght); // / gaussSumm;
        }
     }
}

/**
 * Metode veic attēla izpludināšanu izmantojot Gausa lineārās pludināšanas
 * metodi un piemērojot otrās kārtas binoma koeficientus.
 *  Jo tālāk no izpludināmā pikseļa atrodas tā kaimiņi, jo mazāaka ir tā
 * ietekme uz izmpludināmo pikseli.
 * @param smoothingRadius attēla izpludināšanas rādiuss.
 * @param inputImage apstrādājamais attēls.
 * @param outputImage objekts kurā tiek saglabāts izpludināšanas rezultāts.
 * @author Ainārs Kumpiņš
 */
private void smoothImageGB (int smoothingRadius, Image ImputImage, Image outputImage) {
    // binoma koeficienti netiek vairs atsevišķi deklarēti un izmantoti
    // reizināšanā un dalīšanā ar divnieku pakāpēm tiek izmantota bitu pārbīde
    //final int[] GAUSS_FACT = {1, 2, 1};
    //final int GAUSS_SUMM = 4;
    Image tempImage = new Image(outputImage.getWidth(), outputImage.getHeight());
    //attēla pikseļu vērtību iegūšana un dublēšana palīgmainīgajā attēlā.
    for (int height = 0; height < outputImage.getHeight(); height++) {
        for (int width = 0; width < outputImage.getWidth(); width++) {
            outputImage.pixels[width][height] = ImputImage.pixels[width][height];
        }
    }
    // izpludināšana
    // tā kā tiek izmantots otrās kārtas binomu koeficienti, tad pastāv
    // iespēja, ka masīva indeksi izies ārpus robežām par ne vairāk kā 1 vienību
    // tādēļ attēla pirmās un pēdējās rindas un kolonas tiek izpludinātas
    // atsevišķi.
    for (int i = 0; i < smoothingRadius; i++) {
        for (int height = 0; height < tempImage.getHeight(); height++) {
            // pirmās kolonas izpludināšana
            // labās puses pikseļa (ārpus attēla vērtību)
            // ņem tādu pašu kā kreisā pikseļa vērtību
            tempImage.pixels[0][height] =
                    ( ( outputImage.pixels[0][height] << 1)
                    + ( outputImage.pixels[1][height] << 1) ) >> 2;
            // pēdējās kolonas izpludināšana
            // kreisās puses pikseļa (ārpus attēla vērtību)
            // ņem tādu pašu kā labās puses pikseļa vērtību
            tempImage.pixels[tempImage.getWidth() - 1][height] =
                    ( ( outputImage.pixels[tempImage.getWidth() - 1][height] << 1 )
                    + ( outputImage.pixels[tempImage.getWidth() - 2][height] << 1 ) ) >> 2;
            // pārējās attēla kolonas
            for (int width = 1; width < tempImage.getWidth() - 1; width++) {
                tempImage.pixels[width][height] =
                        ( outputImage.pixels[width - 1][height]
                        + ( outputImage.pixels[width][height] << 1 )
                        + outputImage.pixels[width + 1][height] ) >> 2;
            }
        }
        for (int width = 0; width < outputImage.getWidth(); width++) {
            // pirmā attēla rinda
            outputImage.pixels[width][0] =
                    ( ( tempImage.pixels[width][0] << 1 )
                    + ( tempImage.pixels[width][1] << 1 ) ) >> 2;
            // pēdējā rinda
            outputImage.pixels[width][outputImage.getHeight() - 1] =
                    ( ( tempImage.pixels[width][outputImage.getHeight() - 1] << 1 )
                    + ( tempImage.pixels[width][outputImage.getHeight() - 2] << 1 ) ) >> 2;
            // pārējās rindas
            for (int height = 1; height < outputImage.getHeight() - 1; height++) {
                outputImage.pixels[width][height]
                        = ( tempImage.pixels[width][height - 1]
                          + ( tempImage.pixels[width][height] << 1 )
                          + tempImage.pixels[width][height + 1] ) >> 2;
            }
        }
    }
}
/**
 * Metode veic attēla izpludināšanu izmantojot tīrā spēka metodi. Izpludināšanas
 * rezultāts ir precīzāks par smootImageGB, taču metode ir daudz resursu prasīgāka.
 * @param smoothingRadius attēla izpludināšanas rādiuss.
 * @param input Imageapstrādājamais attēls.
 * @param outputImage entītija kurā tiek saglabāts izpludināšanas rezultāts.
 * @author Ainārs Kumpiņš
 */
private void smoothImage (int smoothingRadius, Image inputImage, Image outputImage) {
    double p = smoothingRadius * Math.sqrt(smoothingRadius);
    double[][] wArr;
    // izpludināšanas koeficientu matrica
    wArr = new double[smoothingRadius+1][smoothingRadius+1];
    //izpludināšanas koeficientu matricas koeficientu aprēķināšana un
    // aizpildīšana
    for (int y = 0; y <= smoothingRadius; y++) {
        for (int x = 0; x <=smoothingRadius; x++) {
            wArr[x][y] = Math.exp(-(x*x + y*y)/p);
        }
    }
    // pikseļu masīva pārstaikāšana
    for (int height = 0; height < outputImage.getHeight(); height++) {
        for (int width = 0; width < outputImage.getWidth(); width++) {
            double w, s = 0.0;
            int value = 0;
    //katra pikseļa jaunās vērtības aprēķināšana
        for (int y = -smoothingRadius; y <= smoothingRadius; y++) {
            for (int x = -smoothingRadius; x <= smoothingRadius; x++) {
                // parbaude vai neiziet ārpus attēla robešām
                    if ( -1 < width+x && width+x < outputImage.getWidth() &&
                         -1 < height+y && height+y < outputImage.getHeight()) {

                        w = wArr[Math.abs(x)][Math.abs(y)];
                        value += w * inputImage.pixels[width+x][height+y];
                        s += w;
                    }
                }
            }
            outputImage.pixels[width][height] = (int)(value / s); 
         }
    }
}

/** Metode, kas aprēķina un atgriež kores intensitātes vērtību norādītajās
 * koordinātēs.
 * @param width pikseļa X koordināte.
 * @param height pikseļa Y koordināte.
 * @param isRidge boolean tipa mainīgais, kurā tiek atgriezta vērtība vai
 * pikselis ir lokālais maksimums, kas tiek padots kā klases lauks,
 * jo metode spēj atgriez tikai vienu vērtību, bet ridgeValleyPixelStrength
 * izsaucošajai funkcijai ir jāzina šī izmainītā datu lauka vērtība.
 * @return 0 , ja nav kore.
 * @return kores intensitātes vērtību, ja ir kore. Ja kore ir virsotne (maksimums)
 * tad isRidge tiek uzstādīts true, ja kore ir ieleja (minimums), tad isRidge
 * tiek uzstādīts false.
 * @author Ainārs Kumpiņš
 */
private int ridgeValleyPixelStrength (int width, int height, IsRidge isRidge) {
    final int offsets[][] = { {1, 0},{1,1}, {0,1}, {-1,1},
                             {-1,0},{-1,-1},{0,-1},{1, -1}};
    // pārbaudāmais pikselis.
    int cPixel = workingImage.pixels[width][height];
    int[] fpp;
    fpp = new int[4];
    // aprēķina visos virzienos pikseļa un tā kaimiņu absolūto vērtību starpību
    for (int i = 0; i < 4; i++) {
        int iOpposite = i+4;
        fpp[i] = Math.abs( workingImage.pixels[width + offsets[i][0]][height + offsets[i][1]]
                - 2*cPixel
                + workingImage.pixels[width + offsets[iOpposite][0]][height + offsets[iOpposite][1]] );
    }
    fpp[0] *= 2;
    fpp[2] *= 2;
    // atrod lielāko starpību
    int fMax = fpp[0];
    int fMaxI = 0;
    for (int i = 1; i < 4; i++) {
        if (fpp[i] > fMax) {
            fMax = fpp[i];
            fMaxI = i;
        }
    }
    // ja lielākās starpības vērtība ir mazāka par pieļaujamo minimumu,
    // tad turpināt nav jēgas.
    if (fMax < settings.lowerThreshold)  {
        return 0;
    }
    // maksimālās starpības perpendijulārā starpība
    int fgg = fpp[(fMaxI+2) & 3];
    // iegūst kaimiņu pikseļus maksimuma starpības virzienā
    int f1 = workingImage.pixels[width + offsets[fMaxI][0]][height + offsets[fMaxI][1]];
    int iOpposite = fMaxI + 4;
    int f2 = workingImage.pixels[width + offsets[iOpposite][0]][height + offsets[iOpposite][1]];
    // Kore ir tad, ja abi pretējie kaimiņu pikseļi ir mazāki, vai lielāki
    // par centrālo pikseli
    isRidge.isRidge = f1 < cPixel && f2 <= cPixel;
    boolean isValley = f1 > cPixel && f2 >= cPixel;
	// izliekumam kores viezīenā jābūt mazākam nekā perpendikulāri tai
    if ((isRidge.isRidge || isValley) && fgg*settings.nonCircularity <= fMax ) {
        return fMax;
    }                                          
    return 0;
}

/**
 * Metode, kas aprēķina un atgriež robežas intensitātes
 * vērtību norādītajās koordinātēs.
 * @param width pikseļa X koordināte.
 * @param height pikseļa Y koordināte.
 * @return pārbaudāmā robežpikseļa intensitātes vērtība
 * @author Ainārs Kumpiņš
 */
private int edgePixelStrength (int width, int height) {
    // nemeklē vērtības attēla malās
    if ( width <=1 || workingImage.getWidth() - 2 <= width
       || height <= 1 || workingImage.getHeight() - 2 <= height) {
        return 0;
    }
    // cetra pikselis
    int cPixel = workingImage.pixels[width][height];
    // aprēķina visos virzienos centrālā pikseļa kaimiņu absolūto vērtību starpību
    int[] fpp;
    fpp = new int[4];
    fpp[0] = Math.abs(workingImage.pixels[width + 1][height]
                    - workingImage.pixels[width - 1][height]);
	fpp[1] = (int) (Math.abs(workingImage.pixels[width + 1][height + 1]
                           - workingImage.pixels[width - 1][height - 1])  / Math.sqrt(2));
	fpp[2] = Math.abs(workingImage.pixels[width][height + 1]
                    - workingImage.pixels[width][height - 1]);
	fpp[3] = (int) (Math.abs(workingImage.pixels[width - 1][height + 1]
                           - workingImage.pixels[width + 1][height - 1])  / Math.sqrt(2));
    // atrod lielāko starpību
    int fMax = fpp[0];
    int fMaxI = 0;
    for (int i =1; i < 4; i++) {
        if (fpp[i] > fMax) {
            fMax = fpp[i];
            fMaxI = i;
        }
    }
    int f1 = 0;
    int f2 = 0;
    int fgg = 0;
    // robežas virzienā pārbauda nākamos kaimiņu pikseļus
    switch (fMaxI) {
        case 0:
            f1 = cPixel - 2*workingImage.pixels[width+1][height]
                        + workingImage.pixels[width+2][height];
            f2 = cPixel - 2*workingImage.pixels[width-1][height]
                        + workingImage.pixels[width-2][height];
            fgg = workingImage.pixels[width-1][height] - 2*cPixel
                + workingImage.pixels[width+1][height];
            break;
        case 1:
            f1 = cPixel - 2*workingImage.pixels[width+1][height+1]
                        + workingImage.pixels[width+2][height+2];
            f2 = cPixel - 2*workingImage.pixels[width-1][height-1]
                        + workingImage.pixels[width-2][height-2];
            fgg = workingImage.pixels[width-1][height-1] - 2*cPixel
                + workingImage.pixels[width+1][height+1];
            break;
        case 2:
            f1 = cPixel - 2*workingImage.pixels[width][height+1]
                        + workingImage.pixels[width][height+2];
            f2 = cPixel - 2*workingImage.pixels[width][height-1]
                        + workingImage.pixels[width][height-2];
            fgg = workingImage.pixels[width][height-1] - 2*cPixel
                + workingImage.pixels[width][height+1];
            break;
        case 3:
            f1 = cPixel - 2*workingImage.pixels[width-1][height+1]
                        + workingImage.pixels[width-2][height+2];
            f2 = cPixel - 2*workingImage.pixels[width+1][height-1]
                        + workingImage.pixels[width+2][height-2];
            fgg = workingImage.pixels[width-1][height+1] - 2*cPixel
                + workingImage.pixels[width+1][height-1];
            break;
    }
    // jo tālāk no centrālā pikseļa, jo lielākai jābūt tā kaimiņu
    // pikseļu starpībai
    if ((f1 <= 0 && f2 > 0) || (f1 > 0 && f2 <=0)) {
        // the pixel has to be closest to zero crossing than its neighbors
        // robežpikselim jābūt visstuvāk
        boolean closest = false;
        if(fgg==0) {
          	closest = true;
        }
        else if ((f1<=0 && fgg<=0) || (f1>0 && fgg>0)) {
           	closest = (Math.abs(fgg) <= Math.abs(f2));
        }
        else {
            closest = (Math.abs(fgg) <= Math.abs(f1));
        }

        if(closest) {
            return Math.abs(fMax);
        }
    }
    return 0;
}

/**
 * Metode, kas atrod attēlā kores (maksimumus).
 * @param inputImage apstrādājamais attēls
 * @param settings Ridge pakotnes parametri
 * @author Ainārs Kumpiņš
 */
public void detectRidges (Image inputImage, RidgeSettings settings) {
    this.settings = settings;
    //attēla priekšapstrāde
    preprocess(inputImage);
    IsRidge isRidge = new IsRidge();
    // pārstaigā attēlu un katram pikselim pārbauda kores vērtību
    // nemeklē vērtības attēla malējos pikseļos
    for (int height = 1; height < workingImage.getHeight()-1; height++) {
        strength[0][height] = 0;
        strength[workingImage.getWidth()-1][height] = 0;
        for (int width = 1; width < workingImage.getWidth()-1; width++) {
            strength[width][height] = ridgeValleyPixelStrength(width, height, isRidge);
            // ja nav kore tad uzstāda 0;
            if (!isRidge.isRidge) {
                strength[width][height] = 0;
            }
        }
    }
    for (int width = 0; width < workingImage.getWidth(); width++) {
        strength[width][0] = 0;
        strength[width][workingImage.getHeight()-1] = 0;
    }
}


/**
 * Metode, kas atrod attēlā kores (minimumu).
 * @param inputImage apstrādājamais attēls
 * @param settings Ridge pakotnes parametri
 * @author Ainārs Kumpiņš
 */
public void detectValleys (Image inputImage, RidgeSettings settings) {
    this.settings = settings;
    preprocess(inputImage);
    IsRidge isRidge = new IsRidge();
    for (int height = 1; height < workingImage.getHeight()-1; height++) {
        strength[0][height] = 0;
        strength[workingImage.getWidth()-1][height] = 0;
        for (int width = 1; width < workingImage.getWidth()-1; width++) {
            strength[width][height] = ridgeValleyPixelStrength(width, height, isRidge);
            // ja ir kore, kura ir virsotne(maksimums),
            // tad uzstāda vērtību 0, jo šī kore nav ieleja(Minimums).
            if (isRidge.isRidge) { strength[width][height] = 0; }
        }
    }
    for (int width = 0; width < workingImage.getWidth(); width++) {
        strength[width][0] = 0;
        strength[width][workingImage.getHeight()-1] = 0;
    }
}

/**
 * Metode, kas atrod attēlā robežas.
 * @param inputImage apstrādājamais attēls.
 * @param settings Ridge pakotnes parametri.
 * @author Ainārs Kumpiņš
 */
public void detectEdges ( Image inputImage, RidgeSettings settings) {
    this.settings = settings;
    //attēla priekšapstrāde
    preprocess(inputImage);
    //parstāigā attēlu un meklē robežvērtības
    for (int height = 0; height < workingImage.getHeight(); height++) {
        for (int width = 0; width < workingImage.getWidth(); width++) {
            strength[width][height] = edgePixelStrength(width, height);
        }
    }
}

/** Metode, kas nepieciešama līniju meklēšanai. Atrod punktus, līnija
 * turpināšanai horizontālajā un vertikālajā virziena.
 * Nākamā punkta intensitātei jābūt lielākai par Ridge settings
 * parametros norādīto lowerThreshold vērtību.
 * @param x līnijās pēdējā pievienotā punkta x koordināte.
 * @param y līnijās pēdējā pievienotā punkta y koordināte.
 * @param xyCoord punkts, kurā saglabā līnijas nākošā punkta koordinātes.
 * @return true, ja nākamais punkts ir atrasts.
 * @return false, ja mākamais punkts nav atrasts.
 * @author Ainārs Kumpiņš
 */
private boolean neighbor4 (int x, int y, Point xyCoord) {
    xyCoord.x = x;
    xyCoord.y = y;
    // kreisais pikselis
    if (strength[x+1][y] >= settings.lowerThreshold) {
        xyCoord.x = x+1;
        return true;
    }
    // labais pikselis
    if (strength[x-1][y] >= settings.lowerThreshold) {
        xyCoord.x = x-1;
        return true;
    }
    // apakšējais pikselis
    if (strength[x][y+1] >= settings.lowerThreshold) {
        xyCoord.y = y+1; return true;
    }
    // augšējais pikselis
    if (strength[x][y-1] >= settings.lowerThreshold) {
        xyCoord.y = y-1; return true;
    }
    return false;
}


/** Metode, kas nepieciešama līniju meklēšanai. Atrod punktus, līnija
 * turpināšanai no padotā pikseļa visos virzienos.
 * Nākamā punkta intensitātei jābūt lielākai par Ridge settings
 * parametros norādīto lowerThreshold vērtību.
 * @param x līnijās pēdējā pievienotā punkta x koordināte.
 * @param y līnijās pēdējā pievienotā punkta y koordināte.
 * @param xyCoord punkts, kurā saglabā līnijas nākošā punkta koordinātes.
 * @return true, ja nākamais punkts ir atrasts.
 * @return false, ja mākamais punkts nav atrasts.
 * @author Ainārs Kumpiņš
 */
private boolean neighbor8 (int x, int y, Point xyCoord) {
    if (neighbor4(x, y, xyCoord)) {
        return true;
    }
    if (strength[x+1][y+1] >= settings.lowerThreshold) {
        xyCoord.x = x+1; xyCoord.y = y+1;
        return true;
    }
    if (strength[x-1][y+1] >= settings.lowerThreshold) {
        xyCoord.x = x-1; xyCoord.y = y+1;
        return true;
    }
    if (strength[x+1][y-1] >= settings.lowerThreshold) {
        xyCoord.x = x+1; xyCoord.y = y-1;
        return true;
    }
    if (strength[x-1][y-1] >= settings.lowerThreshold) {
        xyCoord.x = x-1; xyCoord.y = y-1;
        return true;
    }
    return false;
}

/** Klases iekšējā metode, kas nepieciešama līniju meklēšanai un izdala
 * no attēla vienu nepārtrauktu līnīju.
 * @param x pirmā punkta X koordināte, no kuras sākas līnijas meklēšana.
 * @param y līnijās pirmā punkta Y koordināte, no kuras sākas līnijas meklēšana.
 * @return Edge klases objektu (atrasto līniju).
 * @author Ainārs Kumpiņš
 */
private Edge findEdge (int x, int y) {
    Edge edge = new Edge();
    edge.points =  new ArrayList<Point>();
    edge.totalStrenght = 0;
    boolean next = true;
    // meklē nākamo līnijas punktu kamēr tāds vairs netiek atrasts
    while (next) {
        edge.totalStrenght += strength[x][y];
        strength[x][y] = 0;
        Point p = new Point(x,y);
        edge.points.add(p);
        Point xyCoord = new Point();
        next = neighbor8(x, y, xyCoord);
        if (next) {
            x = xyCoord.x;
            y = xyCoord.y;
        }
    }
    return edge;
}

/** Metode, kas meklē līnijas visā attēlā. Lai varētu meklēt Ridge entītijā
 * meklētu līnijas pirms tam ir jābūt izsauktai detectRidges, detectValleys
 * vai detectEdges metodei.
 * @return List<Edge> līniju sarakstu
 * @author Ainārs Kumpiņš
 */
public List<Edge> traceLines() {
    if (strength == null) {
        throw new IllegalArgumentException("strenght array not set.");
    }
    List<Edge> edgeList = new ArrayList<Edge>();
    // pārstaigā strenght masību un meklē punktus ar pietiekami lielu
    // intensitātes vērtību, lai meklētu līniju
    for (int y = 0; y < workingImage.getHeight(); y++) {
        for (int x = 0; x < workingImage.getWidth(); x++) {
            // līnijai pievieno tikai tos punktus, kas ir ar pietiekami lielu
            // intensitāti.
            if (strength[x][y] >= settings.upperThreshold) {
                // meklē līniju no atrastā sākumpunkta
                Edge e = findEdge(x,y);
                Point xyCoord = new Point();
                // meklē līniju no atrastā sākumpunkta pretējā virzienā
                if (neighbor8(x, y, xyCoord)) {
                    Edge e1 = findEdge(xyCoord.x, xyCoord.y);
                    // apvieno divas līniju puses
                    Edge newEdge = new Edge();
                    newEdge.points =  new ArrayList<Point>();
                    Collections.reverse(e1.points);
                    newEdge.points.addAll(0, e.points);
                    newEdge.points.addAll(0, e1.points);
                    newEdge.totalStrenght = e.totalStrenght + e1.totalStrenght;
                    e = newEdge;
                }
              // līniju sarakstam pievieno līniju
              edgeList.add(e);
            }
        }
    }
    return edgeList;
}

/** Metode sadala līniju taisnes nogriežņos.
 * @param edge sadalāmā līnija
 * @param maxDistance maksimālais punkta attālums līdz punktam,
 * lai tas tiktu pieskaitīts nogrieznim.
 * @param jointSegments nosaka vai nākošā līnijas nogriežņa sākumpunkts būs
 * iepriekšējā beigu punkts vai arī tas būs nākošais līnijas punkts aiz
 * iepriekšējā nogriežņa beigu punkta.
 * @return List<LineSegment2D> nogriežņu sarakstu
 * @author Ainārs Kumpiņš
 */
public List<LineSegment2D> getLineSegments
        (Edge edge, double maxDistance, boolean jointSegments) {
    if (edge == null || maxDistance < 0) {
        throw new IllegalArgumentException("IllegalArgument in Ridge.ridge.getLineSegments");
    }
    List<LineSegment2D> segments = new ArrayList<LineSegment2D>();
    // nogriežņa sākumpunkts
    Point startPoint = null;
    // nogriežņa pagaidu punkts
    Point endPoint = new Point();
    // nogriežņa meklēšanās procesā pēdējais punkts (pagaidu beigu punkts)
    Point tempEnd = null;
    boolean segmentSet = true;
    boolean lineBreak = false;
    // pagaidu pārbaudāmais nogrieznis. Starp tā galapunktiem tiek pārbaudīts
    // vai punkti, kas ir starp tiem atbilst nogriežņa veidošanas nosacijumiem.
    LineSegment2D tempSegment = new LineSegment2D();
    // Punktu saraksts, kas atrodas starp pārbaudāmā nogriežņa galapunktiem.
    List<Point> inLinePoints = new ArrayList<Point>();
    // līnijas pārstaigāšana
    for (Point pointItem: edge.points) {
        if (segmentSet == false) {
            lineBreak = false;
            // pagaidu beigu punkts ir līnijas tekošais pārbaudāmais punkts
            tempEnd = pointItem;
            tempSegment.move(startPoint, tempEnd);
            // pārbauda visus punktus, kas atrodas starp pagaidu nogriežņa
            // galapunktiem
            for (Point tempPoint: inLinePoints) {
                if (tempSegment.distance(tempPoint) > maxDistance) {
                    lineBreak = true;
                    break;
                }
            }
            // ja visi punkti pieder nogriežņa līnijai pārvieto nogriežņa
            // beigu punktu tālāk
            if (!lineBreak) {
                endPoint.move(tempEnd.x, tempEnd.y);
                inLinePoints.add(tempEnd);
            }
            // ja kāds no starppunktiem vairs neietilpst nogriežņa līnijā
           // izveido jaunu nogriezni un pievieno to nogriežņu sarakstam
            else {
                LineSegment2D segment =
                        new LineSegment2D (startPoint.x, startPoint.y,
                                             endPoint.x, endPoint.y);
                segments.add(segment);
                segmentSet = true;
            }
        }
        else {
            segmentSet = false;
            inLinePoints.clear();
            if (lineBreak) {
                if (jointSegments) {
                    // next segment Start point is previous segment End point
                    startPoint.move(endPoint.x, endPoint.y);
                    inLinePoints.add(tempEnd);
                    inLinePoints.add(pointItem);
                }
                else {
                    // next segment Start point is next point after previous segment End point
                    startPoint = tempEnd;
                    inLinePoints.add(pointItem);
                }
            }
            else {
                    startPoint = new Point(pointItem);
                }
        }
    }
    if (!segmentSet && tempEnd != null) {
        LineSegment2D segment = new LineSegment2D(startPoint, endPoint);
        segments.add(segment);
    }
    return segments;
}

/** Metode atrod attēlā līnijas un sadala tās taisnes nogriežņos.
 * pirms šīs metodes izsaukšanas klases entītijai, kurai izsauc šo metodi
 * jābūt izsauktai getRidge, getValley vai getEdge metodei.
 * @param edge sadalāmā līnija
 * @param maxDistance maksimālais punkta attālums līdz punktam,
 * lai tas tiktu pieskaitīts nogrieznim.
 * @param jointSegments nosaka vai nākošā līnijas nogriežņa sākumpunkts būs
 * iepriekšējā beigu punkts vai arī tas būs nākošais līnijas punkts aiz
 * iepriekšējā nogriežņa beigu punkta.
 * @return List<LineSegment2D> nogriežņu sarakstu
 * @author Ainārs Kumpiņš
 */
public List<LineSegment2D> getLineSegments (double maxDistance, boolean jointSegments) {
    List<Edge> edgeList = traceLines();
    List<LineSegment2D> segments= new ArrayList<LineSegment2D>();
    for (Edge edgeItem: edgeList) {
        segments.addAll(getLineSegments(edgeItem, maxDistance, jointSegments));
    }
    return segments;
}

/**Metode apvieno visus nogriežņus, kas atrodas pietiekami tuvu viens otram un
 * kuri ar pietiekami mazu kļūdu turpina viens otru. Metode izmanto tīrā spēka
 * piegājienu un salīdzina katru nogriezni ar visiem pārējiem.
 * @param segments nogriežņu saraksts.
 * @param concatRadius maksimālais attālums kādā meklēs divu apvienojamu nogriežņu galapunktus.
 * @param maxDistance maksimālā nogriežņu galapunktu nobīde, kāda drīkst
 * būt no jaunveidojamā nogriežņa, lai divus nogriežņus apvienotu
 * @return true, ja ir apvienoti vismaz 2 nogriežņi
 *  false, ja nav apvienots neviens nogrieznis
 */
public boolean concatSegmentsBR
        (List<LineSegment2D> segments, double concatRadius, double maxDistance) {
    // vai ir sekmīgi apvienoti nogriežņi
    boolean concatenated = true;
    // vai nogriežņi veido jaunu nogriezni
    boolean line = false;
    // jaunizveidotā nogriežņa sākuma un beigu punkti
    Point startP = null;
    Point endP = null;
    // jaunizveidotā nogriežņa veidojošo nogriežņu galapunkti,
    // kas atrodas radius attālumā viens no otra
    Point point1 = null;
    Point point2 = null;
    // meklē apvienojamos nogriežņus kamēr ir ko apvienot
    while (concatenated) {
        concatenated = false;
        // parbauda nogriežnu pāri
        for (int i = 0; i < segments.size(); i++) {
            LineSegment2D current = segments.get(i);
            for (int j = 0; j < segments.size(); j++) {
                LineSegment2D next = segments.get(j);
                // partrauc ja nogriezni salīdzina pašu ar sevi
                if (next == current) {
                    break;
                }
                line = false;
                // pēc kārta pārbauda attālumu starp abu nogriežņu visiem
                // galapunktiem
                if (current.getStart().distance(next.getStart()) <= concatRadius) {
                    startP = new Point(current.getEnd());
                    endP = new Point(next.getEnd());
                    point1 = current.getStart();
                    point2 = next.getStart();
                    line = true;
                }
                else if (current.getStart().distance(next.getEnd()) <= concatRadius) {
                    startP = new Point(current.getEnd());
                    endP = new Point(next.getStart());
                    point1 = current.getStart();
                    point2 = next.getEnd();
                    line = true;
                }
                else if (current.getEnd().distance(next.getStart()) <= concatRadius) {
                    startP = new Point(current.getStart());
                    endP = new Point(next.getEnd());
                    point1 = current.getEnd();
                    point2 = next.getStart();
                    line = true;
                }
                else if (current.getEnd().distance(next.getEnd()) <= concatRadius) {
                    startP = new Point(current.getStart());
                    endP = new Point(next.getStart());
                    point1 = current.getEnd();
                    point2 = next.getEnd();
                    line = true;

                }
                // ja nogriežņi ir pietiekami tuvu viens otram
                // pārbauda vai to veidotā lauztā līnij ir pietiekami taisna
                if (line) {
                    LineSegment2D tempSegment = new LineSegment2D(startP, endP);
                    if (  tempSegment.distance(point1) <= maxDistance
                          && tempSegment.distance(point2) <= maxDistance ) {
                        current.move(startP, endP);
                        segments.remove(j);
                        concatenated = true;
                    }
                }
            }
        }
    }
    return concatenated;
}

/** Metode noteiktā rādiusā no nogriežņa galapunkta meklē blakus esošos nogriežņus.
* tiek izsaukta concatSegments metodē
* @param segments nogriežņu saraksts, kurā tiek meklēti kaimiņu nogriežņi
* @param imageGrid režģis, kurā izvietoti nogriežņi optimālākai kaimiņu meklēšanai.
* @param supLine nogrieznis, kuram tiek meklēti blakus esošie nogriežņi.
* @param paneRadius režģa rūtiņu rādiuss, kādā tiek meklēti blakus esošie nogriežņi
* @param concatRadius  maksimālais attālums kādā meklēs divu apvienojamu nogriežņu galapunktus.
* @param maxDistance lielākais attālums starp jauno nogriezni veidojošo nogriežņu
 * savstarpēji tuvākajiem galapunktiem un jauno nogriezni.
* @param start mainīgais, kas nosaka vai blakus nogriežņi tiks meklēti
* no padotā nogriežņa sākumpunkta vai no beigu punkta.
*/
private boolean getConcat ( List<LineSegment2D> segments, Grid imageGrid,
                        LineSegment2D supLine, int paneRadius,  double concatRadius, double maxDistance, boolean start) {
        // punkts kuram no kura meklēs tuvumā esošos nogriežnus
        Point target = null;
        // jaunizveidotā nogriežņa sākuma un beigu punkti
        Point startP = null;
        Point endP = null;
        // jaunizveidotā nogriežņa veidojošo nogriežņu galapunkti,
        // kas atrodas radius attālumā viens no otra
        Point cPoint = null;
        LineSegment2D tempSegment = new LineSegment2D();
        // nosaka vai kaimiņus meklēs no nogriežā sākumpunkta vai no beigu punkta
        if (start) {
            target = supLine.getStart();
            startP = supLine.getEnd();
        }
        else {
            target = supLine.getEnd();
            startP = supLine.getStart();
        }
        // vai nogriežņi veido jaunu nogriezni
        boolean line = false;
        //long supID = segments.get(segments.indexOf(supLine)).getID();
        // režģa rūts koordinātes kurā atrodas punkts
        int gXcoor = target.x / imageGrid.paneSize();
        int gYcoor = target.y / imageGrid.paneSize();
        // meklēšanas radius režģa rūtīs
        int xrStart = gXcoor - paneRadius;
        int xrEnd = gXcoor + paneRadius;
        int yrStart = gYcoor - paneRadius;
        int yrEnd = gYcoor + paneRadius;
        // pārbaude vai neiziet ārpus režģa robežām
        if (xrStart < 0)
            xrStart = 0;
        if (xrEnd >= imageGrid.gridWidth())
            xrEnd = imageGrid.gridWidth() - 1;
        if (yrStart < 0) yrStart = 0;
        if (yrEnd >= imageGrid.gridHeight())
            yrEnd = imageGrid.gridHeight() - 1;
        // meklēšana
        for (int y = yrStart; y <= yrEnd; y++) {
            for (int x = xrStart; x <= xrEnd; x++) {
                ArrayList<LineSegment2D> pane = imageGrid.getPane(x, y);
                  for (int i = 0; i < pane.size(); i++) {
                    LineSegment2D slLine = pane.get(i);
                    // pārbauda vai atrastais kaimiņš nav pats nogrieznis
                   if (!supLine.equals(slLine)) {
                        if (target.distance(slLine.getStart()) <= concatRadius) {
                            endP = new Point(slLine.getEnd());
                            cPoint = slLine.getStart();
                            line = true;
                        }
                        else if (target.distance(slLine.getEnd()) <= concatRadius) {
                            endP = new Point(slLine.getStart());
                            cPoint = slLine.getEnd();
                            line = true;
                        }
                        // ja nogriežņi ir pietiekami tuvu viens otram
                        // pārbauda vai to veidotā lauztā līnij ir pietiekami taisna
                        if (line) {
                        tempSegment.move(startP, endP);
                            if (  tempSegment.distance(cPoint) <= maxDistance
                            && tempSegment.distance(target) <= maxDistance ) {
                                segments.remove(slLine);
                                imageGrid.removeLine(slLine);
                                imageGrid.removeLine(supLine);
                                supLine.move(startP, endP);
                                imageGrid.addLinePoints(supLine);
                                return true;
                            }
                        }
                    }
                }
            }
        }
    return false;
}

/**Metode apvieno visus nogriežņus, kas atrodas pietiekami tuvu viens otram un
 * kuri ar pietiekami mazu kļūdu turpina viens otru. Meklēšanas paātrināšanai
 * izmanto Grid režģi.
 * @param segments nogriežņu saraksts.
 * @param concatRadius maksimālais attālums kādā meklēs divu apvienojamu nogriežņu galapunktus.
 * @param maxDistance maksimālā nogriežņu galapunktu nobīde, kāda drīkst
 * būt no jaunveidojamā nogriežņa, lai divus nogriežņus apvienotu
 * @return true, ja ir apvienoti vismaz 2 nogriežņi
 * @return false, ja nav apvienots neviens nogrieznis
 */
public boolean concatSegments
        (List<LineSegment2D> segments, double concatRadius, double maxDistance) {
    int paneSize = 1;
    // režģa rūts izmērs ir vienāds ar pieļaujamo attālumu noapaļotu uz leju
    // līdz vesalam skaitlim
    if (maxDistance != 0) {
        paneSize = (int)concatRadius;
    }
    // režģa rūšu rādius kādā režģī tiek meklēti kaimiņu nogriežņi
    int paneRadius = (int)Math.ceil(maxDistance / paneSize) +1;
    // ievieto līnijas režģī
    Grid imageGrid = new Grid(workingImage.getWidth(), workingImage.getHeight(), paneSize);
    for (LineSegment2D line: segments) {
        imageGrid.addLinePoints(line);
    }
    // vai ir sekmīgi apvienoti nogriežņi
    boolean concatenated = true;
    // meklē apvienojamos nogriežņus kamēr ir ko apvienot
    while (concatenated) {
        concatenated = false;
        // parbauda nogriežnu pāri
        for (int i = 0; i < segments.size(); i++) {
            LineSegment2D supLine = segments.get(i);
            concatenated = getConcat (segments, imageGrid, supLine, paneRadius, concatRadius, maxDistance, true);
            if (!concatenated) {
                concatenated = getConcat (segments, imageGrid, supLine, paneRadius, concatRadius, maxDistance, false);
            }
        }
    }
    return concatenated;
}



/** Metode atgriež līniju sarakstu ar norādīto identifikatora vērtību.
 * @param lineSets nogriežņu saraksts.
 * @param id meklējamā saraksta identifikators.
 * @return nogriežņu sarakst ar norādīto identifikatoru, ja tāds ir atrast
 *  null, ja padotajā nogriežņu sarakstā nav sarakst ar norādīto
 * identifikatoru
 */
public LineSet getLineSet (ArrayList<LineSet> lineSets, long id) {
    LineSet ret = null;
    for (LineSet item: lineSets) {
        if (item.getID() == id) {
            ret = item;
            return ret;
        }
    }
    return ret;
}

    /** Metode noteiktā rādiusā no nogriežņa galapunkta meklē blakus esošos nogriežņus.
    * tiek izsaukta lineGroup metodē
    * @param segments nogriežņu saraksts, kurā tiek meklēti kaimiņu nogriežņi
    * @param symbols nogriežņu grupu saraksts, kurā tiek saglabātas visas atrastās
    * nogriežņu grupas.
    * @param imageGrid režģis, kurā izvietoti nogriežņi optimālākai kaimiņu meklēšanai.
    * @param supLine nogrieznis, kuram tiek meklēti blakus esošie nogriežņi.
    * @param radius režģa rūtiņu rādiuss, kādā tiek meklēti blakus esošie nogriežņi
    * @param maxDistance lielākais attālums starp diviem nogriežņiem pikseļos,
    * lai tie tiktu ieskaitīti vienā nogriežņu grupā.
    * @param start mainīgais, kas nosaka vai blakus nogriežņi tiks meklēti
    * no padotā nogriežņa sākumpunkta vai no beigu punkta.
    */
    private void getNeighbors ( List<LineSegment2D> segments,
                        ArrayList<LineSet> symbols, Grid imageGrid,
                        LineSegment2D supLine, int radius, double maxDistance, boolean start) {
        Point target = null;
        // nosaka vai kaimiņus meklēs no nogriežā sākumpunkta vai no beigu punkta
        if (start)
            target = supLine.getStart();
        else
            target = supLine.getEnd();
        long supID = segments.get(segments.indexOf(supLine)).getID();
        // režģa rūts koordinātes kurā atrodas punkts
        int gXcoor = target.x / imageGrid.paneSize();
        int gYcoor = target.y / imageGrid.paneSize();
        // meklēšanas radius režģa rūtīs
        int xrStart = gXcoor - radius;
        int xrEnd = gXcoor + radius;
        int yrStart = gYcoor - radius;
        int yrEnd = gYcoor + radius;
        // pārbaude vai neiziet ārpus režģa robežām
        if (xrStart < 0) xrStart = 0;
        if (xrEnd >= imageGrid.gridWidth()) xrEnd = imageGrid.gridWidth() - 1;
        if (yrStart < 0) yrStart = 0;
        if (yrEnd >= imageGrid.gridHeight()) yrEnd = imageGrid.gridHeight() - 1;
        // meklēšana
        for (int y = yrStart; y <= yrEnd; y++) {
            for (int x = xrStart; x <= xrEnd; x++) {
                ArrayList<LineSegment2D> pane = imageGrid.getPane(x, y);
                  for (int i = 0; i < pane.size(); i++) {
                    LineSegment2D slLine = pane.get(i);
                    long slID = segments.get(segments.indexOf(slLine)).getID();
                    // pārbauda vai atrastais kaimiņš nav pats nogrieznis
                    if (slID != supID) {
                        // attaluma un krustošanās noteikšana
                        if ( (slLine.distance(target) <= maxDistance)
                              || slLine.intersect(supLine)) {
                            LineSet supSet = getLineSet(symbols, supID);
                            LineSet slSet = getLineSet(symbols, slID);
                            slLine.setID(supID);
                            // pievienojamai nogriežņu grupai nomaina identifikātoru
                            // uz tādu kāds ir nogrieznim kuram pievieno
                            for (LineSegment2D key: slSet.getLines()) {
                                segments.get(segments.indexOf(key)).setID(supID);
                            }
                            // pievieno nogrieznim kaimiņu nogriežņu grupu
                            supSet.addAll(slSet.getLines());
                            // izdzēš pievienoto grupu kā patstāvīgu nogriežņu
                            // grupu
                            symbols.remove(slSet);
                        }
                    }
                }
            }
        }
    }

/** Apvieno nogriežņus, kas atrodas pietiekami tuvu viens otram grupās.
 * @param segments nogriežņu saraksts, kurā tiek meklēti kaimiņu nogriežņi.
 * @param maxDistance lielākais attālums starp diviem nogriežņiem,
 * lai tie tiktu ieskaitīti vienā nogriežņu grupā.
 * @return ArrayList<LineSet> Nogriežņu grupu saraksts.
 */
public  ArrayList<LineSet> lineGroup ( List<LineSegment2D> segments, double maxDistance) {
    if (maxDistance < 0 || segments == null)
        throw new IllegalArgumentException("IllegalArgument in Ridge.ridge.lineGroup\n");
    // režģa rūts izmērs ir 1 pikselis, ja līnijas tiek apvienotas tikai, ja tās
    // pieskarās viena otrai
    int paneSize = 1;
    // režģa rūts izmērs ir vienāds ar pieļaujamo attālumu noapaļotu uz leju
    // līdz vesalam skaitlim
    if (maxDistance != 0) {
        paneSize = (int)maxDistance;
    }
    // režģa rūšu rādius kādā režģī tiek meklēti kaimiņu nogriežņi
    int radius = (int)Math.ceil(maxDistance / paneSize) +1;
    // ievieto līnijas režģī
    // katru nogriezni ievieto savā nogriežņu grupas objektā
    // (sākotnēji katrs nogrieznis veido grupu)
    ArrayList<LineSet> symbols = new ArrayList<LineSet>(segments.size());
    Grid imageGrid = new Grid(workingImage.getWidth(), workingImage.getHeight(), paneSize);
    for (LineSegment2D line: segments) {
        imageGrid.addLine(line);
        LineSet tmpLS = new LineSet();
        tmpLS.add(line);
        tmpLS.setID(line.getID());
        symbols.add(tmpLS);
    }
    // meklē kaimiņus
    for (int i = 0; i < segments.size(); i++) {
        LineSegment2D supLine = segments.get(i);
        getNeighbors( segments, symbols, imageGrid, supLine, radius, maxDistance, true);
        getNeighbors( segments, symbols, imageGrid, supLine, radius, maxDistance, false);
    }
return symbols;
}

} // END OF RIDGE
