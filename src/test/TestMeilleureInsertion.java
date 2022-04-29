/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import ensemble.Cycle;
import ensemble.Echanges;
import instance.Instance;
import instance.reseau.Paire;
import io.InstanceReader;
import operateur.InsertionPaire;

/**
 *
 * @author Bart
 */
public class TestMeilleureInsertion {
    
    public static void main(String[] args) {
        
        
        try{
        InstanceReader read = new InstanceReader("instancesInitiales/KEP_p50_n6_k5_l17.txt");
        Instance i = read.readInstance();
        
        Cycle c = new Cycle(i);
        
        Paire p1 = new Paire(1);
        Paire p2 = new Paire(2);
        
        InsertionPaire insertion2 = new InsertionPaire(c,0, p1);
        InsertionPaire insertion3 = new InsertionPaire(c,1, p2);
        
       // System.out.println(c.toString());
        
        System.out.println(insertion2.isMeilleur(insertion3)); 
        System.out.println(insertion3.isMeilleur(insertion2));
        
        System.out.println("insertion2 deltabenefice : "+insertion2.getDeltaBenefice());
        System.out.println("insertion3 deltabenefice : "+insertion3.getDeltaBenefice());
        
        System.out.println(insertion2.isMouvementRealisable());
        System.out.println(insertion3.isMouvementRealisable());
        
        
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        
        
        
    }
    
}
