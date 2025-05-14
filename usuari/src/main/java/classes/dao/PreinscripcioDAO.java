package classes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import classes.model.Preinscripcio;
import classes.utils.ConnexioBBDD;

public class PreinscripcioDAO {
    
    public List<Preinscripcio> obtenirTots() {
        List<Preinscripcio> preinscripcions = new ArrayList<>();
        String sql = "SELECT codi_estudi, codi_centre, dni, curs, prioritat FROM Preinscripcions";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String codiEstudi = rs.getString("codi_estudi");
                String codiCentre = rs.getString("codi_centre");
                String dni = rs.getString("dni");
                String curs = rs.getString("curs");
                int prioritat = rs.getInt("prioritat");
                
                preinscripcions.add(new Preinscripcio(codiEstudi, codiCentre, dni, curs, prioritat));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtenir totes les preinscripcions: " + e.getMessage());
            e.printStackTrace();
        }
        
        return preinscripcions;
    }
    
    public List<Preinscripcio> obtenirPerAlumne(String dni) {
        List<Preinscripcio> preinscripcions = new ArrayList<>();
        String sql = "SELECT codi_estudi, codi_centre, dni, curs, prioritat " +
                    "FROM Preinscripcions " +
                    "WHERE dni = ? " +
                    "ORDER BY prioritat";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String codiEstudi = rs.getString("codi_estudi");
                    String codiCentre = rs.getString("codi_centre");
                    String curs = rs.getString("curs");
                    int prioritat = rs.getInt("prioritat");
                    
                    preinscripcions.add(new Preinscripcio(codiEstudi, codiCentre, dni, curs, prioritat));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtenir les preinscripcions de l'alumne: " + e.getMessage());
            e.printStackTrace();
        }
        
        return preinscripcions;
    }
    
    public Preinscripcio obtenirPreinscripcio(String codiEstudi, String codiCentre, String dni) {
        String sql = "SELECT codi_estudi, codi_centre, dni, curs, prioritat " +
                    "FROM Preinscripcions " +
                    "WHERE codi_estudi = ? AND codi_centre = ? AND dni = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiEstudi);
            stmt.setString(2, codiCentre);
            stmt.setString(3, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String curs = rs.getString("curs");
                    int prioritat = rs.getInt("prioritat");
                    
                    return new Preinscripcio(codiEstudi, codiCentre, dni, curs, prioritat);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtenir la preinscripció: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean existeixPreinscripcio(String codiEstudi, String codiCentre, String dni) {
        String sql = "SELECT 1 FROM Preinscripcions WHERE codi_estudi = ? AND codi_centre = ? AND dni = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiEstudi);
            stmt.setString(2, codiCentre);
            stmt.setString(3, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); 
            }
            
        } catch (SQLException e) {
            System.err.println("Error al comprovar si existeix la preinscripció: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean existeixPreinscripcioMateixaPrioritat(String dni, int prioritat) {
        String sql = "SELECT 1 FROM Preinscripcions WHERE dni = ? AND prioritat = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            stmt.setInt(2, prioritat);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); 
            }
            
        } catch (SQLException e) {
            System.err.println("Error al comprovar si existeix preinscripció amb mateixa prioritat: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean existeixAltrePreinscripcioMateixaPrioritat(String dni, int prioritat, String codiEstudi, String codiCentre) {
        String sql = "SELECT 1 FROM Preinscripcions " +
                    "WHERE dni = ? AND prioritat = ? " +
                    "AND NOT (codi_estudi = ? AND codi_centre = ?)";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            stmt.setInt(2, prioritat);
            stmt.setString(3, codiEstudi);
            stmt.setString(4, codiCentre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); 
            }
            
        } catch (SQLException e) {
            System.err.println("Error al comprovar si existeix altra preinscripció amb mateixa prioritat: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean inserir(Preinscripcio preinscripcio) {
        String sql = "INSERT INTO Preinscripcions (codi_estudi, codi_centre, dni, curs, prioritat) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, preinscripcio.getCodiEstudi());
            stmt.setString(2, preinscripcio.getCodiCentre());
            stmt.setString(3, preinscripcio.getDni());
            stmt.setString(4, preinscripcio.getCurs());
            stmt.setInt(5, preinscripcio.getPrioritat());
            
            int files = stmt.executeUpdate();
            return files > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al inserir la preinscripció: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualitzar(Preinscripcio preinscripcio) {
        String sql = "UPDATE Preinscripcions SET curs = ?, prioritat = ? " +
                    "WHERE codi_estudi = ? AND codi_centre = ? AND dni = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, preinscripcio.getCurs());
            stmt.setInt(2, preinscripcio.getPrioritat());
            stmt.setString(3, preinscripcio.getCodiEstudi());
            stmt.setString(4, preinscripcio.getCodiCentre());
            stmt.setString(5, preinscripcio.getDni());
            
            int files = stmt.executeUpdate();
            return files > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualitzar la preinscripció: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminar(String codiEstudi, String codiCentre, String dni) {
        String sql = "DELETE FROM Preinscripcions WHERE codi_estudi = ? AND codi_centre = ? AND dni = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiEstudi);
            stmt.setString(2, codiCentre);
            stmt.setString(3, dni);
            
            int files = stmt.executeUpdate();
            return files > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar la preinscripció: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public int comptarPreinscripcionsPerAlumne(String dni) {
        String sql = "SELECT COUNT(*) FROM Preinscripcions WHERE dni = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al comptar les preinscripcions: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
}