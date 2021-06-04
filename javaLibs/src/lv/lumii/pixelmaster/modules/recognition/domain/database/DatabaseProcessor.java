package lv.lumii.pixelmaster.modules.recognition.domain.database;

import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterDatabase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;
/**
 * Klase satur metodes, kuras darbojās ar datu bāzi.
 * @author Sandra Rivare
 * @since 01.04.2009
 **/
public class DatabaseProcessor {

    /**
     * Ielasa informāciju no datu bāzes parametrā cDB.
     * @author Sandra Rivare
     * @return cDB parametru, kas satur informāciju par datu bāzē esošām teksta zīmēm
     * @since 01.04.2009
     **/
    public static CharacterDatabase readDatabase(File database) throws IOException{
        CharacterDatabase cDB = new CharacterDatabase();
		String line;
		int rows=0;
		FileReader fr = new FileReader(database);
		BufferedReader countLinesInFile = new BufferedReader(fr);
		while((line=(countLinesInFile.readLine()))!=null){
			rows++;
		}
		fr.close();
		cDB.rowCount = rows;
		FileReader fr2 = new FileReader(database);

		cDB.letter = new String[cDB.rowCount];
		cDB.V1 = new int[cDB.rowCount];
		cDB.V2 = new int[cDB.rowCount];
		cDB.V3 = new int[cDB.rowCount];
		cDB.V4 = new int[cDB.rowCount];
		cDB.V5 = new int[cDB.rowCount];
		cDB.V6 = new int[cDB.rowCount];
		cDB.V7 = new int[cDB.rowCount];
		cDB.V8 = new int[cDB.rowCount];

		cDB.VV1 = new int[cDB.rowCount];
		cDB.VV2 = new int[cDB.rowCount];
		cDB.VV3 = new int[cDB.rowCount];
		cDB.VV4 = new int[cDB.rowCount];
		cDB.VV5 = new int[cDB.rowCount];
		cDB.VV6 = new int[cDB.rowCount];
		cDB.VV7 = new int[cDB.rowCount];
		cDB.VV8 = new int[cDB.rowCount];

		cDB.fromCenterV1 = new int[cDB.rowCount];
		cDB.fromCenterV2 = new int[cDB.rowCount];
		cDB.fromCenterV3 = new int[cDB.rowCount];
		cDB.fromCenterV4 = new int[cDB.rowCount];

		cDB.variance = new double[cDB.rowCount];
//            cDB.isValid = new boolean[cDB.rowCount];
		BufferedReader br = new BufferedReader(fr2);
		String token = null;
		for(int i=0;(line=(br.readLine())) != null;i++) {
			StringTokenizer st = new StringTokenizer(line);
			token = st.nextToken();
			while(st.hasMoreTokens()){
				cDB.letter[i] = token;
				token = st.nextToken();
				cDB.V1[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.V2[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.V3[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.V4[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.V5[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.V6[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.V7[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.V8[i] = Integer.parseInt(token);

				token = st.nextToken();
				cDB.VV1[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.VV2[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.VV3[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.VV4[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.VV5[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.VV6[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.VV7[i] = Integer.parseInt(token);
				token = st.nextToken();
				cDB.VV8[i] = Integer.parseInt(token);

				token = st.nextToken();
				cDB.fromCenterV1[i]= Integer.parseInt(token);
				token = st.nextToken();
				cDB.fromCenterV2[i]= Integer.parseInt(token);
				token = st.nextToken();
				cDB.fromCenterV3[i]= Integer.parseInt(token);
				token = st.nextToken();
				cDB.fromCenterV4[i]= Integer.parseInt(token);

				cDB.variance[i] = -1;
				//cDB.isValid[i] = false;
			}
		}
		fr2.close();
		return cDB;
    }//end of readDatabase

    /**
     * Pievieno jaunu ierakstu datu bāzei
     * @author Sandra Rivare
     * @param line1 dotais burts
     * @param line2 dotais cipars
     * @param table informācija par attēlā redzamo burtu
     * @return result atgriež rezultātu - vai tika pievienota
     * teksta zīme datu bāzei vai nē
     * @since 01.04.2009
     **/
    public static String addToDatabase(String line1, String line2, CharacterInfo table,
            File database, CharacterDatabase cDB){
        String result = null;
        FileInputStream fis = null;
        String charToBeInserted = null;
        String defaultString = "-";
        if((line1 == defaultString && line2 != defaultString) ||
                (line1 != defaultString && line2 == defaultString)){
            if(line1 == defaultString) charToBeInserted = line2;
            if(line2 == defaultString) charToBeInserted = line1;
            boolean cont=true;

            for(int i=0;i<cDB.rowCount;i++){
                if(cDB.V1[i]==table.V1)
                if(cDB.V2[i]==table.V2)
                if(cDB.V3[i]==table.V3)
                if(cDB.V4[i]==table.V4)
                if(cDB.V5[i]==table.V5)
                if(cDB.V6[i]==table.V6)
                if(cDB.V7[i]==table.V7)
                if(cDB.V8[i]==table.V8)
                if(cDB.VV1[i]==table.VV1)
                if(cDB.VV2[i]==table.VV2)
                if(cDB.VV3[i]==table.VV3)
                if(cDB.VV4[i]==table.VV4)
                if(cDB.VV5[i]==table.VV5)       
                if(cDB.VV6[i]==table.VV6)
                if(cDB.VV7[i]==table.VV7)
                if(cDB.VV8[i]==table.VV8)
                if(cDB.fromCenterV1[i]==table.fromCenterV1)
                if(cDB.fromCenterV2[i]==table.fromCenterV2)
                if(cDB.fromCenterV3[i]==table.fromCenterV3)
                if(cDB.fromCenterV4[i]==table.fromCenterV4){
                    cont = false;
                    result = "Record with this vector length already exists";
                }
                
                if(cont==false) break;                
            }

            if(cont==true){
                try {
                    //tmp
                    File outFile = File.createTempFile("test", null);
                    //input
                    File inFile = database;
                    fis = new FileInputStream(inFile);
                    BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                    // output
                    FileOutputStream fos = new FileOutputStream(outFile);
                    PrintWriter out = new PrintWriter(fos);

                    String thisLine = "";
                    while ((thisLine = in.readLine()) != null) {
                        //if(i == lineno) out.println(lineToBeInserted);
                        out.println(thisLine);
                    }
                    result = "You have jut added "+charToBeInserted+ " to text character database";
                    out.println(charToBeInserted+"\t"+table.V1+"\t"+table.V2+"\t"+table.V3+
                            "\t"+table.V4+"\t"+table.V5+"\t"+table.V6+"\t"+table.V7+"\t"+table.V8+"\t"
                            +table.VV1+"\t"+table.VV2+"\t"+table.VV3+"\t"+table.VV4+"\t"+table.VV5+"\t"+table.VV6
                            +"\t"+table.VV7+"\t"+table.VV8+"\t"+table.fromCenterV1+"\t"+table.fromCenterV2
                            +"\t"+table.fromCenterV3+"\t"+table.fromCenterV4);
                    out.flush();
                    out.close();
                    in.close();
                    inFile.delete();
                    outFile.renameTo(inFile);
                } catch (IOException ex) {
                    Logger.getLogger(DatabaseProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            fis.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DatabaseProcessor.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }
            }
        }else{
            result = "Choose letter OR number";
        }
        return result;
    }//end of addToDatabase
}
