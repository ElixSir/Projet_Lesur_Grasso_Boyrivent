/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instance;

import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author felix
 */
public class Instance {
    // le nom doit être unique
    private final String nom;
    private final int maxChaines;
    private final int maxCycles;
    private final int nbAltruistes;
    private final int nbPaires;
    
    
    /**
     * la clé est l'id de l'altruiste
     */
    private final Map<Integer,Altruiste> altruistes;
    /**
     * la clé est l'id de la paire
     */
    private final Map<Integer,Paire> paires;
    
    public Instance(String nom, int[][] matriceTransplantations, int nbAltruistes, int nbPaires, int maxCycles, int maxChaines) {
        this.nom = nom;
        this.maxCycles = maxCycles;
        this.maxChaines = maxChaines;
        this.nbAltruistes = nbAltruistes;
        this.nbPaires = nbPaires;
        
        this.altruistes = new LinkedHashMap<>();
        this.paires = new LinkedHashMap<>();
        
        int id = 1;
        
        for (int i = 0; i < nbAltruistes; i++, id++) {
            Altruiste a = new Altruiste(id);
            this.ajouterParticipant(a, matriceTransplantations);
        }

        for (int i = 0; i < nbPaires; i++, id++) {
            Paire p = new Paire(id);
            this.ajouterParticipant(p, matriceTransplantations);
        }
        
        /* TODO : CHECK */
    }
    
    /**
     * deux participants ayant le même id ne peuvent être présant 
     * ajoute le participant dans sa collection
     * @param p
     * @param mTrans matrice de transplantation
     * @return false si non ajouté sinon true
     */
    private boolean ajouterParticipant( Participant p, int[][] mTrans ) {
        if( null == p ) return false;
        
        int id = p.getId();
        
        if( this.altruistes.containsKey(id) || this.paires.containsKey(id)  ) return false;
        
        
        if( p instanceof Paire) {
            this.paires.put( id, (Paire) p );
            creerPaireTransplantation( (Paire) p, mTrans);
        } else if ( p instanceof Altruiste ) {
            this.altruistes.put( id,(Altruiste) p );
            creerAltruisteTransplantation( (Altruiste) p, mTrans);
        } else 
            return false;
        
        
        return true;
    }
    
    private void creerPaireTransplantation(Paire pToAdd, int[][] mTrans) {
        int idP = pToAdd.getId();
        
        for (Participant altruiste: this.altruistes.values()) {

            int idA = altruiste.getId();

            int benefice = this.getBenefice(idA, idP, mTrans);

            if( benefice != -1) {
                altruiste.ajouterTransplantation((Paire)pToAdd, benefice);
            }
        }
        
        for (Participant paire : this.paires.values()) {
            
            int idDonneur = paire.getId();
                
            int benefice = this.getBenefice(idDonneur, idP, mTrans);

            paire.ajouterTransplantation((Paire)pToAdd, benefice);
        }
        
    }
    
    private void creerAltruisteTransplantation(Altruiste a, int[][] mTrans) {
        for (Participant paire : this.paires.values()) {
            int idA = a.getId();
            int idP = paire.getId();
            
            int benefice = this.getBenefice(idA,idP, mTrans);
            
            a.ajouterTransplantation( (Paire) paire, benefice );
        }
    }
    
    
    private int getBenefice(int idAltruiste, int idParticipant, int[][] mTrans) {
        return mTrans[idAltruiste - 1][idParticipant - this.nbAltruistes - 1];
    }
    
    public int getMaxChaines() {
        return maxChaines;
    }

    public int getMaxCycles() {
        return maxCycles;
    }
    
    public String getNom() {
        return this.nom;
    }
    
    public int getNbAltruistes() {
        return this.nbAltruistes;
    }
    
    public int getNbPaires() {
        return this.nbPaires;
    }
    
    public LinkedList<Altruiste> getAltruistes() {
        return new LinkedList<>(this.altruistes.values());
    }
    
    public LinkedList<Paire> getPaires() {
        return new LinkedList<>(this.paires.values());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nom);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Instance other = (Instance) obj;
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Instance{" + 
                "\n\tnom = \"" + nom + "\"" +
                ",\n\tmaxChaines = " + maxChaines + 
                ",\n\tmaxCycles = " + maxCycles + 
                // ",\n\t matriceTransplantations=" + matriceTransplantations + 
                ",\n\taltruistes = " + this.altruistesToString() + 
                ",\n\tpaires = " + this.pairesToString() + 
                "\n}";
    }
    
    private String altruistesToString() {
        String s = "[";
        
        if( !this.altruistes.isEmpty() ) {
            
            for(Altruiste a : this.altruistes.values()){
                s += a.toString() + " ,";
            }

            s = s.substring(0, s.length() - 2);
        
        }
        
        return s + "]";
    }
    
    private String pairesToString() {
        String s = "[";
        
        if( !this.paires.isEmpty() ) {
            
            for(Paire a : this.paires.values()){
                s += a.toString() + " ,";
            }

            s = s.substring(0, s.length() - 2);

        }
        
        return s + "]";
    }
    
    
    public static void main(String[] args) {
        
    }
    
}