// Archivo: DatabaseInitializerListener.java
package com.losatuendos.web.util;

import com.losatuendos.datos.ConexionDB;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(">>> INICIANDO APLICACIÓN: Verificando base de datos...");
        try {
            // Leemos nuestro archivo .sql desde los recursos del proyecto
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/init.sql");
            if (inputStream == null) {
                System.err.println("!!! ERROR: No se pudo encontrar el archivo db/init.sql");
                return;
            }
            
            // Usamos un Scanner para leer el contenido del archivo
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String sqlScript = scanner.hasNext() ? scanner.next() : "";
            
            // Dividimos el script en comandos individuales usando el punto y coma como separador
            String[] commands = sqlScript.split(";\\s*");

            // Obtenemos una conexión y ejecutamos cada comando
            try (Connection conn = ConexionDB.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                for (String command : commands) {
                    if (command.trim().length() > 0) {
                        System.out.println("   Ejecutando: " + command.substring(0, Math.min(command.length(), 50)) + "...");
                        stmt.execute(command);
                    }
                }
                System.out.println(">>> Base de datos verificada e inicializada correctamente.");
            }
            
        } catch (SQLException e) {
            System.err.println("!!! ERROR al ejecutar el script de inicialización de la base de datos.");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Este método se ejecuta cuando la aplicación se detiene. No necesitamos hacer nada aquí.
    }
}