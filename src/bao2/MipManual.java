/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bao2;

import static bao2.BAO2.bubbleSort;
import static bao2.BAO2.globalTimer;
import static bao2.BAO2.order_my_hashmap;
import static bao2.BAO2.readRelaxedMiP;
import static bao2.BAO2.writeLine;
import com.ampl.AMPL;
import imrt2.Organs;
import imrt2.TreatmentPlan;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maiik
 */
public class MipManual {
    public AMPL ampl;
    public int numOrgans;
    public int numAngles;
    public ArrayList<TreatmentPlan> generatedBACs; 
    public ArrayList<int []> generatedfixed; 
    public int[][] Vx; //Vx index. % of the volume receiving more than x% of the prescribed dose
                              //One row per organ (usually only for target regions)
    public int[][] Dx; //Dx index. Minimum dose of the hottest x% of the volume.
                              //One row per organ (usually only for OAR regions)
    
    //public static int poolSize = 3;
    public ArrayList<TreatmentPlan> visitedBACs; 
    public double[][] ndp; 
    public ArrayList<TreatmentPlan> ndp_mo; 
    public ArrayList<TreatmentPlan> localOptima;
    //public static ArrayList<Long[]> voxelIndex[]; 
    //public static DDM M;
    public double minChange;//Maicholl VNS
    public  int vecindario; //Maicholl VNS Contador Vecindario
    public  int vecindarios; //Maicholl VNS Max vecindarios
    public  int counterVNS=-1; //Maicholl VNS Counter
    public  int tipoVNS;
    public  int iterationsWithoutImprovement; //Maicholl VNS Max Iteration
    public  int option;
    public static long globalTimer = System.currentTimeMillis(), localTimer,auxAnteriorTimer, auxActualTimer,initTimer;// Maicholl
    public  boolean randomInitial, parallelNeighbourhoodGeneration;
    public  int[][] initialBACs;
    public  String pathFile = "";
    public  int[][] beamletSet;
    public  int step, totalObjFuncEval=0;
    public  int iterations, selectionCriterion;
    public  String jobID;
    public  String solver="ipopt", name="";
    public  double maxX = 400; //Maicholl
    public  int[] ordenVecindades;
    public  int neighbourhoodSize, globalIter, globalObjFuncEval;
    
    public  String BACname= "";//MIPName
    public  int numAnglesMIP=0;
    public TreatmentPlan initialPlan;
    public TreatmentPlan currentSol;
    public TreatmentPlan bestNeighbour;
    public ArrayList<TreatmentPlan> historyPlan;
    
    public String bestFile="";
    public String currentFile = ""; 
    public String summaryDir="";
    public String finalBACsFile="";
    public String beamInfoDir= "";
    // MO FILES
    public String ndpFile = "";
    public String bestSolFile = "";
    public String vBACsFile = "";
    public String image_ndpFile = "";
    public String image_currentFile = "";
    public String image_vBACsFile = "";
    
    public int globalTotalObjFuncEval = 0;
    
    public  String problemType = ""; //  MAICHOLL, PERMITE SABER SI EJECUTAR SO O MO PROBLEM
    public  String moestrategy = ""; // MAICHOLL, PERMITE USAR EL MO DE PAQUETE(ACTUALIZACIÓN NDP LUEGO DE FMO) "Paquete" O ANGEL(ACTUALIZACIÓN NDP LUEGO DE GENERACIÓN VECINDARIOS) "Angel"
    public  String strategy = ""; //  MAICHOLL, PERMITE USAR ESTRATEGIA "SD" O "ND"
    // FIN MO FILES
    public int iterationsCounter;
    
    public boolean rMIP_type; // rMIP Boolean MAICHOLL
    
    public boolean warm_start; // warm-start Boolean MAICHOLL NZ
    
