<%-- Archivo: clientes.jsp --%>
<%@page import="java.util.List"%>
<%@page import="com.losatuendos.model.Cliente"%>

<jsp:include page="header.jsp" />

<main class="container mt-4">
    <div class="d-flex justify-content-between align-items-center">
        <h1>Gestión de Clientes</h1>
        <a href="controlador?accion=mostrarFormularioCliente" class="btn btn-success">Registrar Nuevo Cliente</a>
    </div>
    
    <table class="table table-striped table-hover mt-4">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Dirección</th>
                <th>Teléfono</th>
                <th>Correo</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientes");
                if (clientes != null) {
                    for (Cliente c : clientes) {
            %>
            <tr>
                <td><%= c.getId() %></td>
                <td><%= c.getNombre() %></td>
                <td><%= c.getDireccion() != null ? c.getDireccion() : "" %></td>
                <td><%= c.getTelefono() != null ? c.getTelefono() : "" %></td>
                <td><%= c.getMail() != null ? c.getMail() : "" %></td>
                <td>
                    <a href="controlador?accion=consultarPorCliente&clienteId=<%= c.getId() %>" class="btn btn-info btn-sm">Ver Historial</a>
                    
                    <%-- Botón para eliminar con confirmación --%>
                    <a href="controlador?accion=eliminarCliente&clienteId=<%= c.getId() %>" 
                       class="btn btn-danger btn-sm" 
                       onclick="return confirm('¿Estás seguro de que deseas eliminar a este cliente?');">
                       Eliminar
                    </a>
                </td>
            </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
</main>

<jsp:include page="footer.jsp" />