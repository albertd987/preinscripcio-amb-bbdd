package classes;

import classes.controlador.ControladorAdmin;
import classes.model.ModelAdmin;
import classes.utils.ConnexioBBDD;
import classes.vista.VistaAdmin;

public class Main {
    public static void main(String[] args) {
        try {
            // Executar el fitxer SQL per crear les taules
            boolean taulesCreades = ConnexioBBDD.executarFitxerSQL("administrador\\src\\main\\resources\\scriptaules.sql");
            if (taulesCreades) {
                System.out.println("Estructura de la base de dades creada correctament.");
            } else {
                System.err.println("Hi ha hagut un error al crear l'estructura de la base de dades.");
                return; // Sortir si hi ha hagut un error
            }
            
            // Inicialitzar el model, la vista i el controlador
            ModelAdmin model = new ModelAdmin();
            VistaAdmin vista = new VistaAdmin();
            ControladorAdmin controlador = new ControladorAdmin(model, vista);
            
        } catch (Exception e) {
            System.err.println("Error al iniciar l'aplicació: " + e.getMessage());
            e.printStackTrace();
        }
    }
}