/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.TestAllSolveur;

/**
 *
 * @author felix
 */
public class Main {
    
    public static void main(String[] args) {
        String nomFichierInstance = "./";
        String destinationSol = "./";
        
        if(args.length >=3 && args[0].equals("-inst") && args[2].equals("-dSol"))
        {
            nomFichierInstance = args[1];
        
            if(args.length >= 4)
            {
                destinationSol = args[3];
                File file = new File(destinationSol);
                if(!file.exists())
                {
                   
                    Path path = Paths.get(destinationSol);
                    try {
                        Files.createDirectories(path);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

            System.out.println(destinationSol);
            System.out.println(nomFichierInstance);
            TestAllSolveur test = new TestAllSolveur(nomFichierInstance);
            test.printAllResultats("results", destinationSol);
            
            if(destinationSol.equals("./"))
            {
                destinationSol = "dist";
            }
            System.out.print("Results succesfully saved in " + destinationSol + " !");

        }
        else if(args.length == 1 && args[0].equals("--help")){
            System.out.print("Command that is normally used : java -jar .\\Projet_Lesur_Grasso_Boyrivent.jar "
                    + "-inst ..\\instancesInitiales\\KEP_p100_n11_k5_l17 -dSol ../annexe\n"
                    + "The origin folder is the dist folder");
        }
        else {
            System.out.print("Inputs are not good ! You can use the --help option to see the basic use ");
        }
        
        
    }
    
}
