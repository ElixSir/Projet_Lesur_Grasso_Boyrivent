/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import instance.reseau.Participant;
import io.InstanceReader;
import io.exception.ReaderException;
import operateur.OperateurLocal;
import operateur.TypeOperateurLocal;

import solution.Solution;

/**
 *
 * @author Bart
 */
public class RechercheLocale implements Solveur{
    private Solveur solveurInitial;

    public RechercheLocale(Solveur solveurInitial) {
        this.solveurInitial = solveurInitial;
    }
    
    
    @Override
    public String getNom() {
        return "RechercheLocale("+this.solveurInitial.getNom()+')';
    }

    

    @Override
    public Solution solve(Instance instance) {
           
        Solution s = this.solveurInitial.solve(instance);
        s.creerEchangesVides();
        System.out.println(s);
        boolean improve = true;
        
        while(improve == true){
            improve = false;
            
            for(TypeOperateurLocal type :TypeOperateurLocal.values()){
                    OperateurLocal bestOperateur = s.getMeilleurOperateurLocal(type);
                    System.out.println(bestOperateur);
                    if (bestOperateur.isMouvementAmeliorant()) {
                        s.doMouvementRechercheLocale(bestOperateur);
                        improve = true;
                
                } 
            }
        }
        s.clean();
        return s;
    }
    
    
    /**
     * Test sur la première instance
     * @param args 
     */
    public static void main(String[] args) {
        try{
            System.out.println("main recherchelocale");
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k5_l17.txt");
            //InstanceReader read = new InstanceReader("test/instance_test_InterDeplacement.txt");
            Instance i = read.readInstance();
            
            InsertionSimple algoInitial = new InsertionSimple();
            
            Solution sInitiale = algoInitial.solve(i);
            
            RechercheLocale rechercheLocale = new RechercheLocale(algoInitial);
            
            Solution sRechecheLocale = rechercheLocale.solve(i);
            
            System.out.println("\n Solution Initiale \n");
            System.out.println(sInitiale.toString());
            System.out.println("/////");
            System.out.println("\n Solution Recherche Locale \n");
            System.out.println(sRechecheLocale.toString());
            //System.out.println(sRechecheLocale.check());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
}