package classes;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import classes.controlador.ControladorUsuari;
import classes.model.ModelUsuari;
import classes.utils.ConnexioBBDD;
import classes.vista.VistaUsuari;

public class Main {
    public static void main(String[] args) {
        try {
            try (Connection conn = ConnexioBBDD.getConnection()) {
                System.out.println("Connexió a la base de dades establerta correctament.");
            } catch (SQLException e) {
                String message = "Error de connexió a la base de dades: " + e.getMessage() + 
                        "\nAssegureu-vos que el servidor MySQL està en marxa i que les dades de connexió són correctes.";
                JOptionPane.showMessageDialog(null, message, "Error de connexió", JOptionPane.ERROR_MESSAGE);
                System.err.println(message);
                return;
            }

            SwingUtilities.invokeLater(() -> {
                try {

                    ModelUsuari model = new ModelUsuari();
                    VistaUsuari vista = new VistaUsuari();
                    ControladorUsuari controlador = new ControladorUsuari(model, vista);
                    

                    vista.setVisible(true);
                } catch (Exception e) {
                    String message = "Error al iniciar l'aplicació: " + e.getMessage();
                    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                    System.err.println(message);
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            String message = "Error general de l'aplicació: " + e.getMessage();
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(message);
            e.printStackTrace();
        }
    }
}