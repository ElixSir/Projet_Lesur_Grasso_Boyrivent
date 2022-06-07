/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import solution.Chaine;
import solution.Cycle;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import io.InstanceReader;
import operateur.IntraDeplacement;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

/**
 *
 * @author felix
 */
public class TestIntraDeplacement {
    public static void main(String[] args) {
        try {
            InstanceReader read = new InstanceReader("test/instance_test_IntraDeplacement.txt");
            Instance i = read.readInstance();

            
            // C Y C L E S
            System.out.println("CYCLES");
            Cycle c = new Cycle(i);

            c.ajouterPaire(i.getPaireById(2));
            c.ajouterPaire(i.getPaireById(3));
            c.ajouterPaire(i.getPaireById(4));
            c.ajouterPaire(i.getPaireById(5));
            

            IntraDeplacement intraDeplacementCycle = new IntraDeplacement();
            IntraDeplacement intraDeplacementCycle2 = (IntraDeplacement) OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, c, 0, 2, 1, 0);
            IntraDeplacement intraDeplacementCycle3 = (IntraDeplacement) 
                    OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, c, 0, 3, 1, 0);//0

            IntraDeplacement intraDeplacement4 = (IntraDeplacement) 
                    OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, c, 1, 2, 1, 0);

            System.out.println(c);//cout -1

            System.out.println(intraDeplacementCycle2.isMeilleur(intraDeplacementCycle3));//true
            System.out.println(intraDeplacementCycle2.isMouvementRealisable());//true
            System.out.println(intraDeplacementCycle2.isMouvementAmeliorant());//true
            System.out.println(intraDeplacementCycle.isMouvementRealisable());//false
            System.out.println(intraDeplacementCycle.isMouvementAmeliorant());//false

            System.out.println(intraDeplacementCycle);//benefice -1
            System.out.println(intraDeplacementCycle2);//benefice 0
            System.out.println(intraDeplacementCycle3);//benefice -1

            
            System.out.println("\nTests avancés\n");
            
            System.out.println(c.getMeilleurOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT));

            System.out.println(c.doDeplacement(intraDeplacementCycle));//false
            System.out.println(c.doDeplacement(intraDeplacementCycle2));//true
           
                  
            // C H A I N E S
            System.out.println("\nCHAINES");

            Chaine ch = new Chaine(i,i.getAltruistes().getFirst());
            ch.ajouterPaire(i.getPaireById(1));
            ch.ajouterPaire(i.getPaireById(2));
            ch.ajouterPaire(i.getPaireById(3));
            ch.ajouterPaire(i.getPaireById(4));
            ch.ajouterPaire(i.getPaireById(5));
            
            IntraDeplacement intraDeplacementChaine = new IntraDeplacement();
            IntraDeplacement intraDeplacementChaine2 = (IntraDeplacement) OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, ch, 0, 2, 1, 0);
            IntraDeplacement intraDeplacementChaine3 = (IntraDeplacement) OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, ch, 1, 2, 1, 0);//-1


            System.out.println(c);//cout -1

            System.out.println(intraDeplacementChaine2.isMeilleur(intraDeplacementChaine3));//true
            System.out.println(intraDeplacementChaine2.isMouvementRealisable());//true
            System.out.println(intraDeplacementChaine2.isMouvementAmeliorant());//true
            System.out.println(intraDeplacementChaine3.isMouvementRealisable());//false
            System.out.println(intraDeplacementChaine3.isMouvementAmeliorant());//false

            System.out.println(intraDeplacementChaine);//benefice -1
            System.out.println(intraDeplacementChaine2);//benefice 6
            System.out.println(intraDeplacementChaine3);//benefice 4

            System.out.println("\nTests avancés\n");

            System.out.println(ch.getMeilleurOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT));

            System.out.println(ch.doDeplacement(intraDeplacementChaine));//false
            System.out.println(ch.doDeplacement(intraDeplacementChaine2));//true

        } catch (Exception ex) {
            System.out.println(ex);
        }

        

       
    }
}
