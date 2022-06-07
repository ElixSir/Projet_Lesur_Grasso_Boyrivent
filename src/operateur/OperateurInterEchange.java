/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import solution.Echanges;
import instance.reseau.Paire;



/**
 *
 * @author Bart
 */
public abstract class OperateurInterEchange extends OperateurLocal {
    protected Echanges autreEchange;
    protected int deltaBeneficeEchange;
    protected int beneficeEchange;
    protected int deltaBeneficeAutreEchange;
    protected int beneficeAutreEchange;


    public OperateurInterEchange() {
        super();
        this.deltaBeneficeEchange = -1;
        this.deltaBeneficeAutreEchange = -1;
    }

       
    public OperateurInterEchange(Echanges echange, Echanges autreEchange, int positionI, int positionJ, int longueurI) {
        super(echange,positionI, positionJ, longueurI);
        this.autreEchange = autreEchange;
        this.paireJ = (Paire) autreEchange.getCurrent(positionJ);
        this.benefice = this.evalBenefice();
    }
    
    protected abstract int evalBeneficeEchange();
    protected abstract int evalBeneficeAutreEchange();

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
    protected int evalBenefice() {
        this.beneficeEchange = this.evalBeneficeEchange();
        this.deltaBeneficeAutreEchange = this.evalBeneficeAutreEchange();
        
        if(this.evalBeneficeEchange() == -1|| this.evalBeneficeAutreEchange() == -1){
            return -1;
        }
        return this.evalBeneficeEchange()+ this.evalBeneficeAutreEchange();
    }
    
    
    
}
