package classes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import classes.model.Alumne;
import classes.utils.ConnexioBBDD;

public class AlumneDAO {
    
    /**
     * Insereix múltiples alumnes a la base de dades
     * @param alumnes Llista d'alumnes a inserir
     * @return nombre d'alumnes inserits correctament
     */
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
            
            // Fem commit dels canvis
            conn.commit();
            
            // Restaurem l'autocommit
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
            System.err.println("Error general al inserir alumnes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return comptador;
    }
}