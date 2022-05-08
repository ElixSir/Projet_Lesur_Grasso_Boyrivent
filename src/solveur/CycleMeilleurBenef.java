/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import java.util.LinkedList;
import java.util.List;
import solution.Solution;

/**
 *
 * @author Clem
 */
public class CycleMeilleurBenef implements Solveur{

    @Override
    public String getNom() {
        return "Meilleur Benefice Cycles";
    }
    
    private Paire getPlusGrandBenefice(List<Paire> P,Participant p) {
        Paire best = null;
        int max = 0, benef;
        
        for (Paire paire : P) {
            benef = p.getBeneficeVers(paire);
            if( benef != -1 && max < benef ){
                best = paire;
                max = benef;
            }
        }
        
        return best;
    }

    @Override
    public Solution solve(Instance instance) {
        Solution s = new Solution(instance);
        LinkedList<Paire> paires = instance.getPaires();
        Paire bestP = null;
        
        
        /* plus long cyles */
        while( !paires.isEmpty() ) {
            bestP = paires.getFirst();
            if(s.ajouterPaireNouveauCycle(bestP)) {
                
                paires.remove(bestP);
                bestP = this.getPlusGrandBenefice(paires, bestP);

                if(s.ajouterPaireCycle(bestP)){
                    paires.remove(bestP);

                    while ( (bestP = this.getPlusGrandBenefice(paires, bestP)) != null ) {
                        if( !s.ajouterPaireCycle(bestP)) {
                            break;
                        }
                        paires.remove(bestP);
                    }
                }
            }
        }
                
        /* chaines */
        for (Altruiste altruiste : instance.getAltruistes()) {
            s.ajouterAltruisteNouvelleChaine(altruiste);
            /*
            bestP = this.getPlusGrandBenefice(paires, altruiste);
            if( s.AjouterPaireChaineByAltruiste(altruiste, bestP) ) {
                paires.remove(bestP);
                
                while ( (bestP = this.getPlusGrandBenefice(paires, bestP)) != null ) {
                    if(s.AjouterPaireChaineByAltruiste(altruiste, bestP) ) {
                        break;
                    }
                    paires.remove(bestP);
                }
            }
            */
        }
        s.clean();
        return s;
    }   
}
