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
public class IntraEchange extends OperateurIntraEchange {

    public IntraEchange() {
        super();
    }

    public IntraEchange(Echanges echange, int positionI, int positionJ, int longueurI) {
        super(positionI, positionJ, longueurI, echange);
    }


    @Override
    public String toString() {
        return "IntraEchange{" +  "positionI=" + positionI +  ", positionJ=" + positionJ + ", coutDeplacement=" + benefice +", paireI="+ paireI +", paireJ="+ paireJ+",echange="+this.echange+ '}';
    }
    
    
    
    /*public boolean isTabou(OperateurLocal operateur) {
        if(operateur == null) return false;
        if(!(operateur instanceof IntraEchange)) return false;
        if(operateur.tournee == null || operateur.clientI == null || operateur.clientJ == null) return false;
        if(!this.tournee.equals(operateur.tournee)) return false;
        if(this.clientI.equals(operateur.clientI) && this.clientJ.equals(operateur.clientJ))
            return true;
        if(this.clientI.equals(operateur.clientJ) && this.clientJ.equals(operateur.clientI))
            return true;
        
        return false;
    }*/

    @Override
    protected int evalBenefice() {
        if(echange == null) return -1;
        
        return this.echange.deltaBeneficeEchange(this.positionI, this.positionJ);
    }

    @Override
    protected boolean doMouvement() {
        return this.echange.doEchangeCycle(this);
    }

    
}
