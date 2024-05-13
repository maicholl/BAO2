/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bao2;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Maicholl
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // TODO code application logic here
        /************************************************************
    * Ask user which objective function he/she wants to         * 
    * consider. Options are:                                    *
    * 1- LogFunction                                            *
    * 2- Lexico Rectum
    * 3- Lexico Bladder
    * 4- Cuadratic
    *************************************************************/
    //option = menuInit();
    BAO2 bao = new BAO2();
    String inputFile = args[1];
    bao.readInputFile("./"+inputFile);
   
////    
    /************************************************************
    * initialize some of the variables of the algorithm         * 
    *************************************************************/
    //Thread.currentThread().setName("0"); //0 means main program... kind of currentSol
    bao.jobID = args[0]; // Used by NESI. Corresponds to the JOBID when submitted using file *.sl

    
    
    bao.run();
    /*   
    MipManual mip = new MipManual();
    mip.readInputFile("./"+inputFile);
    mip.jobID = args[0];
    mip.run();
      
    KnitroJava janitro = new KnitroJava();
    janitro.test();
    */
    }
}
