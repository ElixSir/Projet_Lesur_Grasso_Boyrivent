package test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import solution.Chaine;
import solution.Cycle;
import instance.Instance;
import instance.reseau.Paire;
import io.InstanceReader;
import operateur.InsertionPaire;
import operateur.InterEchange;
import operateur.IntraEchange;
import operateur.OperateurIntraEchange;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

/**
 *
 * @author Bart
 */
public class TestInterEchange {

    public static void main(String[] args) {

        try {
            InstanceReader read = new InstanceReader("instancesInitiales/instancedetest.txt");
            Instance i = read.readInstance();

            Cycle c = new Cycle(i);
            //Cycle c2 = new Cycle(i);
            Chaine c2 = new Chaine(i, i.getAltruistes().getFirst());

            /*InsertionPaire insertion1 = new InsertionPaire(c,0, p1);
        insertion1.doMouvementIfRealisable();
        InsertionPaire insertion2 = new InsertionPaire(c,1, p2);
        insertion2.doMouvementIfRealisable();
        InsertionPaire insertion3 = new InsertionPaire(c,2, p3);
        insertion3.doMouvementIfRealisable();
        InsertionPaire insertion4 = new InsertionPaire(c,3, p4);
        insertion4.doMouvementIfRealisable();*/
            c.ajouterPaire(i.getPaireById(1));
            c.ajouterPaire(i.getPaireById(2));
            c.ajouterPaire(i.getPaireById(3));
            c.ajouterPaire(i.getPaireById(4));

            c2.ajouterPaire(i.getPaireById(5));
            c2.ajouterPaire(i.getPaireById(6));
            c2.ajouterPaire(i.getPaireById(7));
            c2.ajouterPaire(i.getPaireById(8));

            System.out.println("Size : " + c.getPaires().size());
            System.out.println("Size C2: " + c2.getPaires().size());

            InterEchange echange2 = new InterEchange(c, c2, 0, 1, 1, 1);
            //InsertionPaire insertion3 = new InsertionPaire(c,1, p2);

            System.out.println("Benefice C: " + c.getBeneficeTotal());
            System.out.println(c);
            System.out.println("Benefice C2: " + c2.getBeneficeTotal());
            System.out.println(c2);
            System.out.println("_________________");
            echange2.doMouvementIfRealisable();
            System.out.println(c);
            System.out.println("_____");
            System.out.println(c2);
            System.out.println("Benefice C:" + c.getBeneficeTotal());
            System.out.println("Benefice C2:" + c2.getBeneficeTotal());

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

}
