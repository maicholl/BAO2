/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bao2;
import static bao2.BAO2.auxActualTimer;
import static bao2.BAO2.initTimer;
import com.ampl.AMPL;
import imrt2.Organs;
import imrt2.TreatmentPlan;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 *
 * @author Maicholl
 */
public class BAO2 { 
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
    public String timerFile="";
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
    public int rMIP_number_allow; // rMIP Int MAICHOLL
    
    public boolean warm_start; // warm-start Boolean MAICHOLL NZ
    
    public boolean geom_rest; // warm-start Boolean MAICHOLL NZ
    
    public HashMap <Integer, Double> mip_relaxed = null;
    public List<Integer> targetList = null;
    public double totalmip = 0;
    
    public BAO2(){
        localTimer = System.currentTimeMillis();
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
    
    /**
     * @param args the command line arguments
     */


    
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
        while(line != null){
            if (!line.contains("%")){
                String[] auxReader = line.split(sp);
                rMIP_number_allow=Integer.parseInt(auxReader[0]);
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
  
    public static void writeLine(String l, BufferedWriter bw) throws IOException{	
        bw.write(l);
    }
    
    // bubbleSort of an 1D array
    public static void bubbleSort(int[] arr) {  
       int n = arr.length; 
       int i, j, temp;
        boolean swapped;
        for (i = 0; i < n - 1; i++)
        {
            swapped = false;
            for (j = 0; j < n - i - 1; j++)
            {
                if (arr[j] > arr[j + 1])
                {
                    // swap arr[j] and arr[j+1]
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
 
            // IF no two elements were
            // swapped by inner loop, then break
            if (swapped == false)
                break;
        }
    }
    
    // getNeigboursRVNS Neighbors are chosen randomly and equally from the neighborhoods 
    public int[][] getNeigboursRVNS(TreatmentPlan sol, int ns){
        ArrayList<int[][]> all_neighbourhoods = new ArrayList<>();
        int[][] auxAngles= new int[ns][numAngles];
        int changeBeam = 0, angle;
        double porc = (double)1/vecindarios;
        Random rand = new Random();
        // Create all neighbourhoods and shuffle neighbours
        for(int k = 0; k < vecindarios; k++){
            try {
                int[][] aux_array = ChooseNeigbours(ordenVecindades[k],sol,ns);
                List<int[]> asList = Arrays.asList(aux_array);
                Collections.shuffle(asList);
                aux_array = asList.toArray(new int[0][0]);
                all_neighbourhoods.add(aux_array);
            } catch (IOException ex) {
                Logger.getLogger(BAO2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Fills the neighborhood to use
        int type_neighbour = 0;
        for(int i = 0; i < ns; i++){
            if(type_neighbour == vecindarios) type_neighbour = 0;
            for(int j = 0; j < numAngles; j++){
                auxAngles[i][j] = all_neighbourhoods.get(type_neighbour)[i][j];
            }
            type_neighbour++;
        }
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    // Previous rVNS neighbourhood definition
    public int[][] getNeigboursReducedVNS(TreatmentPlan sol, int ns){
        int[][] auxAngles= new int[ns][numAngles];
        int changeBeam = 0, angle;
        double porc = (double)1/vecindarios;
        Random rand = new Random();
        for (int j = 0; j<ns;j++){
            for (int i = 0; i<numAngles;i++){
                auxAngles[j][i]=sol.selAngles[i];
            }
            if(changeBeam % numAngles == 0){
                changeBeam = 0 ;
            }
            angle=(int)auxAngles[j][changeBeam];
            int auxangle1 = angle;
            int ocurrencia1 = -1;
            if (rand.nextDouble()<porc){ // corresponde generar vecinos +/-5     
                while(ocurrencia1 == -1){
                    ocurrencia1 = 0;
                    auxangle1 = rand.nextInt(360/step)*step;
                    for (int i = 0; i<numAngles;i++){
                        if(i != changeBeam){
                            if(auxAngles[j][i] == auxangle1){
                                ocurrencia1++;
                            }
                        }
                    }
                    if(ocurrencia1 != 0)
                        ocurrencia1 = -1;
                }
            }else{
                while(ocurrencia1 == -1){
                    ocurrencia1 = 0;
                    if (rand.nextDouble()<porc){
                        auxangle1 = repairAngle(auxangle1-step);
                    }else{
                        auxangle1 = repairAngle(auxangle1+step);
                    }
                    for (int i = 0; i<numAngles;i++){
                        if(i != changeBeam){
                            if(auxAngles[j][i] == auxangle1){
                                ocurrencia1++;
                            }
                        }
                    }
                    if(ocurrencia1 != 0)
                        ocurrencia1 = -1;
                }
            }
            
            
            auxAngles[j][changeBeam]=auxangle1;
            
            changeBeam++;
            
        }       
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    // +-5 Neighbourhood Definition
    public int[][] getNeigboursBasedOnStepSize(TreatmentPlan sol, int ns){
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0, angle;        
        for (int j = 0; j<numAngles;j++){
            for (int i = 0; i<numAngles;i++){
                auxAngles[neighbour][i]=sol.selAngles[i];
                auxAngles[neighbour+1][i]=sol.selAngles[i];
            }
            angle=(int)auxAngles[neighbour][j];
            int auxangle1 = angle;
            int ocurrencia1 = -1;
            int auxangle2 = angle;
            int ocurrencia2 = -1;
            
            // We make sure it is not repeated
            while(ocurrencia1 == -1){
                ocurrencia1 = 0;
                auxangle1 = repairAngle(auxangle1-step);
                for (int i = 0; i<numAngles;i++){
                    if(i != j){
                        if(auxAngles[neighbour][i] == auxangle1){
                            ocurrencia1++;
                        }
                    }
                }
                if(ocurrencia1 != 0)
                    ocurrencia1 = -1;
            }
            // We make sure it is not repeated
            while(ocurrencia2 == -1){
                ocurrencia2 = 0;
                auxangle2 = repairAngle(auxangle2+step);
                for (int i = 0; i<numAngles;i++){
                    if(i != j){
                        if(auxAngles[neighbour+1][i] == auxangle2){
                            ocurrencia2++;
                        }
                    }
                }
                if(ocurrencia2 != 0)
                    ocurrencia2 = -1;
            }
            
            auxAngles[neighbour][j]=auxangle1;
            auxAngles[neighbour+1][j]=auxangle2;
            
            neighbour = neighbour+2;
        }
        // Sort
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    // Ensure angles between [0,355]
    public int repairAngle(int angle){
        if(angle<0){
            angle = 360+angle;
        }else if(angle>=360){
            angle = angle-360;
        }
        return angle;
    }
    
    // +-5 Neighbourhood Definition using Banned angles from rMIP
    public int[][] getNeigbours5StepListedMIP(TreatmentPlan sol, int ns, List<Integer> anglePrefer){
        ArrayList<Integer> actual_bac = new ArrayList<Integer>(); // Create an ArrayList object
        for(int beam_angle:sol.selAngles){
            actual_bac.add(beam_angle);
        }
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0, angle;        
        for (int j = 0; j<numAngles;j++){
            for (int i = 0; i<numAngles;i++){
                auxAngles[neighbour][i]=sol.selAngles[i];
                auxAngles[neighbour+1][i]=sol.selAngles[i];
            }
            angle=(int)auxAngles[neighbour][j];
            auxAngles[neighbour][j] = repairAngle(angle-step);
            auxAngles[neighbour+1][j] = repairAngle(angle+step);
            
            // If the angle repeats or is not in the list, replace it
            while(!anglePrefer.contains(auxAngles[neighbour][j]) || actual_bac.contains(auxAngles[neighbour][j])){
                auxAngles[neighbour][j] = repairAngle(auxAngles[neighbour][j]-step);
            }
            // If the angle repeats or is not in the list, replace it
            while(!anglePrefer.contains(auxAngles[neighbour+1][j]) || actual_bac.contains(auxAngles[neighbour+1][j])){
                auxAngles[neighbour+1][j] = repairAngle(auxAngles[neighbour+1][j]+step);
            }
            neighbour = neighbour+2;
        }
        // Sort it
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }

    // Random Neighbourhood Definition using Roulette from rMIP
    public int[][] getNeigboursListedMIP(double totalruleta, HashMap<Integer,Double> anglePrefer, TreatmentPlan sol, int ns){
        ArrayList<Integer> actual_bac = new ArrayList<Integer>(); // Create an ArrayList object
        for(int beam_angle:sol.selAngles){
            actual_bac.add(beam_angle);
        }
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0, angle, limitVec1, vec1=0, vec2=0, limitVec2;
        double porc = 0.5;
        int[] limitUp,limitDown;
        Random r = new Random();

        /*Replicate the current solution as the list of new neighbours*/
        for (neighbour = 0; neighbour<ns;neighbour++){
            for (int j = 0; j<numAngles;j++){
                auxAngles[neighbour][j]=sol.selAngles[j];
            }
        }
        // Selects the roulette Neighbours
        for (int i = 0; i<sol.selAngles.length;i++){ 
            // Selects the roulette Neighbour
            do{
                double aux1=((double)r.nextInt(100)/100)*totalruleta;
                int indexruleta1 = 0;
                double suma = 0;
                for(Integer iruleta:anglePrefer.keySet()){
                    suma += anglePrefer.get(iruleta);
                    indexruleta1 = iruleta;
                    if(aux1<suma)
                        break;
                }
                auxAngles[i*2][i] = indexruleta1*step;
            }while(actual_bac.contains(auxAngles[(i*2)][i]));
            
            // Selects the roulette Neighbour
            do{
                double aux2=((double)r.nextInt(100)/100)*totalruleta;
                int indexruleta2 = 0;
                double suma = 0;
                for(Integer iruleta:anglePrefer.keySet()){
                    suma += anglePrefer.get(iruleta);
                    indexruleta2 = iruleta;
                    if(aux2<suma)
                        break;
                }
                auxAngles[(i*2)+1][i] = indexruleta2*step;
            }while(actual_bac.contains(auxAngles[(i*2)+1][i]));
        }
        
        // Sort it
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    // Random Neighbourhood Definition
    public int[][] getNeigboursFor_2_OptVNS(TreatmentPlan sol, int ns){ //Maicholl
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0;
        Random r = new Random();

        for (int j = 0; j<numAngles;j++){
            //copia de los angulos en el vecindario
            for (int i = 0; i<numAngles;i++){
                auxAngles[neighbour][i]=sol.selAngles[i];
                auxAngles[neighbour+1][i]=sol.selAngles[i];
            }
            int auxangle1 = 0;
            int ocurrencia1 = -1;
            int auxangle2 = 0;
            int ocurrencia2 = -1;
            
            // Select a different random angle
            while(ocurrencia1 == -1){
                ocurrencia1 = 0;
                auxangle1 = r.nextInt(360/step)*step;
                for (int i = 0; i<numAngles;i++){
                    if(i != j){
                        if(auxAngles[neighbour][i] == auxangle1){
                            ocurrencia1++;
                        }
                    }
                }
                if(ocurrencia1 != 0)
                    ocurrencia1 = -1;
            }
            
            // Select a different random angle
            while(ocurrencia2 == -1){
                ocurrencia2 = 0;
                auxangle2 = r.nextInt(360/step)*step;
                for (int i = 0; i<numAngles;i++){
                    if(i != j){
                        if(auxAngles[neighbour+1][i] == auxangle2){
                            ocurrencia2++;
                        }
                    }
                }
                if(ocurrencia2 != 0)
                    ocurrencia2 = -1;
            }
            
            auxAngles[neighbour][j]=auxangle1;
            auxAngles[neighbour+1][j]=auxangle2;
            neighbour = neighbour+2;
        }
        
        // Sort it
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    // Puede ser +5 o -5 dependiendo de la suerte 
    public int[][] getNeigboursFor_2_OptVNS_And_StepMove(TreatmentPlan sol, int ns){ 
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0;
        int angle;
        int counterSteep = 0;
        int counterRand = 0;
        int[] newAngles=new int[2];
        int[][] limitUp,limitDown;
        int changeAngle;
        Random r = new Random();
        int[] frontAngle = new int[sol.selAngles.length];
        limitUp=new int[2][sol.selAngles.length];
        limitDown=new int[2][sol.selAngles.length];
       //Encontrar angulos prohibidos
        for (int i = 0; i<sol.selAngles.length;i++){
            frontAngle[i] = sol.selAngles[i]+180;
            if( frontAngle[i]>360){
                frontAngle[i] = frontAngle[i] - 360;
            }
            limitUp[0][i]=sol.selAngles[i]+5;
            limitDown[0][i]=sol.selAngles[i]-5;
            limitUp[1][i]=frontAngle[i]+5;
            limitDown[1][i]=frontAngle[i]-5;
        }
        
        /*Generates New Angles that will be inserted in the selAngles*/
        for (int j = 0; j<2;j++){
            boolean flag=true;
            do{
                flag=true;//agregado por loop infinito si se cumple condición Maicholl
                newAngles[j]=r.nextInt(360/step)*step;
                for (int i = 0; i<sol.selAngles.length;i++){
                    if(limitDown[0][i]<0){ // MAicholl Para el angulo como tal
                        if((newAngles[j]>=0 && newAngles[j]<=limitUp[0][i])||(newAngles[j]<=360 && newAngles[j]>=(360+limitDown[0][i]))){
                            flag=false;
                            //break;
                        }
                    }else{
                        if(limitUp[0][i]>360){
                            if((newAngles[j]<=360 && newAngles[j]>=limitDown[0][i])||(newAngles[j]>=0 && newAngles[j]<=(0+(360-limitUp[0][i])))){
                                flag=false;
                                //break;
                            }
                        }else{
                            if(newAngles[j]<=limitUp[0][i] && newAngles[j]>=limitDown[0][i]){
                                flag=false;
                                //break;
                            }
                        }
                    }
                    if(limitDown[1][i]<0){// MAicholl Para el angulo opuesto
                        if((newAngles[j]>=0 && newAngles[j]<=limitUp[1][i])||(newAngles[j]<=360 && newAngles[j]>=(360+limitDown[1][i]))){
                            flag=false;
                            //break;
                        }
                    }else{
                        if(limitUp[1][i]>360){
                            if((newAngles[j]<=360 && newAngles[j]>=limitDown[1][i])||(newAngles[j]>=0 && newAngles[j]<=(0+(360-limitUp[1][i])))){
                                flag=false;
                                //break;
                            }
                        }else{
                            if(newAngles[j]<=limitUp[1][i] && newAngles[j]>=limitDown[1][i]){
                                flag=false;
                                //break;
                            }
                        }
                    }
                }
            }while(!flag);
        }
        //copia de los angulos en el vecindario
        for (neighbour = 0; neighbour<ns;neighbour++){
            for (int i = 0; i<numAngles;i++){
                auxAngles[neighbour][i]=sol.selAngles[i];
            }
        }
        //agregar las restricciones 
        for (int j = 0; j<ns;j++){
            changeAngle = (int)j/2;
            if(Math.random()<=0.5 && counterRand<=ns/2){
                auxAngles[j][changeAngle]=newAngles[j%2];
                counterRand++;
            }else{
                if(counterSteep>ns/2){
                    auxAngles[j][changeAngle]=newAngles[j%2];
                    counterRand++;
                }else{
                    if (auxAngles[j][changeAngle] > 0 & auxAngles[j][changeAngle]< 360-step){
                        angle=(int)auxAngles[j][changeAngle];
                        if(j%2==0){
                            auxAngles[j][changeAngle]=angle-step;
                        }else{
                            auxAngles[j][changeAngle]=angle+step;
                        }
                    }else{
                        if (auxAngles[j][changeAngle]==0){
                            if(j%2==0){
                                auxAngles[j][changeAngle]=360 - step;
                            }else{
                                auxAngles[j][changeAngle]=0 + step;
                            }
                        }else if (auxAngles[j][changeAngle] == (360 - step)){
                            if(j%2==0){
                                angle=(int)auxAngles[j][changeAngle];
                                auxAngles[j][changeAngle]=angle-step;
                            }else{
                                auxAngles[j][changeAngle]=0;
                            }
                        }
                    }
                    counterSteep++;
                }
            }
        }
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    // MIP with 6 angles
    public int[][] getNeigboursMIPVNS(TreatmentPlan sol, int ns){
        int[][] auxAngles= new int[1][6];
        int neighbour = 0;
        Random r = new Random();
        
      
        
        for (int i = 0; i<6;i++){
            boolean flag=false;
            int aux=0;
            Random rand = new Random();
            aux=r.nextInt(360/step)*step;  
            auxAngles[0][i]=aux;
               
        }
        
        for (int i=0;i<auxAngles.length;i++){
            for (int j=0;j<auxAngles[i].length;j++){
                if(auxAngles[i][j]>=360){
                        auxAngles[i][j] = auxAngles[i][j] - 360;
                }else if(auxAngles[i][j]<0){
                        auxAngles[i][j] = 360 + auxAngles[i][j];
                }
            }
        }
        
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    // rVNS with Simulated annealing
    public int[][] getNeigboursReducedVNS_Ann(TreatmentPlan sol, int ns,float t){
        String [] movi = new String[ns];
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0, angle, limitVec1, vec1=0, vec2=0, limitVec2;
        int[] limitUp,limitDown;
        int used_exploid_plus_less =0; //-1= -5 movement 1= +5movement 0 neutral
        Random r = new Random();
        int limit_mov5 = (int)(2*sol.selAngles.length*(float)t);
        int limit_rand = 2*sol.selAngles.length - limit_mov5;
        int counter_mov5 = 0;
        int counter_rand = 0;
        double random_Number = 0;
        double random_Number2 = 0;
        /*  
        limitVec1 = (int) (0.5*ns);
        limitVec2 = ns - limitVec1;
        int random;*/
      
        //Encontrar angulos prohibidos
        limitUp=new int[sol.selAngles.length];
        limitDown=new int[sol.selAngles.length];
        
        for (int i = 0; i<sol.selAngles.length;i++){
            limitUp[i]=sol.selAngles[i]+5;
            limitDown[i]=sol.selAngles[i]-5;
        }
        
        /*Replicate the current solution as the list of new neighbours*/
        for (neighbour = 0; neighbour<ns;neighbour++){
            for (int j = 0; j<numAngles;j++){
                auxAngles[neighbour][j]=sol.selAngles[j];
            }
        }
        
        for (int i = 0; i<sol.selAngles.length;i++){
            used_exploid_plus_less = 0;
            for (int j = 0; j<2;j++){                
                boolean flag=false;
                int aux=0;
                Random rand = new Random();
                random_Number = rand.nextDouble();
                if (((random_Number <= 0.5) || counter_rand>=limit_rand) && counter_mov5<limit_mov5){ // corresponde generar vecinos +/-5   
                    movi[(i*2)+j] = "+-5";
                    counter_mov5++;
                    random_Number2 = rand.nextDouble();
                     if ((random_Number2 <= 0.5 || used_exploid_plus_less == 1)&& used_exploid_plus_less != -1){
                        auxAngles[(i*2)+j][i]=auxAngles[(i*2)+j][i]-step;
                        used_exploid_plus_less = -1;
                     }else if(random_Number2 > 0.5 || used_exploid_plus_less == -1){
                        auxAngles[(i*2)+j][i]=auxAngles[(i*2)+j][i]+step;
                        used_exploid_plus_less = 1;
                     }
                }else if (((random_Number > 0.5) || counter_mov5>=limit_mov5)&& counter_rand<limit_rand){ // corresponde generar vecinos random
                    movi[(i*2)+j] = "Random";
                    counter_rand++;
                    do{
                        aux=r.nextInt(360/step)*step;
                        flag=false;
                        for (int k=0;k<sol.selAngles.length;k++){
                            if (limitUp[k]==aux || limitDown[k]==aux){
                                flag=true;
                            }
                        }
                    }while (flag);
                    auxAngles[(i*2)+j][i]=aux;

                }
            }
                
                
        }
        
        for (int i=0;i<ns;i++){
            for (int j=0;j<auxAngles[i].length;j++){
                if(auxAngles[i][j]>=360){
                        auxAngles[i][j] = auxAngles[i][j] - 360;
                }else if(auxAngles[i][j]<0){
                        auxAngles[i][j] = 360 + auxAngles[i][j];
                }
            }
        }
        
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        for (int i = 0; i<ns; i++){
            System.out.println("Angles generated: " + i + "movimiento "+movi[i]);
           for (int j = 0; j<auxAngles[i].length;j++){
               System.out.print(auxAngles[i][j] + " - " );
           }
           System.out.println();
        }
        return auxAngles;
    }
    
    //Neighbourhood definitions
    public int[][] ChooseNeigbours(int option,TreatmentPlan sol, int ns) throws IOException{ //Maicholl Agregado para VNS
        int[][] auxAngles= new int[ns][numAngles];
        //HashMap <Integer, Double> mip_relaxed = null;
        //List<Integer> targetList = null;
        //double totalmip = 0;
        /*if(rMIP_type){
        //  BLoque preparación rMIP 
            mip_relaxed =  readRelaxedMiP("./mip_relaxed.txt");            
            LinkedHashMap<Integer,Double> orderer_angles = order_my_hashmap(mip_relaxed);
            for(Integer idmip : mip_relaxed.keySet()){
                totalmip += mip_relaxed.get(idmip);
            }
            Set<Integer> bads_angles = orderer_angles.keySet();
            targetList = new ArrayList<>(bads_angles);
            int size_target = targetList.size();
            Collections.reverse(targetList);
            for(int i=size_target-1; i>size_target*(35.0/100.0);i--){
                targetList.remove(i);
            }
            for(int i=0; i< targetList.size();i++){
                targetList.set(i, targetList.get(i)*5);
            }
        }*/
        switch (option){
            case 0:
                auxAngles = getNeigboursRVNS(sol, ns);//Parte caso 1, parte caso 2
            break;
            case 1:
                if(!rMIP_type){
                    auxAngles = getNeigboursBasedOnStepSize(sol, ns); // Ángulo +-5
                }else{
                    auxAngles = getNeigbours5StepListedMIP(sol, ns, targetList); // Ángulo +-5
                }
            break;
            case 2:
                if(!rMIP_type){
                    auxAngles = getNeigboursFor_2_OptVNS(sol, ns); //Suerte random
                }else{
                    auxAngles = getNeigboursListedMIP(totalmip,mip_relaxed,sol, ns); //Suerte random
                }
            break;
            case 3:
                auxAngles = getNeigboursFor_2_OptVNS_And_StepMove(sol, ns); //Suerte +5 -5 random con restricción geométrica
            break;
            case 4:
                auxAngles = getNeigboursMIPVNS(sol, ns); //Suerte +5 -5 random con restricción geométrica
            break;
        }
        return auxAngles;
    }

    // prints on files 
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
                       timerFile = "./Results/logFunction/"+jobID+"singObj_"+name+"timerbestSolution_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
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
           generatedBACs = new ArrayList<>(); //Those BACs that have been generated    
           visitedBACs = new ArrayList<>(); //Those BACs whose neighbourhood has been generated
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
    
    private void inicializarVarsMO() {
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
                ndpFile =     "./Results/logFunction/"   + jobID + "PLS_ndpSet_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                currentFile = "./Results/logFunction/"   + jobID + "PLS_currentSolution_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                summaryDir =  "./Results/logFunction/"   + jobID + "PLS_summary_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                bestSolFile=  "./Results/logFunction/"   + jobID + "PLS_bestSolution_LogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                finalBACsFile = "./Results/logFunction/" + jobID + "PLS_finalBACs_LogFunction_"       +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                vBACsFile =     "./Results/logFunction/" + jobID + "PLS_visitedBACs_LogFunction_"      +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                image_ndpFile =       "./Results/logFunction/" + jobID +"PLS_ndp_logFunction"+globalIter+".txt";
                image_currentFile =   "./Results/logFunction/" + jobID +"PLS_currentSolution_logFunction"+globalIter+".txt";
                image_vBACsFile =     "./Results/logFunction/" + jobID +"PLS_visitedBACs_logFunction"+globalIter+".txt";
                break;
            case 2: 
                ndpFile =     "./Results/lexicoRectum/"+ jobID +"PLS_ndpSet_lexicoRectum_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                currentFile = "./Results/lexicoRectum/"+ jobID +"PLS_currentSolution_LexicoR_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                summaryDir =  "./Results/lexicoRectum/"+ jobID +"PLS_summary_LexicoR_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                bestSolFile=  "./Results/lexicoRectum/"+ jobID +"PLS_bestSolution_LexicoR_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
            case 3: 
                ndpFile =     "./Results/lexicoBladder/"+ jobID +"PLS_ndpSet_lexicoBladder_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                currentFile = "./Results/lexicoBladder/"+ jobID +"PLS_currentSolution_LexicoB_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                summaryDir =  "./Results/lexicoBladder/"+ jobID +"PLS_summary_LexicoB_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                bestSolFile=  "./Results/lexicoBladder/"+ jobID +"PLS_bestSolution_LexicoB_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                break;
            case 4: 
                ndpFile =     "./Results/variableWeights/"   + jobID + "PLS_ndpSet_variableWeights_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                currentFile = "./Results/variableWeights/"   + jobID + "PLS_currentSolution_variableWeights_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                summaryDir =  "./Results/variableWeights/"   + jobID + "PLS_summary_variableWeights_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                bestSolFile=  "./Results/variableWeights/"   + jobID + "PLS_bestSolution_variableWeights_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                finalBACsFile = "./Results/variableWeights/" + jobID + "PLS_finalBACs_variableWeights_"       +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                vBACsFile =     "./Results/variableWeights/" + jobID + "PLS_visitedBACs_variableWeights_"      +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                image_ndpFile =       "./Results/variableWeights/" + jobID +"PLS_ndp_variableWeights"+globalIter+".txt";
                image_currentFile =   "./Results/variableWeights/" + jobID +"PLS_currentSolution_variableWeights"+globalIter+".txt";
                image_vBACsFile =     "./Results/variableWeights/" + jobID +"PLS_visitedBACs_variableWeights"+globalIter+".txt";
                break;
            case 5: 
                ndpFile =           "./Results/convexLogFunction/" + jobID + "PLS_ndpSet_convexLogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                currentFile =       "./Results/convexLogFunction/" + jobID + "PLS_currentSolution_convexLogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                summaryDir =        "./Results/convexLogFunction/" + jobID + "PLS_summary_convexLogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                bestSolFile=        "./Results/convexLogFunction/" + jobID + "PLS_bestSolution_convexLogFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                finalBACsFile =     "./Results/convexLogFunction/" + jobID + "PLS_finalBACs_convexLogFunction_"       +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                vBACsFile =         "./Results/convexLogFunction/" + jobID + "PLS_visitedBACs_convexLogFunction_"      +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                image_ndpFile =     "./Results/convexLogFunction/" + jobID + "PLS_ndp_"+globalIter+".txt";
                image_currentFile = "./Results/convexLogFunction/" + jobID + "PLS_currentSolution_"+globalIter+".txt";
                image_vBACsFile =   "./Results/convexLogFunction/" + jobID + "PLS_visitedBACs_"+globalIter+".txt";
                break;    
            
            case 9: 
                ndpFile =     "./Results/cuadraticFunction/"   + jobID + "PLS_ndpSet_cuadraticFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                currentFile = "./Results/cuadraticFunction/"   + jobID + "PLS_currentSolution_cuadraticFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                summaryDir =  "./Results/cuadraticFunction/"   + jobID + "PLS_summary_cuadraticFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                bestSolFile=  "./Results/cuadraticFunction/"   + jobID + "PLS_bestSolution_cuadraticFunction_"+numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                finalBACsFile = "./Results/cuadraticFunction/" + jobID + "PLS_finalBACs_cuadraticFunction_"       +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                vBACsFile =     "./Results/cuadraticFunction/" + jobID + "PLS_visitedBACs_cuadraticFunction_"      +numAngles+"Beams_step"+step+"_"+globalIter+".txt";
                image_ndpFile =       "./Results/cuadraticFunction/" + jobID +"PLS_ndp_cuadraticFunction"+globalIter+".txt";
                image_currentFile =   "./Results/cuadraticFunction/" + jobID +"PLS_currentSolution_cuadraticFunction"+globalIter+".txt";
                image_vBACsFile =     "./Results/cuadraticFunction/" + jobID +"PLS_visitedBACs_cuadraticFunction"+globalIter+".txt";
                break;
            
        }
           neighbourhoodSize = numAngles * 2; //Two neighbours per beam angle; 
           generatedBACs = new ArrayList<>(); //Those BACs that have been generated    
           visitedBACs = new ArrayList<>(); //Those BACs whose neighbourhood has been generated
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
    
    // Parallel runs
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
    
    // generate and solve the neighbourhood 
    public TreatmentPlan[] generateNeighbourhood( TreatmentPlan sol, 
        int ns, int[][] bs, String cFile, int iteration, int tipomov) throws IOException, InterruptedException, ExecutionException{
        long prevTimer = System.currentTimeMillis();
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
       
        auxAngles=ChooseNeigbours(tipomov,sol,ns); 
         
        sol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood: Preparation ended",false);
        prevTimer = System.currentTimeMillis();
        /**********Neighbours Evaluation************
        * Evaluate neighbours that must be visited *
        *******************************************/
        if (parallelNeighbourhoodGeneration){
            sol.printSolTime(timerFile,0,prevTimer,"generating 10 parallel FMO of generateNeighbourhood...",false);
            prevTimer = System.currentTimeMillis();
            //getNeighbour[] gNeighbour = new getNeighbour[ns]; //Callables
            Future<TreatmentPlan>[] ft = new Future[ns]; //Callables
            ExecutorService pool = Executors.newFixedThreadPool(ns);
            //create Callables and FutureTasks
            //CountDownLatch latch = new CountDownLatch(ns);
            for (int i = 0; i<ns;i++){
                Callable<TreatmentPlan> gNeighbour = new getNeighbour(auxAngles[i], sol, cFile, Integer.toString(i+1));
                ft[i] = new FutureTask<>(gNeighbour);
                ft[i] = pool.submit(gNeighbour);
            }
            pool.shutdown();
            try {
                pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                for (int i = 0; i<ft.length;i++){
                    nhood[i] = new TreatmentPlan(ft[i].get()); 
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
                sol.printSolTime(timerFile,0,prevTimer,"solving "+(k+1)+"/10 sequential FMO of generateNeighbourhood...",false);
                prevTimer = System.currentTimeMillis();
                nhood[i].generateDDM(pathFile);
                for(int ind: nhood[i].getOrganos().keySet()){
                    Organs organos = nhood[i].getOrganos().get(ind);
                    organos.writeMapVoxel(jobID,nhood[i].selAngles);
                }
                nhood[i].generateReferencePoint(sol.getOrganos(), 0, solver, maxX, 1, jobID);
               
                generatedBACs.add(nhood[i]);                    
                totalObjFuncEval++;
               
                nhood[i].printSol( cFile);
                nhood[i].liberateMemory();
                //copyDVHs(o,iteration,i); //Maicholl archivo
                sol.printSolTime(timerFile,0,prevTimer,"ended "+(k+1)+"/10 sequential FMO of generateNeighbourhood...",false);
                prevTimer = System.currentTimeMillis();
                /*Only for NEXT DESCENT Local Search*/
                //if ((name=="nextDescent" || selectionCriterion==5) && nhood[i].getSingleObjectiveValue()<sol.getSingleObjectiveValue()){ //&& sol.singleObjectiveValue-nhood[i].singleObjectiveValue>minChange){//Maicholl
                if (strategy.compareToIgnoreCase("ND")==0 && nhood[i].getSingleObjectiveValue()<sol.getSingleObjectiveValue()){
                    System.out.println("Next Descent");
                    break; //return a neighbourhood with only one solution better than the current solution
                }
                
            }
        }
        sol.printSolTime(timerFile,0,prevTimer,"ended 10 Neighbourhood of generateNeighbourhood...",true);
        prevTimer = System.currentTimeMillis();
        System.out.println("*****************************************" );
        System.out.println("*    NEIGHBOURHOOD HAS BEEN GENERATED   *" );
        System.out.println("*****************************************" );
        return nhood;
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
    // Run 
    public void run() throws IOException, InterruptedException, ExecutionException{
        long prevTimer = System.currentTimeMillis();
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
        // For each run
        for(globalIter = 0; globalIter<iterations; globalIter++){
            if(problemType.compareTo("SO") == 0){
                inicializarVars(); 
            }else if(problemType.compareTo("MO") == 0){
                inicializarVarsMO();
            }
            
            totalObjFuncEval=0;
            initialPlan.setAngles(initialBACs[globalIter]);
            initialPlan.beams = numAngles;
            initialPlan.loadNumBixels(beamInfoDir,step);
            initialPlan.cargarBeams(pathFile);
            initialPlan.printSolTime(timerFile,0,localTimer,"DDMs loading begins",false);
            prevTimer = System.currentTimeMillis();
            initialPlan.iniorgansbeams(initialPlan.selAngles, pathFile, jobID);
            initialPlan.generateDDM(pathFile);
            initialPlan.printSolTime(timerFile,0,prevTimer,"Loading of DDMs is completed and begin the initial BAC is FMO",false);
            prevTimer = System.currentTimeMillis();
            if(selectionCriterion == -1){
                break;
            }
            
            // Solve the initial BAC
            initialPlan.generateReferencePoint(initialPlan.getOrganos(),ordenVecindades[0],solver,maxX,1,jobID);
            initialPlan.printSolTime(timerFile,0,prevTimer,"Initial BAC is solved",false);
            prevTimer = System.currentTimeMillis();
            initialPlan.liberateMemory();
            
            if(warm_start){
                initialPlan.useThisSolution();
                initialPlan.setPrevAngles();
            }
            if(problemType.compareTo("SO") == 0){
                if(selectionCriterion == 0){
                    localSol= rVNS(initialPlan);
                }else if(selectionCriterion == 1 ){
                    localSol= localSearch(initialPlan, ordenVecindades[0]);
                }else if(selectionCriterion == 2){
                    localSol= ite_fixed_rMIP_LS(initialPlan);
                }else if(selectionCriterion == 3){
                    localSol= seq_sel_LS(initialPlan);
                }else if(selectionCriterion == 4){
                    //localSol= ite_fixed_rMIP_bestx_LS(initialPlan,25);
                    localSol= ite_fixed_rMIP_restr_LS(initialPlan);
                }else if(selectionCriterion == 5){
                    localSol= vnd(initialPlan);
                }else if(selectionCriterion == 99){
                    localSol = paramTest(initialPlan);
                }
                
            }else if(problemType.compareTo("MO") == 0){
                if(selectionCriterion <= 5 ){
                    if(moestrategy.equalsIgnoreCase("Angel")){
                        localSol= MO_PLS_Angel(initialPlan, ordenVecindades[0]);
                    }else if(moestrategy.equalsIgnoreCase("Paquete")){
                        localSol= MO_PLS_Paquete(initialPlan, ordenVecindades[0]);
                    }else if(moestrategy.equalsIgnoreCase("MOrVNS")){
                        localSol= MO_rVNS(initialPlan, 0);
                    }
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
    
    // Local Search
    public TreatmentPlan localSearch(TreatmentPlan ini, int tipomov) throws InterruptedException, ExecutionException, IOException {
        long prevTimer = System.currentTimeMillis();
        initialPlan.printSolTime(timerFile,0,prevTimer,"localSearch begins",false);
        prevTimer = System.currentTimeMillis();
        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;  
        if(rMIP_type){
        //  BLoque preparación rMIP 
            mip_relaxed =  readRelaxedMiP("./mip_relaxed.txt");            
            LinkedHashMap<Integer,Double> orderer_angles = order_my_hashmap(mip_relaxed);
            for(Integer idmip : mip_relaxed.keySet()){
                totalmip += mip_relaxed.get(idmip);
            }
            Set<Integer> bads_angles = orderer_angles.keySet();
            targetList = new ArrayList<>(bads_angles);
            int size_target = targetList.size();
            Collections.reverse(targetList);
            if(tipomov == 1){
                for(int i=size_target-1; i>=rMIP_number_allow;i--){
                    targetList.remove(i);
                }
            }
            for(int i=0; i< targetList.size();i++){
                targetList.set(i, targetList.get(i)*5);
            }
        }
        currentSol = new TreatmentPlan(ini);
        while (!endCriterion){
            TreatmentPlan[] neighbourhood;
            
            initialPlan.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood begins",false);
            prevTimer = System.currentTimeMillis();
            // generate the neighbourhood
            neighbourhood = generateNeighbourhood(currentSol, neighbourhoodSize, beamletSet, currentFile,iterationsCounter, tipomov);
            
            // gets the best neighbour
            bestNeighbour = new TreatmentPlan(getBestNeighbour(iterationsCounter,neighbourhood,neighbourhoodSize));
            
            if (bestNeighbour.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(bestNeighbour);
                currentSol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood ended with better solution",true);
                prevTimer = System.currentTimeMillis();
                for(TreatmentPlan ns:neighbourhood){
                    currentSol.copyAngle(ns, ns.selAngles);
                }
                printCurrent(currentSol);
                try {
                    currentSol.printSol(bestFile,tipomov);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }         
            }else{
                endCriterion=true;   
                currentSol.printSolTime(timerFile,0,localTimer,"generateNeighbourhood ended, algorithm ended",true);
            }
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
    
    // rVNS
    public TreatmentPlan rVNS(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        int tipomov = 0; // Mix all neighbourhoods
        long prevTimer = System.currentTimeMillis();
        initialPlan.printSolTime(timerFile,0,prevTimer,"rVNS begins",false);
        prevTimer = System.currentTimeMillis();
        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;  
       
        currentSol = new TreatmentPlan(ini);
        while (!endCriterion){
            TreatmentPlan[] neighbourhood;
            
            initialPlan.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood begins",false);
            prevTimer = System.currentTimeMillis();
            // generate the mixed neighbourhood
            neighbourhood = generateNeighbourhood(currentSol, neighbourhoodSize, beamletSet, currentFile,iterationsCounter, tipomov);
            
            // gets the best neighbour
            bestNeighbour = new TreatmentPlan(getBestNeighbour(iterationsCounter,neighbourhood,neighbourhoodSize));
            
            if (bestNeighbour.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(bestNeighbour);
                currentSol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood ended with better solution",true);
                prevTimer = System.currentTimeMillis();
                for(TreatmentPlan ns:neighbourhood){
                    currentSol.copyAngle(ns, ns.selAngles);

                }
                printCurrent(currentSol);
                try {
                    currentSol.printSol(bestFile,tipomov);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }         
            }else{
                
                endCriterion=true;   
                currentSol.printSolTime(timerFile,0,localTimer,"generateNeighbourhood ended, algorithm ended",true);
            }
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

    public int neighbourhoodChange(TreatmentPlan[] neighbourhood,TreatmentPlan currentSol,TreatmentPlan bestNeighbour, int k){
        if(bestNeighbour.getSingleObjectiveValue() < currentSol.getSingleObjectiveValue()){
            //currentSol = new TreatmentPlan(bestNeighbour);
            for(TreatmentPlan ns:neighbourhood){
                bestNeighbour.copyAngle(ns, ns.selAngles);
            }
            printCurrent(bestNeighbour);
            try {
                bestNeighbour.printSol(bestFile,k);
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
            k = 0;
        }else{
            k++;
        }
        return k;
    }
    
    public int neighbourhoodChangeMO(TreatmentPlan[][] neighbourhood,TreatmentPlan currentSol,TreatmentPlan bestNeighbour, int k){
        if(bestNeighbour.getSingleObjectiveValue() < currentSol.getSingleObjectiveValue()){
            //currentSol = new TreatmentPlan(bestNeighbour);
            for(TreatmentPlan[] nss:neighbourhood){
                for(TreatmentPlan ns:nss){
                    bestNeighbour.copyAngle(ns, ns.selAngles);
                }
            }
            printCurrent(bestNeighbour);
            try {
                bestNeighbour.printSol(bestFile,k);
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
            k = 0;
        }else{
            k++;
        }
        return k;
    }
    
    public TreatmentPlan vnd(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        iterationsCounter = 0;
        TreatmentPlan currentSol = null;
        TreatmentPlan previous = null;
        TreatmentPlan bestNeighbour = null;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        int tipomov = 0;
        currentSol = new TreatmentPlan(ini);
        do{
            endCriterion = false;
            while (!endCriterion){
                System.out.println("=====================================================");
                System.out.println("Se está utilizando el tipo de movimiento: " + tipomov);
                System.out.println("=====================================================");
                previous = new TreatmentPlan(currentSol);
                TreatmentPlan[] neighbourhood;
                neighbourhood = generateNeighbourhood(currentSol, neighbourhoodSize, beamletSet, currentFile,iterationsCounter, ordenVecindades[tipomov]);
                bestNeighbour = new TreatmentPlan(getBestNeighbour(iterationsCounter,neighbourhood,neighbourhoodSize));
                tipomov = neighbourhoodChange(neighbourhood,currentSol,bestNeighbour,tipomov);
                if(tipomov == 0) currentSol = new TreatmentPlan(bestNeighbour);
                neighbourhood = null;
                bestNeighbour = null;
                if(tipomov >= ordenVecindades.length){
                    endCriterion = true;
                }
                if (!checkFile.exists()) {
                    endCriterion = true;
                    System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
                }
                iterationsCounter++;
            }
        }while(currentSol.getSingleObjectiveValue()<previous.getSingleObjectiveValue());
        printSolution(localTimer, currentSol);
        
        return currentSol;
    }
    
    public TreatmentPlan prev_vnd(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {

        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        int tipomov =0;
        currentSol = new TreatmentPlan(ini);
        
        while (!endCriterion){
            bestNeighbour = localSearch(currentSol, ordenVecindades[tipomov]);
            if (bestNeighbour.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(bestNeighbour);
            }else{
                tipomov++;                
            }
            bestNeighbour = null;
            if(tipomov >= ordenVecindades.length){
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
    
    public static boolean sameBAC(int [] bac1, int [] bac2){
        boolean flag=false;
            for (int j=0;j<bac1.length;j++){
                flag=false; //Assuming bac1 is not equal to bac2
                for (int k=0;k<bac2.length;k++){
                    if (bac1[j]==bac2[k]){
                        flag=true;
                        break;
                    }
                }
                if (flag==false){
                    break;
                }
            }
        return flag;
    }
    
    public TreatmentPlan carga72Plan(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {

        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        int tipomov =0;
        currentSol = new TreatmentPlan(ini);
        
        while (!endCriterion){
            bestNeighbour = localSearch(currentSol, ordenVecindades[tipomov]);
            if (bestNeighbour.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(bestNeighbour);
            }else{
                tipomov++;                
            }
            bestNeighbour = null;
            if(tipomov >= ordenVecindades.length){
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
    
    private void addNewNDP(TreatmentPlan currentSol) {
        boolean contains=false;
        if (!(ndp_mo == null)){
            /*for (TreatmentPlan ndp1 : ndp) {
                contains=true;
                for (Beam selAngle : ndp1.selAngles) {
                    contains=false;
                    for (Beam selAngle1 : currentSol.selAngles) {
                        if (selAngle == selAngle1) {
                            contains=true;
                            break;
                        }
                    }
                    if (contains ==false){
                        break;
                    }
                }
                if (contains == true){
                    break;
                }
            }*/
            contains=false;
            for (TreatmentPlan ndp1 : ndp_mo) {
                if (sameBAC(currentSol.selAngles, ndp1.selAngles)){
                    contains=true;
                    break;
                }
            }
            if (!contains){
                boolean isDominated=false;
                for (int i=0;i<ndp_mo.size();i++){
                    if (ndp_mo.get(i).isDominated(currentSol)){
                        ndp_mo.remove(i);
                        i--;
                    }else if(currentSol.isDominated(ndp_mo.get(i))){
                        isDominated = true;
                        break;
                    }
                }
                if (!isDominated){
                    ndp_mo.add(currentSol);
                }
            }
        }else{
            ndp_mo.add(currentSol);
        }
    }
    
    public ArrayList<TreatmentPlan> getSolutionsToBeVisited(){
      ArrayList<TreatmentPlan> nonVisitedSol = new ArrayList<>();
      
      for (int i=0; i<ndp_mo.size();i++){
          if (!ndp_mo.get(i).isVisited()){
              //Re-chech this point has not been visited before
              for (int j=0; j<visitedBACs.size();j++){
                  if (sameBAC(ndp_mo.get(i).selAngles, visitedBACs.get(j).selAngles)){
                      ndp_mo.get(i).visited();
                  }
              }
              if (!ndp_mo.get(i).isVisited()){
                  nonVisitedSol.add(ndp_mo.get(i));
              }
          }
      }
      
        return nonVisitedSol;
    }
    
    public void addNewVisitedBAC(TreatmentPlan sol){
      //A "visited BAC" is a BAC whose neighbourhood has been generated
      visitedBACs.add(sol);
  }
    private void update_NDP_List(TreatmentPlan[] ns, int functionChoosen) {
        int added=0;
        int removed=0;
        boolean contains=false; //ndp contains treatment plan n \in ns
        for (TreatmentPlan n : ns) {
            if(n != null){
                /*for (TreatmentPlan ndp1 : ndp) {
                    contains=true;
                    for (Beam selAngle : ndp1.selAngles) {
                        contains=false;
                        for (Beam selAngle1 : n.selAngles) {
                            if (selAngle == selAngle1) {
                                contains=true;
                                break;
                            }
                        }
                        if (contains ==false){
                            break;
                        }
                    }
                    if (contains == true){
                        break;
                    }
                }*/
                contains=false;
                for (TreatmentPlan ndp1 : ndp_mo) {
                    if (sameBAC(n.selAngles, ndp1.selAngles)){
                        contains=true;
                        break;
                    }
                }
                if (!contains) {
                    boolean isDominated=false;
                    for (int i = 0; i<ndp_mo.size(); i++) {
                        if (ndp_mo.get(i).isDominated(n)) {
                            ndp_mo.remove(i);
                            removed++;
                            i--;
                        } else if (n.isDominated(ndp_mo.get(i))) {
                            isDominated = true;
                            break;
                        }
                    }
                    if (!isDominated) {
                        ndp_mo.add(n);
                        added++;
                    }
                }
            }
        }
        
        System.out.println(added + "solutions from the neighbourhood have been added to the NDP list. " + removed + 
                "solutions have been removed. There are " + ndp_mo.size()+ "ndp in the list");
    }
    
    public void printNDP(int vecindad, String dirFile, String image_dirFile, int iter) throws IOException{
        BufferedWriter bwFile1 = null ;
        BufferedWriter bwFile2 = null ;
        File summaryFile_1 = new File(dirFile);
        File summaryFile_2 = new File(image_dirFile);
        if (summaryFile_1.exists()) {
          bwFile1 = new BufferedWriter(new FileWriter(summaryFile_1, true));
        }else{
          bwFile1 = new BufferedWriter(new FileWriter(summaryFile_1));
        }
        if (summaryFile_2.exists()) {
          bwFile2 = new BufferedWriter(new FileWriter(summaryFile_2, true));
        }else{
          bwFile2 = new BufferedWriter(new FileWriter(summaryFile_2));
        }
        writeLine("############## ITER " +iter+ "##############\n",bwFile1);
        writeLine("Vecindad: "+vecindad+" Number of non-dominated BACs: \t" + ndp_mo.size() + "\n",bwFile1);
        for (TreatmentPlan ndp1 : ndp_mo) {
            for (int j = 0; j<numAngles; j++) {
                writeLine(ndp1.selAngles[j] + "\t", bwFile1);
                writeLine(ndp1.selAngles[j] + "\t", bwFile2);
            }
            for (int j = 0; j<numOrgans; j++) {
                writeLine("0\t", bwFile1);
                writeLine("0\t", bwFile2);
            }
            for (int j = 0; j<numOrgans + 1; j++) {
                writeLine(ndp1.getgEUD()[j] + "\t", bwFile1);
                writeLine(ndp1.getgEUD()[j] + "\t", bwFile2);
            }
            writeLine(ndp1.getSingleObjectiveValue() + "\n", bwFile1);
            writeLine(ndp1.getSingleObjectiveValue() + "\n", bwFile2);
        }
        bwFile1.close();
        bwFile2.close();
  }
  
    
    public void printSummary(String dirFile, int lineOption, long initTime, String iter, 
        TreatmentPlan currentSol) throws IOException{
        BufferedWriter bwFile = null ;
        File summaryFile = new File(dirFile);
        if (summaryFile.exists()) {
          bwFile = new BufferedWriter(new FileWriter(summaryFile, true));
        }else{
          bwFile = new BufferedWriter(new FileWriter(summaryFile));
        }
        switch(lineOption){
            case 0: //register the initial BAC used in the search
                writeLine("############## ITER " +iter+ "##############\n",bwFile);
                writeLine("Initial BAC:\t",bwFile);
                for (int i = 0; i<numAngles-1;i++){
                  writeLine(currentSol.selAngles[i] + " - ",bwFile);
                }
                writeLine(currentSol.selAngles[numAngles-1] + "\n",bwFile);
                break;
            case 1:
                writeLine("############## ITER " +iter+ "##############\n",bwFile);
                writeLine("Number of Visited BACs      : \t" + visitedBACs.size() + "\n",bwFile);
                writeLine("Number of non-dominated BACs: \t" + ndp_mo.size() + "\n",bwFile);
                int auxCount = 0;
                for (TreatmentPlan ndp1 : ndp_mo) {
                  if (!ndp1.isVisited()) {
                      auxCount++;
                  }
                }
                writeLine("Number of BACs to be visited: \t" + auxCount + "\n",bwFile);
                writeLine("Elapsed Time: \t" + (System.currentTimeMillis()-initTime)/1000 +"\n",bwFile);
                break;
          case 2:
                writeLine("############## SUMMARY ITARATION " +globalIter+ "##############\n",bwFile);
                writeLine("Total Obj. Func. Eval.      :\t"+totalObjFuncEval+"\n",bwFile);
                writeLine("Total Time (secs)           :\t"+(System.currentTimeMillis()- initTime)/1000+"\n",bwFile);
                writeLine("Number of Visited BACs      : \t" + visitedBACs.size() + "\n",bwFile);
                writeLine("Number of non-dominated BACs: \t" + ndp_mo.size() + "\n",bwFile);
                writeLine("Total Func. Eval. from first iteration: "+globalTotalObjFuncEval+"\n",bwFile);
                writeLine("Total Time elapsed from first iteration (secs)           :\t"+(System.currentTimeMillis()- globalTimer)/1000+"\n",bwFile);
                for (TreatmentPlan ndp1 : ndp_mo) {
                    for (int j = 0; j<numAngles; j++) {
                        writeLine(ndp1.selAngles[j] + "\t", bwFile);
                    }
                    for (int j = 0; j<numOrgans; j++) {
                        writeLine("0\t", bwFile);
                    }
                    for (int j = 0; j<numOrgans; j++) {
                        writeLine(ndp1.getgEUD()[j] + "\t", bwFile);
                    }
                    writeLine(ndp1.getSingleObjectiveValue() + "\n", bwFile);
                }
                break;
        }
        bwFile.close();
    }
    
    public double hipervolume(){
        Collections.sort(ndp_mo, Collections.reverseOrder());
        double hipvol = 0;
        double nadir_x = 0;
        double nadir_y = 0;        
        double actual_x = 0;
        double actual_y = 0;
        int i = 0;
        for(TreatmentPlan plan:ndp_mo){
            if(i == 0){
                nadir_x = plan.getOrganos().get(1).getDesiredDose();
                nadir_y = plan.getOrganos().get(2).getDesiredDose();
            }
            actual_x = plan.getgEUD()[1];
            actual_y = plan.getgEUD()[2];
            i++;
        }
        return 0.0;
    }
    
    
    
    // MO VND
    public TreatmentPlan MO_PLS_Angel(TreatmentPlan ini, int tipomov) throws InterruptedException, ExecutionException, IOException {
        ndp_mo = new ArrayList<>();
        generatedBACs = new ArrayList<>(); 

        //public static int poolSize = 3;
        visitedBACs = new ArrayList<>(); 
        localOptima = new ArrayList<>();
        
        TreatmentPlan currentSol = null;
        TreatmentPlan previous = null;
        TreatmentPlan bestNeighbour = null;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        currentSol = new TreatmentPlan(ini);
        
        totalObjFuncEval++;
        generatedBACs.add(currentSol);
        System.out.println("done!");
        addNewNDP(currentSol);
        ArrayList<TreatmentPlan> solToBeVisited = new ArrayList<>();
        
        //Dtermine the list of BACs to ve visited in this iteration
        solToBeVisited.addAll(getSolutionsToBeVisited());
        iterationsCounter = 0;
        endCriterion = false;
        while (!endCriterion){
            
            
            TreatmentPlan[][]neighbourhood= new TreatmentPlan[solToBeVisited.size()][neighbourhoodSize];
            for (int i=0;i<solToBeVisited.size();i++){
                System.out.println("=====================================================");
                System.out.println("Se está utilizando el tipo de movimiento: " + tipomov);
                System.out.println("=====================================================");
                previous = new TreatmentPlan(currentSol);
                currentSol = new TreatmentPlan(solToBeVisited.get(i));
                neighbourhood[i] = generateNeighbourhood(currentSol, neighbourhoodSize, beamletSet, currentFile,iterationsCounter, tipomov);
                System.out.println("Tiempo generada vecindad "+i+": "+(System.currentTimeMillis()- initTimer)/1000);// Maicholl
                auxActualTimer = System.currentTimeMillis() ;//Maicholl aqui
                solToBeVisited.get(i).visited();
                addNewVisitedBAC(solToBeVisited.get(i));
            }   
            /* After all solutions are visited, a dominance 
             * analysis over the entire set is performed */
            System.out.println("Tiempo inicio actualizacion NDP  " +(System.currentTimeMillis()- auxActualTimer)/1000);// Maicholl
            auxActualTimer = System.currentTimeMillis(); //Maicholl aqui
            
            for (int i=0;i<solToBeVisited.size();i++){
                update_NDP_List(neighbourhood[i], option);
            }
            System.out.println("Tiempo final actualizacion NDP  " +(System.currentTimeMillis()- auxActualTimer)/1000);// Maicholl
            auxActualTimer = System.currentTimeMillis(); //Maicholl aqui
            printNDP(tipomov, ndpFile, image_ndpFile, iterationsCounter);
            
            //Dtermine the list of BACs to ve visited in the next iteration
            solToBeVisited = new ArrayList<>();
            solToBeVisited.addAll(getSolutionsToBeVisited());
            
            if (solToBeVisited.size() == 0){
                endCriterion=true;
            }
            printSummary(summaryDir, 1, localTimer, Integer.toString(iterationsCounter) , currentSol);
            
            //if(tipomov == 0) currentSol = new TreatmentPlan(bestNeighbour);
            neighbourhood = null;
            bestNeighbour = null;
            
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
            iterationsCounter++;
        }
       
        System.out.println ("Pareto Local Search has finished");
        System.out.println("Final Time (secs): " + (System.currentTimeMillis()-localTimer)/1000);
        globalTotalObjFuncEval = globalTotalObjFuncEval + totalObjFuncEval;
        printSummary(summaryDir, 2, localTimer, Integer.toString(globalIter) , currentSol);
        
        
        return currentSol;
    }
    
    private boolean add_if_better_NDP_List(TreatmentPlan n) {
        int added=0;
        int removed=0;
        boolean contains=false; //ndp contains treatment plan n \in ns
        
        if(n != null){
            contains=false;
            for (TreatmentPlan ndp1 : ndp_mo) {
                if (sameBAC(n.selAngles, ndp1.selAngles)){
                    contains=true;
                    break;
                }
            }
            if (!contains) {
                boolean isDominated=false;
                for (int i = 0; i<ndp_mo.size(); i++) {
                    if (ndp_mo.get(i).isDominated(n)) {
                        ndp_mo.remove(i);
                        removed++;
                        i--;
                    } else if (n.isDominated(ndp_mo.get(i))) {
                        isDominated = true;
                        break;
                    }
                }
                if (!isDominated) {
                    ndp_mo.add(n);
                    added++;
                }else return false;
            }else return false;
        }else return false;
        
        return true;
    }
    
    public TreatmentPlan generateNeighbourhood_paquete( TreatmentPlan sol, 
        int ns, int[][] bs, String cFile, int iteration, int tipomov) throws IOException, InterruptedException, ExecutionException{
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
       
        auxAngles=ChooseNeigbours(tipomov,sol,ns); 
         
        
        /**********Neighbours Evaluation************
        * Evaluate neighbours that must be visited *
        *******************************************/
        
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

            nhood[i].generateDDM(pathFile);
            for(int ind: nhood[i].getOrganos().keySet()){
                Organs organos = nhood[i].getOrganos().get(ind);
                organos.writeMapVoxel(jobID,nhood[i].selAngles);
            }
            nhood[i].generateReferencePoint(sol.getOrganos(), 0, solver, maxX, 1, jobID);

            generatedBACs.add(nhood[i]);                    
            totalObjFuncEval++;

            nhood[i].printSol( cFile);
            nhood[i].liberateMemory();
            //copyDVHs(o,iteration,i); //Maicholl archivo

            /*Only for NEXT DESCENT Local Search*/
            //if ((name=="nextDescent" || selectionCriterion==5) && nhood[i].getSingleObjectiveValue()<sol.getSingleObjectiveValue()){ //&& sol.singleObjectiveValue-nhood[i].singleObjectiveValue>minChange){//Maicholl
            if (add_if_better_NDP_List(nhood[i])){
                System.out.println("*****************************************" );
                System.out.println("*    NEIGHBOURHOOD HAS BEEN GENERATED   *" );
                System.out.println("*****************************************" );
                return nhood[i];
            }
        
        }
        return null;
        
    }
    
    
    public TreatmentPlan MO_PLS_Paquete(TreatmentPlan ini, int tipomov) throws InterruptedException, ExecutionException, IOException {
        ndp_mo = new ArrayList<>();
        generatedBACs = new ArrayList<>(); 

        //public static int poolSize = 3;
        visitedBACs = new ArrayList<>(); 
        localOptima = new ArrayList<>();
        
        TreatmentPlan currentSol = null;
        TreatmentPlan previous = null;
        TreatmentPlan bestNeighbour = null;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        currentSol = new TreatmentPlan(ini);
        
        totalObjFuncEval++;
        generatedBACs.add(currentSol);
        System.out.println("done!");
        addNewNDP(currentSol);
        LinkedList<TreatmentPlan> solToBeVisited = new LinkedList<>();
        
        //Dtermine the list of BACs to ve visited in this iteration
        solToBeVisited.addAll(getSolutionsToBeVisited());
       
        endCriterion = false;
        while (!endCriterion){
            
            TreatmentPlan[][]neighbourhood= new TreatmentPlan[solToBeVisited.size()][neighbourhoodSize];
            
                System.out.println("=====================================================");
                System.out.println("Se está utilizando el tipo de movimiento: " + tipomov);
                System.out.println("=====================================================");
                previous = new TreatmentPlan(currentSol);
                currentSol = new TreatmentPlan(solToBeVisited.get(solToBeVisited.size()-1));
                TreatmentPlan curr = null;
                curr = generateNeighbourhood_paquete(currentSol, neighbourhoodSize, beamletSet, currentFile,iterationsCounter, tipomov);
                System.out.println("Tiempo generada vecindad : "+(System.currentTimeMillis()- initTimer)/1000);// Maicholl
                auxActualTimer = System.currentTimeMillis() ;//Maicholl aqui
                solToBeVisited.get(solToBeVisited.size()-1).visited();
                addNewVisitedBAC(solToBeVisited.get(solToBeVisited.size()-1)); 
            /* After all solutions are visited, a dominance 
             * analysis over the entire set is performed */
            System.out.println("Tiempo inicio actualizacion NDP  " +(System.currentTimeMillis()- auxActualTimer)/1000);// Maicholl
            auxActualTimer = System.currentTimeMillis(); //Maicholl aqui
            
            
            System.out.println("Tiempo final actualizacion NDP  " +(System.currentTimeMillis()- auxActualTimer)/1000);// Maicholl
            auxActualTimer = System.currentTimeMillis(); //Maicholl aqui
            printNDP(tipomov, ndpFile, image_ndpFile, iterationsCounter);
            
            //Dtermine the list of BACs to ve visited in the next iteration
            solToBeVisited = new LinkedList<>();
            solToBeVisited.addAll(getSolutionsToBeVisited());
            
            if (solToBeVisited.size() == 0){
                endCriterion=true;
            }
            printSummary(summaryDir, 1, localTimer, Integer.toString(iterationsCounter) , currentSol);
            
            //if(tipomov == 0) currentSol = new TreatmentPlan(bestNeighbour);
            neighbourhood = null;
            bestNeighbour = null;
            
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
            iterationsCounter++;
        }
       
        System.out.println ("Pareto Local Search has finished");
        System.out.println("Final Time (secs): " + (System.currentTimeMillis()-localTimer)/1000);
        globalTotalObjFuncEval = globalTotalObjFuncEval + totalObjFuncEval;
        printSummary(summaryDir, 2, localTimer, Integer.toString(globalIter) , currentSol);
        
        
        return currentSol;
    }
    
    public static LinkedHashMap<Integer,Double> order_my_hashmap(HashMap<Integer,Double> map_to_order){
        Set<Map.Entry<Integer,Double>> entries = map_to_order.entrySet();
        // Now let's sort HashMap by keys first 
        // all you need to do is create a TreeMap with mappings of HashMap
        // TreeMap keeps all entries in sorted order
        TreeMap<Integer,Double> sorted = new TreeMap<>(map_to_order);
        Set<Map.Entry<Integer,Double>> mappings = sorted.entrySet();

        // Now let's sort the HashMap by values
        // there is no direct way to sort HashMap by values but you
        // can do this by writing your own comparator, which takes
        // Map.Entry object and arrange them in order increasing 
        // or decreasing by values.
        
        Comparator<Map.Entry<Integer,Double>> valueComparator = new Comparator<Map.Entry<Integer,Double>>() {
            
            @Override
            public int compare(Map.Entry<Integer,Double> e1, Map.Entry<Integer,Double> e2) {
                Double v1 = e1.getValue();
                Double v2 = e2.getValue();
                return v1.compareTo(v2);
            }
        };
        
        // Sort method needs a List, so let's first convert Set to List in Java
        List<Map.Entry<Integer,Double>> listOfEntries = new ArrayList<Map.Entry<Integer,Double>>(entries);
        
        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator);
        
        LinkedHashMap<Integer,Double> sortedByValue = new LinkedHashMap<Integer,Double>(listOfEntries.size());
        
        // copying entries from List to Map
        for(Map.Entry<Integer,Double> entry : listOfEntries){
            sortedByValue.put(entry.getKey(), entry.getValue());
        }
        return sortedByValue;
    }
    
    public static HashMap<Integer,Double> readRelaxedMiP(String dir) throws IOException{
        HashMap<Integer,Double> angles_uses = new HashMap<>();
        String sp="\t";
        //String dir = "./inputFile.txt";
        File f = new File(dir);
        BufferedReader fileIn = new BufferedReader(new FileReader(f));
        String line = "";
        line=fileIn.readLine();
        //First read numbero of Organs and angles
        while(line != null){
            if (!line.contains("%")){
                for(int i = 0; i<72;i++){
                    String[] auxReader = line.split(sp);
                    angles_uses.put(Integer.parseInt(auxReader[0]),Double.parseDouble(auxReader[1]));
                    line=fileIn.readLine();
                }
                break;
            }
            line=fileIn.readLine();
        }
        fileIn.close();
        
        return angles_uses;
  }

 //MO_rVNS 
public TreatmentPlan MO_rVNS(TreatmentPlan ini, int tipomov) throws InterruptedException, ExecutionException, IOException {
        ndp_mo = new ArrayList<>();
        generatedBACs = new ArrayList<>(); 

        //public static int poolSize = 3;
        visitedBACs = new ArrayList<>(); 
        localOptima = new ArrayList<>();
        
        TreatmentPlan currentSol = null;
        TreatmentPlan previous = null;
        TreatmentPlan bestNeighbour = null;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;
        currentSol = new TreatmentPlan(ini);
        
        totalObjFuncEval++;
        generatedBACs.add(currentSol);
        System.out.println("done!");
        addNewNDP(currentSol);
        ArrayList<TreatmentPlan> solToBeVisited = new ArrayList<>();
        
        //Dtermine the list of BACs to ve visited in this iteration
        solToBeVisited.addAll(getSolutionsToBeVisited());
        iterationsCounter = 0;
        endCriterion = false;
        while (!endCriterion){
            
            
            TreatmentPlan[][]neighbourhood= new TreatmentPlan[solToBeVisited.size()][neighbourhoodSize];
            for (int i=0;i<solToBeVisited.size();i++){
                System.out.println("=====================================================");
                System.out.println("Se está utilizando el tipo de movimiento: " + tipomov);
                System.out.println("=====================================================");
                previous = new TreatmentPlan(currentSol);
                currentSol = new TreatmentPlan(solToBeVisited.get(i));
                neighbourhood[i] = generateNeighbourhood(currentSol, neighbourhoodSize, beamletSet, currentFile,iterationsCounter, tipomov);
                System.out.println("Tiempo generada vecindad "+i+": "+(System.currentTimeMillis()- initTimer)/1000);// Maicholl
                auxActualTimer = System.currentTimeMillis() ;//Maicholl aqui
                solToBeVisited.get(i).visited();
                addNewVisitedBAC(solToBeVisited.get(i));
            }   
            /* After all solutions are visited, a dominance 
             * analysis over the entire set is performed */
            System.out.println("Tiempo inicio actualizacion NDP  " +(System.currentTimeMillis()- auxActualTimer)/1000);// Maicholl
            auxActualTimer = System.currentTimeMillis(); //Maicholl aqui
            
            for (int i=0;i<solToBeVisited.size();i++){
                update_NDP_List(neighbourhood[i], option);
            }
            System.out.println("Tiempo final actualizacion NDP  " +(System.currentTimeMillis()- auxActualTimer)/1000);// Maicholl
            auxActualTimer = System.currentTimeMillis(); //Maicholl aqui
            printNDP(tipomov, ndpFile, image_ndpFile, iterationsCounter);
            
            //Dtermine the list of BACs to ve visited in the next iteration
            solToBeVisited = new ArrayList<>();
            solToBeVisited.addAll(getSolutionsToBeVisited());
            
            if (solToBeVisited.size() == 0){
                endCriterion=true;
            }
            printSummary(summaryDir, 1, localTimer, Integer.toString(iterationsCounter) , currentSol);
            
            //if(tipomov == 0) currentSol = new TreatmentPlan(bestNeighbour);
            neighbourhood = null;
            bestNeighbour = null;
            
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
            iterationsCounter++;
        }
       
        System.out.println ("MO-rVNS has finished");
        System.out.println("Final Time (secs): " + (System.currentTimeMillis()-localTimer)/1000);
        globalTotalObjFuncEval = globalTotalObjFuncEval + totalObjFuncEval;
        printSummary(summaryDir, 2, localTimer, Integer.toString(globalIter) , currentSol);
        
        
        return currentSol;
    }
   public TreatmentPlan ite_fixed_rMIP_LS(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        try{
            int[] all_ang;
            int[][] auxAngles= new int[neighbourhoodSize][numAngles];
            HashMap <Integer, Double> mip_relaxed = null;
            List<Integer> targetList = null;
            double totalmip = 0;
            long prevTimer = System.currentTimeMillis();
            initialPlan.printSolTime(timerFile,0,prevTimer,"ite_fixed_rMIP_LS begins",false);
            prevTimer = System.currentTimeMillis();
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
            
            if(rMIP_type){
            //  BLoque preparación rMIP 
                mip_relaxed =  readRelaxedMiP("./mip_relaxed.txt");            
                LinkedHashMap<Integer,Double> orderer_angles = order_my_hashmap(mip_relaxed);
                for(Integer idmip : mip_relaxed.keySet()){
                    totalmip += mip_relaxed.get(idmip);
                }
                Set<Integer> bads_angles = orderer_angles.keySet();
                targetList = new ArrayList<>(bads_angles);
                int size_target = targetList.size();
                Collections.reverse(targetList);
                for(int i=size_target-1; i>=rMIP_number_allow;i--){
                    targetList.remove(i);
                }
                for(int i=0; i< targetList.size();i++){
                    targetList.set(i, targetList.get(i)*5);
                }
                for(int i = 0; i < ini.selAngles.length; i++){
                    if(!targetList.contains(ini.selAngles[i])){
                        targetList.add(ini.selAngles[i]);
                    }
                }

                all_ang = new int[targetList.size()];
                for(int i=0; i< targetList.size();i++){
                    all_ang[i] =  targetList.get(i);
                }
                Arrays.sort(all_ang);
            }else{
                all_ang = new int[72];
                targetList = new ArrayList<>();
                for(int p=0;p<72;p++){
                    /*if(p==16){
                        angulos_sel.add(5);
                    }*/
                    all_ang[p] = (p*5);
                }
            } 
            while (!endCriterion){
                System.arraycopy(currentSol.selAngles, 0, prevBAC, 0, numAngles);
                ArrayList<Integer> angletochange = new ArrayList<>();
                for(int f = 0; f < iterSol.selAngles.length; f++){
                    angletochange.add(iterSol.selAngles[f]);
                }
                Collections.shuffle(angletochange, new Random());

                for(int z=0;z<numAngles;z++){
                    int selected_z = angletochange.remove(0);
                    int ind_sel = -1;
                    if(strategy.compareToIgnoreCase("ND")==0){
                        for(int i = 0; i < iterSol.selAngles.length; i++){
                            if(iterSol.selAngles[i] == selected_z){
                                ind_sel = i;
                            }

                        }
                    }else{
                        for(int i = 0; i < currentSol.selAngles.length; i++){
                            if(currentSol.selAngles[i] == selected_z){
                                ind_sel = i;
                            }

                        }
                    }
                    System.out.println("################################################");
                    System.out.println("# Changing the beam angle "+ind_sel+" #");
                    System.out.println("################################################");
                    //bestNeighbour = manual_FMO(iterSol, z, bac_explored);
                    if(strategy.compareToIgnoreCase("ND")==0){
                        initialPlan.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed begins",false);
                        prevTimer = System.currentTimeMillis();
                        bestNeighbour = generateNeighbourhood_rMIP_fixed(iterSol, neighbourhoodSize, 
                            beamletSet, currentFile,iterationsCounter, z, ind_sel, all_ang);
                    }else{
                        initialPlan.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed begins",false);
                        prevTimer = System.currentTimeMillis();
                        bestNeighbour = generateNeighbourhood_rMIP_fixed(currentSol, neighbourhoodSize, 
                            beamletSet, currentFile,iterationsCounter, z, ind_sel, all_ang);
                    }
                    if (bestNeighbour.getSingleObjectiveValue()<iterSol.getSingleObjectiveValue()){
                        iterSol = new TreatmentPlan(bestNeighbour);
                        iterSol.printSolTime(timerFile,0,prevTimer,"rMIP for beam angle "+(z+1)+" ended, getting better solution",true);
                        prevTimer = System.currentTimeMillis();
                        printCurrent(iterSol);
                        try {
                            iterSol.printSol(bestFile,z);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }        
                    }else{
                        currentSol.printSolTime(timerFile,0,prevTimer,"rMIP for beam angle "+(z+1)+" ended, worst solution ",false);
                        prevTimer = System.currentTimeMillis();
                    }
                }



                if (iterSol.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                    currentSol = new TreatmentPlan(iterSol);
                    currentSol.printSolTime(timerFile,0,prevTimer,"rMIP ended with better solution",true);
                    prevTimer = System.currentTimeMillis();
                    printCurrent(currentSol);
                    try {
                        currentSol.printSol(bestFile,0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }    
                }else{
                    endCriterion = true;
                    currentSol.printSolTime(timerFile,0,localTimer,"rMIP ended, algorithm ended",true);
                    prevTimer = System.currentTimeMillis();
                }

                if (!checkFile.exists()) {
                    endCriterion = true;
                    System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
                }
            }
            printSolution(localTimer, currentSol);
        } catch (IOException e) {
            
            
        
        } finally {
            ampl.close();
            ampl = null;
        }
        return currentSol;
    }
    

    public TreatmentPlan generateNeighbourhood_rMIP_fixed( TreatmentPlan sol, 
            int ns, int[][] bs, String cFile, int iteration, int ang_sel, int ind_sel, int[] all_ang) throws IOException, InterruptedException, ExecutionException{
        
        
        
        ArrayList<TreatmentPlan> prev_BACs = new ArrayList<>();
        long prevTimer = System.currentTimeMillis();
        int[] fixed;
        int angle;
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
        int aux_cont=0;
        fixed = new int[4];
        for(int i=0;i<sol.selAngles.length;i++){
            if(i!=ind_sel){
                fixed[aux_cont] = sol.selAngles[i];
                aux_cont++;
            }
        }
        Arrays.sort(fixed);
        //auxAngles=ChooseNeigbours_mip_manual(angulos_doc,sol,ns,previous_angles,indexBAC); 
        
        
        /**********Neighbours Evaluation************
        * Evaluate neighbours that must be visited *
        *******************************************/
        
                
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
            ArrayList<Integer> all_angles_aux;
            all_angles_aux = new ArrayList<>();
            if(geom_rest){
                ArrayList<Integer> restricted_aux = new ArrayList<>();
                for(int i = 0; i < fixed.length; i++){
                    int a = repairAngle(fixed[i]-5);
                    int b = repairAngle(fixed[i]+5);
                    int c = repairAngle(fixed[i]-10);
                    int d = repairAngle(fixed[i]+10);
                    int e = repairAngle(fixed[i]-15);
                    int f = repairAngle(fixed[i]+15);
                    if(!Arrays.asList(fixed).contains(a))
                        restricted_aux.add(a);
                    if(!Arrays.asList(fixed).contains(b))
                        restricted_aux.add(b);
                    if(!Arrays.asList(fixed).contains(c))
                        restricted_aux.add(c);
                    if(!Arrays.asList(fixed).contains(d))
                        restricted_aux.add(d);
                    if(!Arrays.asList(fixed).contains(e))
                        restricted_aux.add(e);
                    if(!Arrays.asList(fixed).contains(f))
                        restricted_aux.add(f);
                }
                for(int i = 0; i < all_ang.length; i++){
                    if(!restricted_aux.contains(all_ang[i])){
                        all_angles_aux.add(all_ang[i]);
                    }
                }
                all_ang = new int[all_angles_aux.size()];
                for(int i = 0; i < all_angles_aux.size(); i++){
                    all_ang[i] = all_angles_aux.get(i);
                }
                
            }
            HashMap<Integer,Integer> map_angles_index = new HashMap<>();
            HashMap<Integer,Integer> map_index_angles = new HashMap<>();
            for(int k = 0; k < all_ang.length; k++){
                map_angles_index.put(all_ang[k], k+1);
                map_index_angles.put(k+1, all_ang[k]);
            }
            currentSol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed: Preparation ended, generating DDMs...",false);
            prevTimer = System.currentTimeMillis();
            generatedfixed.add(fixed);
            sol.generateDDM(pathFile, all_ang);
            for(int ind: sol.getOrganos().keySet()){
                Organs organos = sol.getOrganos().get(ind);
                organos.writeMapVoxel(jobID,all_ang);
            }
            currentSol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed: DDMs Generated, starting fixed rMIP...",false);
            prevTimer = System.currentTimeMillis();
            ampl = sol.generateReferencePoint(sol.getOrganos(), 0, solver, maxX, 1, jobID, all_ang,  fixed, ampl,timerFile);
            currentSol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed: fixed rMIP ended...",false);
            currentSol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed: starting FMO of fixed beam angle with best of beam vector...",false);
            prevTimer = System.currentTimeMillis();
            aux_sol = new TreatmentPlan(currentSol);
            for (int i = 0; i<aux_sol.selAngles.length;i++){
                if(i <aux_sol.selAngles.length-1){
                    aux_sol.selAngles[i] = fixed[i]; 
                }else{
                    aux_sol.selAngles[i] =  map_index_angles.get((int) sol.beams_vector[4][0]+1); 
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
                generatedBACs.add(new TreatmentPlan(aux_sol)); 
            }
            currentSol.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed: Solved FMO of fixed beam angle with best of beam vector...",false);
            prevTimer = System.currentTimeMillis();
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
    
    public TreatmentPlan seq_sel_LS(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        long prevTimer = System.currentTimeMillis();
        initialPlan.printSolTime(timerFile,0,prevTimer,"seq_sel_LS begins",false);
        prevTimer = System.currentTimeMillis();
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
                int selected_z = angletochange.remove(0);
                int ind_sel = -1;
                if(strategy.compareToIgnoreCase("ND")==0){
                    for(int i = 0; i < iterSol.selAngles.length; i++){
                        if(iterSol.selAngles[i] == selected_z){
                            ind_sel = i;
                        }

                    }
                    initialPlan.printSolTime(timerFile,0,prevTimer,"manual_FMO begins",false);
                    prevTimer = System.currentTimeMillis();
                    bestNeighbour = manual_FMO(iterSol, z, ind_sel);
                }else{
                    for(int i = 0; i < currentSol.selAngles.length; i++){
                        if(currentSol.selAngles[i] == selected_z){
                            ind_sel = i;
                        }

                    }
                    currentSol.printSolTime(timerFile,0,prevTimer,"manual_FMO for beam angle "+(z+1)+" begins",false);
                    prevTimer = System.currentTimeMillis();
                    bestNeighbour = manual_FMO(currentSol, z, ind_sel);
                    
                }
                
                
                if (bestNeighbour.getSingleObjectiveValue()<iterSol.getSingleObjectiveValue()){
                    iterSol = new TreatmentPlan(bestNeighbour);
                    iterSol.printSolTime(timerFile,0,prevTimer,"manual_FMO for beam angle "+(z+1)+" ended, getting better solution",true);
                    prevTimer = System.currentTimeMillis();
                }else{
                    currentSol.printSolTime(timerFile,0,prevTimer,"manual_FMO for beam angle "+(z+1)+" ended, bad solution ",false);
                    prevTimer = System.currentTimeMillis();
                }
            }
            
            
            
            if (iterSol.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(iterSol);
                currentSol.printSolTime(timerFile,0,prevTimer,"manual_FMO ended with better solution",true);
                prevTimer = System.currentTimeMillis();
                
            }else{
                endCriterion = true;
                currentSol.printSolTime(timerFile,0,localTimer,"manual_FMO ended, algorithm ended",true);
            }
            
            if (!checkFile.exists()) {
                endCriterion = true;
                System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
            }
        }
        printSolution(localTimer, currentSol);
        
        return currentSol;
    }
    
    public TreatmentPlan manual_FMO(TreatmentPlan ini, int indexBAC, int ind_sel) throws InterruptedException, ExecutionException, IOException {
        ArrayList<TreatmentPlan> prev_BACs = new ArrayList<>();
        
        HashMap <Integer, Double> mip_relaxed = null;
        double totalmip = 0;
        
        long prevTimer = System.currentTimeMillis();
        int[] previous_angles = new int[numAngles];
        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;  
        
        if(rMIP_type){
        //  BLoque preparación rMIP 
            mip_relaxed =  readRelaxedMiP("./mip_relaxed.txt");            
            LinkedHashMap<Integer,Double> orderer_angles = order_my_hashmap(mip_relaxed);
            for(Integer idmip : mip_relaxed.keySet()){
                totalmip += mip_relaxed.get(idmip);
            }
            Set<Integer> bads_angles = orderer_angles.keySet();
            targetList = new ArrayList<>(bads_angles);
            int size_target = targetList.size();
            Collections.reverse(targetList);
            for(int i=size_target-1; i>=rMIP_number_allow;i--){
                targetList.remove(i);
            }
            for(int i=0; i< targetList.size();i++){
                targetList.set(i, targetList.get(i)*5);
            }
        }else{
            targetList = new ArrayList<>();
            for(int p=0;p<72;p++){
                /*if(p==16){
                    angulos_sel.add(5);
                }*/
                targetList.add(p*5);
            }
        }     
        currentSol = new TreatmentPlan(ini);
        for(int t=0;t<numAngles;t++){
            previous_angles[t] = currentSol.selAngles[t];
            int index = targetList.indexOf(previous_angles[t]);
            if(index != -1)
                targetList.remove(index);
        }
        currentSol.printSolTime(timerFile,0,prevTimer,"manual_FMO: Preparation ended",false);
        prevTimer = System.currentTimeMillis();
        while (!endCriterion){
            TreatmentPlan[] neighbourhood;
            
            currentSol.printSolTime(timerFile,0,prevTimer,"generating 10 Neighbourhood of mip_manual...",false);
            prevTimer = System.currentTimeMillis();
            neighbourhood = generateNeighbourhood_mip_manual(currentSol, neighbourhoodSize, 
                    beamletSet, currentFile,iterationsCounter, targetList, previous_angles,ind_sel, prev_BACs);
            
            bestNeighbour = new TreatmentPlan(getBestNeighbour(iterationsCounter,neighbourhood,neighbourhoodSize));
            
            if (bestNeighbour.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                currentSol = new TreatmentPlan(bestNeighbour);
                currentSol.printSolTime(timerFile,0,prevTimer,"ended 10 Neighbourhood of mip_manual... with better best Neighbour",true);
                prevTimer = System.currentTimeMillis();
                for(TreatmentPlan ns:neighbourhood){
                    currentSol.copyAngle(ns, ns.selAngles);

                }
                printCurrent(currentSol);
                try {
                    currentSol.printSol(bestFile,indexBAC);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }         
            }else{
                currentSol.printSolTime(timerFile,0,prevTimer,"ended 10 Neighbourhood of mip_manual... NO best Neighbour",false);
                prevTimer = System.currentTimeMillis();
            }
            if(targetList.size()<1)
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
    

    public TreatmentPlan[] generateNeighbourhood_mip_manual( TreatmentPlan sol, 
            int ns, int[][] bs, String cFile, int iteration, List<Integer> angulos_doc, 
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
                        repited_auxAngles.add(i);
                        repited_bac_explored.add(j);
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
                    Callable<TreatmentPlan> gNeighbour = new BAO2.getNeighbour(auxAngles[i], sol, cFile, Integer.toString(i+1));
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
                        generatedBACs.add(new TreatmentPlan(nhood[i]));  
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
                    generatedBACs.add(new TreatmentPlan(nhood[i]));  
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
 
    public int[][] ChooseNeigbours_mip_manual(List<Integer> angulos_sels, TreatmentPlan sol, int ns, int[] previous_angles, int indexBAC) throws IOException{ //Maicholl Agregado para VNS
        //Crear los vecinos quitando angulos de angulos_sels 
        int[][] auxAngles= new int[ns][numAngles];
        HashMap <Integer, Double> mip_relaxed = null;
        List<Integer> targetList = null;
        double totalmip = 0;
        
       
        auxAngles = getNeigboursAllSize_mip_manual(sol, ns,angulos_sels, previous_angles, indexBAC); // Ángulo +-5
          
        return auxAngles;
    }
    
    public int[][] getNeigboursAllSize_mip_manual(TreatmentPlan sol, int ns, List<Integer> angulos_sels,int[] previous_angles, int indexBAC){
        int[][] auxAngles= new int[ns][numAngles];
        int neighbour = 0, angle;        
        for (int j = 0; j<numAngles;j++){
            
            for (int i = 0; i<numAngles;i++){
                auxAngles[neighbour][i]=previous_angles[i];
                auxAngles[neighbour+1][i]=previous_angles[i];
            }
            angle=(int)auxAngles[neighbour][j];
            
            if(angulos_sels.size()>=1)
                auxAngles[neighbour][indexBAC] = angulos_sels.remove(0);
            if(angulos_sels.size()>=1)
                auxAngles[neighbour+1][indexBAC] = angulos_sels.remove(0);
            
            
            neighbour = neighbour+2;
        }
        for (int i = 0; i<auxAngles.length;i++){
             bubbleSort(auxAngles[i]);   
        }
        return auxAngles;
    }
    
    public TreatmentPlan ite_fixed_rMIP_bestx_LS(TreatmentPlan ini, int n_bests) throws InterruptedException, ExecutionException, IOException {
        try{
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
                    int selected_z = angletochange.remove(0);
                    int ind_sel = -1;
                    if(strategy.compareToIgnoreCase("ND")==0){
                        for(int i = 0; i < iterSol.selAngles.length; i++){
                            if(iterSol.selAngles[i] == selected_z){
                                ind_sel = i;
                            }

                        }
                    }else{
                        for(int i = 0; i < currentSol.selAngles.length; i++){
                            if(currentSol.selAngles[i] == selected_z){
                                ind_sel = i;
                            }

                        }
                    }
                    System.out.println("################################################");
                    System.out.println("# Changing the beam angle "+ind_sel+" #");
                    System.out.println("################################################");
                    //bestNeighbour = manual_FMO(iterSol, z, bac_explored);
                    if(strategy.compareToIgnoreCase("ND")==0)
                        bestNeighbour = generateNeighbourhood_rMIP_fixed_top10(iterSol, neighbourhoodSize, 
                            beamletSet, currentFile,iterationsCounter, angulos_sel, z, ind_sel,n_bests);
                    else
                        bestNeighbour = generateNeighbourhood_rMIP_fixed_top10(currentSol, neighbourhoodSize, 
                            beamletSet, currentFile,iterationsCounter, angulos_sel, z, ind_sel,n_bests);
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
        } catch (IOException e) {
            
            
        
        } finally {
            ampl.close();
            ampl = null;
        }
        return currentSol;
    }
    
    public TreatmentPlan generateNeighbourhood_rMIP_fixed_top10( TreatmentPlan sol, 
            int ns, int[][] bs, String cFile, int iteration, int[] all_ang, int ang_sel, int ind_sel, int n_bests) throws IOException, InterruptedException, ExecutionException{
        int[] fixed;
        int angle;
        int[][] auxAngles = new int[ns][numAngles];
        
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
            ampl = sol.generateReferencePoint(sol.getOrganos(), 0, solver, maxX, 1, jobID, all_ang,  fixed, ampl,timerFile);

            
            
            
            
            
            ArrayList<int[]> to_solve = new ArrayList<>();
            for(int i = 0; i < n_bests; i++){
                int[] aux_bac = new int[fixed.length+1];
                for(int j = 0; j < fixed.length; j++){
                    aux_bac[j] = fixed[j];
                }
                aux_bac[fixed.length] = ((int) sol.beams_vector[i+4][0])*5;
                bubbleSort(aux_bac);
                to_solve.add(aux_bac);
            }
            aux_sol = new TreatmentPlan(sol);
            while(to_solve.size()>0){
                for(int j = 0; j < ns; j++){
                    if(to_solve.size() > 0){
                        int[] aux_tosolve = to_solve.removeLast();
                        for(int k = 0; k < numAngles; k++){
                            auxAngles[j][k] = aux_tosolve[k];
                        }
                    }else{
                        for(int k = 0; k < numAngles; k++){
                            auxAngles[j][k] = sol.selAngles[k];
                        }
                    }
                    
                }
                TreatmentPlan[] aux_nhood;
                aux_nhood = generateNeighbourhood_top_manual( sol, ns, cFile, iteration, auxAngles);
                for(int i = 0; i < aux_nhood.length; i++ ){
                    if(aux_nhood[i].getSingleObjectiveValue() < aux_sol.getSingleObjectiveValue()){
                        aux_sol = new TreatmentPlan(aux_nhood[i]);
                    }
                }
                
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
    
    public TreatmentPlan[] generateNeighbourhood_top_manual( TreatmentPlan sol, 
            int ns, String cFile, int iteration, int[][] auxAngles) throws IOException, InterruptedException, ExecutionException{
        int angle;
            TreatmentPlan[] nhood = new TreatmentPlan [ns];
       
        /**********Calculate BAC to be Visited**********
        * We generate (2 * #beams) neighbours of our   *
        * current solution. Each beam angle is modified*
        * in +/- "step" degrees.                       *
        ***********************************************/
       
        ArrayList<Integer> repited_auxAngles = new ArrayList<>();
        ArrayList<Integer> repited_bac_explored = new ArrayList<>();
        if(generatedBACs.size()>0){
            for(int i=0;i<auxAngles.length;i++){
                for(int j=0;j<generatedBACs.size();j++){
                    if(Arrays.equals(auxAngles[i], generatedBACs.get(j).selAngles)){
                        repited_auxAngles.add(i);
                        repited_bac_explored.add(j);
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
                    Callable<TreatmentPlan> gNeighbour = new BAO2.getNeighbour(auxAngles[i], sol, cFile, Integer.toString(i+1));
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
                        generatedBACs.add(new TreatmentPlan(nhood[i]));  
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
                    generatedBACs.add(new TreatmentPlan(nhood[i]));  
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
    
    public TreatmentPlan ite_fixed_rMIP_restr_LS(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        try{
            int[] all_ang;
            int[][] auxAngles= new int[neighbourhoodSize][numAngles];
            HashMap <Integer, Double> mip_relaxed = null;
            List<Integer> targetList = null;
            double totalmip = 0;
            long prevTimer = System.currentTimeMillis();
            initialPlan.printSolTime(timerFile,0,prevTimer,"ite_fixed_rMIP_LS begins",false);
            prevTimer = System.currentTimeMillis();
            geom_rest = true;
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
            if(rMIP_type){
            //  BLoque preparación rMIP 
                mip_relaxed =  readRelaxedMiP("./mip_relaxed.txt");            
                LinkedHashMap<Integer,Double> orderer_angles = order_my_hashmap(mip_relaxed);
                for(Integer idmip : mip_relaxed.keySet()){
                    totalmip += mip_relaxed.get(idmip);
                }
                Set<Integer> bads_angles = orderer_angles.keySet();
                targetList = new ArrayList<>(bads_angles);
                int size_target = targetList.size();
                Collections.reverse(targetList);
                for(int i=size_target-1; i>=rMIP_number_allow;i--){
                    targetList.remove(i);
                }
                for(int i=0; i< targetList.size();i++){
                    targetList.set(i, targetList.get(i)*5);
                }
                for(int i = 0; i < ini.selAngles.length; i++){
                    if(!targetList.contains(ini.selAngles[i])){
                        targetList.add(ini.selAngles[i]);
                    }
                }

                all_ang = new int[targetList.size()];
                for(int i=0; i< targetList.size();i++){
                    all_ang[i] =  targetList.get(i);
                }
                Arrays.sort(all_ang);
            }else{
                all_ang = new int[72];
                targetList = new ArrayList<>();
                for(int p=0;p<72;p++){
                    /*if(p==16){
                        angulos_sel.add(5);
                    }*/
                    all_ang[p] = (p*5);
                }
            } 
            while (!endCriterion){
                System.arraycopy(currentSol.selAngles, 0, prevBAC, 0, numAngles);
                ArrayList<Integer> angletochange = new ArrayList<>();
                for(int f = 0; f < iterSol.selAngles.length; f++){
                    angletochange.add(iterSol.selAngles[f]);
                }
                Collections.shuffle(angletochange, new Random());

                for(int z=0;z<numAngles;z++){
                    int selected_z = angletochange.remove(0);
                    int ind_sel = -1;
                    if(strategy.compareToIgnoreCase("ND")==0){
                        for(int i = 0; i < iterSol.selAngles.length; i++){
                            if(iterSol.selAngles[i] == selected_z){
                                ind_sel = i;
                            }

                        }
                    }else{
                        for(int i = 0; i < currentSol.selAngles.length; i++){
                            if(currentSol.selAngles[i] == selected_z){
                                ind_sel = i;
                            }

                        }
                    }
                    System.out.println("################################################");
                    System.out.println("# Changing the beam angle "+ind_sel+" #");
                    System.out.println("################################################");
                    //bestNeighbour = manual_FMO(iterSol, z, bac_explored);
                    if(strategy.compareToIgnoreCase("ND")==0){
                        initialPlan.printSolTime(timerFile,z,prevTimer,"generateNeighbourhood_rMIP_fixed begins",false);
                        prevTimer = System.currentTimeMillis();
                        bestNeighbour = generateNeighbourhood_rMIP_fixed(iterSol, neighbourhoodSize, 
                            beamletSet, currentFile,iterationsCounter, z, ind_sel, all_ang);
                    }else{
                        initialPlan.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood_rMIP_fixed begins",false);
                        prevTimer = System.currentTimeMillis();
                        bestNeighbour = generateNeighbourhood_rMIP_fixed(currentSol, neighbourhoodSize, 
                            beamletSet, currentFile,iterationsCounter, z, ind_sel, all_ang);
                    }
                    if (bestNeighbour.getSingleObjectiveValue()<iterSol.getSingleObjectiveValue()){
                        iterSol = new TreatmentPlan(bestNeighbour);
                        iterSol.printSolTime(timerFile,z,prevTimer,"rMIP for beam angle "+(z+1)+" ended, getting better solution",true);
                        prevTimer = System.currentTimeMillis();
                        printCurrent(iterSol);
                        try {
                            iterSol.printSol(bestFile,z);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }        
                    }else{
                        currentSol.printSolTime(timerFile,z,prevTimer,"rMIP for beam angle "+(z+1)+" ended, worst solution ",false);
                        prevTimer = System.currentTimeMillis();
                    }
                }



                if (iterSol.getSingleObjectiveValue()<currentSol.getSingleObjectiveValue()){
                    currentSol = new TreatmentPlan(iterSol);
                    currentSol.printSolTime(timerFile,0,prevTimer,"rMIP ended with better solution",true);
                    prevTimer = System.currentTimeMillis();
                    printCurrent(currentSol);
                    try {
                        currentSol.printSol(bestFile,0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }    
                }else{
                    endCriterion = true;
                    currentSol.printSolTime(timerFile,0,localTimer,"rMIP ended, algorithm ended",true);
                }

                if (!checkFile.exists()) {
                    endCriterion = true;
                    System.out.println("ALGORITHM HAS BEEN SHUT DOWN. CHECKFILE NOT FOUND");
                }
            }
            printSolution(localTimer, currentSol);
        } catch (IOException e) {
            
            
        
        } finally {
            ampl.close();
            ampl = null;
        }
        
        return currentSol;
    }
    
    public TreatmentPlan paramTest(TreatmentPlan ini) throws InterruptedException, ExecutionException, IOException {
        int[] eud_ptv = {77};
        int[] a_1 = {-100};
        int[] a_2 = {8,20};
        int[] a_3 = {2,8,20};
        int[] a_4 = {2,8,20};
        long prevTimer = System.currentTimeMillis();
        initialPlan.printSolTime(timerFile,0,prevTimer,"localSearch begins",false);
        prevTimer = System.currentTimeMillis();
        TreatmentPlan currentSol;
        TreatmentPlan bestNeighbour;
        Random r = new Random();
        long localTimer = System.currentTimeMillis();
        File checkFile = new File("./checkFiles/"+jobID+"checkFile.txt");
        int iterationsLimit;
        boolean endCriterion = false;
        iterationsLimit = iterations;  
        currentSol = new TreatmentPlan(ini);
        
            TreatmentPlan[] neighbourhood;
            
            initialPlan.printSolTime(timerFile,0,prevTimer,"generateNeighbourhood begins",false);
            prevTimer = System.currentTimeMillis();
            // generate the neighbourhood
            currentSol = generatealloptions(currentSol, neighbourhoodSize, beamletSet, currentFile,iterationsCounter, eud_ptv,a_1,a_2,a_3,a_4);
            
            // gets the best neighbour
           // bestNeighbour = new TreatmentPlan(getBestNeighbour(iterationsCounter,neighbourhood,neighbourhoodSize));
            
            
            bestNeighbour = null;
            
            iterationsCounter++;
        
        printSolution(localTimer, currentSol);
        
        return currentSol;
    }
    
    public TreatmentPlan generatealloptions( TreatmentPlan sol, 
        int ns, int[][] bs, String cFile, int iteration, int[] eud_ptv, int[] a_1, int[] a_2, int[] a_3, int[] a_4) throws IOException, InterruptedException, ExecutionException{
        long prevTimer = System.currentTimeMillis();
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
       
        //auxAngles=ChooseNeigbours(tipomov,sol,ns); 
        if (parallelNeighbourhoodGeneration){        
                                
            //getNeighbour[] gNeighbour = new getNeighbour[ns]; //Callables
            Future<TreatmentPlan> ft; //Callables
            ExecutorService pool = new ThreadPoolExecutor(10,10,0L,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(10),new ThreadPoolExecutor.CallerRunsPolicy());
            //create Callables and FutureTasks
            //CountDownLatch latch = new CountDownLatch(ns);
            int h = 0;
            for(int i=0;i<eud_ptv.length;i++){
                int ptv = eud_ptv[i];
                for(int j=0;j<a_1.length;j++){
                    int a1 = a_1[j];
                    for(int k=0;k<a_2.length;k++){
                        int a2 = a_2[k];
                        for(int l=0;l<a_3.length;l++){
                            int a3 = a_3[l];
                            for(int m=0;m<a_4.length;m++){
                                int a4 = a_4[m];
                                System.out.println("====================================" );
                                System.out.println("GENERATING NEIGHBOURHOOD FOR BAC: " );
                                System.out.println("====================================" );

                                System.out.println("eud_ptv = "+ptv+" , a1 = "+a1+" , a2 = "+a2+"  , a3 = "+a3+" , a4 = "+a4);
                                int[] params = {ptv,a1,a2,a3,a4};
                                sol.printSolTime(timerFile,0,prevTimer,"solving FMO with... eud_ptv = "+ptv+" , a1 = "+a1+" , a2 = "+a2+"  , a3 = "+a3+" , a4 = "+a4,false);
                                prevTimer = System.currentTimeMillis();
                                
                                Callable<TreatmentPlan> parallelNeighbour = new BAO2.parallelNeighbour(sol.selAngles, sol, cFile, Integer.toString(h+1),params);
                                ft = new FutureTask<>(parallelNeighbour);
                                ft = pool.submit(parallelNeighbour);
                    
                                h++;
                            }
                        }
                    }
                }
            }    
             
            pool.shutdown();
        }else{
            for(int i=0;i<eud_ptv.length;i++){
                int ptv = eud_ptv[i];
                for(int j=0;j<a_1.length;j++){
                    int a1 = a_1[j];
                    for(int k=0;k<a_2.length;k++){
                        int a2 = a_2[k];
                        for(int l=0;l<a_3.length;l++){
                            int a3 = a_3[l];
                            for(int m=0;m<a_4.length;m++){
                                int a4 = a_4[m];
                                System.out.println("====================================" );
                                System.out.println("GENERATING NEIGHBOURHOOD FOR BAC: " );
                                System.out.println("====================================" );

                                System.out.println("eud_ptv = "+ptv+" , a1 = "+a1+" , a2 = "+a2+"  , a3 = "+a3+" , a4 = "+a4);
                                int[] params = {ptv,a1,a2,a3,a4};
                                sol.printSolTime(timerFile,0,prevTimer,"solving FMO with... eud_ptv = "+ptv+" , a1 = "+a1+" , a2 = "+a2+"  , a3 = "+a3+" , a4 = "+a4,false);
                                prevTimer = System.currentTimeMillis();
                                TreatmentPlan aux = new TreatmentPlan(sol);
                                aux.generateDDM(pathFile);
                                for(int ind: aux.getOrganos().keySet()){
                                    Organs organos = aux.getOrganos().get(ind);
                                    organos.writeMapVoxel(jobID,aux.selAngles);
                                }
                                aux.generateReferencePoint_ajust(sol.getOrganos(), 0, solver, maxX, 1, jobID, params);


                                totalObjFuncEval++;

                                aux.printSol( cFile);
                                aux.liberateMemory();
                                Organs[] organos = new Organs[aux.getOrganos().size()];
                                int cont = 0;
                                for(int ind: aux.getOrganos().keySet()){
                                    organos[cont] = aux.getOrganos().get(ind);
                                    cont++;
                                }
                                createDVH_CSV(organos,jobID,params); //Maicholl archivo
                                sol.printSolTime(timerFile,0,prevTimer,"ended FMO with... eud_ptv = "+ptv+" , a1 = "+a1+" , a2 = "+a2+"  , a3 = "+a3+" , a4 = "+a4,false);
                                prevTimer = System.currentTimeMillis();


                            }
                        }
                    }
                }
            }
        }
        //sol.printSolTime(timerFile,0,prevTimer,"ended 10 Neighbourhood of generateNeighbourhood...",true);
        prevTimer = System.currentTimeMillis();
        System.out.println("*****************************************" );
        System.out.println("*    NEIGHBOURHOOD HAS BEEN GENERATED   *" );
        System.out.println("*****************************************" );
        return sol;
    }
    
    public String createDVH_CSV(Organs[] o, String jobID, int[] params)
            throws IOException{//katty
        String dir;
        //String input="./Pruebas/inputFile_"+jobThreadID+".txt";
        
        int n=0;
        
        String csvFile="./DVHs/"+jobID+"_DVH_ptvub_"+params[0]+"_a1_"+params[1]+"_a2_"+params[2]+"_a3_"+params[3]+"_a4_"+params[4]+".csv";
        File file = new File(csvFile);
        if (file.createNewFile()){
            writeToFile(csvFile,
                        "Type,Dose,Volume\n");
            for (int j = 0; j< o.length; j++) {

                ArrayList<Double> dose = new ArrayList<Double>();
                dir = jobID + "DVH_" + o[j].getName() + ".txt";
                dose=readFileDVH(dir);
    //            System.out.println(dose);
                float sizeAux = dose.size();
                float porcentaje = 0;
                for(int i=0;i<dose.size();i++){
                    porcentaje=((sizeAux-i)/sizeAux)*100;
                    writeToFile(csvFile,
                            o[j].getName()+","+dose.get(i)+","+porcentaje+"\n");
                }
            }
           // createJuliaScriptFile(input,"./Pruebas/",jobThreadID+"_DataDVH_"+n+".csv");
           return csvFile;
        }else{
            System.out.println("File "+csvFile+" already exist, please delete this file or change number in file ");
            return "";
        }
        
    }
    
    public static void writeToFile(String dirFile,String line)throws IOException{
        File file =new File(dirFile);    

        //if file doesnt exists, then create it    
        if(!file.exists()){    
            file.createNewFile();    
            //System.out.println("New File Created Now");    
        }    

        //true = append file    
            FileWriter fileWritter = new FileWriter(file,true);        
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(line);
            bufferWritter.close();
            fileWritter.close();
    }
    
    public ArrayList<Double> readFileDVH(String dir) 
            throws FileNotFoundException, IOException{
        ArrayList<Double> testList = new ArrayList<Double>(); //dosis
        //String sp="\t";
        //String dir = "./test2-OptDVH_Rectum.txt";
        File f = new File(dir);
        BufferedReader fileIn = new BufferedReader(new FileReader(f));
        String line = "";
        line = fileIn.readLine();
        int aux = 1;
        while (line != null) {
            if (!line.contains("%") && !line.contains("sum") && !line.contains("voxel") && !line.contains("inte") && !line.contains(":=") && !line.contains("[") && !line.contains("]")) {
                String[] auxReader = line.split("\\s");
                for (int i = 0; i < auxReader.length; i++) {
                    if (!auxReader[i].equals("")) {
                        if ((aux % 2) == 0) {
                            //System.out.println("i= "+i+" value= "+auxReader[i]);
                            testList.add(Double.parseDouble(auxReader[i]));
                        }
                        aux++;
                    }
                }
            }
            line = fileIn.readLine();
        }
        fileIn.close();
        Collections.sort(testList);
        return testList;
    }
    
    // Parallel runs
    public class parallelNeighbour implements Callable<TreatmentPlan> {
        private int[] auxAngles;
        private TreatmentPlan sol;
        private final String cFile;
        private final String processName;
        private Organs[] org;
        private int[] params;

        public parallelNeighbour(int[] auxAng, TreatmentPlan currentSol, String cF, String neighbourNum, int[] params){
            Thread.currentThread().setName(neighbourNum);
            this.processName = jobID + "_" + Thread.currentThread().getName();
            System.out.println("Generating Neighbour Num: " + processName);
            auxAngles = new int[auxAng.length];
            System.arraycopy(auxAng, 0, auxAngles, 0, auxAngles.length);
            this.params = new int[params.length];
            System.arraycopy(params, 0, this.params, 0, params.length);
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

            neighbour.generateReferencePoint_ajust(sol.getOrganos(), 0, solver, maxX, 1, this.processName,params);
            //neighbour.getVxDx(this.processName, this.org, Vx, Dx);
            //synchronized(visitedBACs){
                //addNewVisitedBAC(neighbour); //Add Angles and Reference Point
            //    generatedBACs.add(neighbour);
            //}
            totalObjFuncEval++;

            neighbour.printSol(cFile);
            neighbour.liberateMemory();
            Organs[] organos = new Organs[neighbour.getOrganos().size()];
            int cont = 0;
            for(int ind: neighbour.getOrganos().keySet()){
                organos[cont] = neighbour.getOrganos().get(ind);
                cont++;
            }
            createDVH_CSV(organos,this.processName,params); //Maicholl archivo
            
            

            return neighbour;
        }
    }
    public static ExecutorService newFixedThreadPool(int nThreads){
    
        return new ThreadPoolExecutor(nThreads,nThreads,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    }
}

