/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import ensemble.Chaine;
import ensemble.Cycle;
import instance.Instance;
import instance.reseau.Paire;
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

    public LinkedList<Chaine> getChaines() {
        return new LinkedList<>(chaines);
    }

    public LinkedList<Cycle> getCycles() {
        return new LinkedList<>(cycles);
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

}
