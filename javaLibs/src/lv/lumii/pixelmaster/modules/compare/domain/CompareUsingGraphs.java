package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGrid;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;


/**
 *
 * Compares two images
 *
 * @author Aivars Šāblis
 *
 * double compare coefficient
 */
public class CompareUsingGraphs {

    public static RasterImage CompareObj(RasterImage source, RasterImage target, StringBuffer info, int command)
    {

       if ((source != null) && (target != null))
       {
        RasterImage workSource = new RasterImage(source.getWidth(), source.getHeight());
        RasterImage workTarget = new RasterImage(target.getWidth(), target.getHeight());
        source.copyTo(workSource);
        target.copyTo(workTarget);

        RasterImage returnImg = new RasterImage(source.getWidth(), source.getHeight());

        //workSource = convert.modifyImage(workSource);
        //workTarget = convert.modifyImage(workTarget);



        OGraph tmpA = null;
        OGraph tmpB = null;

        if (command >= 0)
        {

           tmpA = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workSource, command);
           //returnImg = ImageProcessor.printLine(source, command);
           tmpB = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workTarget, command);
           //returnImg = ImageProcessor.printLineAdd(target, returnImg, command);
        }
        else
        {
           tmpA = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workSource, 6);
            //workSource = ImageProcessor.printLine(workSource, 6);
           tmpB = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workTarget, 6);
        }

        //make graph grid
       // OGrid grid = new OGrid(tmpA, source.width, source.height, 10);
        //grid.makeGrid();
           

        try {
            OCompareLines comp = new OCompareLines(tmpA, tmpB);
            double coefA = 0;
            double coefB = 0;

            int maxWidth = 0;
            if (source.getWidth() > target.getWidth()) {
                maxWidth = source.getWidth();
            } else
            {
                maxWidth = target.getWidth();
            }
            int maxHeight = 0;
            if (source.getHeight() > target.getHeight()) {
                maxHeight = source.getHeight();
            } else
            {
                maxHeight = target.getHeight();
            }
            comp.setRasterImage(maxWidth, maxHeight);

            //comp.DoCompareA(2);
            //comp.DoCompareB(2);

            coefA = comp.DoCompareA(6, source.getWidth(), source.getHeight(), target.getWidth(), target.getHeight());
            coefB = comp.DoCompareB(6, target.getWidth(), target.getHeight(), target.getWidth(), target.getHeight());
            
            returnImg = comp.returnRasterImage();

            if (info != null) {
                //info.setText(null);
                info.append("\nDiffA:\n" + coefA);
                info.append("\nDiffB:\n" + coefB);
                coefA = coefA + coefB;
                info.append("\nTotalDiff:\n" + coefA);
            }

            System.out.println("Total dif" + coefA);


        } catch (OGraph.EdgelessGraphException e) {
            e.printStackTrace();
        } catch (OGraph.WeightlessGraphException e) {
            e.printStackTrace();
        }
           return returnImg;
       }

        //source.ImageProcessor.printLine(ImageCompareFrame.rImage, 6);
        //target.ImageProcessor.printLine(ImageCompareFrame.rImage, 6);

       return source;
    }

    public static RasterImage CompareObj2(RasterImage source, RasterImage target, StringBuffer info, int command)
    {

       if ((source != null) && (target != null))
       {
        RasterImage workSource = new RasterImage(source.getWidth(), source.getHeight());
        RasterImage workTarget = new RasterImage(target.getWidth(), target.getHeight());
        source.copyTo(workSource);
        target.copyTo(workTarget);

        RasterImage returnImg = new RasterImage(source.getWidth(), source.getHeight());

        //workSource = convert.modifyImage(workSource);
        //workTarget = convert.modifyImage(workTarget);



        OGraph tmpA = null;
        OGraph tmpB = null;

        if (command >= 0)
        {

           tmpA = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workSource, command);
           //returnImg = ImageProcessor.printLine(source, command);
           tmpB = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workTarget, command);
           //returnImg = ImageProcessor.printLineAdd(target, returnImg, command);
        }
        else
        {
           tmpA = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workSource, 6);
            //workSource = ImageProcessor.printLine(workSource, 6);
           tmpB = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workTarget, 6);
        }

        try {
            OCompareLines comp = new OCompareLines(tmpA, tmpB);
            double coefA, coefB;

            int maxWidth = 0;
            if (source.getWidth() > target.getWidth()) {
                maxWidth = source.getWidth();
            } else
            {
                maxWidth = target.getWidth();
            }
            int maxHeight = 0;
            if (source.getHeight() > target.getHeight()) {
                maxHeight = source.getHeight();
            } else
            {
                maxHeight = target.getHeight();
            }
            comp.setRasterImage(maxWidth, maxHeight);

            //comp.DoCompareA(2);
            //comp.DoCompareB(2);

            coefA = comp.DoCompareA(2);
            coefB = comp.DoCompareB(2);

            System.out.println("Total dif" + (coefA + coefB));

            returnImg = comp.returnRasterImage();

            if (info != null) {
                info.append("\nDiffA:\n" + coefA);
                info.append("\nDiffB:\n" + coefB);
                coefA = coefA + coefB;
                info.append("\nTotalDiff:\n" + coefA);
            }



        } catch (OGraph.EdgelessGraphException e) {
            e.printStackTrace();
        } catch (OGraph.WeightlessGraphException e) {
            e.printStackTrace();
        }
           return returnImg;
       }

        //source.ImageProcessor.printLine(ImageCompareFrame.rImage, 6);
        //target.ImageProcessor.printLine(ImageCompareFrame.rImage, 6);

       return source;
    }
}