    public MipManual(){
        ampl = null;
        numOrgans = 0;
        numAngles = 0;
        generatedBACs = new ArrayList<>(); 

        //public static int poolSize = 3;
        visitedBACs = new ArrayList<>(); 
        localOptima = new ArrayList<>();
        //public static ArrayList<Long[]> voxelIndex[]; 
        //public static DDM M;
        minChange = 0;//Maicholl VNS
        vecindario = 0; //Maicholl VNS Contador Vecindario
        vecindarios = 0; //Maicholl VNS Max vecindarios
        counterVNS=-1; //Maicholl VNS Counter
        tipoVNS = 0;
        iterationsWithoutImprovement = 0; //Maicholl VNS Max Iteration
        option = 1;
        randomInitial = false;
        parallelNeighbourhoodGeneration = true;
        pathFile = "";
        step = 5; 
        totalObjFuncEval=0;
        iterations = 100;
        selectionCriterion = 1;
        iterationsCounter = 0;
        jobID = "default";
        globalTimer = 0;
        solver="knitro";
        name="";
        maxX = 400; //Maicholl
        neighbourhoodSize = 1;
        globalIter = 0;
        globalObjFuncEval = 1000000000;

        BACname= "";//MIPName
        numAnglesMIP=0;
       
        historyPlan = new ArrayList<>();

        bestFile="";
        currentFile = ""; 
        summaryDir="";
        finalBACsFile="";
    
        rMIP_type = false;
        
        ndp_mo = new ArrayList<>();
        
    }
    
    
    public void readInputFile(String dir) throws IOException{
        
        String sp="\t";
        //String dir = "./inputFile.txt";
        File f = new File(dir);
        BufferedReader fileIn = new BufferedReader(new FileReader(f));
        String line = "";
        line=fileIn.readLine();
        //First read numbero of Organs and angles
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                numOrgans = Integer.parseInt(auxReader[0]);
                numAngles = Integer.parseInt(auxReader[1]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get MO o SO
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                problemType=auxReader[0];
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get Angel o Paquete
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                moestrategy=auxReader[0];
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }  
        
        //get SD or ND
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                strategy=auxReader[0];
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        
        //get rMIp o not rMIP
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                if (auxReader[0].equalsIgnoreCase("true")){
                    rMIP_type=true;
                }else{
                    rMIP_type=false;
                }
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        
        //get WARM START o not WARM START
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                if (auxReader[0].equalsIgnoreCase("true")){
                    warm_start=true;
                }else{
                    warm_start=false;
                }
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        
        //Go to the next input line
        while(line != null){
            if (!line.contains("%")){
                break;
            }
            line=fileIn.readLine();
        }
        //Info Organs
        initialPlan = new TreatmentPlan(0,0,numAngles, numOrgans);
        initialPlan.warm_start = warm_start;
        
        while(line != null){
            if (!line.contains("%")){
                for (int y=0;y<numOrgans;y++){
                    String[] auxReader = line.split(sp);
                    initialPlan.getOrganos().put(y, new Organs(
                        auxReader[0], 
                        Integer.parseInt(auxReader[1]), 
                        Double.parseDouble(auxReader[2]),
                        Double.parseDouble(auxReader[3]),
                        Double.parseDouble(auxReader[4]),
                        Double.parseDouble(auxReader[5]),
                        Double.parseDouble(auxReader[6]),
                        Double.parseDouble(auxReader[7]),
                        Double.parseDouble(auxReader[8]), 
                        Integer.parseInt(auxReader[9]),
                        Integer.parseInt(auxReader[10]), 
                        Integer.parseInt(auxReader[11]), 
                        Boolean.parseBoolean(auxReader[12])));
                    line=fileIn.readLine();
                }
                break;
            }
            line=fileIn.readLine();
        }
        //get filepath
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                pathFile=auxReader[0];
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get option
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                option=Integer.parseInt(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get selectionCriterion (1 = LS, 2 = nextDescent, 3= gradientBas)
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                selectionCriterion=Integer.parseInt(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //MAICHOLL # VECINDADES
        tipoVNS=0;
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                tipoVNS=Integer.parseInt(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        vecindarios=1;
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                vecindarios=Integer.parseInt(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //MAICHOLL VECINDADES
        while(line != null){
            ordenVecindades = new int[vecindarios];
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                for(int i=0; i<vecindarios;i++){
                    ordenVecindades[i] = Integer.parseInt(auxReader[i]);
                }
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get stepsize
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                step=Integer.parseInt(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get iterations
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                iterations=Integer.parseInt(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get solver
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                solver=auxReader[0];
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
         //get MaxX Maicholl
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                maxX = Integer.parseInt(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        //get Parallel neighbour generation flag
           while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                parallelNeighbourhoodGeneration=Boolean.parseBoolean(auxReader[0]);
                line=fileIn.readLine();
                break;
            }
            line=fileIn.readLine();
        }
        
        //get Vx Index. % of the volume receiving more than x% of the prescribed dose
        Vx = new int[numOrgans][];
        while(line != null){
            if (!line.contains("%")){
                while(!line.contains("%")){
                    String[] auxReader = line.split(sp);
                    Vx[Integer.parseInt(auxReader[0])]=new int[auxReader.length-1];
                    for(int i=1;i<auxReader.length;i++){
                        Vx[Integer.parseInt(auxReader[0])][i-1]=Integer.parseInt(auxReader[i]);
                    }
                    line=fileIn.readLine();
                }
                break;
            }
            line=fileIn.readLine();
        }
        
        //get Dx Index. Minimum dose of the hottest x% of the volume
        Dx = new int[numOrgans][];
        while(line != null){
            if (!line.contains("%")){
                while(!line.contains("%")){
                    String[] auxReader = line.split(sp);
                    Dx[Integer.parseInt(auxReader[0])]=new int[auxReader.length-1];
                    for(int i=1;i<auxReader.length;i++){
                        Dx[Integer.parseInt(auxReader[0])][i-1]=Integer.parseInt(auxReader[i]);
                    }
                    line=fileIn.readLine();
                }
                break;
            }
            line=fileIn.readLine();
        }
        
        //get initial BACs
        initialBACs = new int[iterations][numAngles];
        
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                if (!auxReader[0].equals("none")){
                    randomInitial=false;
                    for (int i=0; i<iterations;i++){
                        for (int j=0; j<numAngles;j++){
                            initialBACs[i][j]= Integer.parseInt(auxReader[j]);
                        }
                        line=fileIn.readLine();
                        auxReader = line.split(sp);
                    }
                }else{
                    randomInitial = true;
                }
                break;
            }
            line=fileIn.readLine();
        }
        fileIn.close();
  }
  
    private void inicializarVars() {
        beamInfoDir = pathFile + "beamsInfo.txt";


               switch (selectionCriterion){
               case 1: //SteepestDescent
                   name = "LS";
                   break;
               case 2: //Next Descent
                   name = "nextDescent";
                   break;
               case 3: //2-opt
                   name = "2-opt";
                   break;
               case 4: //modified 2-Opt
                   name = "m2-opt";
                   break;
               case 5: //VNS
                   name = "VNS-";
                   String aux="";
                   for(int i=1;i<=ordenVecindades.length;i++){
                       if(ordenVecindades[i-1]==1) aux= "StepMove";
                       else if(ordenVecindades[i-1]==2) aux= "Random";
                       else if(ordenVecindades[i-1]==3) aux= "Random_geometricRest";
                       name=name+aux+"-";
                   }
                   vecindario=0;
                   minChange=0.02;
                   iterationsWithoutImprovement=1;
                   counterVNS=0;
                   break;
               case 6: //rVNS-Ann
                   name = "rVNS-Ann-";
                   break;
               case 7: //SA
                   name = "SA-";
                   break;
               case 8:
                   name = "rMIP-LS-";
                   break;
               case 9:
                   name = "nextDescent-rmip-";
                   break;
               case 10:
                   name = "Baned_MIP-LS-";
                   break;
           }

            /******************Start Iterations***************************
           * Each Iteration leads to a Local Optima                    * 
           *************************************************************/        

           switch (option){
               case 1: 
                   finalBACsFile = "./Results/logFunction/"+jobID+"singObj_"+name+"_finalBACs_LogFunction_"+numAngles+"Beams_step"+step+".txt";
                   break;
               case 2: 
                   finalBACsFile = "./Results/lexicoRectum/"+jobID+"singObj_"+name+"_finalBACs_LexicoR_"+numAngles+"Beams_step"+step+".txt";
                   break;
               case 3: 
                   finalBACsFile = "./Results/lexicoBladder/"+jobID+"singObj_"+name+"_finalBACs_LexicoB_"+numAngles+"Beams_step"+step+".txt";
                   break;
               case 4: 
                   finalBACsFile = "./Results/inverseFunction/"+jobID+"singObj_"+name+"_finalBACs_inverseFunction_"+numAngles+"Beams_step"+step+".txt";
                   break;
               case 8: 
                   finalBACsFile = "./Results/UlogFunction/"+jobID+"singObj_"+name+"_finalBACs_ULogFunctionFunction_"+numAngles+"Beams_step"+step+".txt";
                   break;   
               case 9: 
                   finalBACsFile = "./Results/cuadraticFunction/"+jobID+"singObj_"+name+"_finalBACs_LogFunction_"+numAngles+"Beams_step"+step+".txt";
                   break;
               case 10: 
                   finalBACsFile = "./Results/cuadraticFunction/"+jobID+"singObj_"+name+"_finalBACs_LogFunction_"+numAngles+"Beams_step"+step+".txt";
                   break;
           }
           switch (option){
                   case 1: 
                       bestFile = "./Results/logFunction/"+jobID+"singObj_"+name+"bestSolution_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       currentFile = "./Results/logFunction/"+jobID+"singObj_"+name+"generatedSolution_LogFunction"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       summaryDir = "./Results/logFunction/"+jobID+"singObj_"+name+"summary_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       break;
                   case 2: 
                       bestFile = "./Results/lexicoRectum/"+jobID+"singObj_"+name+"bestSolution_LexicoR_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       currentFile = "./Results/lexicoRectum/"+jobID+"singObj_"+name+"generatedSolution_LexicoR_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       summaryDir = "./Results/lexicoRectum/"+jobID+"singObj_"+name+"summary_LexicoR_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       break;
                   case 3: 
                       bestFile = "./Results/lexicoBladder/"+jobID+"singObj_"+name+"bestSolution_LexicoB_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       currentFile = "./Results/lexicoBladder/"+jobID+"singObj_"+name+"generatedSolution_LexicoB_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       summaryDir = "./Results/lexicoBladder/"+jobID+"singObj_"+name+"summary_LexicoB_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       break;
                   case 4: 
                       bestFile = "./Results/inverseFunction/"+jobID+"singObj_"+name+"bestSolution_inverseFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       currentFile = "./Results/inverseFunction/"+jobID+"singObj_"+name+"generatedSolution_inverseFunction"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       summaryDir = "./Results/inverseFunction/"+jobID+"singObj_"+name+"summary_inverseFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       break;
                   case 8: 
                       bestFile = "./Results/UlogFunction/"+jobID+"singObj_"+name+"bestSolution_UlogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       currentFile = "./Results/UlogFunction/"+jobID+"singObj_"+name+"generatedSolution_UlogFunction"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       summaryDir = "./Results/UlogFunction/"+jobID+"singObj_"+name+"summary_UlogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       break;
                   case 9: 
                       bestFile = "./Results/cuadraticFunction/"+jobID+"singObj_"+name+"bestSolution_cuadraticFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       currentFile = "./Results/cuadraticFunction/"+jobID+"singObj_"+name+"generatedSolution_cuadraticFunction"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       summaryDir = "./Results/cuadraticFunction/"+jobID+"singObj_"+name+"summary_cuadraticFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                       break;
               }
           neighbourhoodSize = numAngles * 2; //Two neighbours per beam angle; 
           
            switch (option){
            case 1: 
                bestFile = "./Results/logFunction/"+jobID+"singObj_"+name+"bestSolution_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
            case 2: 
                bestFile = "./Results/lexicoRectum/"+jobID+"singObj_"+name+"bestSolution_LexicoR_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
            case 3: 
                bestFile = "./Results/lexicoBladder/"+jobID+"singObj_"+name+"bestSolution_LexicoB_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
            case 4: 
                bestFile = "./Results/inverseFunction/"+jobID+"singObj_"+name+"bestSolution_inverseFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
            case 8: 
                bestFile = "./Results/UlogFunction/"+jobID+"singObj_"+name+"bestSolution_UlogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
            case 9: 
                bestFile = "./Results/cuadraticFunction/"+jobID+"singObj_"+name+"bestSolution_cuadraticFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
        }
           
    }
    
    public int repairAngle(int angle){
        if(angle<0){
            angle = 360+angle;
        }else if(angle>=360){
            angle = angle-360;
        }
        return angle;
    }
    
    public int[][] getNeigboursAllSize_mip_manual(TreatmentPlan sol, int ns, ArrayList<Integer> angulos_sels,int[] previous_angles, int indexBAC){
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0, angle;        
        for (int j = 0; j<numAngles;j++){
            
            for (int i = 0; i<numAngles;i++){
                auxAngles[neighbour][i]=previous_angles[i];
                auxAngles[neighbour+1][i]=previous_angles[i];
            }
            angle=(int)auxAngles[neighbour][j];
            
            if(angulos_sels.size()>=1)
                auxAngles[neighbour][indexBAC] = angulos_sels.removeFirst();
            if(angulos_sels.size()>=1)
                auxAngles[neighbour+1][indexBAC] = angulos_sels.removeFirst();
            
            
            neighbour = neighbour+2;
        }
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    public int[][] ChooseNeigbours_mip_manual(ArrayList<Integer> angulos_sels, TreatmentPlan sol, int ns, int[] previous_angles, int indexBAC) throws IOException{ //Maicholl Agregado para VNS
        //Crear los vecinos quitando angulos de angulos_sels 
        int[][] auxAngles= new int[ns][numAngles];
        HashMap <Integer, Double> mip_relaxed = null;
        List<Integer> targetList = null;
        double totalmip = 0;
        
       
        auxAngles = getNeigboursAllSize_mip_manual(sol, ns,angulos_sels, previous_angles, indexBAC); // Ángulo +-5
          
        return auxAngles;
    }
    
    public int [] shake(int [] toshake){
        Random r = new Random(); 
        int temp;
        for (int i = 0; i < toshake.length; i++)
        {
            int swap = r.nextInt(toshake.length);
            temp = toshake[swap];
            toshake[swap] = toshake[i];
            toshake[i] = temp;

            
        }
        return toshake;
    }
    
    public class getNeighbour implements Callable<TreatmentPlan> {
    private int[] auxAngles;
    private TreatmentPlan sol;
    private final String cFile;
    private final String processName;
    private Organs[] org;
  
    public getNeighbour(int[] auxAng, TreatmentPlan currentSol, String cF, String neighbourNum){
        Thread.currentThread().setName(neighbourNum);
        this.processName = jobID + "_" + Thread.currentThread().getName();
        System.out.println("Generating Neighbour Num: " + processName);
        auxAngles = new int[auxAng.length];
        System.arraycopy(auxAng, 0, auxAngles, 0, auxAngles.length);
        sol = new TreatmentPlan(currentSol);
        cFile = cF;
    }
    
    @Override
    public TreatmentPlan call() throws Exception {
        int angle;    
        TreatmentPlan neighbour = new TreatmentPlan(sol);
                
        for (int j = 0; j<auxAngles.length;j++){
            neighbour.selAngles[j] = auxAngles[j];
        }
        synchronized(neighbour){
            neighbour.generateDDM(pathFile);
            for(int ind: neighbour.getOrganos().keySet()){
                    Organs organos = neighbour.getOrganos().get(ind);
                    organos.writeMapVoxel(this.processName,neighbour.selAngles);
                }
            //generateDDM(neighbour, this.processName);
        }
       
        neighbour.generateReferencePoint(sol.getOrganos(), 0, solver, maxX, 1, this.processName);
        
        //neighbour.getVxDx(this.processName, this.org, Vx, Dx);
        //synchronized(visitedBACs){
            //addNewVisitedBAC(neighbour); //Add Angles and Reference Point
        //    generatedBACs.add(neighbour);
        //}
        totalObjFuncEval++;
        
        neighbour.printSol(cFile);        
         
        return neighbour;
    }
}
    
    public TreatmentPlan[] generateNeighbourhood_mip_manual( TreatmentPlan sol, 
            int ns, int[][] bs, String cFile, int iteration, ArrayList<Integer> angulos_doc, 
            int[] previous_angles, int indexBAC, ArrayList<TreatmentPlan> bac_explored) throws IOException, InterruptedException, ExecutionException{
        int angle;
        int[][] auxAngles = new int[ns][numAngles];
            TreatmentPlan[] nhood = new TreatmentPlan [ns];
        System.out.println("====================================" );
        System.out.println("GENERATING NEIGHBOURHOOD FOR BAC: " );
        System.out.println("====================================" );
        for (int j = 0; j<numAngles;j++){
            System.out.print(sol.selAngles[j] + " - " );
        }
        System.out.println();
        /**********Calculate BAC to be Visited**********
        * We generate (2 * #beams) neighbours of our   *
        * current solution. Each beam angle is modified*
        * in +/- "step" degrees.                       *
        ***********************************************/
       
        auxAngles=ChooseNeigbours_mip_manual(angulos_doc,sol,ns,previous_angles,indexBAC); 
        
        ArrayList<Integer> repited_auxAngles = new ArrayList<>();
        ArrayList<Integer> repited_bac_explored = new ArrayList<>();
        if(generatedBACs.size()>0){
            for(int i=0;i<auxAngles.length;i++){
                for(int j=0;j<generatedBACs.size();j++){
                    if(Arrays.equals(auxAngles[i], generatedBACs.get(j).selAngles)){
                        repited_auxAngles.addLast(i);
                        repited_bac_explored.addLast(j);
                        break;
                    }
                }
            }
        }
        /**********Neighbours Evaluation************
        * Evaluate neighbours that must be visited *
        *******************************************/
        if (parallelNeighbourhoodGeneration){
            //getNeighbour[] gNeighbour = new getNeighbour[ns]; //Callables
            Future<TreatmentPlan>[] ft = new Future[ns]; //Callables
            ExecutorService pool = Executors.newFixedThreadPool(ns);
            //create Callables and FutureTasks
            //CountDownLatch latch = new CountDownLatch(ns);
            for (int i = 0; i<ns;i++){
                
                if(!repited_auxAngles.contains(i)){
                    Callable<TreatmentPlan> gNeighbour = new MipManual.getNeighbour(auxAngles[i], sol, cFile, Integer.toString(i+1));
                    ft[i] = new FutureTask<>(gNeighbour);
                    ft[i] = pool.submit(gNeighbour);
                }else{
                    System.out.println("Ya resuelto" );
                }
            }
            pool.shutdown();
            try {
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                for (int i = 0; i<ft.length;i++){
                    if(!repited_auxAngles.contains(i)){
                        nhood[i] = new TreatmentPlan(ft[i].get()); 
                        generatedBACs.addLast(new TreatmentPlan(nhood[i]));  
                    }
                }
                for(int i = 0;i<repited_auxAngles.size();i++){
                    nhood[repited_auxAngles.get(i)] = new TreatmentPlan(generatedBACs.get(repited_bac_explored.get(i)));
                }
            } catch (InterruptedException e) {
                //latch.await(); // Wait for countdown
                
            }
            
        }else{
            int[] auxGenerated = new int[ns];
            Random r = new Random(); 
            for (int i = 0; i<ns;i++){
             auxGenerated[i] = i;
            }
            auxGenerated = shake(auxGenerated);
            for (int i = 0; i<ns;i++){
                nhood[i] = new TreatmentPlan(sol);
            }
            for (int k = 0; k<ns;k++){
                int i = auxGenerated[k];
                /**********Setting Up neighbours************
                *******************************************/
                
               
                for (int j = 0; j<numAngles;j++){
                    nhood[i].selAngles[j] = (int) auxAngles[i][j];
                }
                int index_rep = -1;
                for(int y=0;y<repited_auxAngles.size();y++){
                    if(Arrays.equals((generatedBACs.get(repited_bac_explored.get(y))).selAngles, nhood[i].selAngles)){
                        index_rep = y;
                        break;
                    }
                }
                if(index_rep == -1){
                
                
                    nhood[i].generateDDM(pathFile);
                    for(int ind: nhood[i].getOrganos().keySet()){
                        Organs organos = nhood[i].getOrganos().get(ind);
                        organos.writeMapVoxel(jobID,nhood[i].selAngles);
                    }
                    nhood[i].generateReferencePoint(sol.getOrganos(), 0, solver, maxX, 1, jobID);

                                      
                    totalObjFuncEval++;

                    nhood[i].printSol( cFile);
                    nhood[i].liberateMemory();
                    generatedBACs.addLast(new TreatmentPlan(nhood[i]));  
                    //bac_explored.add(new TreatmentPlan(nhood[i]));
                }else{
                    System.out.println("Ya resuelto" );
                    nhood[i] = new TreatmentPlan(generatedBACs.get(repited_bac_explored.get(index_rep)));
                    
                }
                //copyDVHs(o,iteration,i); //Maicholl archivo
                
                /*Only for NEXT DESCENT Local Search*/
                //if ((name=="nextDescent" || selectionCriterion==5) && nhood[i].getSingleObjectiveValue()<sol.getSingleObjectiveValue()){ //&& sol.singleObjectiveValue-nhood[i].singleObjectiveValue>minChange){//Maicholl
                if (strategy.compareToIgnoreCase("ND")==0 && nhood[i].getSingleObjectiveValue()<sol.getSingleObjectiveValue()){
                    System.out.println("Next Descent");
                    break; //return a neighbourhood with only one solution better than the current solution
                }
                
            }
        }
        
        System.out.println("*****************************************" );
        System.out.println("*    NEIGHBOURHOOD HAS BEEN GENERATED   *" );
        System.out.println("*****************************************" );
        return nhood;
    }
    
    public TreatmentPlan generateNeighbourhood_rMIP_fixed( TreatmentPlan sol, 
            int ns, int[][] bs, String cFile, int iteration, int[] all_ang, int ang_sel, int ind_sel) throws IOException, InterruptedException, ExecutionException{
        int[] fixed;
        int angle;
        int[][] auxAngles = new int[ns][numAngles];
            TreatmentPlan[] nhood = new TreatmentPlan [ns];
        System.out.println("====================================" );
        System.out.println("SOLVING NEIGHBOUR FOR BAC: " );
        System.out.println("====================================" );
        for (int j = 0; j<numAngles;j++){
            System.out.print(sol.selAngles[j] + " - " );
        }
        System.out.println();
        /**********Calculate BAC to be Visited**********
        * We generate (2 * #beams) neighbours of our   *
        * current solution. Each beam angle is modified*
        * in +/- "step" degrees.                       *
        ***********************************************/
       
        //auxAngles=ChooseNeigbours_mip_manual(angulos_doc,sol,ns,previous_angles,indexBAC); 
        
        
        /**********Neighbours Evaluation************
        * Evaluate neighbours that must be visited *
        *******************************************/
        int aux_cont=0;
        fixed = new int[4];
        for(int i=0;i<sol.selAngles.length;i++){
            if(i!=ind_sel){
                fixed[aux_cont] = sol.selAngles[i];
                aux_cont++;
            }
        }
        Arrays.sort(fixed);
                
        boolean is_fixed_before = false;
        for(int[] ar:generatedfixed){
        
            Arrays.sort(ar);
            if(Arrays.equals(ar, fixed)){
                is_fixed_before = true;
                break;
            }
        }
        TreatmentPlan aux_sol;
        
        if(!is_fixed_before){
            /**********Setting Up neighbours************
            *******************************************/
            generatedfixed.add(fixed);
            sol.generateDDM(pathFile, all_ang);
            for(int ind: sol.getOrganos().keySet()){
                Organs organos = sol.getOrganos().get(ind);
                organos.writeMapVoxel(jobID,all_ang);
            }
            ampl = sol.generateReferencePoint(sol.getOrganos(), 0, solver, maxX, 1, jobID, all_ang,  fixed, ampl,"");

            aux_sol = new TreatmentPlan(currentSol);
            for (int i = 0; i<aux_sol.selAngles.length;i++){
                if(i <aux_sol.selAngles.length-1){
                    aux_sol.selAngles[i] = fixed[i]; 
                }else{
                    aux_sol.selAngles[i] = (int)(sol.beams_vector[4][0]*5); 
                }
            }
            bubbleSort(aux_sol.selAngles);
            boolean repited = false;
            if(generatedBACs.size()>0){
                for(int j=0;j<generatedBACs.size();j++){
                    if(Arrays.equals(aux_sol.selAngles, generatedBACs.get(j).selAngles)){
                        aux_sol = new TreatmentPlan(generatedBACs.get(j));
                        repited = true;
                        break;
                    }
                }

            }
            if(!repited){
                aux_sol.generateDDM(pathFile);
                for(int ind: aux_sol.getOrganos().keySet()){
                    Organs organos = aux_sol.getOrganos().get(ind);
                    organos.writeMapVoxel(jobID,aux_sol.selAngles);
                }
                aux_sol.generateReferencePoint(aux_sol.getOrganos(), 0, solver, maxX, 1, jobID);
                aux_sol.printSol( cFile);
                aux_sol.liberateMemory();
                generatedBACs.addLast(new TreatmentPlan(aux_sol)); 
            }
        }else{
            aux_sol = new TreatmentPlan(sol);
        }
        totalObjFuncEval++;

         
        //bac_explored.add(new TreatmentPlan(nhood[i]));

        //copyDVHs(o,iteration,i); //Maicholl archivo
    
        
        System.out.println("*****************************************" );
        System.out.println("*    NEIGHBOURHOOD HAS BEEN GENERATED   *" );
        System.out.println("*****************************************" );
        return aux_sol;
    }
    
    public TreatmentPlan getBestNeighbour(int iteration,TreatmentPlan[] nhood, int ns) throws IOException{
        double bestValue= 1000000000;
        int bestIndex= -1;
        for (int i = 0; i<ns;i++){
         
            if (nhood[i] != null){
                if (nhood[i].getSingleObjectiveValue()<bestValue && nhood[i].getSingleObjectiveValue()>0){
                    bestValue = nhood[i].getSingleObjectiveValue();
                    bestIndex = i;
                }
            }
            
        }
        //nhood[bestIndex].createDVH(jobID, name);
        if(warm_start){
            nhood[bestIndex].useThisSolution();
            nhood[bestIndex].setPrevAngles();
        }
        return nhood[bestIndex];
    }
    
    public void printSummary(String dirFile, long localTimer, String iter, TreatmentPlan currentSol) throws IOException{
        BufferedWriter bwFile = null ;
        File summaryFile = new File(dirFile);
          if (summaryFile.exists()) {
              bwFile = new BufferedWriter(new FileWriter(summaryFile, true));
          }else{
              bwFile = new BufferedWriter(new FileWriter(summaryFile));
          }
          writeLine("############## ITER " +iter+ "##############\n",bwFile);
          writeLine("Total Obj. Func. Eval.      :\t"+totalObjFuncEval+"\n",bwFile);
          writeLine("Total Time (secs)           :\t"+(System.currentTimeMillis()- localTimer)/1000+"\n",bwFile);
          writeLine("Total Objective Function Evaluations (Cummulative):\t"+globalObjFuncEval+"\n",bwFile);
          writeLine("Total Time (cummulative):\t"+(System.currentTimeMillis()- globalTimer)+"\n",bwFile);
          //writeLine("Counter:\t"+counter[0]+ "\t" +counter[1]+ "\t" +counter[2]+ "\t" 
          //     +counter[3]+ "\t" +counter[4]+ "\t" +counter[5]+ "\n",bwFile);
          bwFile.close();
    }  
    
    public void printSolution(Long localTimer, TreatmentPlan sol){
        System.out.println ("Local Optima Found");
        System.out.println("Final Time: " + (System.currentTimeMillis()-localTimer)/1000);
        System.out.println("Best Obj Function Value: " + sol.getSingleObjectiveValue());
        System.out.print("Beam Angle Configuration : ");
        for (int i = 0; i<numAngles;i++){
            System.out.print(" " + sol.selAngles[i] + " - "  );
        }
        System.out.println();
        System.out.print("gEUD Values : ");
        for (int i = 0; i<numOrgans;i++){
            System.out.print(" " + sol.getgEUD()[i] + " - "  );
        }
        System.out.print("\n"  );
        globalObjFuncEval = globalObjFuncEval + totalObjFuncEval;
        try {
            printSummary(summaryDir, localTimer, Integer.toString(globalIter) , sol);
        } catch (IOException ex) {
            Logger.getLogger(BAO2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void printCurrent(TreatmentPlan sol) {
        System.out.println("New Best Sol Found : " + sol.getSingleObjectiveValue());
        System.out.print("Beam Angle Configuration : ");
        for (int i = 0; i<numAngles;i++){
            System.out.print(" " + sol.selAngles[i] + " - "  );
        }
        System.out.println();
        System.out.print("gEUD Values : ");
        for (int i = 0; i<numOrgans;i++){
            System.out.print(" " + sol.getgEUD()[i] + " - "  );
        }
        System.out.println();
    }

    public void run() throws IOException, InterruptedException, ExecutionException{
           

        /*************************************************************  
        * Create CheckFile. Allows us to shut down the algorithm     *
        * ***********************************************************/
        BufferedWriter bwCheckFile=null;
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        if (checkFile.exists()){
            bwCheckFile = new BufferedWriter(new FileWriter(checkFile, true));
            writeLine("Remove this file if you want the process to be stopped\n" , bwCheckFile);
            bwCheckFile.close();
        }else{
            bwCheckFile = new BufferedWriter(new FileWriter(checkFile));
            writeLine("Remove this file if you want the process to be stopped\n" , bwCheckFile);
            bwCheckFile.close();
        }
        globalObjFuncEval = 0;
        globalTimer = System.currentTimeMillis();
        String input="./Pruebas/inputFile_"+jobID+".txt";
        TreatmentPlan localSol = null;
        
        visitedBACs = new ArrayList<>(); //Those BACs whose neighbourhood has been generated
        
        for(globalIter = 0; globalIter<iterations; globalIter++){
            generatedBACs = new ArrayList<>(); //Those BACs that have been generated    
            inicializarVars(); 
            
            totalObjFuncEval=0;
            initialPlan.setAngles(initialBACs[globalIter]);
            
            initialPlan.beams = numAngles;
            initialPlan.loadNumBixels(beamInfoDir);
            initialPlan.cargarBeams(pathFile);

            initialPlan.iniorgansbeams(initialPlan.selAngles, pathFile, jobID);
            
                initialPlan.generateDDM(pathFile);
                if(selectionCriterion == -1){
                    break;
                }
                initialPlan.generateReferencePoint(initialPlan.getOrganos(),ordenVecindades[0],solver,maxX,1,jobID);
                initialPlan.liberateMemory();
                if(warm_start){
                    initialPlan.useThisSolution();
                    initialPlan.setPrevAngles();
                }
            
            generatedBACs.addLast(initialPlan);
            
            if(problemType.compareTo("SO") == 0){
                if(selectionCriterion < 5 ){
                    
                    //localSol= prev_vnd(initialPlan);
                    ArrayList<Integer[]> bac_explored = new ArrayList<>();
                    //localSol= manual_FMO(initialPlan, 0, bac_explored);
                    localSol = seq_sel_LS(initialPlan);
                }else if(selectionCriterion == 5 ){
                    
                    localSol = ite_fixed_rMIP_LS(initialPlan);
                }
            }
            //localOptima.add(new TreatmentPlan(localSol));
            //localOptima.get(globalIter).printSol(iterationsCounter, totalObjFuncEval, ordenVecindades[vecindario], finalBACsFile);
            localSol.printSol(iterationsCounter, totalObjFuncEval, ordenVecindades[vecindario], finalBACsFile);
            if (!checkFile.exists()) {
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
                break;
            }
            localSol = null;
        }
        System.out.println("Total Objective Function Evaluations: "+globalObjFuncEval+"\n");
        System.out.println("Total Time (secs): "+(System.currentTimeMillis()-globalTimer)/1000+"\n");
    }
    
    public TreatmentPlan manual_FMO(TreatmentPlan ini, int indexBAC, int ind_sel) throws InterruptedException, ExecutionException, IOException {
        ArrayList<TreatmentPlan> prev_BACs = new ArrayList<>();
        
        
        int[] previous_angles = new int[numAngles];
        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;  
        ArrayList<Integer> angulos_sel = new ArrayList<>();
        for(int p=0;p<72;p++){
            /*if(p==16){
                angulos_sel.add(5);
            }*/
            angulos_sel.add(p*5);
        }
        currentSol = new TreatmentPlan(ini);
        for(int t=0;t<numAngles;t++){
            previous_angles[t] = currentSol.selAngles[t];
            int index = angulos_sel.indexOf(previous_angles[t]);
            if(index != -1)
                angulos_sel.remove(index);
        }
        while (!endCriterion){
            TreatmentPlan[] neighbourhood;
            
            
            neighbourhood = generateNeighbourhood_mip_manual(currentSol, neighbourhoodSize, 
                    beamletSet, currentFile,iterationsCounter, angulos_sel, previous_angles,ind_sel, prev_BACs);
            
            
            bestNeighbour = new TreatmentPlan(getBestNeighbour(iterationsCounter,neighbourhood,neighbourhoodSize));
            
            if (bestNeighbour.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(bestNeighbour);
                for(TreatmentPlan ns:neighbourhood){
                    currentSol.copyAngle(ns, ns.selAngles);

                }
                printCurrent(currentSol);
                try {
                    currentSol.printSol(bestFile,indexBAC);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }         
            }
            if(angulos_sel.size()<1)
                endCriterion=true;
            bestNeighbour = null;
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
            iterationsCounter++;
        }
        printSolution(localTimer, currentSol);
        
        return currentSol;
    }
    

    
    public TreatmentPlan rMIP_fixed(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        ArrayList<TreatmentPlan> prev_BACs = new ArrayList<>();
        int[] fixed = new int[]{70,140,210,280};
        int[] previous_angles = new int[numAngles];
        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;  
        ArrayList<Integer> angulos_sel = new ArrayList<>();
        
        
        currentSol = new TreatmentPlan(ini);
        for(int t=0;t<numAngles;t++){
            previous_angles[t] = currentSol.selAngles[t];
            int index = angulos_sel.indexOf(previous_angles[t]);
            if(index != -1)
                angulos_sel.remove(index);
        }
        while (!endCriterion){
            TreatmentPlan[] neighbourhood;
            
            neighbourhood = null;
           // neighbourhood = generateNeighbourhood_rMIP_fixed(currentSol, neighbourhoodSize, 
                   // beamletSet, currentFile,iterationsCounter, angulos_sel, previous_angles, prev_BACs, fixed);
            
            
            bestNeighbour = new TreatmentPlan(getBestNeighbour(iterationsCounter,neighbourhood,neighbourhoodSize));
            
            if (bestNeighbour.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(bestNeighbour);
                for(TreatmentPlan ns:neighbourhood){
                    currentSol.copyAngle(ns, ns.selAngles);

                }
                printCurrent(currentSol);
                try {
                    currentSol.printSol(bestFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }         
            }
            if(angulos_sel.size()<1)
                endCriterion=true;
            bestNeighbour = null;
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
            iterationsCounter++;
        }
        printSolution(localTimer, currentSol);
        
        return currentSol;
    }
    
    public TreatmentPlan seq_sel_LS(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        ArrayList<Integer[]> bac_explored = new ArrayList<>();
        TreatmentPlan currentSol;
        TreatmentPlan iterSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        int tipomov =0;
        currentSol = new TreatmentPlan(ini);
        iterSol = new TreatmentPlan(ini);
        int prevBAC[];
        prevBAC = new int[numAngles];
        for(int h=0;h<numAngles;h++){
            prevBAC[h]=0;
        }
        while (!endCriterion){
            System.arraycopy(currentSol.selAngles, 0, prevBAC, 0, numAngles);
            ArrayList<Integer> angletochange = new ArrayList<>();
            for(int f = 0; f < iterSol.selAngles.length; f++){
                angletochange.add(iterSol.selAngles[f]);
            }
            Collections.shuffle(angletochange, new Random());
            
            for(int z=0;z<numAngles;z++){
                int selected_z = angletochange.removeFirst();
                int ind_sel = -1;
                for(int i = 0; i < iterSol.selAngles.length; i++){
                    if(iterSol.selAngles[i] == selected_z){
                        ind_sel = i;
                    }

                }
                bestNeighbour = manual_FMO(iterSol, z, ind_sel);
                if (bestNeighbour.getSingleObjectiveValue()<iterSol.getSingleObjectiveValue()){
                    iterSol = new TreatmentPlan(bestNeighbour);
                }
            }
            
            if (iterSol.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(iterSol);
                
            }else{
                endCriterion = true;
            }
            
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
        }
        printSolution(localTimer, currentSol);
        
        return currentSol;
    }
    
    public TreatmentPlan ite_fixed_rMIP_LS(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        generatedfixed = new ArrayList<>();
        ArrayList<Integer[]> bac_explored = new ArrayList<>();
        //TreatmentPlan currentSol;
        TreatmentPlan iterSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        int tipomov =0;
        currentSol = new TreatmentPlan(ini);
        iterSol = new TreatmentPlan(ini);
        int prevBAC[];
        prevBAC = new int[numAngles];
        for(int h=0;h<numAngles;h++){
            prevBAC[h]=0;
        }
        int[] angulos_sel = new int[72];
        for(int p=0;p<72;p++){
            angulos_sel[p] = (p*5);
        }
        
        while (!endCriterion){
            System.arraycopy(currentSol.selAngles, 0, prevBAC, 0, numAngles);
            ArrayList<Integer> angletochange = new ArrayList<>();
            for(int f = 0; f < iterSol.selAngles.length; f++){
                angletochange.add(iterSol.selAngles[f]);
            }
            Collections.shuffle(angletochange, new Random());
            
            for(int z=0;z<numAngles;z++){
                int selected_z = angletochange.removeFirst();
                int ind_sel = -1;
                for(int i = 0; i < iterSol.selAngles.length; i++){
                    if(iterSol.selAngles[i] == selected_z){
                        ind_sel = i;
                    }

                }
                System.out.println("################################################");
                System.out.println("# Changing the beam angle "+ind_sel+" #");
                System.out.println("################################################");
                //bestNeighbour = manual_FMO(iterSol, z, bac_explored);
                bestNeighbour = generateNeighbourhood_rMIP_fixed(iterSol, neighbourhoodSize, 
                    beamletSet, currentFile,iterationsCounter, angulos_sel, z, ind_sel);
                if (bestNeighbour.getSingleObjectiveValue()<iterSol.getSingleObjectiveValue()){
                    iterSol = new TreatmentPlan(bestNeighbour);
                    
                    printCurrent(iterSol);
                    try {
                        iterSol.printSol(bestFile,z);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }        
                }
            }
            
            if (iterSol.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(iterSol);
                printCurrent(currentSol);
                try {
                    currentSol.printSol(bestFile,0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }    
            }else{
                endCriterion = true;
            }
            
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
        }
        printSolution(localTimer, currentSol);
        ampl.close();
        ampl = null;
        return currentSol;
    }
    
    
    public void main_mip() throws IOException, InterruptedException, ExecutionException{
         /*************************************************************  
        * Create CheckFile. Allows us to shut down the algorithm     *
        * ***********************************************************/
        BufferedWriter bwCheckFile=null;
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        if (checkFile.exists()){
            bwCheckFile = new BufferedWriter(new FileWriter(checkFile, true));
            writeLine("Remove this file if you want the process to be stopped\n" , bwCheckFile);
            bwCheckFile.close();
        }else{
            bwCheckFile = new BufferedWriter(new FileWriter(checkFile));
            writeLine("Remove this file if you want the process to be stopped\n" , bwCheckFile);
            bwCheckFile.close();
        }
        globalObjFuncEval = 0;
        globalTimer = System.currentTimeMillis();
        String input="./Pruebas/inputFile_"+jobID+".txt";
        TreatmentPlan localSol = null;
        
        for(globalIter = 0; globalIter<iterations; globalIter++){
            inicializarVars(); 
            
            totalObjFuncEval=0;
            initialPlan.setAngles(initialBACs[globalIter]);
            initialPlan.beams = numAngles;
            initialPlan.loadNumBixels(beamInfoDir);
            initialPlan.cargarBeams(pathFile);

            initialPlan.iniorgansbeams(initialPlan.selAngles, pathFile, jobID);
            initialPlan.generateDDM(pathFile);
            if(selectionCriterion == -1){
                break;
            }
            initialPlan.generateReferencePoint(initialPlan.getOrganos(),ordenVecindades[0],solver,maxX,1,jobID);
            initialPlan.liberateMemory();
            if(warm_start){
                initialPlan.useThisSolution();
                initialPlan.setPrevAngles();
            }
            if(problemType.compareTo("SO") == 0){
                if(selectionCriterion < 5 ){
                    //localSol= prev_vnd(initialPlan);
                }
            }
            //localOptima.add(new TreatmentPlan(localSol));
            //localOptima.get(globalIter).printSol(iterationsCounter, totalObjFuncEval, ordenVecindades[vecindario], finalBACsFile);
            localSol.printSol(iterationsCounter, totalObjFuncEval, ordenVecindades[vecindario], finalBACsFile);
            if (!checkFile.exists()) {
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
                break;
            }
            localSol = null;
        }
        System.out.println("Total Objective Function Evaluations: "+globalObjFuncEval+"\n");
        System.out.println("Total Time (secs): "+(System.currentTimeMillis()-globalTimer)/1000+"\n");
    }
}
