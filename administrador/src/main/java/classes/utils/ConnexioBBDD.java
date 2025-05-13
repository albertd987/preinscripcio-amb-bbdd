package classes.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class ConnexioBBDD {

private static String URL;
    private static String USUARI;
    private static String CONTRASENYA;

   static {
        //Carreguem les propietats de la connexió a la base de dades des d'un arxiu de configuració el qual es troba a /resources
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
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USUARI,CONTRASENYA);
    }    

}

