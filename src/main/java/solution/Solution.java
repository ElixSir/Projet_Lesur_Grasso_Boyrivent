/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solution;

import ensemble.Chaine;
import ensemble.Cycle;
import instance.Instance;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;



/**
 *
 * @author Bart
 */
public class Solution {

    private int coutTotal;
    private Instance instance;
    private LinkedList<Chaine> chaines;
    private LinkedList<Cycle> cycles;

    public Solution(Instance i) {
        this.coutTotal = 0;
        this.instance = i;
        
    }

    public Solution(Solution solution) {
        this.coutTotal = solution.coutTotal;
        this.instance = solution.instance;
        chaines = new LinkedList<Chaine>(); // a voir
        cycles = new LinkedList<Cycle>(); // a voir
       
    }

    public int getCoutTotal() {
        return coutTotal;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    public LinkedList<Chaine> getChaines() {
        return new LinkedList<>(chaines);
    }

    public LinkedList<Cycle> getCycles() {
        return new LinkedList<>(cycles);
    }

    public Instance getInstance() {
        return instance;
    }

    
    
  

}
