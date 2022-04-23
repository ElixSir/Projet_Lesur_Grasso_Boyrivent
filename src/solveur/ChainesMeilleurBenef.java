/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import java.util.List;
import solution.Solution;

/**
 *
 * @author Clem
 */
public class ChainesMeilleurBenef implements Solveur{

    @Override
    public String getNom() {
        return "Meilleur Bénéfice Chaines";
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
        
        /* plus longues chaines */
        for (Altruiste altruiste : instance.getAltruistes()) {
            s.ajouterAltruisteNouvelleChaine(altruiste);
            bestP = this.getPlusGrandBenefice(paires, altruiste);
            if( bestP != null ) {
                if(s.ajouterPaireChaineByAltruiste(altruiste, bestP) ) {
                    paires.remove(bestP);

                    while ( (bestP = this.getPlusGrandBenefice(paires, bestP)) != null ) {
                        if( !s.ajouterPaireChaineByAltruiste(altruiste, bestP)) {
                            break;
                        }
                        paires.remove(bestP);
                    }
                }
            }
        }
        
        /* plus long cyles */
        while( !paires.isEmpty() ) {
            
            bestP = paires.pop();
            
            if(!s.ajouterPaireCycle(bestP)) {
                s.ajouterPaireNouveauCycle(bestP);
            }
            /*if(s.ajouterPaireNouveauCycle(bestP)){
                paires.remove(bestP);
                bestP = this.getPlusGrandBenefice(paires, bestP);

                if(s.ajouterPaireLastCycle(bestP)) {
                    paires.remove(bestP);

                    while ( (bestP = this.getPlusGrandBenefice(paires, bestP)) != null ) {
                        if(s.ajouterPaireLastCycle(bestP)) {
                            paires.remove(bestP);
                        }
                    }
                }
            }*/
        }
        
        return s;
    }
    
    public static void main(String[] args) {

        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k3_l4.txt");
            Instance i = read.readInstance();

            ChainesMeilleurBenef algoSimple = new ChainesMeilleurBenef();

            Solution simple = algoSimple.solve(i);

            System.out.println("solution valide : " + simple.check());
            
            System.out.println(simple.toString());

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
