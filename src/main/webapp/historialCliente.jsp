<%-- Archivo: historialCliente.jsp (Versión Final y Estética) --%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.losatuendos.model.ServicioAlquiler"%>
<%@page import="java.util.List"%>
<%@page import="com.losatuendos.model.Cliente"%>
<jsp:include page="header.jsp" />

<main class="container mt-4">
    <%
        Cliente cliente = (Cliente) request.getAttribute("clienteSeleccionado");
        List<ServicioAlquiler> historial = (List<ServicioAlquiler>) request.getAttribute("historialAlquileres");
        String vistaActual = (String) request.getAttribute("vistaActual");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    %>

    <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
            <div>
                <h4 class="mb-0">
                    <%-- Título dinámico que cambia según la vista --%>
                    <% if ("completo".equals(vistaActual)) { %>
                        Historial Completo de:
                    <% } else { %>
                        Alquileres Vigentes de:
                    <% } %>
                    <b><%= (cliente != null) ? cliente.getNombre() : "Desconocido" %></b>
                </h4>
            </div>
            <div>
                <%-- Botones para cambiar entre vistas --%>
                <a href="controlador?accion=consultarPorCliente&clienteId=<%= cliente.getId() %>&vista=vigentes" class="btn <%= "vigentes".equals(vistaActual) ? "btn-primary" : "btn-outline-primary" %>">Vigentes</a>
                <a href="controlador?accion=consultarPorCliente&clienteId=<%= cliente.getId() %>&vista=completo" class="btn <%= "completo".equals(vistaActual) ? "btn-primary" : "btn-outline-primary" %>">Historial Completo</a>
            </div>
        </div>
        <div class="card-body">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th># Servicio</th>
                        <th>Prenda Alquilada</th>
                        <th>Fecha del Evento</th>
                        <th>Atendido por</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (historial != null && !historial.isEmpty()) {
                            for (ServicioAlquiler sa : historial) {
                    %>
                    <tr>
                        <td><%= sa.getNumero() %></td>
                        <td><%= sa.getPrenda().getDescripcion() %></td>
                        <td><%= sdf.format(sa.getFechaAlquiler()) %></td>
                        <%-- Mostramos el empleado que gestionó el alquiler --%>
                        <td><%= sa.getEmpleado().getNombre() %></td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="4" class="text-center">No se encontraron alquileres para esta vista.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
            <a href="controlador?accion=verClientes" class="btn btn-secondary mt-3">Volver a la lista de clientes</a>
        </div>
    </div>
</main>

<jsp:include page="footer.jsp" />