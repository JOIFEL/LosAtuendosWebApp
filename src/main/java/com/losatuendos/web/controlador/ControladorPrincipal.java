// Archivo: ControladorPrincipal.java
package com.losatuendos.web.controlador;

import com.losatuendos.model.*;
import com.losatuendos.patrones.facade.FachadaAlquiler;
import com.losatuendos.patrones.factory.PrendaFactory; // <-- ¡Importante añadir este!
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;

@WebServlet(name = "ControladorPrincipal", urlPatterns = {"/controlador"})
public class ControladorPrincipal extends HttpServlet {

    private FachadaAlquiler fachada = new FachadaAlquiler();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) accion = "inicio";

        switch (accion) {
            case "verInventario":
                request.setAttribute("listaPrendas", fachada.getInventario());
                request.getRequestDispatcher("inventario.jsp").forward(request, response);
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
                fachada.registrarCliente(id, nombre, "", "", "");
                request.getSession().setAttribute("mensaje", "¡Cliente '" + nombre + "' registrado exitosamente!");
                response.sendRedirect("controlador?accion=verClientes");
                break;
            case "mostrarFormularioAlquiler":
                request.setAttribute("listaClientes", fachada.getClientes());
                request.setAttribute("listaPrendas", fachada.getInventario());
                request.getRequestDispatcher("registrarAlquiler.jsp").forward(request, response);
                break;
                
            case "procesarAlquiler":
                try {
                    String idCliente = request.getParameter("clienteId");
                    String refPrenda = request.getParameter("prendaRef");
                    String fechaStr = request.getParameter("fechaAlquiler");
                    
                    // Convertimos el texto de la fecha a un objeto Date
                    Date fechaAlquiler = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                    
                    // Llamamos al nuevo método de la fachada que incluye la fecha
                    String resultado = fachada.realizarAlquiler(idCliente, refPrenda, fechaAlquiler);
                    
                    request.getSession().setAttribute("mensaje", resultado);
                    response.sendRedirect("index.jsp");
                } catch (ParseException e) {
                    request.getSession().setAttribute("mensaje", "Error: El formato de la fecha no es válido.");
                    response.sendRedirect("controlador?accion=mostrarFormularioAlquiler");
                }
                break;
            
            // Muestra el formulario vacío para añadir una nueva prenda.
            case "mostrarFormularioPrenda":
                request.getRequestDispatcher("registrarPrenda.jsp").forward(request, response);
                break;

            // Procesa los datos del formulario de la nueva prenda.
            case "registrarPrenda":
                // 1. Obtenemos todos los datos del formulario.
                String tipo = request.getParameter("tipo");
                String ref = request.getParameter("ref");
                String talla = request.getParameter("talla");
                double valor = Double.parseDouble(request.getParameter("valor"));
                
                // 2. Usamos nuestra Factory para crear el objeto Prenda.
                Prenda nuevaPrenda = PrendaFactory.crearPrenda(tipo, ref, talla, valor);
                
                // 3. Le pasamos la nueva prenda a la Fachada para que la guarde.
                fachada.agregarPrenda(nuevaPrenda);
                
                // 4. Preparamos un mensaje de éxito y redirigimos al inventario.
                request.getSession().setAttribute("mensaje", "¡Prenda '" + ref + "' registrada exitosamente!");
                response.sendRedirect("controlador?accion=verInventario");
                break;
            
            // Muestra la página de gestión de lavandería.
            case "mostrarGestionLavanderia":
                request.setAttribute("listaInventario", fachada.getInventario());
                request.setAttribute("listaLavado", fachada.getListaParaLavado());
                request.getRequestDispatcher("gestionLavanderia.jsp").forward(request, response);
                break;
            
            // Procesa el envío de una prenda a la lavandería.
            case "enviarALavado":
                String refLavado = request.getParameter("ref");
                // Verificamos si el parámetro 'prioridad' fue enviado (si el checkbox estaba marcado).
                boolean tienePrioridad = request.getParameter("prioridad") != null;
                
                fachada.enviarALavado(refLavado, tienePrioridad);
                request.getSession().setAttribute("mensaje", "Prenda " + refLavado + " enviada a lavandería.");
                response.sendRedirect("controlador?accion=mostrarGestionLavanderia");
                break;
                
            // Procesa la confirmación del envío a lavandería
            case "confirmarEnvioLavanderia":
                int cantidad = fachada.confirmarEnvioLavanderia();
                request.getSession().setAttribute("mensaje", "Se enviaron " + cantidad + " prendas a la lavandería exitosamente.");
                response.sendRedirect("controlador?accion=mostrarGestionLavanderia");
                break;
                
            // Procesa la búsqueda de prendas por talla.
            case "consultarPorTalla":
                String tallaConsultada = request.getParameter("tallaBusqueda");
                List<Prenda> resultados;

                // Verificamos si la búsqueda está vacía o no.
                if (tallaConsultada != null && !tallaConsultada.trim().isEmpty()) {
                    // Si el usuario escribió algo, filtramos la lista.
                    resultados = fachada.consultarPrendasPorTalla(tallaConsultada);
                    request.setAttribute("terminoBusqueda", tallaConsultada);
                } else {
                    // Si la búsqueda está vacía, mostramos el inventario completo.
                    resultados = fachada.getInventario();
                }
                
                request.setAttribute("listaPrendas", resultados);
                request.getRequestDispatcher("inventario.jsp").forward(request, response);
                break;
             
            // Procesa la consulta de historial de alquileres para un cliente específico.
            case "consultarPorCliente":
                // 1. Obtenemos el ID del cliente desde el enlace en que se hizo clic.
                String clienteId = request.getParameter("clienteId");
                
                // 2. Usamos la fachada para buscar tanto al cliente como su historial.
                Cliente cliente = fachada.buscarCliente(clienteId);
                List<ServicioAlquiler> historial = fachada.consultarServiciosPorCliente(clienteId);
                
                // 3. Guardamos ambos resultados para que la vista los pueda usar.
                request.setAttribute("clienteSeleccionado", cliente);
                request.setAttribute("historialAlquileres", historial);
                
                // 4. Enviamos los datos a una nueva página de vista.
                request.getRequestDispatcher("historialCliente.jsp").forward(request, response);
                break;
                
            // Muestra la página con los formularios de consulta.
            case "mostrarConsultas":
                request.getRequestDispatcher("consultas.jsp").forward(request, response);
                break;
                
            // Procesa la búsqueda de un servicio por su número.
            case "consultarPorNumero":
                try {
                    int numero = Integer.parseInt(request.getParameter("numeroServicio"));
                    ServicioAlquiler servicio = fachada.buscarServicioPorNumero(numero);
                    List<ServicioAlquiler> resultadoPorNumero = new ArrayList<>();
                    if (servicio != null) {
                        resultadoPorNumero.add(servicio); // La página de resultados espera una lista.
                    }
                    request.setAttribute("listaResultados", resultadoPorNumero);
                    request.setAttribute("tituloResultado", "Resultado para el servicio #" + numero);
                    request.getRequestDispatcher("resultadosAlquiler.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    // Manejo de error si el usuario no ingresa un número.
                    request.getSession().setAttribute("mensaje", "Error: Por favor ingrese un número de servicio válido.");
                    response.sendRedirect("controlador?accion=mostrarConsultas");
                }
                break;
                
            // Procesa la búsqueda de servicios por fecha.
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
                
            // Muestra la página con la lista de empleados.
            case "verEmpleados":
                request.setAttribute("listaEmpleados", fachada.getEmpleados());
                request.getRequestDispatcher("empleados.jsp").forward(request, response);
                break;
            
            // Muestra el formulario para registrar un nuevo empleado.
            case "mostrarFormularioEmpleado":
                request.getRequestDispatcher("registrarEmpleado.jsp").forward(request, response);
                break;
                
            // Procesa los datos del formulario de registro de empleado.
            case "registrarEmpleado":
                String idEmpleado = request.getParameter("id");
                String nombreEmpleado = request.getParameter("nombre");
                String cargo = request.getParameter("cargo");
                fachada.registrarEmpleado(idEmpleado, nombreEmpleado, "", "", cargo);
                request.getSession().setAttribute("mensaje", "¡Empleado '" + nombreEmpleado + "' registrado exitosamente!");
                response.sendRedirect("controlador?accion=verEmpleados");
                break;

            default:
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
        }
    }
    // ... (doGet y doPost se quedan igual)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}