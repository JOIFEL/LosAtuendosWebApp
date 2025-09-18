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
                String tipo = request.getParameter("tipo");
                String ref = request.getParameter("ref");
                String talla = request.getParameter("talla");
                double valor = Double.parseDouble(request.getParameter("valor"));
                Prenda nuevaPrenda = PrendaFactory.crearPrenda(tipo, ref, talla, valor);
                fachada.agregarPrenda(nuevaPrenda);
                request.getSession().setAttribute("mensaje", "¡Prenda '" + ref + "' registrada exitosamente!");
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
                String id = request.getParameter("id");
                String nombre = request.getParameter("nombre");
                String direccion = request.getParameter("direccion");
                String telefono = request.getParameter("telefono");
                String mail = request.getParameter("mail");
                boolean exitoCliente = fachada.registrarCliente(id, nombre, direccion, telefono, mail);
                if (exitoCliente) {
                    request.getSession().setAttribute("mensaje", "¡Cliente '" + nombre + "' registrado exitosamente!");
                } else {
                    request.getSession().setAttribute("mensaje", "Error: No se pudo registrar el cliente. Es posible que la identificación o el correo ya existan.");
                }
                response.sendRedirect("controlador?accion=verClientes");
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
                String cargo = request.getParameter("cargo");
                fachada.registrarEmpleado(idEmpleado, nombreEmpleado, "", "", cargo);
                request.getSession().setAttribute("mensaje", "¡Empleado '" + nombreEmpleado + "' registrado exitosamente!");
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
                int cantidad = fachada.confirmarEnvioLavanderia();
                request.getSession().setAttribute("mensaje", "Se enviaron " + cantidad + " prendas a la lavandería exitosamente.");
                response.sendRedirect("controlador?accion=mostrarGestionLavanderia");
                break;

            case "mostrarFormularioAlquiler":
                if (usuarioLogueado != null && usuarioLogueado.getRol().equals("ADMIN")) {
                    request.setAttribute("listaClientes", fachada.getClientes());
                }
                request.setAttribute("listaPrendas", fachada.getInventario());
                request.getRequestDispatcher("registrarAlquiler.jsp").forward(request, response);
                break;
            
            case "procesarAlquiler":
                String idClienteAlquiler;
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
                    String resultado = fachada.realizarAlquiler(idClienteAlquiler, refPrenda, fechaAlquiler);
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
                
            case "mostrarConsultas":
                request.getRequestDispatcher("consultas.jsp").forward(request, response);
                break;

            case "consultarPorNumero":
                try {
                    int numero = Integer.parseInt(request.getParameter("numeroServicio"));
                    ServicioAlquiler servicio = fachada.buscarServicioPorNumero(numero);
                    List<ServicioAlquiler> resultadoPorNumero = new ArrayList<>();
                    if (servicio != null) {
                        resultadoPorNumero.add(servicio);
                    }
                    request.setAttribute("listaResultados", resultadoPorNumero);
                    request.setAttribute("tituloResultado", "Resultado para el servicio #" + numero);
                    request.getRequestDispatcher("resultadosAlquiler.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    request.getSession().setAttribute("mensaje", "Error: Por favor ingrese un número de servicio válido.");
                    response.sendRedirect("controlador?accion=mostrarConsultas");
                }
                break;
            
            case "consultarPorFecha":
                 try {
                    String fechaStr = request.getParameter("fechaAlquiler");
                    Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                    List<ServicioAlquiler> resultadosPorFecha = fachada.consultarServiciosPorFecha(fecha);
                    request.setAttribute("listaResultados", resultadosPorFecha);
                    request.setAttribute("tituloResultado", "Resultados para la fecha " + fechaStr);
                    request.getRequestDispatcher("resultadosAlquiler.jsp").forward(request, response);
                } catch (ParseException e) {
                    request.getSession().setAttribute("mensaje", "Error: Formato de fecha no válido.");
                    response.sendRedirect("controlador?accion=mostrarConsultas");
                }
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