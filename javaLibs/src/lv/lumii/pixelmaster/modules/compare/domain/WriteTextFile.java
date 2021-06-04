package lv.lumii.pixelmaster.modules.compare.domain;

import java.io.*;


/**
 * Function export data to .txt file.
 * When call this function don`t forget also call closeFile() function.
 * Function makes file at the same directory as program code.
 * @param: export data
 * @return: .txt file
 * @author: Madara Augstkalne
 * @since: 2010.31.3
 */


public class WriteTextFile {

    private Writer output;

    //Prints some data to a file using a BufferedWriter
    public WriteTextFile(File file)  {

       this.output = null;
       // write mode
//       File file = new File("write.txt");
       try {
           //write mode
           this.output = new BufferedWriter(new java.io.FileWriter(file));

           //append mode
           // this.output = new BufferedWriter(new FileWriter("write.txt", true));

       } catch (IOException e) {
           System.out.println("IOException:");
           e.printStackTrace();
       }

    }

    public void writeInFile(String s) {
       try {
            this.output.write(s);
       } catch (IOException e) {
           System.out.println("IOException:");
           e.printStackTrace();
       }

   }

   public void closeFile() {
       try {
           if (this.output != null) {
           this.output.close();
           }
       } catch (IOException e) {
           System.out.println("IOException:");
           e.printStackTrace();
       }
   }

    public void main(String s){
       writeInFile(s);
    }
}