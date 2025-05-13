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
        
        // Afegim els action listeners als botons
        afegirListeners();
    }
    
    /**
     * Afegeix els listeners als components de la vista
     */
    private void afegirListeners() {
        // Botó carregar amb lambda
        vista.btnCarregar.addActionListener(e -> carregarFitxer());
        
        // Botó sortir amb lambda
        vista.btnSortir.addActionListener(e -> System.exit(0));
    }
    
    /**
     * Llista els fitxers CSV al combobox
     */
    public void llistarNomFitxers() {
        vista.comboFitxer.removeAllItems(); // Netegem el combobox
        
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
    
    /**
     * Carrega el fitxer seleccionat a la base de dades
     */
    private void carregarFitxer() {
        // Comprovem si hi ha algun fitxer seleccionat
        String nomFitxerSeleccionat = (String) vista.comboFitxer.getSelectedItem();
        if (nomFitxerSeleccionat == null || nomFitxerSeleccionat.isEmpty()) {
            vista.lblError.setText("Cal seleccionar un fitxer");
            return;
        }
        
        // Construïm la ruta completa del fitxer
        String rutaFitxer = "administrador\\src\\main\\resources\\" + nomFitxerSeleccionat;
        File fitxer = new File(rutaFitxer);
        
        // Comprovem que el fitxer existeix
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
                int[] resultats = model.carregarCentresEstudis(rutaFitxer);
                vista.lblError.setText("S'han inserit " + resultats[0] + " centres, " +
                                       resultats[1] + " estudis i " +
                                       resultats[2] + " relacions");
                
            } else {
                vista.lblError.setText("Format de fitxer no reconegut");
            }
            
        } catch (Exception ex) {
            vista.lblError.setText("Error al carregar el fitxer: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}