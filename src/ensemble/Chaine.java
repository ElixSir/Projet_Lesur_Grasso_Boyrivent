/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Altruiste;
import instance.reseau.Paire;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 *
 * @author Bart
 */
public class Chaine extends Echanges {
    private int maxChaine;
    private Altruiste altruiste; 
    
    public Chaine(int maxChaine){
        this.maxChaine = maxChaine;
        altruiste = null; // a voir
    }
    
    public Chaine(int maxChaine, Altruiste altruiste){
        this.maxChaine = maxChaine;
        this.altruiste = altruiste; // a voir
    }

    public int getMaxChaine() {
        return maxChaine;
    }
    
    public void setMaxChaine(int m){
        this.maxChaine = m;        
    }

    public Altruiste getAltruiste() {
        return altruiste;
    }
    
    public boolean ajouterAltruiste(Altruiste a){
        if(a == null) return false;
        if(this.altruiste != null) return false; // si un altruiste est deja la alors je peut pas l'ajouter
           // ajouter benefice de de l'altruiste
        
        this.altruiste = a;   
        return true;
    }
    
    
    public boolean possibleAjouter(){
        return this.paires.size()  < this.maxChaine;
    }
    
    
    public static void main(String[] args) {
        
    }

    public void printChaine(PrintWriter ecriture) {
        
        String s = this.altruiste + "\t";
        for (int j = 0; j < paires.size() - 1; j++) {
            Paire paire = paires.get(j);
            s += paire.getId() + "\t";
        }

        ecriture.print(s);
    }
    
    
}
