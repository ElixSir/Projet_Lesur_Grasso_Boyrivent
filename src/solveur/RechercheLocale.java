/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solveur;

import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;

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
      /*  ListeTabou liste = ListeTabou.getInstance();
        liste.vider();
           
        Solution s = this.solveurInitial.solve(instance);

        boolean improve = true;
        
        while(improve == true){
            improve = false;
            
            for(TypeOperateurLocal type :TypeOperateurLocal.values()){
                OperateurLocal bestOperateur = s.getMeilleurOperateurLocal(type);
                System.out.println(bestOperateur);
                if(bestOperateur.isMouvementAmeliorant()){
                    s.doMouvementRechercheLocale(bestOperateur);
                    improve = true;
                } 
            }
        }*/
        return null;
    }
    
    /**
     * Test sur la première instance
     * @param args 
     */
    public static void main(String[] args) {
        try{
            System.out.println("main recherchelocale");
           /* InstanceReader read = new InstanceReader("instances/A-n32-k5.vrp");
            Instance i = read.readInstance();
            
            Solveur solveurInitial = new InsertionPlusProcheVoisin();
            
            RechercheLocale algo = new RechercheLocale(solveurInitial);
            
            Solution s = algo.solve(i);
            
            System.out.println(s.toString());
            System.out.println(s.check());*/
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
