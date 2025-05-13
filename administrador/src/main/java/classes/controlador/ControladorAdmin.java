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
        
    }
    public void llistarNomFitxers(){
            File[] fitxers=model.getFitxers();
            for(int i=0;i<fitxers.length;i++){
                String nomFitxer=fitxers[i].getName();
                vista.comboFitxer.addItem(nomFitxer);
                System.out.println(nomFitxer);
            }
            
        }
            
}

