package classes.model;

import java.io.File;

public class ModelAdmin {
    public ModelAdmin() {
    }

    File carpeta = new File("administrador\\src\\main\\resources");

    public File[] getFitxers() {
        if (carpeta.exists()) {
            File[] fitxers = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
            if (fitxers != null) {
                for (File fitxer : fitxers) {
                    System.out.println(fitxer.getName());
                }
            }
            return fitxers;
        }
        return null;

    }
}
