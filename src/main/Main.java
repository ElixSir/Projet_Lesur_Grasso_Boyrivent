/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

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
            System.out.print("Command that is normally used : java -jar .\\Projet_Lesur_Grasso_Boyrivent.jar -inst ../instancesInitiales -dSol ../annexe\n"
                    + "The origin folder is the dist folder");
        }
        else {
            System.out.print("Inputs are not good ! You can use the --help option to see the basic use ");
        }
        
        
    }
    
}
