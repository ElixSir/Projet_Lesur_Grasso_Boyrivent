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
        this.deltaBenefice = Integer.MAX_VALUE;
    }
    
    public Operateur(Echanges echange) {
        this();
        this.echange = echange;
    }

    public int getDeltaBenefice() {
        return deltaBenefice;
    }
    
    /**
     * N'engendre pas un cout infini, donc on vérifie le cout
     * @return 
     */
    public boolean isMouvementRealisable(){
        if(this.deltaBenefice >= Integer.MAX_VALUE-10000 || this.deltaBenefice <= Integer.MIN_VALUE+10000){
            return false;
        }
        return true;
    }
    
    public boolean isMouvementAmeliorant(){
        if(deltaBenefice < 0)
            return true;
        return false;
    }
    
    /**
     * Renvoie true si l'Opérateur courant est strictement meilleur que l'opérateur passé en param
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
        if(op.getDeltaBenefice() <= this.getDeltaBenefice() ){
            return false;
        }
        return true;
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
