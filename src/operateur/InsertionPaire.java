/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operateur;

import ensemble.Echanges;
import instance.reseau.Paire;

/**
 *
 * @author Bart
 */
public class InsertionPaire extends Operateur {
    
   private int position;
   private Paire paireToInsert;
   
   
    public InsertionPaire(Echanges echange, int position, Paire paireToInsert) {
        super(echange);
        this.position = position;
        this.paireToInsert = paireToInsert;
        this.deltaBenefice = this.evalDeltaBenefice();
    }
    

   
   
   
    @Override
    protected int evalDeltaBenefice() {
        if(this.echange == null) return Integer.MAX_VALUE;
        return this.echange.deltaCoutInsertion(this.position, this.paireToInsert);
    }

    @Override
    protected boolean doMouvement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
