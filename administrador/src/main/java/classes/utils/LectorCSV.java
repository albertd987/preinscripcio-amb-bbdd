package classes.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    // el boolea saltarHeader es per poder reciclar el mètode als dos fitxers, ja
    // que alumnes no te header, pero Centres_estudis si, aquest és el mètode base que es privat, i a sota tinc
    // dos mètodes que el criden, un per llegir fitxers amb header i l'altre sense
    private static List<String[]> llegirCSVBase(String ruta, String separador, boolean saltarheader)
            throws IOException {
        List<String[]> dades = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linia;
            boolean primeralinia = true;        

            while ((linia = br.readLine()) != null) {
                // Per saltarme la primera línia en cas de que sigui un header
                if (primeralinia && saltarheader) {
                    primeralinia = false;
                    continue;
                }

                // Separem la línia en funció del separador
                String[] camps = linia.split(separador);

                // Eliminem espais en blanc al principi i al final de cada camp
                for (int i = 0; i < camps.length; i++) {
                    camps[i] = camps[i].trim();
                }

                dades.add(camps);
                primeralinia = false;
            }
        }

        return dades;
    }

    public static List<String[]> llegirCSVambHeader(String ruta) throws IOException {
        return llegirCSVBase(ruta, ";", true);
    }

    public static List<String[]> llegirCSVsenseHeader(String ruta) throws IOException {
        return llegirCSVBase(ruta, ";", false);
    }
}
