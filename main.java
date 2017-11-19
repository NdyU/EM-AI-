import java.util.Scanner;
import java.nio.file.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.lang.Math;

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

    float countW = 0, countH = 0;
    int probW, probH;
    int totalC = 0;

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
          newNode.setProb(data, 1);
          genderTable.add(newNode);
        } else {

        }

        dataS = sc.next();
        System.out.print(dataS + " ");
        if(!dataS.equals("-")) {
          data =  Integer.parseInt(dataS);
          //Existing label
          newNode = new probNode();
          newNode.setProb(data, 1);
          weightTable.add(newNode);

          //count value 1
          if(data == 1) {
            countW++;
          }
        }

        dataS = sc.next();
        System.out.print(dataS + " ");
        if(!dataS.equals("-")) {
          data =  Integer.parseInt(dataS);
          //Existing label
          newNode = new probNode();
          newNode.setProb(data, 100);
          heightTable.add(newNode);

          if(data == 1 ) {
            countH++;
          }
        }
        System.out.println();
        totalC++;
      }

      boolean changeFlag = false;

      for(int i = 0; i < totalCl i++) {
        if(genderTable.get(i) == NULL)
      }
      // probW = Math.round(countW/totalC * 100);
      // probH = Math.round(countH/totalC * 100);
      //
      // System.out.println("probability Weight: " + probW);
      // System.out.println("probability Height: " + probH);
    } catch (Exception e) {
      System.out.println("Error during file reading process " + dataFile);
      System.err.println("Caught Exception: " + e.getMessage());
    }
  }
}

//Class for probability
class probNode {
  float[] prob;
  public probNode() {
    this.prob = new float[2];
  }

  //Assign the probability to the label
  public void setProb(int label, int prob) {
    this.prob[label] = prob;
  }
}

class model {
  //initial parameters
  static final float G[2] = {.7, .3};
  static final float W_G[2][2] = {{.8, .4}, {.2, .6}};
  static final float H_G[2][2] = {{.7, .3}, {.3, .7}};

  model() {

  }

  //P(W|G)
  //
  //Starting parameters
  //           G：
  //
  //          0    1
  //        ———— ————
  //  W: 0 | .8 | .4 |
  //        ———— ————
  //     1 | .2 | .6 |
  //        ———— ————
  //
  float getW_G(int w_val, int g_val) {
    return this.W_G[w_val][g_val];
  }

  //P(H|G)
  //Starting parameters
  //           G：
  //
  //          0    1
  //        ———— ————
  //  H: 0 | .7 | .3 |
  //        ———— ————
  //     1 | .3 | .7 |
  //        ———— ————
  //
  float getH_G(int h_val, int g_val) {
    return this.H_G[h_val][g_val];
  }

  //P(G)
  //Starting parameters
  //           G：
  //
  //          0    1
  //        ———— ————
  //       | .7 | .3 |
  //        ———— ————
  //
  float getG(int g_val) {
    return this.G[val];
  }

  //W⊥H Given H
  //P(GWH) = P(WH|G)P(G) = P(W|G)P(H|G)P(G)
  float getGWH(int g_val, int w_val, int h_val) {
    return (

      this.getW_G(w_val, g_val) * this.getH_G(h_val, g_val) * this.getG(g_val)

      );
  }

  //Solving for missing data for G
  //P(G|WH) = P(GWH)
  //         -------
  //          P(WH)
  //
  //        = P(WH|G)P(G)
  //          -----------
  //           Σ P(GWH)     -> P(W|G)P(H|G)P(G)
  //           G
  //
  //        =                P(W|G)P(H|G)P(G)
  //          -----------------------------------------------
  //          P(W|G=0)P(H|G=0)P(G=0) + P(W|G=1)P(H|G=1)P(G=1)
  //
  float getEM(int g_val, w_val, h_val) {
    int g_val_xor ^= g_val;
    return (
      this.getGWH(g_val, w_val, h_val)/(this.getGWH(g_val, w_val, h_val) + this.getGWH(g_val_xor, w_val, h_val))
      )
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
