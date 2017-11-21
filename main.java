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

    String dataFile, likelihoodFile;

    if(args.length < 2) {
      System.out.println("Please provide the input filename as the first arg, and output filename as the second arg");
      return;
    } else {
      dataFile= args[0];
      likelihoodFile = args[1];
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

    ArrayList<Integer> genderTable = new ArrayList<Integer>();
    ArrayList<Integer> weightTable = new ArrayList<Integer>();
    ArrayList<Integer> heightTable = new ArrayList<Integer>();

    //probability of g = 0 table with index relative to the value table
    ArrayList<Float> probRow = new ArrayList<Float>();

    ArrayList<Integer> missingIndex = new ArrayList<Integer>();


    int totalC = 0;

    String dataS;
    int gender, weight, height;
    String label;

    try {
      //Read the labels from the input;
      label = sc.next();
      label = sc.next();
      label = sc.next();

      System.out.println("Gender\tWeight\tHeight");

      //Read all the data into the 3 lists
      while(sc.hasNext()) {

        dataS = sc.next();
        System.out.print(dataS + " ");

        //Check for missing data, if data is missing place null at position
        if(!dataS.equals("-")) {
          gender =  Integer.parseInt(dataS);
          //genderTable.add(gender);
          genderTable.add(0);
          gender = gender^1;
          probRow.add((float)gender);
        } else {
          genderTable.add(null);
          missingIndex.add(totalC);
          probRow.add(null);
        }
        //read data for Weight
        dataS = sc.next();
        System.out.print(dataS + " ");
        weight=  Integer.parseInt(dataS);
        weightTable.add(weight);
        //read data for Height
        dataS = sc.next();
        System.out.print(dataS + " ");
        height =  Integer.parseInt(dataS);
        heightTable.add(height);

        System.out.println();
        totalC++;
      }

    } catch (Exception e) {
      System.out.println("Error during file reading process " + dataFile);
      System.err.println("Caught Exception: " + e.getMessage());
    }

    float log_likelihood_prev = 0f, log_likelihood_curr = 0f, changeInLikelihood;

    model EM_model = new model();
    float prob;
    int index;
    int iteration = 0;

    BufferedWriter bw = null;
    try {
      FileWriter fw = new FileWriter(likelihoodFile);
      bw = new BufferedWriter(fw);
    } catch (Exception e) {
      System.out.println("Might be an error with output filename: " + likelihoodFile);
      System.err.println("Caught IOException: " + e.getMessage());
    }

    do {

      System.out.print("Indexes of missing data: ");
      for(int i = 0; i < missingIndex.size(); i++) {
        index = missingIndex.get(i);
        System.out.print(index + " , ");
        // (g_val, w_val, h_val)
        prob = EM_model.getG_WH(0, weightTable.get(index), heightTable.get(index));
        probRow.set(index, prob);
      }

      System.out.println();

      System.out.println("Table after 1 iteration");
      for(int i = 0; i < genderTable.size(); i++) {
        System.out.println("Gender\tWeight\tHeight\tProb");
        System.out.println(genderTable.get(i) + "\t" + weightTable.get(i) + "\t" + heightTable.get(i) + "\t" + probRow.get(i));
      }

      float count_g_0 = 0, count_w_0_g_0 = 0, count_w_1_g_0 = 0, count_h_0_g_0 = 0, count_h_1_g_0 = 0;
      float count_g_1 = 0, count_w_0_g_1 = 0, count_w_1_g_1 = 0, count_h_0_g_1 = 0, count_h_1_g_1 = 0;
      int rowCount = 0;
      for(int i = 0; i < probRow.size(); i++) {
        count_g_0 += probRow.get(i);
        count_g_1 += 1-probRow.get(i);

        if(weightTable.get(i) == 0) {
          count_w_0_g_0 += probRow.get(i);
          count_w_0_g_1 += 1 - probRow.get(i);
        } else {
          count_w_1_g_0 += probRow.get(i);
          count_w_1_g_1 += 1 - probRow.get(i);
        }

        if(heightTable.get(i) == 0) {
          count_h_0_g_0 += probRow.get(i);
          count_h_0_g_1 += 1 - probRow.get(i);
        } else {
          count_h_1_g_0 += probRow.get(i);
          count_h_1_g_1 += 1 - probRow.get(i);
        }
        rowCount++;
      }

      // float prob_g_0, prob_w_0_g_0, prob_w_1_g_0, prob_h_0_g_0, prob_h_1_g_0;
      // float prob_g_1, prob_w_0_g_1, prob_w_1_g_1, prob_h_1_g_1, prob_h_1_g_1;

      //recompute probability for model
      EM_model.G[0] = count_g_0/rowCount;
      EM_model.G[1] = count_g_1/rowCount;
      EM_model.W_G[0][0] = count_w_0_g_0/count_g_0;
      EM_model.W_G[1][0] = count_w_1_g_0/count_g_0;
      EM_model.W_G[0][1] = count_w_0_g_1/count_g_1;
      EM_model.W_G[1][1] = count_w_1_g_1/count_g_1;
      EM_model.H_G[0][0] = count_h_0_g_0/count_g_0;
      EM_model.H_G[1][0] = count_h_1_g_0/count_g_0;
      EM_model.H_G[0][1] = count_h_0_g_1/count_g_1;
      EM_model.H_G[1][1] = count_h_1_g_1/count_g_1;

      EM_model.printModel();

      // break;

      log_likelihood_prev = log_likelihood_curr;
      log_likelihood_curr = 0;
      //find the likelihood of the model
      for(int i = 0; i < genderTable.size(); i++) {
        if(genderTable.get(i) == null) {
          log_likelihood_curr += Math.abs(Math.log(EM_model.getGWH(0,weightTable.get(i), heightTable.get(i)) + EM_model.getGWH(1, weightTable.get(i), heightTable.get(i))));
        } else {
          log_likelihood_curr += Math.abs(Math.log(EM_model.getGWH(genderTable.get(i), weightTable.get(i), heightTable.get(i))));
        }
      }
      System.out.println("Sum of log likelihood = " + log_likelihood_curr);
      System.out.println("previous likelihood = " + log_likelihood_prev);
      changeInLikelihood = Math.abs(log_likelihood_curr - log_likelihood_prev);
      System.out.println("Change in likelihood = " + changeInLikelihood);
      iteration++;
      try {
        bw.write(log_likelihood_curr + "\t" + iteration);
        bw.newLine();
        bw.flush();
      } catch (Exception e) {
        System.out.println("Error while writing to file" + likelihoodFile);
      }

      // if(iteration == 3) {
      //   break;
      // }
    } while(changeInLikelihood > .0001f);

    try {
      bw.close();
    } catch (Exception e) {
      System.out.println("Error when closing file stream" + likelihoodFile);
    }

    System.out.println("Number of iterations: " + iteration);
  }
}

