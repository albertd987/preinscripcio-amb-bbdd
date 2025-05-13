package classes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classes.model.Centre;
import classes.model.CentreEstudi;
import classes.model.Estudi;
import classes.utils.ConnexioBBDD;

public class CentreEstudiDAO {
    
    /**
     * Processa i insereix les dades de centres i estudis des de Centre_Estudis.csv
     * @param dades Llista de arrays de strings amb les dades del fitxer CSV
     * @return Un array amb [nombreCentresInserits, nombreEstudisInserits, nombreRelacionsInserides]
     */
    public int[] processarCentresEstudis(List<String[]> dades) {
        int[] resultats = new int[3]; // [centres, estudis, relacions]
        
        // Utilitzem Maps per evitar duplicats
        Map<String, Centre> centres = new HashMap<>();
        Map<String, Estudi> estudis = new HashMap<>();
        Map<String, CentreEstudi> relacions = new HashMap<>();
        
        // Processem les dades del CSV
        for (String[] fila : dades) {
            if (fila.length >= 4) {
                String codiCentre = fila[0];
                String nomCentre = fila[1];
                String codiEstudi = fila[2];
                String nomEstudi = fila[3];
                
                // Afegim el centre si no existeix
                if (!centres.containsKey(codiCentre)) {
                    centres.put(codiCentre, new Centre(codiCentre, nomCentre));
                }
                
                // Afegim l'estudi si no existeix
                if (!estudis.containsKey(codiEstudi)) {
                    estudis.put(codiEstudi, new Estudi(codiEstudi, nomEstudi));
                }
                
                // Afegim la relació centre-estudi
                String clauRelacio = codiCentre + "-" + codiEstudi;
                if (!relacions.containsKey(clauRelacio)) {
                    relacions.put(clauRelacio, new CentreEstudi(codiCentre, codiEstudi));
                }
            }
        }
        
        // Inserim els centres
        resultats[0] = inserirCentres(centres.values().toArray(new Centre[0]));
        
        // Inserim els estudis
        resultats[1] = inserirEstudis(estudis.values().toArray(new Estudi[0]));
        
        // Inserim les relacions
        resultats[2] = inserirRelacions(relacions.values().toArray(new CentreEstudi[0]));
        
        return resultats;
    }
    
    /**
     * Insereix centres a la base de dades
     * @param centres Array de centres a inserir
     * @return nombre de centres inserits
     */
    private int inserirCentres(Centre[] centres) {
        String sql = "INSERT INTO Centres (codi_centre, nom_centre) VALUES (?, ?)";
        int comptador = 0;
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (Centre centre : centres) {
                stmt.setString(1, centre.getCodiCentre());
                stmt.setString(2, centre.getNomCentre());
                
                try {
                    stmt.executeUpdate();
                    comptador++;
                } catch (SQLException e) {
                    System.err.println("Error al inserir el centre " + centre.getCodiCentre() + ": " + e.getMessage());
                }
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
            System.err.println("Error general al inserir centres: " + e.getMessage());
            e.printStackTrace();
        }
        
        return comptador;
    }
    
    /**
     * Insereix estudis a la base de dades
     * @param estudis Array d'estudis a inserir
     * @return nombre d'estudis inserits
     */
    private int inserirEstudis(Estudi[] estudis) {
        String sql = "INSERT INTO Estudis (codi_estudi, nom_estudi) VALUES (?, ?)";
        int comptador = 0;
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (Estudi estudi : estudis) {
                stmt.setString(1, estudi.getCodiEstudi());
                stmt.setString(2, estudi.getNomEstudi());
                
                try {
                    stmt.executeUpdate();
                    comptador++;
                } catch (SQLException e) {
                    System.err.println("Error al inserir l'estudi " + estudi.getCodiEstudi() + ": " + e.getMessage());
                }
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
            System.err.println("Error general al inserir estudis: " + e.getMessage());
            e.printStackTrace();
        }
        
        return comptador;
    }
    
    /**
     * Insereix relacions centre-estudi a la base de dades
     * @param relacions Array de relacions a inserir
     * @return nombre de relacions inserides
     */
    private int inserirRelacions(CentreEstudi[] relacions) {
        String sql = "INSERT INTO Centre_Estudis (codi_centre, codi_estudi) VALUES (?, ?)";
        int comptador = 0;
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (CentreEstudi relacio : relacions) {
                stmt.setString(1, relacio.getCodiCentre());
                stmt.setString(2, relacio.getCodiEstudi());
                
                try {
                    stmt.executeUpdate();
                    comptador++;
                } catch (SQLException e) {
                    System.err.println("Error al inserir la relació entre centre " + 
                            relacio.getCodiCentre() + " i estudi " + relacio.getCodiEstudi() + 
                            ": " + e.getMessage());
                }
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
            System.err.println("Error general al inserir relacions centre-estudi: " + e.getMessage());
            e.printStackTrace();
        }
        
        return comptador;
    }
}