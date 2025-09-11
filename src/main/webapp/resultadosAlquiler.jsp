<%-- Archivo: resultadosAlquiler.jsp --%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.losatuendos.model.ServicioAlquiler"%>
<%@page import="java.util.List"%>

<jsp:include page="header.jsp" />

<main class="container mt-4">
    <h1><%= request.getAttribute("tituloResultado") %></h1>

    <table class="table table-striped table-hover mt-4">
        <thead class="table-dark">
            <tr>
                <th># Servicio</th>
                <th>Cliente</th>
                <th>Prenda Alquilada</th>
                <th>Fecha de Solicitud</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<ServicioAlquiler> resultados = (List<ServicioAlquiler>) request.getAttribute("listaResultados");
                if (resultados != null && !resultados.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    for (ServicioAlquiler sa : resultados) {
            %>
            <tr>
                <td><%= sa.getNumero() %></td>
                <td><%= sa.getCliente().getNombre() %></td>
                <td><%= sa.getPrenda().getDescripcion() %></td>
                <td><%= sdf.format(sa.getFechaSolicitud()) %></td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="4" class="text-center">No se encontraron alquileres que coincidan con la búsqueda.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
    <a href="controlador?accion=mostrarConsultas" class="btn btn-secondary mt-3">Realizar otra consulta</a>
</main>

<jsp:include page="footer.jsp" />
