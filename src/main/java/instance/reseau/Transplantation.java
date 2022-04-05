/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instance.reseau;

/**
 *
 * @author Bart
 */
public class Transplantation {
    private final int benefice;
    private final Paire beneficiaire;
    private final Participant donneur;
    
    /**
     * le bénéficiaire ne peut être qu'un donneur
     * @param donneur
     * @param beneficiaire
     * @param benefice 
     */
    public Transplantation(Participant donneur, Paire beneficiaire, int benefice ) {   
       this.benefice = benefice;
       this.donneur = donneur;
       this.beneficiaire = beneficiaire;
    }

    public int getBenefice() {
        return benefice;
    }

    public Paire getBeneficiaire() {
        return beneficiaire;
    }

    public Participant getDonneur() {
        return donneur;
    }
}
