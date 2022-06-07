/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import solution.Echanges;


/**
 *
 * @author Bart
 */
public abstract class OperateurIntraEchange extends OperateurLocal{

    public OperateurIntraEchange() {
        super();
    }

    public OperateurIntraEchange(int positionI, int positionJ, int longueurI, Echanges echange) {
        super(echange,positionI, positionJ, longueurI);
        this.benefice = this.evalBenefice();
    }
    
    protected abstract int evalBenefice();
}
