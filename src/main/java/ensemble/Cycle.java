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
public class Cycle extends Echanges {
    private int maxCycle;

    public Cycle() {
        maxCycle = 1;
    }

    public int getMaxCycle() {
        return maxCycle;
    }
}
