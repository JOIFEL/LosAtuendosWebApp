// Archivo: FachadaAlquiler.java
package com.losatuendos.patrones.facade;

import com.losatuendos.datos.ConexionDB;
import com.losatuendos.model.*;
import com.losatuendos.patrones.decorator.PrioridadLavadoDecorator;
import com.losatuendos.patrones.factory.PrendaFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors; 

public class FachadaAlquiler {

    private List<Prenda> inventario = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private List<Empleado> empleados = new ArrayList<>();
    private List<ServicioAlquiler> alquileres = new ArrayList<>();
    private List<Prenda> listaParaLavado = new ArrayList<>();

    public FachadaAlquiler() {
        // Datos de prueba para iniciar la aplicación
        inventario.add(PrendaFactory.crearPrenda("vestido", "V01", "M", 150000));
        inventario.add(PrendaFactory.crearPrenda("traje", "T01", "L", 200000));
        inventario.add(PrendaFactory.crearPrenda("disfraz", "D01", "S", 100000));
        clientes.add(new Cliente("123", "Carlos Perez", "Calle Falsa 123", "555-1234", "carlos@mail.com"));
        empleados.add(new Empleado("E01", "Ana García", "Av. Siempre Viva", "555-5678", "Vendedora"));
    }

    // --- Métodos para la aplicación web ---
    public List<Prenda> getInventario() {
        List<Prenda> inventario = new ArrayList<>();
        String sql = "SELECT * FROM prendas";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Prenda p = PrendaFactory.crearPrenda(rs.getString("tipo"), rs.getString("ref"), rs.getString("talla"), rs.getDouble("valor_alquiler"));
                inventario.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventario;
    }
    
    public List<Prenda> getListaParaLavado() {
        return this.listaParaLavado;
    }

