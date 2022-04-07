/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Paire;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *
 * @author Bart
 */
public class Cycle extends Echanges {
    private int maxCycle;
    

    public Cycle(int maxCycle) {
        this.maxCycle = maxCycle;
    }

    public int getMaxCycle() {
        return maxCycle;
    }
    
    public void setMaxCycle(int m){
        this.maxCycle = m;
    }
    

    
    public boolean possibleAjouter(){
        return this.paires.size()  < this.maxCycle;
    }
 
    public static void main(String[] args) {
        
    }

    public void printCycle(PrintWriter ecriture) {
        String s = "";

        for (int j = 0; j < paires.size() - 1; j++) {
            Paire paire = paires.get(j);
            s += paire.getId() + "\t";
        }

        ecriture.print(s);
    }
}
