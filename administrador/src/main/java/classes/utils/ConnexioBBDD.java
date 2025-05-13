package classes.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnexioBBDD {

    private static String URL;
    private static String USUARI;
    private static String CONTRASENYA;

    static {
        // Carreguem les propietats de la connexió a la base de dades des d'un arxiu de
        // configuració el qual es troba a /resources
        try (FileInputStream fis = new FileInputStream("administrador\\src\\main\\resources\\config.properties")) {
            Properties properties = new Properties();
            properties.load(fis);
            URL = properties.getProperty("db.url");
            USUARI = properties.getProperty("db.user");
            CONTRASENYA = properties.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Error al carregar l'arxiu de configuració " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARI, CONTRASENYA);
    }

    public static boolean executarFitxerSQL(String rutaFitxer) {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                BufferedReader reader = new BufferedReader(new FileReader(rutaFitxer))) {

            StringBuilder script = new StringBuilder();
            String linia;

            // Llegim tot el fitxer
            while ((linia = reader.readLine()) != null) {
                script.append(linia);

                // Si la línia acaba amb punt i coma, executo l'statement
                if (linia.trim().endsWith(";")) {
                    stmt.execute(script.toString());
                    script.setLength(0); // Reinicio el StringBuilder
                }
            }

            System.out.println("Fitxer SQL executat correctament."); // debugging
            return true;

        } catch (IOException | SQLException e) {
            System.err.println("Error al executar el fitxer SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
