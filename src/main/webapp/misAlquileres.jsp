<%-- Archivo: misAlquileres.jsp --%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.losatuendos.model.ServicioAlquiler"%>
<%@page import="java.util.List"%>

<%-- Incluimos el encabezado y menú de navegación --%>
<jsp:include page="header.jsp" />

<main class="container mt-4">
    <h1>Mi Historial de Alquileres</h1>
    
    <table class="table table-striped table-hover mt-4">
        <thead class="table-dark">
            <tr>
                <th># Servicio</th>
                <th>Prenda Alquilada</th>
                <th>Fecha de Solicitud</th>
                <th>Fecha del Evento</th>
            </tr>
        </thead>
        <tbody>
            <%
                // Obtenemos la lista que nos envió el controlador
                List<ServicioAlquiler> historial = (List<ServicioAlquiler>) request.getAttribute("historialAlquileres");
                
                // Verificamos si la lista tiene contenido
                if (historial != null && !historial.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    for (ServicioAlquiler sa : historial) {
            %>
            <tr>
                <td><%= sa.getNumero() %></td>
                <td><%= sa.getPrenda().getDescripcion() %></td>
                <td><%= sdf.format(sa.getFechaSolicitud()) %></td>
                <td><%= sdf.format(sa.getFechaAlquiler()) %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <%-- Mensaje que se muestra si el cliente no tiene alquileres --%>
            <tr>
                <td colspan="4" class="text-center">Aún no tienes alquileres registrados.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</main>

<%-- Incluimos el pie de página --%>
<jsp:include page="footer.jsp" />