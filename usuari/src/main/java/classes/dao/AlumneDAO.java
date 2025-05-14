package classes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import classes.model.Alumne;
import classes.utils.ConnexioBBDD;

public class AlumneDAO {

    public int inserirAlumnes(List<Alumne> alumnes) {
        String sql = "INSERT INTO Alumnes (dni, nom_alumne) VALUES (?, ?)";
        int comptador = 0;
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Desactivem l'autocommit per fer una operació per lots
            conn.setAutoCommit(false);
            
            for (Alumne alumne : alumnes) {
                stmt.setString(1, alumne.getDni());
                stmt.setString(2, alumne.getNomAlumne());
                
                try {
                    stmt.executeUpdate();
                    comptador++;
                } catch (SQLException e) {
                    System.err.println("Error al inserir l'alumne amb DNI " + alumne.getDni() + ": " + e.getMessage());
                }
            }
            
            // Faig commit dels canvis
            conn.commit();
            
            // Restauro l'autocommit
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
        }
        
        return comptador;
    }
    

    
    public List<Alumne> obtenirTots() {
        List<Alumne> alumnes = new ArrayList<>();
        String sql = "SELECT dni, nom_alumne FROM Alumnes ORDER BY nom_alumne";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String dni = rs.getString("dni");
                String nomAlumne = rs.getString("nom_alumne");
                alumnes.add(new Alumne(dni, nomAlumne));
            }
            
        } catch (SQLException e) {
        }
        
        return alumnes;
    }
    
    public Alumne obtenirPerDni(String dni) {
        String sql = "SELECT dni, nom_alumne FROM Alumnes WHERE dni = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nomAlumne = rs.getString("nom_alumne");
                    return new Alumne(dni, nomAlumne);
                }
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return null;
    }
}