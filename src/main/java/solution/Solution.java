/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import ensemble.Chaine;
import ensemble.Cycle;
import instance.Instance;
import instance.reseau.Paire;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;



/**
 *
 * @author Bart
 */
public class Solution {

    private int coutTotal;
    private Instance instance;
    private LinkedList<Chaine> chaines;
    private LinkedList<Cycle> cycles;

    public Solution(Instance i) {
        this.coutTotal = 0;
        this.instance = i;
        
    }

    public Solution(Solution solution) {
        this.coutTotal = solution.coutTotal;
        this.instance = solution.instance;
        chaines = new LinkedList<Chaine>(); // a voir
        cycles = new LinkedList<Cycle>(); // a voir
       
    }

    public int getCoutTotal() {
        return coutTotal;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    public Instance getInstance() {
        return instance;
    }

/* Pas utile pour le moment mais peut le devenir par la suite
    public boolean ajouterChaine(Chaine c){
       if(c == null) return true;
        this.chaines.add(c);
        return true;
    }
    
    public boolean ajouterCycle(Cycle c){
        if(c == null) return false;
        
        this.cycles.add(c);
        return true;
    }*/
    
    public boolean ajouterPaireCycleExistant(Paire p){
        if(p == null) return false;
        
        //Parcours des tournees existantes
        for(Cycle c : this.cycles){
            if(c.ajouterPaire(p)){
                // penser a mettre a jour le cout ici
                return true;
            }
        }

        return false;
    }
    
    
    public boolean ajouterPaireNouveauCycle(Paire p, int taillecycle){
        if(p == null) return false;
        
        Cycle nouveauCycle = new Cycle(taillecycle);
        if(nouveauCycle.ajouterPaire(p)){ 
             // penser a mettre a jour le cout ici
            this.cycles.add(nouveauCycle);
            return true;
        }
        return false;
    }
    
      public boolean ajouterPaireChaineExistante(Paire p){
        if(p == null) return false;
        
        //Parcours des tournees existantes
        for(Chaine c : this.chaines){
            if(c.ajouterPaire(p)){
                // penser a mettre a jour le cout ici
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Solution" 
                + "\n\tcoutTotal=" + coutTotal 
                + "\n\tinstance=" + instance 
                + "\n\tchaines=" + chaines 
                + "\n\tcycles=" + cycles + '}';
    }

    public void printSolution(PrintWriter ecriture) {
        // Cout total de la solution
        ecriture.println("// Cout total de la solution");
        ecriture.println(this.coutTotal);
        // Description de la solution
        // Cycles
        ecriture.println("// Description de la solution");
        ecriture.println("// Cycles");
        printCycles(ecriture); //Affiches les donneurs des différents cycles
        ecriture.print("\n");
        // Chaines
        ecriture.print("// Chaines");
        printChaines(ecriture); //Affiches les donneurs des différentes chaines
    }
    
    /**
     * //Affiches les donneurs des différents cycles de la solution
     *
     * @param sol
     * @param ecriture
     */
    private void printCycles(PrintWriter ecriture) {
        if (this.cycles != null) {

            for (int i = 0; i < cycles.size(); i++) {
                Cycle cycle = cycles.get(i);
                cycle.printCycle(ecriture);
                
            }
        }

    }
    
    /**
     * //Affiches les donneurs des différentes chaines de la solution
     *
     * @param sol
     * @param ecriture
     */
    private void printChaines(PrintWriter ecriture) {
        if (this.chaines != null) {
            for (int i = 0; i < chaines.size(); i++) {
                Chaine chaine = chaines.get(i);
                chaine.printChaine(ecriture);
               
            }
        }

    }
      
    /**
     * Checker de la class Solution
     *
     * @return
     *
     * A modifier
     */
    public boolean check() {
        return true;
    }

}
