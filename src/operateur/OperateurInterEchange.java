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
    protected int beneficeEchange;
    protected int deltaBeneficeEchange;
    protected int beneficeAutreEchange;
    protected int deltaBeneficeAutreEchange;


    public OperateurInterEchange() {
        super();
        this.beneficeEchange = Integer.MAX_VALUE;
        this.beneficeAutreEchange = Integer.MAX_VALUE;
    }

    public OperateurInterEchange(Echanges echange, Echanges autreEchange, int positionI, int positionJ, int longueurI, int longueurJ) {
        super(echange, positionI, positionJ, longueurI, longueurJ);
        this.autreEchange = autreEchange;
        this.paireJ = (Paire) autreEchange.getCurrent(positionJ);
        this.paireI = (Paire) echange.getCurrent(positionI);
        this.deltaBeneficeAutreEchange = -1;
        this.deltaBenefice = -1;
        this.evalBenefice();

    }

    public Echanges getAutreEchange() {
        return this.autreEchange;
    }

    public Echanges getEchange() {
        return this.echange;
    }

    public int getBeneficeEchange() {
        return this.beneficeEchange;
    }

    public int getBeneficeAutreEchange() {
        return this.beneficeAutreEchange;
    }

}
