package lv.lumii.pixelmaster.modules.compare.domain;

import lv.lumii.pixelmaster.core.api.domain.RGB;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lv.lumii.pixelmaster.modules.compare.domain.graph.DefaultEdge;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraph;
import lv.lumii.pixelmaster.modules.compare.domain.graph.OGraphReader;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Math.*;
import static lv.lumii.pixelmaster.modules.ridge.domain.Util.drawLine;

/**
 * Recognize text from picture
 * @author Madara Augstkalne
 * @sice 19.04.2010
 */

public class RecognizeText {

    private static int n = 50; // number of alphabet symbols
    private static int alpha = 7;  // 37 for alphabet-1

    // information about alphabet symbols
    // first line save symbol meaning, second OGraph location
    private static String[][] alphabet = new String[n][2];

    // symbol OGraph
    private static OGraph [] symbol = new OGraph[50];

    // list to store recognized symbol data
    private static LinkedList<FoundSymbol> symbolList = new LinkedList<FoundSymbol>();

    private static RasterImage resultImg;

    //recognized symbol temporary value
    private static String tmpSymbol;


    /**
     * Load information about symbol form database.
     * Read symbol information in array
     */
    public static void LoadSymbolInfo(BufferedReader br)
    {
        assert !(br==null);

        try
        {

          for (int i = 0; i<alpha; i++)
          {
              alphabet[i][0] = br.readLine();
              alphabet[i][1] = br.readLine();
              //System.out.println("alphabet[0][0]) " + alphabet[i][0]);
              //System.out.println("alphabet[0][1]) " + alphabet[i][1]);

              // connect components after load them form database
              /* OGraph symbolGraph = OGraphReader.read(new File(alphabet[i][1]));

              ConnectedComponents connectDBsymbol = new ConnectedComponents(symbolGraph);

              DBsymbol = connectDBsymbol.FindGraphComponents();

              for (int t = 0; t < DBsymbol.size(); t++) {
              // System.out.println("size " + DBsymbol.size());
              symbol[i] = DBsymbol.get(t);

              } */

			symbol[i] = OGraphReader.read(new BufferedReader(new InputStreamReader(new RecognizeText().getClass().getResourceAsStream("/lv/lumii/pixelmaster/modules/compare/domain/" + alphabet[i][1]))));

          }
        br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Maintain symbol information
     * @param symbol
     * @param coefficient
     * @param centerOfMass
     * @return symbol value
     * @return symbol coefficient
     * @return symbol center of Mass
     */
    private static class FoundSymbol
    {
        /*fields*/
        String symbol;
        double coefficient;
        Point2D centerOfMass;

        /*constructor*/
        FoundSymbol (String s, double coeff, Point2D coordXY)
        {
            this.symbol = s;
            this.coefficient = coeff;
            this.centerOfMass = coordXY;
        }

        /* accessors */
        String getSymbol()
        {
            return this.symbol;
        }
        double getCoefficient()
        {
            return this.coefficient;
        }
        Point2D getCenterOfMass()
        {
            return this.centerOfMass;
        }
    }

    /**
     * Sort symbols by them x y mass center coordinates.
     * Store information in symbolList
     * @param elem
     */

    private static void SortSymbolList(FoundSymbol elem)
    {
        //check list last element
        boolean added;

        if (symbolList.size() > 1)
        {
            FoundSymbol tmp1 = symbolList.getFirst();
            if (tmp1.getCenterOfMass().getX() > elem.getCenterOfMass().getX())
            {
                symbolList.addFirst(elem);
            }
            else
            {
                added = false;
                for (int it = 1; it < symbolList.size(); it++)
                {
                    FoundSymbol tmp = symbolList.get(it);
                    if ( tmp.getCenterOfMass().getX() > elem.getCenterOfMass().getX() && !added)
                    {
                        symbolList.add(it, elem);
                        //System.out.println("Milddle: " + elem.getSymbol() + " " + elem.getCenterOfMass());
                        added = true;
                    }
                }
                if (!added)
                {
                    symbolList.addLast(elem);
                    //System.out.println("Last if size > 1: " + elem.getSymbol() + " " + elem.getCenterOfMass());
                }
            }
        }
        else if (symbolList.size() == 0)
        {
            symbolList.add(elem); // put as first element in list
            //System.out.println("Fist: " + elem.getSymbol() + " " + elem.getCenterOfMass());
        }
        else
        {
            FoundSymbol tmp = symbolList.get(0);
            if (tmp.getCenterOfMass().getX() < elem.getCenterOfMass().getX())
            {
                symbolList.addLast(elem);
                //System.out.println("Last: " + elem.getSymbol() + " " + elem.getCenterOfMass());
            }
            else
            {
                symbolList.addFirst(elem);
                //System.out.println("Fist in else case: " + elem.getSymbol() + " " + elem.getCenterOfMass());
            }
        }
    }

    /**
     * Compare component to symbol graph
     * @param component
     * @return comparing coefficient
     */

    public static double FindSymbol(OGraph component)
    {
        double tmpCoef = Double.POSITIVE_INFINITY;
        double coefB;

        if (component != null)
        {
            OGraph tmpB;
            try
            {
                //define variables for compare
                OCompareLines comp;
                comp = new OCompareLines();
                comp.LoadGraphA(component);

                //Go over array which contains symbol graphs from DB
                for (int i = 0; i <alpha; i++)
                {
                    tmpB = symbol[i];
                    comp.LoadGraphB(tmpB);

                    coefB = comp.DoCompareB(1);

                    //System.out.println("koeficients " + coefB);

                    if (tmpCoef > coefB)
                    {
                        tmpCoef = coefB;
                        tmpSymbol = alphabet[i][0];
                    }

                }

            } catch (OGraph.EdgelessGraphException e) {
                e.printStackTrace();
            } catch (OGraph.WeightlessGraphException e) {
                e.printStackTrace();
            }

        }
        return tmpCoef;
    }

    /**
     * Draw component edges in source image
     * @param component
     */

    private static void DrawSymbol(OGraph component)
    {

        java.util.List<DefaultEdge>  edgeSet = component.edgeSet();

        for(DefaultEdge edges : edgeSet)
        {
            Point av1 = new Point ((int)round(component.getEdgeSource(edges).getX()), (int)round(component.getEdgeSource(edges).getY()));
            Point av2 = new Point ((int)round(component.getEdgeTarget(edges).getX()), (int)round(component.getEdgeTarget(edges).getY()));

            drawLine(resultImg, av1, av2, RGB.getRandomColor());
        }

    }

    /**
     * Provide correct symbol recognition
     * @param source
     * @param info
     * @return resultImg
     * @throws newgraph.OGraph.EdgelessGraphException
     * @throws newgraph.OGraph.WeightlessGraphException
     */

    public static RasterImage CompareSymbol(RasterImage source, StringBuffer info) throws OGraph.EdgelessGraphException, OGraph.WeightlessGraphException
    {

        java.util.List<OGraph> componentList;
        componentList = new ArrayList<OGraph>();

        double symbolCoef;

        RasterImage workSource;
        if (source != null)
        {
            resultImg = source;

            workSource = new RasterImage(resultImg.getWidth(), resultImg.getHeight());

           /* int lines = resultImg.height/240;
            if (lines == 0)
            {
                lines = 1;
            }  */

            resultImg.copyTo(workSource);

            OGraph tmpA;
            //get source image line graph
            //use method: Draw lines segment: valley
            tmpA = lv.lumii.pixelmaster.modules.compare.domain.Util.printLineToGraph(workSource, 5);
			//Start read from DB
			LoadSymbolInfo(new BufferedReader(new InputStreamReader(new RecognizeText().getClass().getResourceAsStream("/lv/lumii/pixelmaster/modules/compare/domain/CharacterDB/Alphabets/alphabet-2.in"))));

            ConnectedComponents connectedComponent = new ConnectedComponents(tmpA);

            //Modify componentList. Throw useless components and compare closest
            componentList = connectedComponent.FindGraphComponents();

            for (int i = 0; i < componentList.size(); i++)
            {
               // System.out.println("LIST SIZE: " + componentList.size());
                symbolCoef = FindSymbol(componentList.get(i));

                if ((symbolCoef < 0.6))
                {
                    FoundSymbol elem = new FoundSymbol(tmpSymbol, symbolCoef, componentList.get(i).getCenterOfMass());
                    //System.out.println("Simbols " + elem.getSymbol() +" "  +  elem.getCoefficient() + " " + elem.getCenterOfMass());
                    SortSymbolList(elem);
                    DrawSymbol(componentList.get(i));
                }
            }

           // System.out.println("ATPAZIITIE");
            for (int im = 0; im < symbolList.size(); im++) 
            {
                System.out.println("Symbol : " + symbolList.get(im).getSymbol() + " " + symbolList.get(im).getCoefficient());
                //info.append(symbolList.get(im).getSymbol());
            }

            if(symbolList.size() > 1)
            {
               // int oneLine = 1;
               // while (oneLine <= lines)
               // {
                   // int lineHeight = oneLine*90;


                     for (int ji = 0; ji <symbolList.size(); ji++)
                     {
                        // if ((symbolList.get(ji).getCenterOfMass().getY() < lineHeight) && symbolList.get(ji).getCenterOfMass().getY() > lineHeight - 90)
                        // {
                             info.append(symbolList.get(ji).getSymbol());
                       //  }

                        double massCenterDistance;

                         if (ji+1 != symbolList.size())
                         {
                             Point2D iCenterOfMass =  symbolList.get(ji).getCenterOfMass();
                             Point2D jCenterOfMass =  symbolList.get(ji+1).getCenterOfMass();

                             massCenterDistance = abs(iCenterOfMass.getX()-jCenterOfMass.getX());

                             if (massCenterDistance > 100)
                             {
                                info.append(" ");
                             }
                         }       
                     }

                  //  info.append("\n");
                  //  oneLine++;
                }

            }

            //clear symbol list 
            symbolList.clear();

        //}

        return resultImg;
    }
}