package classes.controlador;

import java.io.File;

import classes.model.ModelAdmin;
import classes.vista.VistaAdmin;

public class ControladorAdmin {
    private ModelAdmin model;
    private VistaAdmin vista;

    public ControladorAdmin(ModelAdmin model, VistaAdmin vista) {
        this.model = model;
        this.vista = vista;
        llistarNomFitxers();
        afegirListeners();
    }

    private void afegirListeners() {
        vista.btnCarregar.addActionListener(e -> carregarFitxer());
        vista.btnSortir.addActionListener(e -> System.exit(0));
    }
    
    public void llistarNomFitxers() {
        vista.comboFitxer.removeAllItems();
        
        File[] fitxers = model.getFitxers();
        if (fitxers != null && fitxers.length > 0) {
            for (File fitxer : fitxers) {
                String nomFitxer = fitxer.getName();
                vista.comboFitxer.addItem(nomFitxer);
            }
        } else {
            vista.lblError.setText("No s'han trobat fitxers CSV a la carpeta de recursos");
        }
    }
    

    private void carregarFitxer() {
        String nomFitxerSeleccionat = (String) vista.comboFitxer.getSelectedItem();
        if (nomFitxerSeleccionat == null || nomFitxerSeleccionat.isEmpty()) {
            vista.lblError.setText("Cal seleccionar un fitxer");
            return;
        }
        
        String rutaFitxer = "administrador\\src\\main\\resources\\" + nomFitxerSeleccionat;
        File fitxer = new File(rutaFitxer);
        if (!fitxer.exists()) {
            vista.lblError.setText("El fitxer no existeix: " + rutaFitxer);
            return;
        }
        
        // Segons el nom del fitxer, carreguem les dades
        try {
            if (nomFitxerSeleccionat.toLowerCase().contains("alumnes")) {
                // Carreguem fitxer d'alumnes
                int alumnesInserits = model.carregarAlumnes(rutaFitxer);
                vista.lblError.setText("S'han inserit " + alumnesInserits + " alumnes correctament");
                
            } else if (nomFitxerSeleccionat.toLowerCase().contains("centre") || 
                      nomFitxerSeleccionat.toLowerCase().contains("estudi")) {
                // Carreguem fitxer de centres i estudis
                // El model processa el fitxer i retorna el nombre de centres, estudis i relacions, aquests es reflexen a la vista
                int[] resultats = model.carregarCentresEstudis(rutaFitxer);
                vista.lblError.setText("S'han inserit " + resultats[0] + " centres, " +
                                       resultats[1] + " estudis i " +
                                       resultats[2] + " relacions");
                
            } else {
                vista.lblError.setText("Format de fitxer no reconegut");
            }
            
        } catch (Exception ex) {
            //qualsevol excepció es mostrarà al mateix label de la vista, tot i que no hi hauria d'haver cap excepció en aquest programa
            vista.lblError.setText("Error al carregar el fitxer: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}