//Class for probability
// class probNode {
//   float[] prob;
//   boolean flag;
//   public probNode(int label, float prob) {
//     this.prob = new FLOAT[2]();
//     this.prob[label][prob];
//     flag = false;
//   }
//
//   //Assign the probability to the label
//   public void setProb(int label, int prob) {
//     this.prob[label] = prob;
//   }
//
//   public void setEvaluation() {
//     flag = true;
//   }
//
//   public boolean needEvaluation() {
//     return flag;
//   }
// }

class model {
  //initial parameters
  Float G[];
  Float W_G[][];
  Float H_G[][];

  model() {
    G = new Float[2];
    W_G = new Float[2][2];
    H_G = new Float[2][2];

    G[0] = .7f;
    G[1] = 1-G[0];
    W_G[0][0] = .8f;
    W_G[0][1] = 1-W_G[0][0];
    W_G[1][0] = .2f;
    W_G[1][1] = 1-W_G[1][0];
    H_G[0][0] = .7f;
    H_G[0][1] = 1-H_G[0][0];
    H_G[1][0] = .3f;
    H_G[1][1] = 1-H_G[1][0];

    System.out.println("Starting Parameters:");
    this.printModel();
  }

  void printModel() {
    System.out.println("P(G = 0) = " + this.G[0]);
    System.out.println("P(G = 1) = " + this.G[1]);
    System.out.println("P(W = 0 | G = 0) = " + this.W_G[0][0]);
    System.out.println("P(W = 1 | G = 0) = " + this.W_G[1][0]);
    System.out.println("P(W = 0 | G = 1) = " + this.W_G[0][1]);
    System.out.println("P(W = 1 | G = 1) = " + this.W_G[1][1]);
    System.out.println("P(H = 0 | G = 0) = " + this.H_G[0][0]);
    System.out.println("P(H = 1 | G = 0) = " + this.H_G[1][0]);
    System.out.println("P(H = 0 | G = 1) = " + this.H_G[0][1]);
    System.out.println("P(H = 1 | G = 1) = " + this.H_G[1][1]);
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
    return this.G[g_val];
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
  float getG_WH(int g_val, int w_val, int h_val) {
    int g_val_xor = g_val^1;
    return (
      this.getGWH(g_val, w_val, h_val)/(this.getGWH(g_val, w_val, h_val) + this.getGWH(g_val_xor, w_val, h_val))
      );
  }
}
