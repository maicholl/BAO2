/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bao2;

import com.ampl.AMPL;
import com.ampl.DataFrame;
import com.ampl.Objective;
import com.ampl.Parameter;
import com.ampl.Tuple;
import com.ampl.Variable;
import java.io.IOException;

/**
 *
 * @author Maiik
 */
public class KnitroJava {

    public void amplcito(){
        // Create an AMPL instance
        AMPL ampl = new AMPL();

        try {
          int presolve;
          // Get the value of the option presolve and print
          presolve = ampl.getIntOption("presolve");
          System.out.format("AMPL presolve is %d%n", presolve);
          // Set the value to false (maps to 0)
          ampl.setBoolOption("presolve", false);
          // Get the value of the option presolve and print
          presolve = ampl.getIntOption("presolve");
          System.out.format("AMPL presolve is now %d%n", presolve);

          // Set the value of the boolean option show_stats to true (maps to 1
          // in AMPL)
          ampl.setBoolOption("show_stats", true);
          // Print its value as an integer
          System.out.format("AMPL show_stats is %d%n", ampl.getIntOption("show_stats"));
          // Set the value of the boolean option show_stats to false (maps to
          // 0 in AMPL)
          ampl.setBoolOption("show_stats", false);
          // Print its value as an integer
          System.out.format("AMPL show_stats is %d%n", ampl.getIntOption("show_stats"));
        } finally {
          ampl.close();
        }
    }
    
    public void test(){
        
        // Create an AMPL instance
        AMPL ampl = new AMPL();
        try {
           
            
            
            // Interpret the two files
            ampl.read("Next5logisticModel.mod");
            ampl.readData("Next5DDM_PTVHD.dat");
            ampl.readData("Next5DDM_RECTUM.dat");
            ampl.readData("Next5DDM_BLADDER.dat");
            ampl.readData("Next5extraLogFunction.dat");
            ampl.readData("x.dat");
            
            
           
            Objective totalcost = ampl.getObjective("Total_Cost");
            // Print it
            System.out.format("Objective is: %f%n", totalcost.value());
            
            Parameter ba = ampl.getParameter("ba");
            System.out.println(ba.getValues()); 
            // Get objective entity by AMPL name
             // Solve
            ampl.solve();
            
            ba.setValues(5);
            System.out.println(ba.getValues()); 
            //ampl.solve();
            ampl.eval("solve Total_Cost;");
            totalcost = ampl.getObjective("Total_Cost");
            // Print it
            System.out.format("Objective is: %f%n", totalcost.value());
/*
            // Reassign data - specific instances
            Parameter cost = ampl.getParameter("x125");
            cost.setValues((Tuple[]) new Object[] { "BEEF", "HAM" }, new double[] { 5.01, 4.55 });
            System.out.println("Increased costs of beef and ham.");

            // Resolve and display objective
            ampl.solve();
            System.out.format("New objective value: %f%n", totalcost.value());

            // Reassign data - all instances
            cost.setValues(new double[] { 3, 5, 5, 6, 1, 2, 5.01, 4.55 });
            System.out.println("Updated all costs");

            // Resolve and display objective
            ampl.solve();
            System.out.format("New objective value: %f%n", totalcost.value());

            // Get the values of the variable Buy in a dataframe object
            Variable buy = ampl.getVariable("Buy");
            DataFrame df = buy.getValues();
            // Print them
            System.out.println(df);

            // Get the values of an expression into a DataFrame object
            DataFrame df2 = ampl.getData("{j in FOOD} 100*Buy[j]/Buy[j].ub");
            // Print them
            System.out.println(df2);
            */
        } catch (IOException e) {
            ampl.close();
            System.out.println(e.getMessage());
        } finally {
            ampl.close();
        }
    }
}
