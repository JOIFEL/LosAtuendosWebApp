// Archivo: ControladorPrincipal.java (Versión Final Verificada)
package com.losatuendos.web.controlador;

import com.losatuendos.model.*;
import com.losatuendos.patrones.facade.FachadaAlquiler;
import com.losatuendos.patrones.factory.PrendaFactory;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ControladorPrincipal", urlPatterns = {"/controlador"})
public class ControladorPrincipal extends HttpServlet {

    private FachadaAlquiler fachada = new FachadaAlquiler();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "inicio";
        }
        
        Usuario usuarioLogueado = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        switch (accion) {
            
            case "login":
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                Usuario usuario = fachada.validarLogin(username, password);
                if (usuario != null) {
                    request.getSession().setAttribute("usuarioLogueado", usuario);
                    response.sendRedirect("index.jsp");
                } else {
                    request.setAttribute("error", "Usuario o contraseña incorrectos.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
                break;
            
            case "logout":
                request.getSession().invalidate();
                response.sendRedirect("login.jsp");
                break;

            case "verInventario":
                request.setAttribute("listaPrendas", fachada.getInventario());
                request.getRequestDispatcher("inventario.jsp").forward(request, response);
                break;
                
            case "mostrarFormularioPrenda":
                request.getRequestDispatcher("registrarPrenda.jsp").forward(request, response);
                break;

            case "registrarPrenda":
                // 1. Obtenemos todos los datos del formulario.
                String tipo = request.getParameter("tipo");
                String color = request.getParameter("color");
                String marca = request.getParameter("marca");
                String talla = request.getParameter("talla");
                double valor = Double.parseDouble(request.getParameter("valor"));
                
                Prenda nuevaPrenda = null;
                
                // 2. Decidimos qué tipo de prenda crear basado en la selección.
                if ("vestido".equals(tipo)) {
                    boolean pedreria = request.getParameter("pedreria") != null; // Checkbox
                    String altura = request.getParameter("altura");
                    int cantPiezas = Integer.parseInt(request.getParameter("cantPiezas"));
                    nuevaPrenda = new VestidoDama(color, marca, talla, valor, pedreria, altura, cantPiezas);
                } else if ("traje".equals(tipo)) {
                    String tipoTraje = request.getParameter("tipoTraje");
                    String aderezo = request.getParameter("aderezo");
                    nuevaPrenda = new TrajeCaballero(color, marca, talla, valor, tipoTraje, aderezo);
                } else if ("disfraz".equals(tipo)) {
                    String nombreDisfraz = request.getParameter("nombreDisfraz");
                    nuevaPrenda = new Disfraz(color, marca, talla, valor, nombreDisfraz);
                }

                // 3. Le pasamos la nueva prenda a la Fachada para que la guarde y genere el ref.
                boolean exitoAgregar = fachada.agregarPrenda(nuevaPrenda);
                
                // 4. Preparamos un mensaje de éxito o error y redirigimos.
                if(exitoAgregar){
                    request.getSession().setAttribute("mensaje", "¡Prenda registrada exitosamente con el Ref: " + nuevaPrenda.getRef() + "!");
                } else {
                    request.getSession().setAttribute("mensaje", "Error: No se pudo registrar la nueva prenda.");
                }
                response.sendRedirect("controlador?accion=verInventario");
                break;
                
            case "eliminarPrenda":
                String prendaRef = request.getParameter("prendaRef");
                boolean exito = fachada.eliminarPrenda(prendaRef);
                
                if(exito) {
                    request.getSession().setAttribute("mensaje", "Prenda con Ref: " + prendaRef + " eliminada exitosamente.");
                } else {
                    request.getSession().setAttribute("mensaje", "Error: No se pudo eliminar la prenda. Es posible que tenga alquileres asociados.");
                }
                
                response.sendRedirect("controlador?accion=verInventario");
                break;

            case "verClientes":
                request.setAttribute("listaClientes", fachada.getClientes());
                request.getRequestDispatcher("clientes.jsp").forward(request, response);
                break;
                
            case "mostrarFormularioCliente":
                request.getRequestDispatcher("registrarCliente.jsp").forward(request, response);
                break;

            case "registrarCliente":
                // 1. Obtenemos los datos del formulario
                String idClienteForm = request.getParameter("id");
                String nombreCliente = request.getParameter("nombre");
                String direccion = request.getParameter("direccion");
                String telefono = request.getParameter("telefono");
                String mail = request.getParameter("mail");
                
                // 2. Intentamos registrar al cliente
                boolean exitoCliente = fachada.registrarCliente(idClienteForm, nombreCliente, direccion, telefono, mail);
                
                // 3. Decidimos a dónde ir basándonos en quién hizo la acción
                if (exitoCliente) {
                    // Verificamos si hay un administrador logueado
                    Usuario usuarioActual = (Usuario) request.getSession().getAttribute("usuarioLogueado");
                    if (usuarioActual != null && usuarioActual.getRol().equals("ADMIN")) {
                        // Si es un admin, lo mantenemos en el panel de gestión
                        request.getSession().setAttribute("mensaje", "¡Cliente '" + nombreCliente + "' registrado exitosamente!");
                        response.sendRedirect("controlador?accion=verClientes");
                    } else {
                        // Si no hay nadie logueado, es un cliente nuevo, lo mandamos al login
                        request.setAttribute("mensajeRegistro", "¡Te has registrado exitosamente! Ahora puedes iniciar sesión.");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                } else {
                    // Si el registro falla, lo devolvemos al formulario con un error
                    request.setAttribute("errorRegistro", "Error: No se pudo registrar. La identificación o el correo ya podrían existir.");
                    request.getRequestDispatcher("registrarCliente.jsp").forward(request, response);
                }
                break;
                
            case "eliminarCliente":
                String clienteIdParaEliminar = request.getParameter("clienteId");
                boolean borradoExitoso = fachada.eliminarCliente(clienteIdParaEliminar);
                if(borradoExitoso) {
                    request.getSession().setAttribute("mensaje", "Cliente eliminado exitosamente.");
                } else {
                    request.getSession().setAttribute("mensaje", "Error: No se pudo eliminar al cliente. Es posible que tenga alquileres asociados.");
                }
                response.sendRedirect("controlador?accion=verClientes");
                break;
            
            case "mostrarMisDatos":
                // Obtenemos el usuario de la sesión para saber qué cliente buscar.
                Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioLogueado");
                if (usuarioSesion != null && usuarioSesion.getRol().equals("CLIENTE")) {
                    // Usamos la fachada para obtener el objeto Cliente completo.
                    Cliente cliente = fachada.buscarCliente(usuarioSesion.getClienteId());
                    // Guardamos el cliente en el request para que la página JSP pueda usarlo.
                    request.setAttribute("clienteDatos", cliente);
                }
                request.getRequestDispatcher("misDatos.jsp").forward(request, response);
                break;

            case "actualizarMisDatos":
                // 1. Obtenemos el usuario de la sesión para asegurarnos de actualizar al correcto.
                Usuario usuarioActual = (Usuario) request.getSession().getAttribute("usuarioLogueado");
                
                if (usuarioActual != null && usuarioActual.getRol().equals("CLIENTE")) {
                    // 2. Obtenemos los nuevos datos del formulario.
                    String nuevoNombre = request.getParameter("nombre");
                    String nuevaDireccion = request.getParameter("direccion");
                    String nuevoTelefono = request.getParameter("telefono");

                    // 3. Creamos un objeto Cliente con la información actualizada.
                    Cliente clienteActualizado = new Cliente(
                        usuarioActual.getClienteId(), // El ID no cambia
                        nuevoNombre,
                        nuevaDireccion,
                        nuevoTelefono,
                        usuarioActual.getUsername() // El email (username) tampoco cambia
                    );

                    // 4. Le pasamos el objeto a la fachada para que lo guarde en la BD.
                    boolean exitoActualizacion = fachada.actualizarCliente(clienteActualizado);
                    
                    if (exitoActualizacion) {
                        request.getSession().setAttribute("mensaje", "¡Tus datos han sido actualizados exitosamente!");
                    } else {
                        request.getSession().setAttribute("mensaje", "Error: No se pudieron actualizar tus datos.");
                    }
                }
                
                // Redirigimos de vuelta a la misma página 
                response.sendRedirect("controlador?accion=mostrarMisDatos");
                break;

            case "verEmpleados":
                request.setAttribute("listaEmpleados", fachada.getEmpleados());
                request.getRequestDispatcher("empleados.jsp").forward(request, response);
                break;
            
            case "mostrarFormularioEmpleado":
                request.getRequestDispatcher("registrarEmpleado.jsp").forward(request, response);
                break;
                
            case "registrarEmpleado":
                String idEmpleado = request.getParameter("id");
                String nombreEmpleado = request.getParameter("nombre");
                String direccionEmpleado = request.getParameter("direccion");
                String telefonoEmpleado = request.getParameter("telefono");
                String cargo = request.getParameter("cargo");
                
                fachada.registrarEmpleado(idEmpleado, nombreEmpleado, direccionEmpleado, telefonoEmpleado, cargo);
                request.getSession().setAttribute("mensaje", "¡Empleado '" + nombreEmpleado + "' registrado exitosamente!");
                response.sendRedirect("controlador?accion=verEmpleados");
                break;
                
            case "eliminarEmpleado":
                String empleadoIdParaEliminar = request.getParameter("empleadoId");
                boolean borradoEmpleado = fachada.eliminarEmpleado(empleadoIdParaEliminar);
                
                if(borradoEmpleado) {
                    request.getSession().setAttribute("mensaje", "Empleado eliminado exitosamente.");
                } else {
                    request.getSession().setAttribute("mensaje", "Error: No se pudo eliminar al empleado. Es posible que tenga alquileres asociados.");
                }
                
                response.sendRedirect("controlador?accion=verEmpleados");
                break;
                
            case "mostrarGestionLavanderia":
                request.setAttribute("listaInventario", fachada.getInventario());
                request.setAttribute("listaLavado", fachada.getListaParaLavado());
                request.getRequestDispatcher("gestionLavanderia.jsp").forward(request, response);
                break;
            
            case "enviarALavado":
                String refLavado = request.getParameter("ref");
                boolean tienePrioridad = request.getParameter("prioridad") != null;
                fachada.enviarALavado(refLavado, tienePrioridad);
                request.getSession().setAttribute("mensaje", "Prenda " + refLavado + " enviada a lavandería.");
                response.sendRedirect("controlador?accion=mostrarGestionLavanderia");
                break;
                
            case "confirmarEnvioLavanderia":
                List<Prenda> prendasEnviadas = fachada.confirmarEnvioLavanderia();
                
                // Construimos un mensaje detallado con HTML
                StringBuilder mensajeHtml = new StringBuilder("Se enviaron " + prendasEnviadas.size() + " prendas a la lavandería: <ul>");
                for(Prenda p : prendasEnviadas) {
                    mensajeHtml.append("<li>").append(p.getDescripcion()).append("</li>");
                }
                mensajeHtml.append("</ul>");
                
                request.getSession().setAttribute("mensaje", mensajeHtml.toString());
                response.sendRedirect("controlador?accion=mostrarGestionLavanderia");
                break;

            case "mostrarFormularioAlquiler":
                if (usuarioLogueado != null && usuarioLogueado.getRol().equals("ADMIN")) {
                    request.setAttribute("listaClientes", fachada.getClientes());
                    // NUEVO: Añadimos la lista de empleados para el formulario del admin
                    request.setAttribute("listaEmpleados", fachada.getEmpleados());
                }
                request.setAttribute("listaPrendas", fachada.getInventario());
                request.getRequestDispatcher("registrarAlquiler.jsp").forward(request, response);
                break;
            
            case "procesarAlquiler":
                String idClienteAlquiler;
                String idEmpleadoSeleccionado = request.getParameter("empleadoId"); // Será null si es un cliente
                
                if (usuarioLogueado != null && usuarioLogueado.getRol().equals("ADMIN")) {
                    idClienteAlquiler = request.getParameter("clienteId");
                } else {
                    idClienteAlquiler = (usuarioLogueado != null) ? usuarioLogueado.getClienteId() : null;
                }
                
                if (idClienteAlquiler == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                try {
                    String refPrenda = request.getParameter("prendaRef");
                    String fechaStr = request.getParameter("fechaAlquiler");
                    Date fechaAlquiler = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                    
                    // Llamamos a la fachada. Ella se encargará de la lógica del empleado.
                    String resultado = fachada.realizarAlquiler(idClienteAlquiler, idEmpleadoSeleccionado, refPrenda, fechaAlquiler);
                    
                    request.getSession().setAttribute("mensaje", resultado);
                    if (usuarioLogueado.getRol().equals("CLIENTE")) {
                         response.sendRedirect("controlador?accion=verMisAlquileres");
                    } else {
                         response.sendRedirect("index.jsp");
                    }
                } catch (ParseException e) {
                    request.getSession().setAttribute("mensaje", "Error: El formato de la fecha no es válido.");
                    response.sendRedirect("controlador?accion=mostrarFormularioAlquiler");
                }
                break;

            case "verMisAlquileres":
                if (usuarioLogueado != null && usuarioLogueado.getRol().equals("CLIENTE")) {
                    List<ServicioAlquiler> historial = fachada.consultarServiciosPorCliente(usuarioLogueado.getClienteId());
                    request.setAttribute("historialAlquileres", historial);
                    request.getRequestDispatcher("misAlquileres.jsp").forward(request, response);
                } else {
                    response.sendRedirect("index.jsp");
                }
                break;
                
            // Muestra el panel de búsqueda avanzada de alquileres
            case "mostrarPanelBusqueda":
                // Cargamos las listas necesarias para los menús desplegables del formulario
                request.setAttribute("listaClientes", fachada.getClientes());
                request.setAttribute("listaEmpleados", fachada.getEmpleados());
                request.getRequestDispatcher("alquileres.jsp").forward(request, response);
                break;

            // Procesa los filtros del panel de búsqueda y muestra los resultados
            case "buscarAlquileres":
                // Obtenemos los filtros del formulario (pueden venir vacíos)
                String clienteId = request.getParameter("clienteId");
                String empleadoId = request.getParameter("empleadoId");
                String fechaInicioStr = request.getParameter("fechaInicio");
                String fechaFinStr = request.getParameter("fechaFin");
                
                // Convertimos las fechas
                Date fechaInicio = null;
                Date fechaFin = null;
                try {
                    if (fechaInicioStr != null && !fechaInicioStr.isEmpty()) {
                        fechaInicio = new SimpleDateFormat("yyyy-MM-dd").parse(fechaInicioStr);
                    }
                    if (fechaFinStr != null && !fechaFinStr.isEmpty()) {
                        fechaFin = new SimpleDateFormat("yyyy-MM-dd").parse(fechaFinStr);
                    }
                } catch (ParseException e) {
                    e.printStackTrace(); // Manejo de error de fecha
                }
                
                List<ServicioAlquiler> resultados = fachada.buscarAlquileres(clienteId, empleadoId, fechaInicio, fechaFin);
                
                // Reenviamos los resultados a la misma página para mostrarlos
                request.setAttribute("listaResultados", resultados);
                // También reenviamos las listas para que los desplegables sigan funcionando
                request.setAttribute("listaClientes", fachada.getClientes());
                request.setAttribute("listaEmpleados", fachada.getEmpleados());
                request.getRequestDispatcher("alquileres.jsp").forward(request, response);
                break;

            case "consultarPorTalla":
                String tallaConsultada = request.getParameter("tallaBusqueda");
                List<Prenda> resultadosPorTalla;
                if (tallaConsultada != null && !tallaConsultada.trim().isEmpty()) {
                    resultadosPorTalla = fachada.consultarPrendasPorTalla(tallaConsultada);
                    request.setAttribute("terminoBusqueda", tallaConsultada);
                } else {
                    resultadosPorTalla = fachada.getInventario();
                }
                request.setAttribute("listaPrendas", resultadosPorTalla);
                request.getRequestDispatcher("inventario.jsp").forward(request, response);
                break;
            
            case "consultarPorCliente":
                // 1. Obtenemos el ID del cliente y el tipo de vista que se solicita (vigentes o completo)
                String clienteIdConsulta = request.getParameter("clienteId");
                String vista = request.getParameter("vista");
                if (vista == null || vista.isEmpty()) {
                    vista = "vigentes"; // Por defecto, mostramos los alquileres vigentes
                }

                // 2. Buscamos al cliente
                Cliente cliente = fachada.buscarCliente(clienteIdConsulta);
                List<ServicioAlquiler> historial;

                // 3. Decidimos qué método de la fachada llamar según la vista solicitada
                if ("completo".equals(vista)) {
                    historial = fachada.consultarHistorialCompletoPorCliente(clienteIdConsulta);
                } else {
                    historial = fachada.consultarServiciosPorCliente(clienteIdConsulta); // Este busca los vigentes
                }
                
                // 4. Guardamos todos los datos para que la página JSP los pueda usar
                request.setAttribute("clienteSeleccionado", cliente);
                request.setAttribute("historialAlquileres", historial);
                request.setAttribute("vistaActual", vista); // Guardamos la vista actual para los botones
                
                request.getRequestDispatcher("historialCliente.jsp").forward(request, response);
                break;
                
            case "diagnostico":
                try (Connection conn = com.losatuendos.datos.ConexionDB.getConnection()) {
                    if (conn != null) {
                        request.setAttribute("conexionExitosa", true);
                        conn.close();
                    } else {
                        request.setAttribute("conexionExitosa", false);
                        request.setAttribute("errorDetallado", "El método ConexionDB.getConnection() devolvió null.");
                    }
                } catch (java.sql.SQLException e) {
                    request.setAttribute("conexionExitosa", false);
                    request.setAttribute("errorDetallado", e.getMessage());
                    e.printStackTrace();
                }
                request.getRequestDispatcher("diagnostico.jsp").forward(request, response);
                break;

            default:
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}