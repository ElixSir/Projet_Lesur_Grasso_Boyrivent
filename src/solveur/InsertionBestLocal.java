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
 * @author felix
 */
public class InsertionBestLocal implements Solveur{


        @Override
        public String getNom() {
            return "Insertion Best Local";
        }

        private Paire getPlusGrandBenefice(List<Paire> P, Participant p) {
            Paire best = null;
            int max = 0, benef;

            for (Paire paire : P) {
                benef = p.getBeneficeVers(paire);
                if (benef != -1 && max < benef) {
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
                if (bestP != null) {
                    if (s.ajouterPaireChaineByAltruiste(altruiste, bestP)) {
                        paires.remove(bestP);

                        while ((bestP = this.getPlusGrandBenefice(paires, bestP)) != null) {
                            if (!s.ajouterPaireChaineByAltruiste(altruiste, bestP)) {
                                break;
                            }
                            paires.remove(bestP);
                        }
                    }
                }
            }

            while (!paires.isEmpty()) {
                bestP = paires.getFirst();
                if (s.ajouterPaireNouveauCycle(bestP)) {

                    paires.remove(bestP);
                    
                    while ((bestP = this.getPlusGrandBenefice(paires, bestP)) != null) {
                        if (!s.ajouterPaireCycle(bestP)) {
                            break;
                        }
                        paires.remove(bestP);
                        
                    }
                }
            }
            s.clean();
            return s;
        }

        public static void main(String[] args) {

            try {
                InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k3_l4.txt");
                Instance i = read.readInstance();

                InsertionBestLocal algoBestLocal = new InsertionBestLocal();

                Solution bestLocal = algoBestLocal.solve(i);
                

                System.out.println("solution valide : " + bestLocal.check());

                System.out.println(bestLocal.toString());

            } catch (ReaderException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }


