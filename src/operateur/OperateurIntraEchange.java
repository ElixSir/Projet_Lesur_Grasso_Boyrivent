/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import solution.ensemble.Echanges;


/**
 *
 * @author Bart
 */
public abstract class OperateurIntraEchange extends OperateurLocal{

    public OperateurIntraEchange() {
        super();
    }

    public OperateurIntraEchange(int positionI, int positionJ, Echanges echange, int longueurI, int longueurJ) {
        super(echange,positionI, positionJ, longueurI, longueurJ);
        this.benefice = this.evalBenefice();
    }
    
    protected abstract int evalBenefice();
}
