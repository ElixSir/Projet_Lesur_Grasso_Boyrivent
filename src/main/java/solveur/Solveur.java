/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solveur;


import instance.Instance;
import solution.Solution;

/**
 *
 * @author Bart
 */

public interface Solveur {

    public String getNom();

    public Solution solve(Instance instance);

    /* public Solution solve(Instance instance){
        Solution soluce = new Solution(instance);
        
            for(Client clt: instance.getClients()){
                if(!soluce.ajouterClientTourneeExistante(clt)){
                    soluce.ajouterClientNouvelleTournee(clt);
                }
            }
            return soluce;
    }*/
}
