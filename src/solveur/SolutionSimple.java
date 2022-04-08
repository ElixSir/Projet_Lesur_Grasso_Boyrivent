/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveur;

import ensemble.Chaine;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;

/**
 *
 * @author Bart
 */
public class SolutionSimple implements Solveur{
    
    public String getNom(){
        return "Solution simple";
    }
    
    
    public void unuseLine(int matrix[][], int line){ // mettre 0 dans la matrice a la ligne donneur pour dire qu'il est fait
        for(int i=0; i< matrix[line].length; i++){
            matrix[line][i] = 0;
        }
    }
    
    @Override
    public Solution solve(Instance instance){
        
        Solution soluce = new Solution(instance); // solution 
       /* int N = 2; // nombre de donneurs altruistes
        int P = 5; // nombre de paires patient-donneurs
        
        int K = 3; // taille max cycles
        int L = 3; // taille max chaines
        
        int lignes = N+P; // nombre de lignes du tableau
        int colones = P; // nombre de colones du tableau
        
        int matrix[][] = { // matrice de bénéfice
            {5,-1,-1,-1,-1},
            {2,-1,-1,-1,-1},
            {-1,-1,2,-1,-1},
            {-1,-1,-1,1,-1},
            {-1,-1,-1,-1,2},
            {-1, 3,4,-1,-1},
            {-1,-1,-1,6,-1}
        };*/
        
        

        return soluce;
    }
    
    public static void main(String[] args) {

        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n0_k3_l0.txt");
            Instance i = read.readInstance();

            SolutionSimple algoSimple = new SolutionSimple();

            Solution simple = algoSimple.solve(i);

            System.out.println(simple.toString());

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
}
    
    

