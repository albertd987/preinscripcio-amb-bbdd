package classes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import classes.model.Centre;
import classes.utils.ConnexioBBDD;

public class CentreDAO {
    
    public List<Centre> obtenirTots() {
        List<Centre> centres = new ArrayList<>();
        String sql = "SELECT codi_centre, nom_centre FROM Centres ORDER BY nom_centre";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String codiCentre = rs.getString("codi_centre");
                String nomCentre = rs.getString("nom_centre");
                centres.add(new Centre(codiCentre, nomCentre));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtenir els centres: " + e.getMessage());
            e.printStackTrace();
        }
        
        return centres;
    }
    
    public Centre obtenirPerCodi(String codiCentre) {
        String sql = "SELECT codi_centre, nom_centre FROM Centres WHERE codi_centre = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiCentre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nomCentre = rs.getString("nom_centre");
                    return new Centre(codiCentre, nomCentre);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtenir el centre: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}