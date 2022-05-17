/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import solution.ensemble.Echanges;
import instance.reseau.Paire;



/**
 *
 * @author Bart
 */
public abstract class OperateurInterEchange extends OperateurLocal {
    protected Echanges autreEchange;
    protected int deltaBeneficeEchange;
    protected int deltaBeneficeAutreEchange;


    public OperateurInterEchange() {
        super();
        this.deltaBeneficeEchange = Integer.MAX_VALUE;
        this.deltaBeneficeAutreEchange = Integer.MAX_VALUE;
    }

       
    public OperateurInterEchange(Echanges echange, Echanges autreEchange, int positionI, int positionJ) {
        super(echange,positionI, positionJ);
        this.autreEchange = autreEchange;
        this.paireJ = (Paire) autreEchange.getCurrent(positionJ);
        this.deltaBenefice = this.evalDeltaBenefice();
    }
    
    protected abstract int evalDeltaBeneficeEchange();
    protected abstract int evalDeltaBeneficeAutreEchange();

    public Echanges getAutreEchange() {
        return this.autreEchange;
    }

    public Echanges getEchange() {
        return this.echange;
    }

    public int getDeltaBeneficeEchange() {
        return this.deltaBeneficeEchange;
    }

    public int getDeltaBeneficeAutreEchange() {
        return this.deltaBeneficeAutreEchange;
    }
    
    
    
    @Override
    protected int evalDeltaBenefice() {
        this.deltaBenefice = this.evalDeltaBeneficeEchange();
        this.deltaBeneficeAutreEchange = this.evalDeltaBeneficeAutreEchange();
        
        if(this.evalDeltaBeneficeEchange() == Integer.MAX_VALUE || this.evalDeltaBeneficeAutreEchange() == Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }
        return this.evalDeltaBeneficeEchange()+ this.evalDeltaBeneficeAutreEchange();
    }
    
    
    
}
