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
                <%-- NUEVA COLUMNA --%>
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
                <td>
                    <%-- NUEVO ENLACE: Llama al controlador con la acci�n y el ID espec�fico del cliente --%>
                    <a href="controlador?accion=consultarPorCliente&clienteId=<%= c.getId() %>" class="btn btn-info btn-sm">Ver Historial</a>
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