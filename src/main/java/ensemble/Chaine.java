/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Altruiste;
import instance.reseau.Paire;
import java.util.LinkedList;

/**
 *
 * @author Bart
 */
public class Chaine extends Echanges {
    private int maxChaine;
    private Altruiste altruiste; 
    
    public Chaine(){
        maxChaine = 0;
        altruiste = null; // a voir
    }

    public int getMaxChaine() {
        return maxChaine;
    }

    public Altruiste getAltruiste() {
        return altruiste;
    }
}
