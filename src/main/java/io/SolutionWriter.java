/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io;

import instance.Instance;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;
import solution.Solution;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Clem
 */
public class SolutionWriter {
    /**
     * Le fichier contenant l'instance.
     */
    private File instanceFile;
    private Solution solution;
    
    private final String fileFormat = "txt";
    private final String outputPath = "";
    
    
    public SolutionWriter( Solution solution) throws ReaderException {
        String instanceName = this.outputPath;
        this.solution = solution;
        this.instanceFile = new File(instanceName);
    }
    
    /**
     * Methode principale pour ecrire le fichier de solution.
     * @throws ReaderException lorsque les donnees dans le fichier d'instance 
     * sont manquantes ou au mauvais format.
     */
    public void writeSolution() throws ReaderException {
        try{
            FileWriter f = new FileWriter(this.instanceFile.getAbsolutePath());
            BufferedWriter br = new BufferedWriter(f);
            
            
            
            br.close();
            f.close();
            
        } catch (FileNotFoundException ex) {
            throw new FileExistException(instanceFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        
    }
}
