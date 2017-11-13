import java.util.Scanner;
import java.nio.file.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class main {
  public static void main(String[] args) {

    String dataFile, paraFile;

    if(args.length < 2) {
      System.out.println("Please provide the input filename as the first arg, and output filename as the second arg");
      return;
    } else {
      dataFile= args[0];
      paraFile = args[1];
    }

    //defing the file reader
    Path filePath;
    Scanner sc = null;

    //Initializing the file reader
    try {
      //Reader
      filePath = Paths.get(dataFile);
      sc = new Scanner(filePath);
    } catch(IOException e) {
      System.out.println("Might be an error with output filename: " + dataFile);
      System.err.println("Caught IOException: " + e.getMessage());
    }

    ArrayList<probNode> genderTable = new ArrayList<probNode>();
    ArrayList<probNode> weightTable = new ArrayList<probNode>();
    ArrayList<probNode> heightTable = new ArrayList<probNode>();

    String dataS;
    int data;
    String label;

    probNode newNode;
    try {
      //Read the labels from the input;
      label = sc.next();
      label = sc.next();
      label = sc.next();

      System.out.println("Gender\tWeight\tHeight");

      while(sc.hasNext()) {
        dataS = sc.next();
        System.out.print(dataS + " ");
        if(!dataS.equals("-")) {
          data =  Integer.parseInt(dataS);
          //Existing label
          newNode = new probNode();
          newNode.setProb(data, 100);
          genderTable.add(newNode);
        }

        dataS = sc.next();
        System.out.print(dataS + " ");
        if(!dataS.equals("-")) {
          data =  Integer.parseInt(dataS);
          //Existing label
          newNode = new probNode();
          newNode.setProb(data, 100);
          genderTable.add(newNode);
        }

        dataS = sc.next();
        System.out.print(dataS + " ");
        if(!dataS.equals("-")) {
          data =  Integer.parseInt(dataS);
          //Existing label
          newNode = new probNode();
          newNode.setProb(data, 100);
          genderTable.add(newNode);
        }
        System.out.println();
      }
    } catch (Exception e) {
      System.out.println("Error during file reading process " + dataFile);
      System.err.println("Caught Exception: " + e.getMessage());
    }
  }
}

//Class for probability
class probNode {
  int[] prob;
  public probNode() {
    this.prob = new int[2];
  }

  //Assign the probability to the label
  public void setProb(int label, int prob) {
    this.prob[label] = prob;
  }
}

class EM {

  public EM() {

  }

  public void eStep(){

  }
  public void mStep() {

  }
}
