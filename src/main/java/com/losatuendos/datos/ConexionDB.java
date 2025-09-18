// Archivo: ConexionDB.java (Versión Final Simplificada)
package com.losatuendos.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3308/los_atuendos_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    public static Connection getConnection() throws SQLException {
        // Con los conectores modernos (JDBC 4.0+), la línea Class.forName() ya no es necesaria.
        // El DriverManager encuentra el driver automáticamente gracias a la dependencia de Maven.
        // Esto hace el código más limpio y menos propenso a errores.
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}