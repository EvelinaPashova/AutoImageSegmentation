
package lv.lumii.pixelmaster.modules.ridge.domain;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import lv.lumii.pixelmaster.modules.binarization.domain.GrayScale;

/**
 * This class contains methods that were previously in the class ImageProcessor.
 */
public class Util {

	public static void newDatabase() throws IOException {
		File dir = new File(System.getProperty("user.home") +
			"/.pixelmaster/modules/ridge/");
		File file = new File(dir.getAbsolutePath() + "/charValuesDB.txt");
		dir.mkdirs();
		file.createNewFile();

		Reader reader = new InputStreamReader(new Util().getClass().getResourceAsStream("/lv/lumii/pixelmaster/modules/ridge/domain/charValuesDB.txt"));
		FileWriter fw = new FileWriter(file);

		// temporary buffer for the data
		char cbuf[] = new char[1000];

		int howMany = reader.read(cbuf);
		while (howMany != -1) {
			fw.write(cbuf, 0, howMany);
			howMany = reader.read(cbuf);
		}
		reader.close();
		fw.flush();
		fw.close();
	}

	/**
	 * Temp function.
	 * Draws ridges, edges or valleys. Allocates memory for the resulting image.
	 * @param inputImage RasterImage to work with (ownership: caller)
	 * @param act Action to perform (0-edge, 1-ridge, 2-valley, 3-edge).
	 * @pre inputImage != null
	 * @pre inputImage.invariant()==true
	 * @post inputImage will not be modified
	 * @post (returned image).invariant()==true
	 * @post (returned image).getWidth()==inputImage.getWidth() and (returned image).getHeight()==inputImage.getHeight()
	 * @return The resulting RasterImage (ownership: caller)
	 * @author Ainars Kumpins.
	 * @since 09.03.2009
	 */
	public static RasterImage ridgeImage (RasterImage inputImage, int act) {
		assert !(inputImage == null || act<0 || act>3);
		RasterImage ridgeRI = new RasterImage(inputImage.getWidth(), inputImage.getHeight());
		GrayScale.greyImage(inputImage, ridgeRI);
        Image ridgeI = Image.convertFromRI(inputImage);
		//RidgeSettings sett = new RidgeSettings(3, 1.2, 2000, 200);//whiteTargetBlackPart
		//RidgeSettings sett = new RidgeSettings(3, 1.2, 600, 60);  //whiteTargetWhitePart
		//RidgeSettings sett = new RidgeSettings(3, 1.2, 13100, 1310);  //whiteTargetEdge
		int smoothingRadius = 3;
        double nonCircularity = 1.2;
        int upperThreshold = 10;
        int lowerThreshold = 1;
        // not used in this method
        double maxGetLinesDistance = 1;
        double concatRadius = 3;
        double maxConcatDistance = 1;
        double maxClusterLineDistance = 4;
        double maxSymbolScale = 1.5;
        boolean jointSegments = true;
        RidgeSettings sett
                = new RidgeSettings( smoothingRadius, nonCircularity, upperThreshold, lowerThreshold,
                    maxGetLinesDistance,  maxConcatDistance, concatRadius,
                    maxClusterLineDistance, maxSymbolScale, jointSegments);
		Ridge ridge = new Ridge();
		switch (act) {
			case 0: ridge.detectEdges(ridgeI, sett);
			break;
			case 1: ridge.detectRidges(ridgeI, sett);
			break;
			case 2: ridge.detectValleys(ridgeI, sett);
			break;
			case 3: ridge.detectEdges(ridgeI, sett);
			break;
		}
		if (act > 0)
		{
			int colour =0;
			for (int height = 0; height < ridge.workingImage.getHeight(); height++) {
				for (int width = 0; width <ridge.workingImage.getWidth(); width++) {
					colour = 3 * ridge.getStength(width, height);
					if (colour > 254) {colour = 254;}
					ridgeRI.setRGB(ridge.workingImage.getWidth() * height + width,
						RGB.getRGB(colour, colour, colour));
				}
			}
		}
		else
		{
			int colour =0;
			for (int height = 0; height < ridge.workingImage.getHeight(); height++) {
				for (int width = 0; width <ridge.workingImage.getWidth(); width++) {
					colour = ridge.workingImage.pixels[width][height];
					if (colour > 254) {colour = 254;}
					ridgeRI.setRGB(ridge.workingImage.getWidth() * height + width,
						RGB.getRGB(colour, colour, colour));
				}
			}
		}
		return ridgeRI;
	}

