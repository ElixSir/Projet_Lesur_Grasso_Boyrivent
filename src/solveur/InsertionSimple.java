/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import solution.Solution;

/**
 *
 * @author Clem
 */
public class InsertionSimple implements Solveur{

    @Override
    public String getNom() {
        return "Insertion Simple";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution s = new Solution(instance);
        
        for (Altruiste altruiste : instance.getAltruistes()) {
            s.ajouterAltruisteNouvelleChaine(altruiste);
        }
        
        for (Paire paire : instance.getPaires()) {
            if( !s.ajouterPaireChaine(paire) ) {
                if( !s.ajouterPaireCycle(paire) ) {
                    s.ajouterPaireNouveauCycle(paire);
                }
            } 
        }
        
        return s;
    }
    
}
