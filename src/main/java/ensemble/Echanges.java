/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensemble;

import instance.reseau.Paire;
import java.util.LinkedList;

/**
 *
 * @author Bart
 */
public abstract class Echanges {
    protected int beneficeTotal;
    protected LinkedList<Paire> paires;
    
    public Echanges(){
        beneficeTotal = 0;
        paires = new LinkedList<Paire>();
    }

    public int getBeneficeTotal() {
        return beneficeTotal;
    }

    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(paires);
    }
 
    

    
    
}
