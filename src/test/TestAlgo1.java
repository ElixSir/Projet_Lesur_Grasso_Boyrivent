/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import instance.Instance;
import instance.reseau.Participant;
import instance.reseau.Transplantation;
import java.util.LinkedList;
import solution.Solution;

/**
 *
 * @author felix
 */
public class TestAlgo1 {

    public void Solve(Instance instance) {
        Solution s = new Solution();
        LinkedList<Participant> participants = instance.getParticipants();
	for (Participant p : participants)
        {
            for (Transplantation t : p.getTransplantations().values())  {
                if (t.echangePossible()){
                    s.doEchange(t);
                }

            }
           
        }
        
    }
}