    public List<Cliente> getClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(rs.getString("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("mail")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
    
    public List<Prenda> consultarPrendasPorTalla(String talla) {
        // Usamos la API de Streams de Java para una búsqueda limpia y moderna.
        return inventario.stream()
                         .filter(prenda -> prenda.getTalla().equalsIgnoreCase(talla))
                         .collect(Collectors.toList());
    }
    
    public List<ServicioAlquiler> consultarServiciosPorCliente(String clienteId) {
        return alquileres.stream()
                         .filter(alquiler -> alquiler.getCliente().getId().equals(clienteId))
                         .collect(Collectors.toList());
    }
    
    public List<ServicioAlquiler> consultarServiciosPorFecha(Date fecha) {
        // Formateador para comparar solo el día, mes y año, ignorando la hora.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaBuscada = sdf.format(fecha);
        
        return alquileres.stream()
                         .filter(a -> sdf.format(a.getFechaSolicitud()).equals(fechaBuscada))
                         .collect(Collectors.toList());
    }
    
    public List<Empleado> getEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                empleados.add(new Empleado(rs.getString("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("cargo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

    public boolean registrarCliente(String id, String nombre, String direccion, String telefono, String mail) {
    // Primero, intentamos guardar el cliente
    String sqlCliente = "INSERT INTO clientes (id, nombre, direccion, telefono, mail) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = ConexionDB.getConnection();
         PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente)) {
        
        pstmtCliente.setString(1, id);
        pstmtCliente.setString(2, nombre);
        pstmtCliente.setString(3, direccion);
        pstmtCliente.setString(4, telefono);
        pstmtCliente.setString(5, mail);
        
        int filasAfectadas = pstmtCliente.executeUpdate();
        if (filasAfectadas > 0) {
            // SI EL CLIENTE SE GUARDÓ BIEN, AHORA CREAMOS SU USUARIO
            String sqlUsuario = "INSERT INTO usuarios (username, password, rol, cliente_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmtUsuario = conn.prepareStatement(sqlUsuario)) {
                // Por simplicidad, el username será el email y la contraseña será el número de ID.
                // En una app real, se le pediría al usuario crear su propia contraseña.
                pstmtUsuario.setString(1, mail); // Usamos el email como nombre de usuario
                pstmtUsuario.setString(2, id);   // Usamos el ID como contraseña inicial
                pstmtUsuario.setString(3, "CLIENTE");
                pstmtUsuario.setString(4, id);
                pstmtUsuario.executeUpdate();
            }
            return true; // Éxito en ambos pasos
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Si algo falla, devuelve false
    }
    
    public boolean eliminarCliente(String clienteId) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, clienteId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            // Este error suele ocurrir si el cliente tiene alquileres asociados (violación de clave foránea)
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
    
     public Usuario validarLogin(String username, String password) {
        // --- PISTAS DE DEPURACIÓN ---
        System.out.println("--- INTENTO DE LOGIN ---");
        System.out.println("Usuario recibido por la fachada: [" + username + "]");
        System.out.println("Contraseña recibida por la fachada: [" + password + "]");
        // -------------------------

        Usuario usuarioEncontrado = null;
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username.trim()); // Añadimos .trim() por si hay espacios extra
            pstmt.setString(2, password.trim());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si encontramos un usuario, creamos el objeto
                    System.out.println(">>> ÉXITO: Se encontró un usuario en la BD.");
                    usuarioEncontrado = new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("rol"),
                        rs.getString("cliente_id")
                    );
                } else {
                    System.out.println("!!! FALLO: La consulta SQL no devolvió ningún resultado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("!!! ERROR SQL al intentar validar el login: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("--- FIN DEL INTENTO DE LOGIN ---");
        return usuarioEncontrado;
    }
    
    public void registrarEmpleado(String id, String nombre, String dir, String tel, String cargo) {
        empleados.add(new Empleado(id, nombre, dir, tel, cargo));
    }    
    
    public void agregarPrenda(Prenda nuevaPrenda) {
        if (nuevaPrenda != null) {
            this.inventario.add(nuevaPrenda);
        }
    }
    
    public void enviarALavado(String refPrenda, boolean conPrioridad) {
        // Buscamos la prenda original en el inventario.
        Prenda prendaAEnviar = inventario.stream()
                                         .filter(p -> p.getRef().equals(refPrenda))
                                         .findFirst()
                                         .orElse(null);

        if (prendaAEnviar != null) {
            
            if (conPrioridad) {
                // Si se necesita prioridad, envolvemos el objeto original con el decorador.
                prendaAEnviar = new com.losatuendos.patrones.decorator.PrioridadLavadoDecorator(prendaAEnviar);
            }
            listaParaLavado.add(prendaAEnviar);
        }
    }
    
    public int confirmarEnvioLavanderia() {
        int cantidadEnviada = listaParaLavado.size();
        listaParaLavado.clear(); // Vaciamos la lista
        return cantidadEnviada;
    }

    public String realizarAlquiler(String idCliente, String refPrenda, Date fechaAlquiler) {
    // Formateador para comparar fechas sin tener en cuenta la hora.
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String fechaBuscada = sdf.format(fechaAlquiler);

    // --- VERIFICACIÓN DE DISPONIBILIDAD ---
    for (ServicioAlquiler alquilerExistente : alquileres) {
        String fechaExistente = sdf.format(alquilerExistente.getFechaAlquiler());
        if (alquilerExistente.getPrenda().getRef().equals(refPrenda) && fechaExistente.equals(fechaBuscada)) {
            // Si encontramos la misma prenda para el mismo día, no está disponible.
            return "Error: La prenda " + refPrenda + " ya se encuentra alquilada para la fecha seleccionada.";
        }
    }

    // Si pasa la verificación, procedemos a registrar el alquiler.
    Cliente cliente = buscarCliente(idCliente);
    Prenda prenda = inventario.stream().filter(p -> p.getRef().equals(refPrenda)).findFirst().orElse(null);
    Empleado empleado = empleados.get(0); // Tomamos el primer empleado por defecto

    if (cliente != null && prenda != null) {
        int nuevoNumero = alquileres.size() + 1;
        ServicioAlquiler nuevoAlquiler = new ServicioAlquiler(nuevoNumero, cliente, empleado, prenda, fechaAlquiler);
        alquileres.add(nuevoAlquiler);
        return "¡Alquiler #" + nuevoNumero + " registrado exitosamente para el día " + fechaBuscada + "!";
    } else {
        return "Error: No se pudo encontrar el cliente o la prenda especificada.";
    }
    }
    
    public Cliente buscarCliente(String clienteId) {
        return clientes.stream()
                       .filter(c -> c.getId().equals(clienteId))
                       .findFirst()
                       .orElse(null);
    }
    
     public ServicioAlquiler buscarServicioPorNumero(int numero) {
        return alquileres.stream()
                         .filter(a -> a.getNumero() == numero)
                         .findFirst()
                         .orElse(null);
    }
}