package classes.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import classes.model.Estudi;
import classes.utils.ConnexioBBDD;

public class EstudiDAO {
    
    public List<Estudi> obtenirTots() {
        List<Estudi> estudis = new ArrayList<>();
        String sql = "SELECT codi_estudi, nom_estudi FROM Estudis ORDER BY nom_estudi";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String codiEstudi = rs.getString("codi_estudi");
                String nomEstudi = rs.getString("nom_estudi");
                estudis.add(new Estudi(codiEstudi, nomEstudi));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtenir els estudis: " + e.getMessage());
            e.printStackTrace();
        }
        
        return estudis;
    }
    
    public Estudi obtenirPerCodi(String codiEstudi) {
        String sql = "SELECT codi_estudi, nom_estudi FROM Estudis WHERE codi_estudi = ?";
        
        try (Connection conn = ConnexioBBDD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codiEstudi);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nomEstudi = rs.getString("nom_estudi");
                    return new Estudi(codiEstudi, nomEstudi);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtenir l'estudi: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}