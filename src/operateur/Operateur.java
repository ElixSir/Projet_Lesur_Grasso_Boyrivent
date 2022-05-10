/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operateur;

import ensemble.Echanges;



/**
 *
 * @author yanni
 */
public abstract class Operateur {
    protected Echanges echange;
    protected int deltaBenefice;

    public Operateur(){
        this.deltaBenefice = -1;
    }
    
    public Operateur(Echanges echange) {
        this();
        this.echange = echange;
    }

    public int getDeltaBenefice() {
        return deltaBenefice;
    }
    
    /**
     * N'engendre pas un cout infini, donc on v�rifie le cout
     * @return 
     */
    public boolean isMouvementRealisable(){
        if(this.deltaBenefice == -1){
            return false;
        }
        return true;
    }
    
    public boolean isMouvementAmeliorant(){
        if(deltaBenefice > 0)
            return true;
        return false;
    }
    
    /**
     * Renvoie true si l'Op�rateur courant est strictement meilleur que l'op�rateur pass� en param
     * @param op
     * @return 
     */
    /*public boolean isMeilleur(Operateur op){
        if(op == null) return true;
        return this.getDeltaCout() <= op.getDeltaCout();
    }*/
    
    public boolean isMeilleur(Operateur op){
        if(op == null){
            return true;
        }
        if(this.getDeltaBenefice() > op.getDeltaBenefice()){
            return true;
        }
        return false;
    }

    protected abstract int evalDeltaBenefice();
    protected abstract boolean doMouvement();
    
    public boolean doMouvementIfRealisable(){
        if(!this.isMouvementRealisable()){
            return false;
        }
        return this.doMouvement();
    }

    public Echanges getEchange() {
        return this.echange;
    }
    
    

    @Override
    public String toString() {
        return "Operateur{" + "echange=" + this.echange + ", \ndeltaBenefice=" + deltaBenefice + '}';
    }
    
    
}
