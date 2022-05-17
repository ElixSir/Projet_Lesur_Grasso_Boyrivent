/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import ensemble.Chaine;
import ensemble.Cycle;
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import io.InstanceReader;
import operateur.IntraEchangeCycle;
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
            Cycle c = new Cycle(i);

            c.ajouterPaire(i.getPaireById(2));
            c.ajouterPaire(i.getPaireById(3));
            c.ajouterPaire(i.getPaireById(4));
            c.ajouterPaire(i.getPaireById(5));
            

            System.out.println("Size : " + c.getPaires().size());

            IntraEchangeCycle echange2 = new IntraEchangeCycle(c, 0, 2);

            //InsertionPaire insertion3 = new InsertionPaire(c,1, p2);
            System.out.println("Benefice : " + c.getBeneficeTotal());
            System.out.println(c);
            System.out.println("_________________");
            echange2.doMouvementIfRealisable();
            System.out.println(c);
            System.out.println("Benefice :" + c.getBeneficeTotal());
            
            System.out.println(c);
            System.out.println(c.deltaBeneficeDeplacement(0, 2));//0
            //Insertion avant le point 2
            System.out.println(c.deltaBeneficeDeplacement(0, 1));//-1
            System.out.println(c.deltaBeneficeDeplacement(0, -1));//+INF
            System.out.println(c.deltaBeneficeDeplacement(-1, 3));//+INF
            System.out.println(c.deltaBeneficeDeplacement(3, 3));//+INF
            


            Chaine ch = new Chaine(i,i.getAltruistes().getFirst());
            ch.ajouterPaire(i.getPaireById(2));
            ch.ajouterPaire(i.getPaireById(2));
            ch.ajouterPaire(i.getPaireById(3));
            ch.ajouterPaire(i.getPaireById(4));
            ch.ajouterPaire(i.getPaireById(5));

        } catch (Exception ex) {
            System.out.println(ex);
        }

        

        IntraDeplacement intraDeplacement = new IntraDeplacement();
        IntraDeplacement intraDeplacement2 = (IntraDeplacement) OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, 0, 2, t);
        IntraDeplacement intraDeplacement3 = (IntraDeplacement) OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, 0, 3, t);//0

        IntraDeplacement intraDeplacement4 = (IntraDeplacement) OperateurLocal.getOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT, 1, 2, t);

        System.out.println(intraDeplacement2.isMeilleur(intraDeplacement3));//false
        System.out.println(intraDeplacement2.isMouvementRealisable());//true
        System.out.println(intraDeplacement2.isMouvementAmeliorant());//false

        System.out.println(intraDeplacement);
        System.out.println(intraDeplacement2);//cout 10

        System.out.println(t.getMeilleurOperateurIntra(TypeOperateurLocal.INTRA_DEPLACEMENT));

        System.out.println(t.doDeplacement(intraDeplacement));//false
        System.out.println(t.doDeplacement(intraDeplacement2));//true

        System.out.println("TEST Tabou");
        System.out.println(intraDeplacement2.isTabou(intraDeplacement2));//true
        System.out.println(intraDeplacement2.isTabou(intraDeplacement3));//true
        System.out.println(intraDeplacement2.isTabou(intraDeplacement4));//false
    }
}
