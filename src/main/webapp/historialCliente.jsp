<%-- Archivo: historialCliente.jsp --%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.losatuendos.model.ServicioAlquiler"%>
<%@page import="java.util.List"%>
<%@page import="com.losatuendos.model.Cliente"%>

<jsp:include page="header.jsp" />

<main class="container mt-4">
    <%
        // Obtenemos los datos que nos envió el controlador.
        Cliente cliente = (Cliente) request.getAttribute("clienteSeleccionado");
        List<ServicioAlquiler> historial = (List<ServicioAlquiler>) request.getAttribute("historialAlquileres");
        // Formateador para mostrar fechas de forma legible.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    %>

    <h1>Historial de Alquileres de: <b><%= (cliente != null) ? cliente.getNombre() : "Desconocido" %></b></h1>
    
    <table class="table table-striped table-hover mt-4">
        <thead class="table-dark">
            <tr>
                <th># Servicio</th>
                <th>Prenda Alquilada</th>
                <th>Fecha de Solicitud</th>
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
                <td><%= sdf.format(sa.getFechaSolicitud()) %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="3" class="text-center">Este cliente no tiene alquileres registrados.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
    <a href="controlador?accion=verClientes" class="btn btn-secondary mt-3">Volver a la lista de clientes</a>
</main>

<jsp:include page="footer.jsp" />