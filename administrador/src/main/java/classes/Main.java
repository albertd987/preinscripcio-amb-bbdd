package classes;

import classes.controlador.ControladorAdmin;
import classes.model.ModelAdmin;
import classes.vista.VistaAdmin;

public class Main {
    public static void main(String[] args) {
        ModelAdmin model = new ModelAdmin();
        VistaAdmin vista = new VistaAdmin();
        ControladorAdmin controlador = new ControladorAdmin(model, vista);
        
    }
}