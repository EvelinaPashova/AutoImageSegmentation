
package lv.lumii.pixelmaster.modules.ridge.domain;

import java.util.UUID;
import java.util.ArrayList;
import java.io.*;
import java.util.StringTokenizer;
import java.io.IOException;
/**
 *
 * @author Kumpa
 */
public class SymbolInfo {
    private String name;
    private long id;
    private int[] values;

    public SymbolInfo() {
        id = UUID.randomUUID().getMostSignificantBits();
    }

    public SymbolInfo(String name, int valuesCount) {
        if (valuesCount < 1) {
            throw new IllegalArgumentException();
        }
        id = UUID.randomUUID().getMostSignificantBits();
        values = new int[valuesCount];
        this.name = name;
    }

    public SymbolInfo(Symbol symbol) {
        name = symbol.getName();
        id = symbol.getId();
        values = symbol.lineSum();
    }

    public boolean setName(String name) {
        if (this.name != name) {
            this.name = name;
            return true;
        }
        return false;
    }

    public boolean setId (long id) {
        if (this.id != id) {
            this.id = id;
            return true;
        }
        return false;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getValues() {
        return values;
    }
    
    public boolean set (int index, int value) {
        if (index > values.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        else if (values[index] != value) {
            values[index] = value;
            return true;
        }
        return false;
    }

    public boolean set (int [] value) {
        if (value.equals(values)) {
            return false;
        }
        values = value;
        return true;
    }

    /**
     * Salīdzina šī elementa values vērtības ar parametros norādītās SymbolInfo datubāzes elementu
     * values vērtībām un atgriež tā SybmolInfo name vērtību ar kuru ir vismazākā
     * value atšķirību summa.
     * @param data
     * @param maxDifference
     * @param maxDiffCount
     * @param maxDiffSum
     * @return
     */
    public boolean compare (ArrayList<SymbolInfo> data,
        int maxDifference, int maxDiffSum, int maxDiffCount) {
        if (data == null || values.length <=0) {
            throw new IllegalArgumentException("Ridge.SymbolInfo.compare IllegalArgument");
        }
        int smalestDiffSum = maxDiffSum + 1;
        boolean nameSet = false;
        for (SymbolInfo dataItem: data) {
            int difference = 0;
            int diffSum = 0;
            int diffCount = 0;
            boolean skip = false;
            int[] tmpValues = dataItem.getValues();
            for (int index = 0; index < values.length; index++) {
                difference = Math.abs(values[index] - tmpValues[index]);
                    if (difference < maxDifference) {
                        diffSum += difference;
                        if (difference != 0) {
                            diffCount += 1;
                        }
                        if (diffSum > maxDiffSum || diffCount > maxDiffCount) {
                            skip = true;
                            break;
                        }
                    }
                    else {
                        skip = true;
                        break;
                    }
            }
            if (diffSum < smalestDiffSum && skip != true && diffSum !=-1) {
                smalestDiffSum = diffSum;
                this.name = dataItem.getName();
                nameSet = true;
            }
        }
        return nameSet;
    }

    public String stringTo() {
        String result = "";
        result = name + " ";
        for (int item: values) {
            result += item;
            result += " ";
        }
        return result;
    }
    
    // from http://blog.etl.luc.edu/2007/02/java-issue-making-sure-string-is-int.html
    public static boolean isIntegerStringMatch(String string) {
        return string.matches("[+-]?[0-9]+");
    }
    /**
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public static ArrayList<SymbolInfo> fileRead(File file) throws IOException{
        if (file == null)
            throw new IllegalArgumentException();
        ArrayList<SymbolInfo> data = new ArrayList<SymbolInfo>();
        File inFile = null;
        FileReader fReader = null;
        BufferedReader bufReader = null;
        try {
            inFile = file;
            String line;
            int valuesCount = 0;

            fReader = new FileReader(inFile);
            bufReader = new BufferedReader(fReader);
            String token = null;
            while( (line=(bufReader.readLine())) != null) {
                StringTokenizer sToken = new StringTokenizer(line);
                if (line.length() != 0) {
                    token = sToken.nextToken();
                
                    boolean skip = false;
                    while(sToken.hasMoreTokens() && !token.contains("#")){
                            if (token.equals("values_count=")) {
                                token = sToken.nextToken();
                                if (isIntegerStringMatch(token)) {
                                    valuesCount = Integer.parseInt(token);
                                }
                            }
                            else if (valuesCount != 0) {
                                SymbolInfo tempSymbolInfo = new SymbolInfo(token, valuesCount);
                                int index = 0;
                                while(sToken.hasMoreTokens()){
                                    token = sToken.nextToken();
                                    if (index < valuesCount && !token.contains("#")
                                            && isIntegerStringMatch(token)) {
                                        tempSymbolInfo.set(index, Integer.parseInt(token));
                                        index++;

                                    }
                                    else {
                                        skip = true;
                                    }
                                }
                                if  (!skip)
                                    data.add(tempSymbolInfo);
                            }
                            else token = sToken.nextToken();
                    }
                }
            }
          bufReader.close();  
        }
        catch (IOException ioe) {
            System.out.println("IOException Symbol.fileReader");
        }
        finally {
            if (bufReader != null) {
                bufReader.close();
            }
        }
        return data;
    }

    public static boolean fileWrite (SymbolInfo data, File file, boolean allowNew) throws IOException{
        if (file == null)
            throw new IllegalArgumentException();
        ArrayList<SymbolInfo> fileData = new ArrayList<SymbolInfo>();
        File inFile = null;
        File outFile = null;
        FileReader fReader = null;
        BufferedReader bufReader = null;
        FileWriter fWriter = null;
        BufferedWriter bWriter = null;
        PrintWriter pWriter = null;
        try {
            String line;
            inFile = file;
            if (!inFile.exists()) {
                if (allowNew == false || !inFile.canRead()) {
                    return false;
                }
            }
            outFile = File.createTempFile("test", null);
            fReader = new FileReader(inFile);
            bufReader = new BufferedReader(fReader);
            fWriter = new FileWriter (outFile);
            bWriter = new BufferedWriter(fWriter);
            pWriter = new PrintWriter (bWriter, true);

            String token = null;
            boolean added = false;
            boolean canAdd = false;
            boolean exist = false;
            boolean foundSymbol = false;
            boolean endOfSymbol = false;
            while ( (line=(bufReader.readLine())) != null ) {
                if (line.contains("values_count=") && !line.contains("#")) {
                    canAdd = true;
                }
                if (line.length() != 0 && !added && canAdd) {
                    StringTokenizer sToken = new StringTokenizer(line);
                    token = sToken.nextToken();
                    if (token.equals(data.getName())) {
                        foundSymbol = true;
                        if (line.contains(data.stringTo())) {
                            exist = true;
                        }
                    }
                    else if (foundSymbol) {
                        endOfSymbol = true;
                    }
                }
                if (!exist && endOfSymbol && !added) {
                    pWriter.println(data.stringTo());
                    added = true;
                }
                pWriter.println(line);

            }
            if (!added && !exist) {
                pWriter.println(data.stringTo());
            }
          bufReader.close();
          pWriter.close();

          inFile.delete();
          outFile.renameTo(inFile);
        }
        catch (IOException ioe){
            System.out.println("IOException Symbol.fileWriter");
        }
        finally {
            if (bufReader != null) {
                bufReader.close();
            }
            if (pWriter != null) {
                pWriter.close();
            }
        }
        return true;
    }

    public String toString() {
        String result = "Name: " + name;
        result +=" ID: " + id + "\n Values: ";
        for (int item: values) {
            result += item;
            result += " ";
        }
        return result;
    }



}
