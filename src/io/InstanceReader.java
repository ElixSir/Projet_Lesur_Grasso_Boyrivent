/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

// TO CHECK : import des classes Instance, Client, Depot et Point
import instance.Instance;
import instance.reseau.Altruiste;
import instance.reseau.Paire;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 * Cette classe permet de lire une instance pour les TPs du cours de LE4-SI
 * POO pour l'optimisation.
 * 
 * Les instances sont fournies sur moodle au format ".vrp".
 * 
 * Pour que le lecteur d'instance fonctionne correctement, il faut que les 
 * signatures des constructeurs des classes Depot, Client, et Instances, ainsi 
 * que la methode ajouterClient de la classe Instance soient bien conformes a 
 * la description dans le sujet du TP.
 * Des commentaires annotes avec 'TO CHECK' vous permettent de facilement reperer
 * dans cette classe les lignes que vous devez verifier et modifier si besoin. 
 * 
 * @author Maxime Ogier
 */
public class InstanceReader {
    /**
     * Le fichier contenant l'instance.
     */
    private File instanceFile;
    
    private final String fileFormat = "txt";
    
    /**
     * Constructeur par donnee du chemin du fichier d'instance.
     * @param inputPath le chemin du fichier d'instance, qui doit se terminer 
     * par l'extension du fichier (.xml).
     * @throws ReaderException lorsque le fichier n'est pas au bon format ou 
     * ne peut pas etre ouvert.
     */
    public InstanceReader(String inputPath) throws ReaderException {
        if (inputPath == null) {
            throw new OpenFileException();
        }
        if (!inputPath.endsWith("." + this.fileFormat)) {
            throw new FormatFileException(this.fileFormat,this.fileFormat);
        }
        String instanceName = inputPath;
        this.instanceFile = new File(instanceName);
    }
    
    /**
     * Methode principale pour lire le fichier d'instance.
     * @return l'instance lue
     * @throws ReaderException lorsque les donnees dans le fichier d'instance 
     * sont manquantes ou au mauvais format.
     */
    public Instance readInstance() throws ReaderException {
        try{
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            
            int id = 1;
            
            String instanceName = this.instanceFile.getName();
            instanceName = instanceName.replace("."+this.fileFormat, "");
            
            int nbPaires = this.lireInt(br);
            int nbAltruistes = this.lireInt(br);
            int maxCycle = this.lireInt(br);
            int maxChaine = this.lireInt(br);
            
            // System.out.println("nbPaires : " + nbPaires);
            // System.out.println("nbAltruistes : " + nbAltruistes);
            
            int[][] matriceDeCorrelation = this.lireMatrice(br, nbAltruistes ,nbPaires);
            
            // this.printMatrice(matriceDeCorrelation);
            
            Instance instance = new Instance( instanceName, matriceDeCorrelation, maxCycle, maxChaine );
            
            for (int i = 0; i < nbAltruistes; i++, id++) {
                Altruiste a = new Altruiste(id);
                instance.ajouterParticipant(a);
            }
            
            for (int i = 0; i < nbPaires; i++, id++) {
                Paire p = new Paire(id);
                instance.ajouterParticipant(p);
            }
            
            br.close();
            f.close();
            
            return instance;
        } catch (FileNotFoundException ex) {
            throw new FileExistException(instanceFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
        }
    }
    
    /**
     * 
     * @param line
     * @return true si c'est une ligne avec des données
     */
    private boolean isDataLine(String line) {
        return (
                true//line != null 
                && !line.equals("")
                && !line.equals("\n")
                && !line.startsWith("//") 
                );
    }
    
    /**
     * 
     * @param br
     * @return la prochaine line avec la data
     * @throws IOException 
     */
    private String getDataLine(BufferedReader br) throws IOException{
        String line = br.readLine();
        
        while( !this.isDataLine(line) ) line = br.readLine();
        
        return line;
    }
    
    /**
     * 
     * @param br
     * @return une ligne parsé en int
     * @throws IOException 
     */
    private int lireInt(BufferedReader br) throws IOException {
        String s = this.getDataLine(br);
        
        return Integer.parseInt(s);
    }
    
    /**
     * 
     * @param br
     * @param nbAltruistes
     * @param nbPaires
     * @return
     * @throws IOException 
     */
    private int[][] lireMatrice(BufferedReader br, int nbAltruistes, int nbPaires) throws IOException {
        int[][] matrice = new int[nbAltruistes + nbPaires][nbPaires];
        
        for(int i = 0; i < matrice.length; i++ ){
            matrice[i] = lireLigneMatrice(br, nbPaires);
        }
        
        return matrice;
    }
    
    /**
     * 
     * @param br
     * @param nbColonnes
     * @return
     * @throws IOException 
     */
    private int[] lireLigneMatrice(BufferedReader br, int nbColonnes) throws IOException {
        int[] res = new int[nbColonnes] ;
        String ligne = this.getDataLine(br);
        String[] values = ligne.split("\t");
        
        for (int i = 0; i < values.length; i++) {
            res[i] = Integer.parseInt( values[i] );
        }
        
        return res;
    }
    
    /**
     * affiche la matrice de correlation
     * @param m 
     */
    private void printMatrice(int[][] m ) {
        System.out.println("Matrice (" + m.length + ","+ m[0].length + "): " );
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + "\t");
            }
            System.out.println("");
        }
    }
    
    
    /**
     * Test de lecture d'une instance.
     * @param args 
     */
    public static void main(String[] args) {
        File folder = new File("instancesInitiales");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    // TO CHECK : constructeur de InstanceReader
                    InstanceReader reader = new InstanceReader(file.getAbsolutePath());
                    // TO CHECK : lecture d'une instance avec la classe InstanceReader
                    Instance i = reader.readInstance();
                    System.out.println(i.toString());
                } catch (ReaderException ex) {
                    System.out.println("L'instance " + file.getAbsolutePath()
                            + " n'a pas pu etre lue correctement");
                }
            }
        }
        
        /*
        try {
            
            
            InstanceReader reader = new InstanceReader("instancesInitiales/KEP_p9_n0_k3_l0.txt");
            Instance i = reader.readInstance();
            System.out.println("Instance lue avec succes !");
            System.out.println("Instance " + i.getNom() + " :");
            System.out.println(i.toString());
            
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
        */
    }
}
