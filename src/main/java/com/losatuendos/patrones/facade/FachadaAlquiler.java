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
import java.sql.Statement;
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
        // El constructor está vacío. La inicialización de datos se hace desde init.sql
    }

    // --- Métodos para la aplicación web ---
    public List<Prenda> getInventario() {
        List<Prenda> inventario = new ArrayList<>();
        String sql = "SELECT * FROM prendas";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Sacamos todos los datos de la fila actual de la base de datos
                String tipo = rs.getString("tipo");
                String ref = rs.getString("ref");
                String color = rs.getString("color");
                String marca = rs.getString("marca");
                String talla = rs.getString("talla");
                double valor = rs.getDouble("valor_alquiler");
                
                Prenda p = null; // Creamos una variable para la nueva prenda
                
                // Usamos la fábrica para crear el objeto correcto
                if ("vestido".equalsIgnoreCase(tipo)) {
                    p = new VestidoDama(color, marca, talla, valor, rs.getBoolean("pedreria"), rs.getString("altura"), rs.getInt("cant_piezas"));
                } else if ("traje".equalsIgnoreCase(tipo)) {
                    p = new TrajeCaballero(color, marca, talla, valor, rs.getString("tipo"), rs.getString("aderezo"));
                } else if ("disfraz".equalsIgnoreCase(tipo)) {
                    p = new Disfraz(color, marca, talla, valor, rs.getString("nombre_disfraz"));
                }
                
                // Si se creó la prenda, le asignamos su ref y la añadimos a la lista
                if (p != null) {
                    p.setRef(ref);
                    inventario.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventario;
    }
    
    public List<Prenda> getListaParaLavado() {
        List<Prenda> listaLavado = new ArrayList<>();
        // Consulta SQL que une las tablas 'lavanderia' y 'prendas'
        String sql = "SELECT p.*, l.es_prioritario FROM prendas p JOIN lavanderia l ON p.ref = l.prenda_ref";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                // Creamos la prenda base desde la base de datos
                Prenda prenda = buscarPrenda(rs.getString("ref"));
                boolean esPrioritario = rs.getBoolean("es_prioritario");

                if (prenda != null) {
                    // Si es prioritario, aplicamos el Decorator dinámicamente
                    if (esPrioritario) {
                        prenda = new PrioridadLavadoDecorator(prenda);
                    }
                    listaLavado.add(prenda);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLavado;
    }

    public List<Cliente> getClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(rs.getString("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("mail")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return clientes;
    }
    
    public List<Empleado> getEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados";
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql); 
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                empleados.add(new Empleado(
                    rs.getString("id"), 
                    rs.getString("nombre"), 
                    rs.getString("direccion"), 
                    rs.getString("telefono"), 
                    rs.getString("cargo"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }
    
    public List<Prenda> consultarPrendasPorTalla(String talla) {
        // Usamos la API de Streams de Java
        return getInventario().stream()
                         // Comparamos ignorando mayúsculas/minúsculas y espacios.
                         .filter(prenda -> prenda.getTalla() != null && prenda.getTalla().trim().equalsIgnoreCase(talla.trim()))
                         .collect(Collectors.toList());
    }
    
    public List<ServicioAlquiler> consultarServiciosPorCliente(String clienteId) {
        List<ServicioAlquiler> historial = new ArrayList<>();
        String sql = "SELECT * FROM alquileres WHERE cliente_id = ? AND fecha_alquiler >= CURDATE() ORDER BY fecha_alquiler ASC";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                // Para cada alquiler, buscamos los objetos completos asociados
                Cliente cliente = buscarCliente(rs.getString("cliente_id"));
                Empleado empleado = buscarEmpleado(rs.getString("empleado_id"));
                Prenda prenda = buscarPrenda(rs.getString("prenda_ref"));
                
                if (cliente != null && empleado != null && prenda != null) {
                    ServicioAlquiler sa = new ServicioAlquiler(
                        rs.getInt("numero"),
                        cliente,
                        empleado,
                        prenda,
                        rs.getDate("fecha_alquiler")
                    );
                    historial.add(sa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historial;
    }
    
    public List<ServicioAlquiler> consultarHistorialCompletoPorCliente(String clienteId) {
        List<ServicioAlquiler> historial = new ArrayList<>();
        // Ordenamos por fecha descendente para mostrar lo último primero
        String sql = "SELECT * FROM alquileres WHERE cliente_id = ? ORDER BY fecha_alquiler DESC";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                // La lógica interna para construir los objetos se mantiene igual
                Cliente cliente = buscarCliente(rs.getString("cliente_id"));
                Empleado empleado = buscarEmpleado(rs.getString("empleado_id"));
                Prenda prenda = buscarPrenda(rs.getString("prenda_ref"));
                
                if (cliente != null && empleado != null && prenda != null) {
                    ServicioAlquiler sa = new ServicioAlquiler(
                        rs.getInt("numero"), cliente, empleado, prenda, rs.getDate("fecha_alquiler")
                    );
                    sa.setFechaSolicitud(rs.getTimestamp("fecha_solicitud"));
                    historial.add(sa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historial;
    }
    
    public List<ServicioAlquiler> consultarServiciosPorFecha(Date fecha) {
        // Formateador para comparar solo el día, mes y año, ignorando la hora.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaBuscada = sdf.format(fecha);
        
        return alquileres.stream()
                         .filter(a -> sdf.format(a.getFechaSolicitud()).equals(fechaBuscada))
                         .collect(Collectors.toList());
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
    
    public boolean actualizarCliente(Cliente cliente) {
        // El email (username) y el ID no se deben cambiar, solo los otros datos.
        String sql = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ? WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getDireccion());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getId()); // Usamos el ID en la cláusula WHERE
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
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
    
    public void registrarEmpleado(String id, String nombre, String direccion, String telefono, String cargo) {
        String sql = "INSERT INTO empleados (id, nombre, direccion, telefono, cargo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, nombre);
            pstmt.setString(3, direccion);
            pstmt.setString(4, telefono);
            pstmt.setString(5, cargo);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean eliminarEmpleado(String empleadoId) {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, empleadoId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }
    
    public boolean agregarPrenda(Prenda nuevaPrenda) {
        String tipo = "";
        String prefijo = "";
        if (nuevaPrenda instanceof VestidoDama) { tipo = "vestido"; prefijo = "V"; } 
        else if (nuevaPrenda instanceof TrajeCaballero) { tipo = "traje"; prefijo = "T"; } 
        else if (nuevaPrenda instanceof Disfraz) { tipo = "disfraz"; prefijo = "D"; }

        String sqlSelectCount = "SELECT COUNT(*) FROM prendas WHERE tipo = ?";
        String sqlInsert = "INSERT INTO prendas (ref, tipo, descripcion, color, marca, talla, valor_alquiler, pedreria, altura, cant_piezas, aderezo, nombre_disfraz) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection()) {
            long nuevoNumero;
            try (PreparedStatement pstmtCount = conn.prepareStatement(sqlSelectCount)) {
                pstmtCount.setString(1, tipo);
                ResultSet rs = pstmtCount.executeQuery();
                rs.next();
                nuevoNumero = rs.getLong(1) + 1;
            }
            String nuevoRef = String.format("%s%03d", prefijo, nuevoNumero);
            nuevaPrenda.setRef(nuevoRef);

            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setString(1, nuevaPrenda.getRef());
                pstmtInsert.setString(2, tipo);
                pstmtInsert.setString(3, nuevaPrenda.getDescripcion());
                pstmtInsert.setString(4, nuevaPrenda.getColor());
                pstmtInsert.setString(5, nuevaPrenda.getMarca());
                pstmtInsert.setString(6, nuevaPrenda.getTalla());
                pstmtInsert.setDouble(7, nuevaPrenda.getValorAlquiler());
                
                if (nuevaPrenda instanceof VestidoDama vd) {
                    pstmtInsert.setBoolean(8, vd.tienePedreria());
                    pstmtInsert.setString(9, vd.getAltura());
                    pstmtInsert.setInt(10, vd.getCantPiezas());
                    pstmtInsert.setNull(11, java.sql.Types.VARCHAR);
                    pstmtInsert.setNull(12, java.sql.Types.VARCHAR);
                } else if (nuevaPrenda instanceof TrajeCaballero tc) {
                    pstmtInsert.setNull(8, java.sql.Types.BOOLEAN);
                    pstmtInsert.setNull(9, java.sql.Types.VARCHAR);
                    pstmtInsert.setNull(10, java.sql.Types.INTEGER);
                    pstmtInsert.setString(11, tc.getAderezo());
                    pstmtInsert.setNull(12, java.sql.Types.VARCHAR);
                } else if (nuevaPrenda instanceof Disfraz d) {
                    pstmtInsert.setNull(8, java.sql.Types.BOOLEAN);
                    pstmtInsert.setNull(9, java.sql.Types.VARCHAR);
                    pstmtInsert.setNull(10, java.sql.Types.INTEGER);
                    pstmtInsert.setNull(11, java.sql.Types.VARCHAR);
                    pstmtInsert.setString(12, d.getNombreDisfraz());
                }
                return pstmtInsert.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        }
    
    public boolean eliminarPrenda(String prendaRef) {
        String sql = "DELETE FROM prendas WHERE ref = ?";
        try (Connection conn = ConexionDB.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, prendaRef);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            // Este error suele ocurrir si la prenda tiene alquileres asociados (violación de clave foránea).
            System.err.println("Error al eliminar prenda: " + e.getMessage());
            return false;
        }
    }
    
    public void enviarALavado(String refPrenda, boolean conPrioridad) {
        String sql = "INSERT INTO lavanderia (prenda_ref, es_prioritario) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, refPrenda);
            pstmt.setBoolean(2, conPrioridad);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Prenda> confirmarEnvioLavanderia() {
        // 1. Primero obtenemos la lista de prendas que vamos a enviar para el mensaje
        List<Prenda> prendasEnviadas = getListaParaLavado();
        
        // 2. Luego, vaciamos la tabla
        String sql = "DELETE FROM lavanderia";
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 3. Devolvemos la lista de prendas que se enviaron
        return prendasEnviadas;
    }

    public String realizarAlquiler(String idCliente, String idEmpleado, String refPrenda, Date fechaAlquiler) {
        // La lógica de verificación de disponibilidad de la prenda se mantiene igual...
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaBuscada = sdf.format(fechaAlquiler);
        String sqlCheck = "SELECT COUNT(*) FROM alquileres WHERE prenda_ref = ? AND fecha_alquiler = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setString(1, refPrenda);
            pstmtCheck.setString(2, fechaBuscada);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return "Error: La prenda " + refPrenda + " ya se encuentra alquilada para la fecha seleccionada.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al verificar disponibilidad.";
        }

        // Buscamos todas las entidades necesarias
        Cliente cliente = buscarCliente(idCliente);
        Prenda prenda = buscarPrenda(refPrenda);
        // Buscamos al empleado específico que viene como parámetro
        Empleado empleadoAsignado = buscarEmpleado(idEmpleado);
        
        // Verificamos que todas las entidades existan
        if (cliente != null && prenda != null && empleadoAsignado != null) {
            String sqlInsert = "INSERT INTO alquileres (fecha_solicitud, fecha_alquiler, cliente_id, empleado_id, prenda_ref) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setTimestamp(1, new Timestamp(new Date().getTime()));
                pstmtInsert.setDate(2, new java.sql.Date(fechaAlquiler.getTime()));
                pstmtInsert.setString(3, idCliente);
                pstmtInsert.setString(4, empleadoAsignado.getId()); // Guardamos el ID del empleado seleccionado
                pstmtInsert.setString(5, refPrenda);
                pstmtInsert.executeUpdate();
                return "¡Alquiler registrado exitosamente para el día " + fechaBuscada + "!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error al registrar el alquiler.";
            }
        } else {
            // Damos un error más específico si algo no se encontró
            return "Error: No se pudo encontrar el cliente, la prenda o el empleado especificado.";
        }
    }
    
    public Cliente buscarCliente(String clienteId) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, clienteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(rs.getString("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("mail"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public Prenda buscarPrenda(String ref) {
        String sql = "SELECT * FROM prendas WHERE ref = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ref);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si encontramos una prenda, leemos todos sus datos de la fila
                    String tipo = rs.getString("tipo");
                    String color = rs.getString("color");
                    String marca = rs.getString("marca");
                    String talla = rs.getString("talla");
                    double valor = rs.getDouble("valor_alquiler");

                    Prenda p = null; // Creamos una variable para la prenda

                    // Usamos la misma lógica que en getInventario para crear el objeto correcto
                    if ("vestido".equalsIgnoreCase(tipo)) {
                        p = new VestidoDama(color, marca, talla, valor, rs.getBoolean("pedreria"), rs.getString("altura"), rs.getInt("cant_piezas"));
                    } else if ("traje".equalsIgnoreCase(tipo)) {
                        // Asumimos un tipo por defecto ya que no lo guardamos por separado
                        p = new TrajeCaballero(color, marca, talla, valor, "Convencional", rs.getString("aderezo"));
                    } else if ("disfraz".equalsIgnoreCase(tipo)) {
                        p = new Disfraz(color, marca, talla, valor, rs.getString("nombre_disfraz"));
                    }

                    if (p != null) {
                        p.setRef(ref); // Asignamos la referencia
                        return p; // Devolvemos la prenda encontrada
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra nada o hay un error, devuelve null
    }
    
    public Empleado buscarEmpleado(String empleadoId) {
        String sql = "SELECT * FROM empleados WHERE id = ?";
        try (Connection conn = ConexionDB.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empleadoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(rs.getString("id"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono"), rs.getString("cargo"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public ServicioAlquiler buscarServicioPorNumero(int numero) {
        return alquileres.stream()
                         .filter(a -> a.getNumero() == numero)
                         .findFirst()
                         .orElse(null);
    }
    
    public List<ServicioAlquiler> buscarAlquileres(String clienteId, String empleadoId, Date fechaInicio, Date fechaFin) {
        List<ServicioAlquiler> resultados = new ArrayList<>();
        // Construimos una consulta SQL dinámica
        StringBuilder sql = new StringBuilder("SELECT * FROM alquileres WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (clienteId != null && !clienteId.isEmpty()) {
            sql.append(" AND cliente_id = ?");
            params.add(clienteId);
        }
        if (empleadoId != null && !empleadoId.isEmpty()) {
            sql.append(" AND empleado_id = ?");
            params.add(empleadoId);
        }
        if (fechaInicio != null) {
            sql.append(" AND fecha_alquiler >= ?");
            params.add(new java.sql.Date(fechaInicio.getTime()));
        }
        if (fechaFin != null) {
            sql.append(" AND fecha_alquiler <= ?");
            params.add(new java.sql.Date(fechaFin.getTime()));
        }
        
        sql.append(" ORDER BY fecha_alquiler DESC");

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            // Asignamos los parámetros a la consulta
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Reutilizamos la lógica para construir el objeto ServicioAlquiler
                Cliente c = buscarCliente(rs.getString("cliente_id"));
                Empleado e = buscarEmpleado(rs.getString("empleado_id"));
                Prenda p = buscarPrenda(rs.getString("prenda_ref"));
                if (c != null && e != null && p != null) {
                    ServicioAlquiler sa = new ServicioAlquiler(rs.getInt("numero"), c, e, p, rs.getDate("fecha_alquiler"));
                    sa.setFechaSolicitud(rs.getTimestamp("fecha_solicitud"));
                    resultados.add(sa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }
}