	/**
	 * Temp function.
	 * Draws ridge, edge or valley lines. Allocates memory for the resulting image.
	 * @param inputImage The original image (ownership: caller)
	 * @param act Action to perform (1 - 12).
	 * @pre inputImage != null
	 * @pre inputImage.invariant()==true
	 * @post inputImage will not be modified
	 * @post (returned image).invariant()==true
	 * @post (returned image).getWidth()==inputImage.getWidth() and (returned image).getHeight()==inputImage.getHeight()
	 * @return The resulting RasterImage (ownership: caller)
	 * @author Ainars Kumpins.
	 * @since 16.03.2009
	 */
	public static RasterImage printLine (RasterImage inputImage, int act) {
		assert !(inputImage == null || act < 1 || act > 12);
        Ridge ridge = new Ridge();
        Image ridgeI = Image.convertFromRI(inputImage);
        // PARAM TO PASS
        int smoothingRadius = 2;
        double nonCircularity = 1.2;
        int upperThreshold = 5;
        int lowerThreshold = 2;
        // max attālums starp pixeli un nogriezni, lai tas piederētu nogrieznim
        double maxGetLinesDistance = 1;
        // maksimālais attālums kādā meklē blakus esošās nogriežņu galapunktus, lai nogriežņus mēģinātu savienot
        double concatRadius = 3;
        // max punkta attālums līdz nogrieznim, lai tas piederētu nogrieznim (nogriežņu apvienošanas gadījumā)
        double maxConcatDistance = 1;
        // maksimālais attālums starp diviem nogriežņiem, lai tie tiktu ieskaitīti vienā nogriežņu kopā
        double maxClusterLineDistance = 4;
        // maksimālā attiecība starp nogriežņu veidoto kopu(garākā dimensija) un tās struktūras mērogojuma izmēru
        double maxSymbolScale = 1.5;
        //ja jointSegment = false, tad, meklējot nogriežņus, nākošā nogriežņa sākumpunkts būs nākošais punkts
        // aiz iepriekšējā nogriežņa beigu punkta (nosacijums protams darbojas tikai vienas līnijas ietvaros)
        boolean jointSegments = true;
        RidgeSettings sett
                = new RidgeSettings( smoothingRadius, nonCircularity, upperThreshold, lowerThreshold,
                    maxGetLinesDistance,  maxConcatDistance, concatRadius,
                    maxClusterLineDistance, maxSymbolScale, jointSegments);
        ridge.setSettings(sett);


		if (act == 1 || act == 4 || act == 7 || act == 10)
            ridge.detectRidges(ridgeI, sett);
        else if (act == 2 || act == 5 || act == 8 || act == 11)
            ridge.detectValleys(ridgeI, sett);
        else if (act == 3 || act == 6 || act == 9 || act == 12)
            ridge.detectEdges(ridgeI, sett);

        RasterImage outputImage = new RasterImage(inputImage.getWidth(), inputImage.getHeight());
        if (act < 4) {
            List<Edge> edgeList = new ArrayList<Edge>();
            edgeList = ridge.traceLines();

            for (Edge edgeItem: edgeList) {
                int color = RGB.getRandomColor();
                for (Point pointItem: edgeItem.points) {
                    outputImage.setRGB(outputImage.getWidth() * pointItem.y + pointItem.x, color);
                }
            }
            return outputImage;
        }

        List<LineSegment2D> lineSegments = new ArrayList<LineSegment2D>();
        lineSegments = ridge.getLineSegments(maxGetLinesDistance, jointSegments);
        if (act > 6) {
            ridge.concatSegments(lineSegments, concatRadius, maxConcatDistance);
        }
        if (act < 10) {
            for (LineSegment2D lineItem: lineSegments) {
                drawLine(outputImage, lineItem.getStart(), lineItem.getEnd(), RGB.getRandomColor());
            }
			return outputImage;
        }
        else {
            ArrayList<LineSet> lineClusters = null;
            lineClusters = ridge.lineGroup(lineSegments, maxClusterLineDistance);

            // print symbols to console
            for (LineSet set: lineClusters) {
                Symbol symb = new Symbol();
                if (symb.toSymbol(set.getLines(), maxSymbolScale)) {
                    SymbolInfo pr = new SymbolInfo(symb);
                    System.out.println(pr.stringTo());
                }
            }
            // draw symbols
            for (LineSet set: lineClusters) {
                int color = RGB.getRandomColor();
                for (LineSegment2D lineItem: set.getLines()) {
                    drawLine(outputImage, lineItem.getStart(), lineItem.getEnd(), color);
                }
            }
            return outputImage;
        }
	}

