<%-- Archivo: clientes.jsp --%>
<%@page import="java.util.List"%>
<%@page import="com.losatuendos.model.Cliente"%>

<jsp:include page="header.jsp" />

<main class="container mt-4">
    <div class="d-flex justify-content-between align-items-center">
        <h1>Gesti�n de Clientes</h1>
        <a href="controlador?accion=mostrarFormularioCliente" class="btn btn-success">Registrar Nuevo Cliente</a>
    </div>
    
    <table class="table table-striped table-hover mt-4">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Direcci�n</th>
                <th>Tel�fono</th>
                <th>Correo</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientes");
                if (clientes != null && !clientes.isEmpty()) {
                    for (Cliente c : clientes) {
            %>
            <tr>
                <td><%= c.getId() %></td>
                <td><%= c.getNombre() %></td>
                <td><%= c.getDireccion() != null ? c.getDireccion() : "" %></td>
                <td><%= c.getTelefono() != null ? c.getTelefono() : "" %></td>
                <td><%= c.getMail() != null ? c.getMail() : "" %></td>
                <td>
                    <%-- Bot�n para ver el historial espec�fico de este cliente --%>
                    <a href="controlador?accion=consultarPorCliente&clienteId=<%= c.getId() %>" class="btn btn-info btn-sm mb-1">Ver Historial</a>
                    
                    <%-- Bot�n para eliminar a este cliente, con confirmaci�n --%>
                    <a href="controlador?accion=eliminarCliente&clienteId=<%= c.getId() %>" 
                       class="btn btn-danger btn-sm" 
                       onclick="return confirm('�Est�s seguro de que deseas eliminar a <%= c.getNombre() %>? Esta acci�n es permanente.');">
                       Eliminar
                    </a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="6" class="text-center">No hay clientes registrados.</td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
</main>

<jsp:include page="footer.jsp" />