package classes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classes.model.Centre;
import classes.model.CentreEstudi;
import classes.model.Estudi;
import classes.utils.ConnexioBBDD;

public class CentreEstudiDAO {


    public int[] processarCentresEstudis(List<String[]> dades) {
        int[] resultats = new int[3]; // [centres, estudis, relacions]
        
        // Utilitzo Maps per evitar duplicats, així a la base de dades no em cal fer trigger x controlar-ho
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
                }
            }
            
            conn.commit();
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
            System.err.println( e.getMessage());
            e.printStackTrace();
        }
        
        return comptador;
    }
    
    // Nous mètodes per la secció de l'usuari
    
    public List<CentreEstudi> obtenirTots() {
        List<CentreEstudi> relacions = new ArrayList<>();
        String sql = "SELECT codi_centre, codi_estudi FROM Centre_Estudis";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String codiCentre = rs.getString("codi_centre");
                String codiEstudi = rs.getString("codi_estudi");
                relacions.add(new CentreEstudi(codiCentre, codiEstudi));
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        return relacions;
    }

    public List<Estudi> obtenirEstudisByCentre(String codiCentre) {
        List<Estudi> estudis = new ArrayList<>();
        String sql = "SELECT e.codi_estudi, e.nom_estudi " +
                    "FROM Estudis e " +
                    "JOIN Centre_Estudis ce ON e.codi_estudi = ce.codi_estudi " +
                    "WHERE ce.codi_centre = ? " +
                    "ORDER BY e.nom_estudi";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiCentre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String codiEstudi = rs.getString("codi_estudi");
                    String nomEstudi = rs.getString("nom_estudi");
                    estudis.add(new Estudi(codiEstudi, nomEstudi));
                }
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        return estudis;
    }
    
    public List<Centre> obtenirCentresByEstudi(String codiEstudi) {
        List<Centre> centres = new ArrayList<>();
        String sql = "SELECT c.codi_centre, c.nom_centre " +
                    "FROM Centres c " +
                    "JOIN Centre_Estudis ce ON c.codi_centre = ce.codi_centre " +
                    "WHERE ce.codi_estudi = ? " +
                    "ORDER BY c.nom_centre";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiEstudi);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String codiCentre = rs.getString("codi_centre");
                    String nomCentre = rs.getString("nom_centre");
                    centres.add(new Centre(codiCentre, nomCentre));
                }
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
        return centres;
    }
    
    public boolean existeixRelacio(String codiCentre, String codiEstudi) {
        String sql = "SELECT 1 FROM Centre_Estudis WHERE codi_centre = ? AND codi_estudi = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiCentre);
            stmt.setString(2, codiEstudi);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si hi han resultats hi ha una relació existent
            }
            
        } catch (SQLException e) {
            System.err.println("Error al comprovar si existeix la relació: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}