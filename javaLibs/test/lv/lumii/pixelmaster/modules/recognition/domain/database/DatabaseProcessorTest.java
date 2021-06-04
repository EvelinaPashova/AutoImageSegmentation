package lv.lumii.pixelmaster.modules.recognition.domain.database;

import lv.lumii.pixelmaster.modules.recognition.domain.database.DatabaseProcessor;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterDatabase;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;

/**
 *
 * @author Sandra Rivare
 * @since 16.04.2009.
 */
public class DatabaseProcessorTest extends TestCase {
    
    public DatabaseProcessorTest(String testName) {
        super(testName);
    }

   

    /**
     * Test of addToDatabase method, of class DatabaseProcessor.
     * @modified 23.04.2009
     */
    public void testAddToDatabase() throws IOException {
        System.out.println("DatabaseProcessor: addToDatabase");

        CharacterInfo table = new CharacterInfo();
        CharacterDatabase cDB = new CharacterDatabase();
        table.V1 = 28;
        table.V2 = 50;
        table.V3 = 26;
        table.V4 = 18;
        table.V5 = 64;
        table.V6 = 62;
        table.V7 = 25;
        table.V8 = 28;
        table.VV1 = 42;
        table.VV2 = 23;
        table.VV3 = 23;
        table.VV4 = 40;
        table.VV5 = 25;
        table.VV6 = 28;
        table.VV7 = 31;
        table.VV8 = 27;
        table.fromCenterV1 = 15;
        table.fromCenterV2 = 36;
        table.fromCenterV3 = 13;
        table.fromCenterV4 = 7;

        int V1 = 28;
        int V2 = 50;
        int V3 = 26;
        int V4 = 18;
        int V5 = 64;
        int V6 = 62;
        int V7 = 25;
        int V8 = 28;
        int VV1 = 42;
        int VV2 = 23;
        int VV3 = 23;
        int VV4 = 40;
        int VV5 = 25;
        int VV6 = 28;
        int VV7 = 31;
        int VV8 = 27;
        int fromCenterV1 = 15;
        int fromCenterV2 = 36;
        int fromCenterV3 = 13;
        int fromCenterV4 = 7;

        File f = File.createTempFile("test", null);
		f.deleteOnExit();

        String result1 = DatabaseProcessor.addToDatabase("B", "-", table, f, cDB);
        String result2 = DatabaseProcessor.addToDatabase("-", "0", table, f, cDB);
        String result3 = DatabaseProcessor.addToDatabase("-", "-", table, f, cDB);
        String result4 = DatabaseProcessor.addToDatabase("B", "0", table, f, cDB);

		cDB = DatabaseProcessor.readDatabase(f);
		assertEquals(V1, cDB.V1[0]);
		assertEquals(V2, cDB.V2[0]);
		assertEquals(V3, cDB.V3[0]);
		assertEquals(V4, cDB.V4[0]);
		assertEquals(V5, cDB.V5[0]);
		assertEquals(V6, cDB.V6[0]);
		assertEquals(V7, cDB.V7[0]);
		assertEquals(V8, cDB.V8[0]);
		assertEquals(VV1, cDB.VV1[0]);
		assertEquals(VV2, cDB.VV2[0]);
		assertEquals(VV3, cDB.VV3[0]);
		assertEquals(VV4, cDB.VV4[0]);
		assertEquals(VV5, cDB.VV5[0]);
		assertEquals(VV6, cDB.VV6[0]);
		assertEquals(VV7, cDB.VV7[0]);
		assertEquals(VV8, cDB.VV8[0]);
		assertEquals(fromCenterV1, cDB.fromCenterV1[0]);
		assertEquals(fromCenterV2, cDB.fromCenterV2[0]);
		assertEquals(fromCenterV3, cDB.fromCenterV3[0]);
		assertEquals(fromCenterV4, cDB.fromCenterV4[0]);

		assertEquals("You have jut added B to text character database", result1);
		assertEquals("You have jut added 0 to text character database", result2);
		assertEquals("Choose letter OR number", result3);
		assertEquals("Choose letter OR number", result4);
    }

}
