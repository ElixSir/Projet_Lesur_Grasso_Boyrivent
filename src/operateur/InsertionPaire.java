/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operateur;

import solution.Echanges;
import instance.reseau.Paire;
import solution.Cycle;

/**
 *
 * @author Bart
 */
public class InsertionPaire extends Operateur {
    
   private int position;
   private Paire paire;

    public InsertionPaire() {
        super();
    }
   
   
   
    public InsertionPaire(Echanges echange, int position, Paire paireToInsert) {
        super(echange);
        this.position = position;
        this.paire = paireToInsert;
        this.benefice = this.evalBenefice();
    }
    

   
   
   
    @Override
    protected int evalBenefice() {
        if(this.echange == null) return -1;

        return this.echange.deltaBeneficeInsertion(this.position, this.paire);
    }

    @Override
    protected boolean doMouvement() {
        return this.echange.doInsertion(this);
    }

    public Paire getPaire() {
        return paire;
    }

    public int getPosition() {
        return position;
    }
    
}