    //TEMP used in printLineSegments(), replace with Graphic.drawLine();
    public static void drawLine (RasterImage workingImage, Point start, Point end, int color) {
        int startX, startY, endX, endY;
        if ((start.y > end.y) ) {
            startX = end.x; startY = end.y;
            endX = start.x; endY = start.y;
        }
        else {
            startX = start.x; startY = start.y;
            endX = end.x; endY = end.y;
        }
        if (end.x - start.x == 0) {
            for (int y = startY; y <= endY; y++) {
                workingImage.setRGB(workingImage.getWidth() * y + start.x, color);
            }
        }
        else {
            double slope = (double) (endY - startY) / (endX - startX);
            double yInt = startY - slope * startX;
            if (slope < 1 && slope > -1) {
                if (startX > endX) {
                    int temp = startX; startX = endX; endX = temp;
                    temp = startY; startY = endY; endY = temp;
                }
                for (int x = startX; x <= endX; x++) {
                    double y = x * slope + yInt;
                    workingImage.setRGB(workingImage.getWidth() * (int)Math.ceil(y) + x, color);
                }
            }
            else {
                for (int y = startY; y <= endY; y++) {
                    double x = (y - yInt)/ slope;
                    workingImage.setRGB(workingImage.getWidth() * y + (int)Math.ceil(x), color);
                }
            }
        }
    }

	/**
	 * Temp function.
	 */
	public static ArrayList<Symbol> getSymbols (RasterImage inputImage) {
		assert !(inputImage == null);
        Ridge ridge = new Ridge();
        Image ridgeI = Image.convertFromRI(inputImage);
        // PARAM TO PASS
        int smoothingRadius = 3;
        double nonCircularity = 1.2;
        int upperThreshold = 10;
        int lowerThreshold = 1;
        // max attālums starp pixeli un nogriezni, lai tas piederētu nogrieznim
        double maxGetLinesDistance = 1;
        // maksimālais attālums kādā meklē blakus esošās nogriežņu galapunktus, lai nogriežņus mēģinātu savienot
        double concatRadius = 3;
        // max punkta attālums līdz nogrieznim, lai tas piederētu nogrieznim (nogriežņu apvienošanas gadījumā)
        double maxConcatDistance = 1;
        // maksimālais attālums starp diviem nogriežņiem, lai tie tiktu ieskaitīti vienā nogriežņu kopā
        double maxClusterLineDistance = 4;
        // maksimālā attiecība starp nogriežņu veidoto kopu(garākā dimensija) un tās struktūras mērogojuma izmēru
        double maxSymbolScale = 1.5;
        //ja jointSegment = false, tad, meklējot nogriežņus, nākošā nogriežņa sākumpunkts būs nākošais punkts
        // aiz iepriekšējā nogriežņa beigu punkta (nosacijums protams darbojas tikai vienas līnijas ietvaros)
        boolean jointSegments = true;
        RidgeSettings sett
                = new RidgeSettings( smoothingRadius, nonCircularity, upperThreshold, lowerThreshold,
                    maxGetLinesDistance,  maxConcatDistance, concatRadius,
                    maxClusterLineDistance, maxSymbolScale, jointSegments);
        ridge.setSettings(sett);

        ridge.detectValleys(ridgeI, sett);
        RasterImage outputImage = new RasterImage(inputImage.getWidth(), inputImage.getHeight());

        List<LineSegment2D> lineSegments = new ArrayList<LineSegment2D>();
        ArrayList<LineSet> lineClusters = null;
        ArrayList<Symbol> symbols = new ArrayList<Symbol>();
        lineSegments = ridge.getLineSegments(maxGetLinesDistance, jointSegments);
        ridge.concatSegments(lineSegments, concatRadius, maxConcatDistance);
        lineClusters = ridge.lineGroup(lineSegments, maxClusterLineDistance);

            // add symbols
            for (LineSet set: lineClusters) {
                Symbol symb = new Symbol();
                if (symb.toSymbol(set.getLines(), maxSymbolScale)) {
                    symbols.add(symb);
                }
            }

            return symbols;

	}
}
