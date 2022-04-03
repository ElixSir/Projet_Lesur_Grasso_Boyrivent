/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

// TO CHECK : import des classes Instance, Client, Depot et Point
import instance.Instance;
import instance.reseau.Participant;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
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
    
    private final String fileFormat = "vrp";
    
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
            
            Instance instance = new Instance(this.instanceFile.getName());
            
            int nbPaires = this.lireInt(br);
            int nbAltruistes = this.lireInt(br);
            int maxCycle = this.lireInt(br);
            int maxChaine = this.lireInt(br);
            
            Map<Integer,Participant> participants = lireParticipants(br);
            
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
    
    private Map<Integer,Participant> lireParticipants(BufferedReader br) throws IOException {
        Map<Integer,Participant> ps = new LinkedHashMap<>();
        String line = this.getDataLine(br);
        
        while (line != null) {
            Participant p = this.lireUnParticipant(line);
            ps.put(p.getId(), p);
            line = this.getDataLine(br);
        }
        
        return ps;
    }
    
    private Participant lireUnParticipant(String line) throws IOException, NumberFormatException {
        Participant p;
        
        return p;
    }
    
    
    /**
     * Test de lecture d'une instance.
     * @param args 
     */
    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            reader.readInstance();
            System.out.println("Instance lue avec succes !");
            System.out.println("Depot : ");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
