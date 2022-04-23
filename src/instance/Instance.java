/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package instance;

import instance.reseau.Altruiste;
import instance.reseau.Paire;
import instance.reseau.Participant;
import instance.reseau.Transplantation;
import io.InstanceReader;
import io.exception.ReaderException;
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
            this.ajouterParticipant(a);
        }

        for (int i = 0; i < nbPaires; i++, id++) {
            Paire p = new Paire(id);
            this.ajouterParticipant(p);
        }
        
        this.ajouterTransplantations(matriceTransplantations);
        
        /* CHECK */
        this.check(matriceTransplantations);
    }
    
    /**
     * deux participants ayant le même id ne peuvent être présant 
     * ajoute le participant dans sa collection
     * @param p
     * @param mTrans matrice de transplantation
     * @return false si non ajouté sinon true
     */
    private boolean ajouterParticipant( Participant p) {
        if( null == p ) return false;
        
        int id = p.getId();
        
        if( this.altruistes.containsKey(id) || this.paires.containsKey(id)  ) return false;
        
        
        if( p instanceof Paire) {
            this.paires.put( id, (Paire) p );
        } else if ( p instanceof Altruiste ) {
            this.altruistes.put( id,(Altruiste) p );
        } else 
            return false;
        
        
        return true;
    }
    
    private void ajouterTransplantations(int[][] matriceTransplantation) {
        
        int idA, idP, benefice;
        
        for (Altruiste a : this.altruistes.values()) {
            
            idA = a.getId();
            
            for (Paire p : this.paires.values()) {
            
                idP = p.getId();
                benefice = this.getBenefice(idA, idP, matriceTransplantation);
                
                if(benefice >= 0) {
                    a.ajouterTransplantation(p, benefice);
                }
            }
        }
        
        for (Paire donneur : this.paires.values()) {
            
            idA = donneur.getId();
            
            for (Paire receveur : this.paires.values()) {
            
                idP = receveur.getId();
                benefice = this.getBenefice(idA, idP, matriceTransplantation);
                
                if(benefice >= 0) {
                    donneur.ajouterTransplantation(receveur, benefice);
                }
            }
        }
    }
    
    
    private int getBenefice(int idDonneur, int idReceveur, int[][] mTrans) {
        // System.out.println("i: " + (idDonneur - 1) + ", j: " + (idReceveur- this.nbAltruistes - 1));
        return mTrans[idDonneur - 1][idReceveur - this.nbAltruistes - 1];
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
    
    public Paire getPaireById(int id) {
        return this.paires.get(id);
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
    
    public boolean check(int[][] matriceTransplantations) {
        boolean checker = true;
        int checkNbDiffTrans = this.nbParticipantsTransplantationsManquantes( matriceTransplantations);
        int[][] m = matriceTransplantations.clone();
        
        if( m[0].length != this.nbPaires) {
            System.err.println("[CHECK - Instance] : La taille de la matrice ne correspond pas avec le nombre de paires données : ( length : " + m[0].length + ", this.nbPaires : " + this.nbPaires + " )");
            checker = false;
        }
        
        if( m[0].length != this.paires.size()) {
            System.err.println("[CHECK - Instance] : La taille de la matrice ne correspond pas avec le nombre de paires ajoutées dans l'instance : ( length : " + m[0].length + ", paires size : " + this.paires.size() + " )");
            checker = false;
        }
        
        if( this.nbAltruistes != this.altruistes.size()) {
            System.err.println("[CHECK - Instance] : Le nombre d'altruistes données ne correspond pas avec le nombre d'altruistes ajoutés dans l'instance : ( matrice length : " + m.length + ", altruistes size : " + this.altruistes.size() + " )");
            checker = false;
        }
        
        if( checkNbDiffTrans > 0 ) {
            System.err.println("[CHECK - Instance] : " + checkNbDiffTrans + " participant(s) n'ont pas toutes leur(s) transplantations");
            checker = false;
        }
        
        return checker;
    }
    
    private int nbParticipantsTransplantationsManquantes(int[][] matriceTransplantations) {
        int[][] m = matriceTransplantations.clone();
        int nbParti = 0;
        
        for (Altruiste a : this.altruistes.values()) {
            int n = 0;
            for (int i = 0; i < m[a.getId() - 1].length; i++) {
                int t = m[a.getId() - 1][i];
                if(t >= 0) 
                    n ++;
            }
            
            if(a.getTransplantations().size() != n) {
                nbParti++;
            }
        }
        
        for (Paire p : this.paires.values()) {
            int n = 0;
            for (int i = 0; i < m[p.getId() - 1].length; i++) {
                int t = m[p.getId() - 1][i];
                if(t >= 0) 
                    n ++;
            }
            
            if(p.getTransplantations().size() != n) {
                nbParti++;
            }
        }
        
        return nbParti;
    } 
    
    
    public static void main(String[] args) {
        // KEP_p9_n1_k3_l3
        try {
            InstanceReader read = new InstanceReader("instancesInitiales/KEP_p100_n11_k3_l4.txt");
            Instance i = read.readInstance();

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}