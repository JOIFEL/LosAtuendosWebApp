<%-- Archivo: empleados.jsp --%>
<%@page import="java.util.List"%>
<%@page import="com.losatuendos.model.Empleado"%>
<jsp:include page="header.jsp" />

<main class="container mt-4">
    <div class="d-flex justify-content-between align-items-center">
        <h1>Gestión de Empleados</h1>
        <a href="controlador?accion=mostrarFormularioEmpleado" class="btn btn-success">Registrar Nuevo Empleado</a>
    </div>

    <table class="table table-striped table-hover mt-4">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Cargo</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Empleado> empleados = (List<Empleado>) request.getAttribute("listaEmpleados");
                if (empleados != null) {
                    for (Empleado e : empleados) {
            %>
            <tr>
                <td><%= e.getId() %></td>
                <td><%= e.getNombre() %></td>
                <td><%= e.getCargo() %></td>
            </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
</main>

<jsp:include page="footer.jsp" />
