<%-- Archivo: alquileres.jsp --%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.losatuendos.model.ServicioAlquiler"%>
<%@page import="com.losatuendos.model.Empleado"%>
<%@page import="com.losatuendos.model.Cliente"%>
<%@page import="java.util.List"%>
<jsp:include page="header.jsp" />

<%-- Obtenemos las listas que nos envió el controlador --%>
<%
    List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
    List<Empleado> listaEmpleados = (List<Empleado>) request.getAttribute("listaEmpleados");
    List<ServicioAlquiler> listaResultados = (List<ServicioAlquiler>) request.getAttribute("listaResultados");
%>

<main class="container mt-4">
    <div class="card">
        <div class="card-header">
            <h4 class="mb-0">Panel de Búsqueda de Alquileres</h4>
        </div>
        <div class="card-body">
            <form action="controlador" method="GET">
                <input type="hidden" name="accion" value="buscarAlquileres">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="clienteId" class="form-label">Filtrar por Cliente</label>
                        <select class="form-select" name="clienteId">
                            <option value="">-- Todos los Clientes --</option>
                            <% if (listaClientes != null) for (Cliente c : listaClientes) { %>
                            <option value="<%= c.getId() %>"><%= c.getNombre() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="empleadoId" class="form-label">Filtrar por Empleado</label>
                        <select class="form-select" name="empleadoId">
                            <option value="">-- Todos los Empleados --</option>
                            <% if (listaEmpleados != null) for (Empleado e : listaEmpleados) { %>
                            <option value="<%= e.getId() %>"><%= e.getNombre() %></option>
                            <% } %>
                        </select>
                    </div>
                </div>
                <div class="row align-items-end">
                    <div class="col-md-5 mb-3">
                        <label for="fechaInicio" class="form-label">Desde la Fecha</label>
                        <input type="date" class="form-control" name="fechaInicio">
                    </div>
                    <div class="col-md-5 mb-3">
                        <label for="fechaFin" class="form-label">Hasta la Fecha</label>
                        <input type="date" class="form-control" name="fechaFin">
                    </div>
                    <div class="col-md-2 mb-3 d-grid">
                        <button type="submit" class="btn btn-primary">Buscar</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <%-- Solo mostramos la tabla de resultados si se ha hecho una búsqueda --%>
    <% if (listaResultados != null) { %>
    <div class="card mt-4">
        <div class="card-header"><h5 class="mb-0">Resultados de la Búsqueda</h5></div>
        <div class="card-body">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th># Servicio</th>
                        <th>Cliente</th>
                        <th>Prenda</th>
                        <th>Fecha Evento</th>
                        <th>Atendido por</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (!listaResultados.isEmpty()) { 
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        for (ServicioAlquiler sa : listaResultados) { %>
                    <tr>
                        <td><%= sa.getNumero() %></td>
                        <td><%= sa.getCliente().getNombre() %></td>
                        <td><%= sa.getPrenda().getDescripcion() %></td>
                        <td><%= sdf.format(sa.getFechaAlquiler()) %></td>
                        <td><%= sa.getEmpleado().getNombre() %></td>
                    </tr>
                    <% } } else { %>
                    <tr><td colspan="5" class="text-center">No se encontraron alquileres con los filtros seleccionados.</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
    <% } %>
</main>

<jsp:include page="footer.jsp" />
