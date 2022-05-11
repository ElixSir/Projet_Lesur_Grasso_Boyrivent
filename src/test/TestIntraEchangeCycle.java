package test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import ensemble.Cycle;
import instance.Instance;
import instance.reseau.Paire;
import io.InstanceReader;
import operateur.InsertionPaire;
import operateur.IntraEchangeCycle;
import operateur.OperateurIntraEchange;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

/**
 *
 * @author yanni
 */
public class TestIntraEchangeCycle {
    
    public static void main(String[] args) {

        
        
        try{
        InstanceReader read = new InstanceReader("instancesInitiales/instancedetest.txt");
        Instance i = read.readInstance();
        
        Cycle c = new Cycle(i);
        
       
        
       
        /*InsertionPaire insertion1 = new InsertionPaire(c,0, p1);
        insertion1.doMouvementIfRealisable();
        InsertionPaire insertion2 = new InsertionPaire(c,1, p2);
        insertion2.doMouvementIfRealisable();
        InsertionPaire insertion3 = new InsertionPaire(c,2, p3);
        insertion3.doMouvementIfRealisable();
        InsertionPaire insertion4 = new InsertionPaire(c,3, p4);
        insertion4.doMouvementIfRealisable();*/
        
        c.ajouterPaire( i.getPaireById(1));
        c.ajouterPaire( i.getPaireById(2));
        c.ajouterPaire( i.getPaireById(3));
        c.ajouterPaire( i.getPaireById(4));
        
            System.out.println("Size : "+c.getPaires().size());
        
<<<<<<< HEAD
        IntraEchangeCycle echange2 = new IntraEchangeCycle(c,0, 2);
=======
        IntraEchangeCycle echange2 = new IntraEchangeCycle(c,1, 2);
>>>>>>> main
        //InsertionPaire insertion3 = new InsertionPaire(c,1, p2);
        
            System.out.println("Benefice : "+ c.getBeneficeTotal());
            System.out.println(c);
            System.out.println("_________________");
            echange2.doMouvementIfRealisable();
            System.out.println(c);
            System.out.println("Benefice :" +c.getBeneficeTotal());
            
            
            
        
        }catch(Exception ex){
            System.out.println(ex);
        }
        



     
    }
            
    
}
