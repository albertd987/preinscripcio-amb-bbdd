package classes.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classes.dao.AlumneDAO;
import classes.dao.CentreEstudiDAO;
import classes.utils.LectorCSV;

public class ModelAdmin {
    private AlumneDAO alumneDAO;
    private CentreEstudiDAO centreEstudiDAO;
    
    public ModelAdmin() {
        alumneDAO = new AlumneDAO();
        centreEstudiDAO = new CentreEstudiDAO();
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
    
    /**
     * Carrega i processa el fitxer d'alumnes
     * @param rutaFitxer Ruta al fitxer d'alumnes
     * @return Nombre d'alumnes inserits
     */
    public int carregarAlumnes(String rutaFitxer) {
        try {
            List<String[]> dadesAlumnes = LectorCSV.llegirCSVsenseHeader(rutaFitxer);
            List<Alumne> alumnes = new ArrayList<>();
            
            for (String[] fila : dadesAlumnes) {
                if (fila.length >= 2) {
                    String dni = fila[0];
                    String nomAlumne = fila[1];
                    alumnes.add(new Alumne(dni, nomAlumne));
                }
            }
            
            return alumneDAO.inserirAlumnes(alumnes);
            
        } catch (IOException e) {
            System.err.println("Error al llegir el fitxer d'alumnes: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Carrega i processa el fitxer de centres i estudis
     * @param rutaFitxer Ruta al fitxer de centres i estudis
     * @return Un array amb [nombreCentresInserits, nombreEstudisInserits, nombreRelacionsInserides]
     */
    public int[] carregarCentresEstudis(String rutaFitxer) {
        try {
            List<String[]> dades = LectorCSV.llegirCSVambHeader(rutaFitxer);
            return centreEstudiDAO.processarCentresEstudis(dades);
            
        } catch (IOException e) {
            System.err.println("Error al llegir el fitxer de centres i estudis: " + e.getMessage());
            e.printStackTrace();
            return new int[] {0, 0, 0};
        }
    }
}