
package solveur;

import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import io.InstanceReader;
import io.exception.ReaderException;
import java.util.LinkedList;
import operateur.InsertionPaire;
import solution.Solution;

/**
 *
 * @author felix
 */
public class MeilleureInsertion implements Solveur{

    @Override
    public String getNom() {
        return "Meilleure Insertion";
    }

    @Override
    public Solution solve(Instance instance) {
        LinkedList<Paire> paires = instance.getPaires();
        Solution s = new Solution(instance);
        
        for (Altruiste a : instance.getAltruistes()) {
            s.ajouterAltruisteNouvelleChaine(a);
        }
        
        InsertionPaire isnMeilleur = new InsertionPaire();

        while (!paires.isEmpty()) {
            isnMeilleur = getMeilleurInsertionPaireSolution(s, paires);
            if (s.doInsertion(isnMeilleur)) {
                paires.remove(isnMeilleur.getPaire());
            } else {
                s.ajouterPaireNouveauCycle(paires.getFirst());
                paires.remove(paires.getFirst());
            }
        }
        s.clean();
        return s;
    }

    private InsertionPaire getMeilleurInsertionPaireSolution(Solution s, LinkedList<Paire> paires) {
        InsertionPaire meilleur = new InsertionPaire();
        InsertionPaire courant = new InsertionPaire();
        for (Paire p : paires) {
            courant = s.getMeilleurInsertion(p);
            if (courant.isMeilleur(meilleur)) {
                meilleur = courant;
            }
        }
        return meilleur;
    }

    public static void main(String[] args) {
        Instance instance1;

        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k5_l17.txt");
            instance1 = read.readInstance();

            MeilleureInsertion meilleureInsertion = new MeilleureInsertion();
            Solution solution1 = meilleureInsertion.solve(instance1);

            System.out.println(solution1);
            solution1.check();

            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }

    }
    
}
