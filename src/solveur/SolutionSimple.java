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
import instance.reseau.Participant;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import solution.Solution;

/**
 *
 * @author Bart
 */
public class SolutionSimple implements Solveur{
    
    public String getNom(){
        return "Solution simple";
    }
    
    
    @Override
    public Solution solve(Instance instance){
        
        Solution soluce = new Solution(instance); // solution 

        
       // LinkedList<Paire> list = new LinkedList<Paire>(instance.getPaires());
        //LinkedList<Altruiste> listAltruiste = new LinkedList<Altruiste>(instance.getAltruistes());
        
        LinkedList<Participant> list = new LinkedList<Participant>();
        
        for(Participant p: instance.getAltruistes()){
            list.add(p);
        }
        
        for(Participant p: instance.getPaires()){
            list.add(p);
        }
        
        
        boolean fin = true;
        
        while(fin){
             fin = false;
            
            
            
            
        } 
        soluce.clean();
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
    
    

