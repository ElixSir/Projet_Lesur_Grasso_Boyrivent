/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import instance.Instance;
import io.InstanceReader;
import operateur.InterDeplacement;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;
import solution.Chaine;
import solution.Cycle;

/**
 *
 * @author felix
 */
public class TestInterDeplacement {
    public static void main(String[] args) {
        try {
            InstanceReader read = new InstanceReader("test/instance_test_InterDeplacement.txt");
            Instance i = read.readInstance();

            System.out.println(i);
            
            Cycle autreCycle = new Cycle(i);
            autreCycle.ajouterPaire(i.getPaireById(5));
            autreCycle.ajouterPaire(i.getPaireById(6));
           
            // C Y C L E S
            System.out.println("CYCLES");
            Cycle c = new Cycle(i);

            c.ajouterPaire(i.getPaireById(2));
            c.ajouterPaire(i.getPaireById(3));
            c.ajouterPaire(i.getPaireById(4));


            InterDeplacement interDeplacementCycle = new InterDeplacement();
            InterDeplacement interDeplacementCycle2 = (InterDeplacement) 
                    OperateurLocal.getOperateurInter(TypeOperateurLocal.INTER_DEPLACEMENT, c, autreCycle, 0, 1, 1, 0);
            InterDeplacement interDeplacementCycle3 = (InterDeplacement) 
                    OperateurLocal.getOperateurInter(TypeOperateurLocal.INTER_DEPLACEMENT, c, autreCycle, 1, 1, 1, 0);//0


            System.out.println(c);//cout -1

            System.out.println(interDeplacementCycle2.isMeilleur(interDeplacementCycle3));//false
            System.out.println(interDeplacementCycle2.isMouvementRealisable());//true
            System.out.println(interDeplacementCycle2.isMouvementAmeliorant());//true
            System.out.println(interDeplacementCycle.isMouvementRealisable());//false
            System.out.println(interDeplacementCycle.isMouvementAmeliorant());//false

            System.out.println(interDeplacementCycle);//benefice -1
            System.out.println(interDeplacementCycle2);//benefice 2
            System.out.println(interDeplacementCycle3);//benefice 2

            /*System.out.println("\nTests avancés\n");;

            System.out.println(c.getMeilleurOperateurInter(TypeOperateurLocal.INTER_DEPLACEMENT));

            System.out.println(c.doDeplacement(interDeplacementCycle));//false
            System.out.println(c.doDeplacement(interDeplacementCycle2));//true*/

            // C H A I N E S
            System.out.println("\nCHAINES");

            Chaine ch = new Chaine(i, i.getAltruistes().getFirst());
            ch.ajouterPaire(i.getPaireById(1));
            ch.ajouterPaire(i.getPaireById(2));
            ch.ajouterPaire(i.getPaireById(3));
            ch.ajouterPaire(i.getPaireById(4));

            InterDeplacement intraDeplacementChaine = new InterDeplacement();
            InterDeplacement intraDeplacementChaine2 = (InterDeplacement) 
                    OperateurLocal.getOperateurInter(TypeOperateurLocal.INTER_DEPLACEMENT, ch, autreCycle, 0, 1, 1, 0);
            InterDeplacement intraDeplacementChaine3 = (InterDeplacement) 
                    OperateurLocal.getOperateurInter(TypeOperateurLocal.INTER_DEPLACEMENT, ch, autreCycle, 1, 1, 1, 0);//-1

            System.out.println(c);//cout -1

            System.out.println(intraDeplacementChaine2.isMeilleur(intraDeplacementChaine3));//true
            System.out.println(intraDeplacementChaine2.isMouvementRealisable());//true
            System.out.println(intraDeplacementChaine2.isMouvementAmeliorant());//true
            System.out.println(intraDeplacementChaine3.isMouvementRealisable());//false
            System.out.println(intraDeplacementChaine3.isMouvementAmeliorant());//false

            System.out.println(intraDeplacementChaine);//benefice -1
            System.out.println(intraDeplacementChaine2);//benefice 2
            System.out.println(intraDeplacementChaine3);//benefice 2

//            System.out.println("\nTests avancés\n");
//
//            System.out.println(ch.getMeilleurOperateurInter(TypeOperateurLocal.INTER_DEPLACEMENT));
//
//            System.out.println(ch.doDeplacement(intraDeplacementChaine));//false
//            System.out.println(ch.doDeplacement(intraDeplacementChaine2));//true

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }
}
