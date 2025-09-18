// Archivo: DatabaseInitializerListener.java (Versión Definitiva)
package com.losatuendos.web.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializerListener implements ServletContextListener {

    // URL para conectar al SERVIDOR MySQL, SIN especificar una base de datos.
    private static final String SERVER_URL = "jdbc:mysql://localhost:3308/?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(">>> INICIANDO APLICACIÓN: Ejecutando script de inicialización de BD...");
        
        try {
            // Registramos el driver una sola vez
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("!!! ERROR CRÍTICO: Driver de MySQL no encontrado. Revisa la dependencia en pom.xml y la librería en Tomcat.");
            e.printStackTrace();
            return;
        }

        // Usamos try-with-resources para asegurar que la conexión y el statement se cierren
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USUARIO, CONTRASENA);
             Statement stmt = conn.createStatement()) {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db/init.sql");
            if (inputStream == null) {
                System.err.println("!!! ERROR: No se pudo encontrar el archivo db/init.sql en src/main/resources/db");
                return;
            }
            
            // Leemos el script completo y lo ejecutamos.
            // El propio script contiene "CREATE DATABASE..." y "USE..."
            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter(";\\s*");

            while(scanner.hasNext()){
                String command = scanner.next().trim();
                 if (!command.isEmpty()) {
                    System.out.println("   Ejecutando SQL: " + command.substring(0, Math.min(command.length(), 70)) + "...");
                    stmt.execute(command);
                }
            }
            
            System.out.println(">>> ÉXITO: Script de base de datos ejecutado correctamente.");
            
        } catch (Exception e) {
            System.err.println("!!! ERROR al ejecutar el script de inicialización.");
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}