<%-- Archivo: registrarAlquiler.jsp --%>
<%@page import="com.losatuendos.model.Prenda"%>
<%@page import="com.losatuendos.model.Cliente"%>
<%@page import="java.util.List"%>

<jsp:include page="header.jsp" />

<main class="container mt-4">
    <h1>Registrar Nuevo Alquiler</h1>
    
    <form action="controlador" method="POST" class="mt-4">
        <input type="hidden" name="accion" value="procesarAlquiler">
        
        <div class="mb-3">
            <label for="clienteId" class="form-label">Seleccionar Cliente</label>
            <select class="form-select" id="clienteId" name="clienteId" required>
                <option value="" disabled selected>-- Elija un cliente --</option>
                <%-- Este código Java llena el menú desplegable con los clientes --%>
                <%
                    List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientes");
                    if (clientes != null) {
                        for (Cliente c : clientes) {
                %>
                <option value="<%= c.getId() %>"><%= c.getNombre() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        
        <div class="mb-3">
            <label for="prendaRef" class="form-label">Seleccionar Prenda</label>
            <select class="form-select" id="prendaRef" name="prendaRef" required>
                <option value="" disabled selected>-- Elija una prenda --</option>
                <%-- Este código Java llena el menú desplegable con las prendas --%>
                <%
                    List<Prenda> prendas = (List<Prenda>) request.getAttribute("listaPrendas");
                    if (prendas != null) {
                        for (Prenda p : prendas) {
                %>
                <option value="<%= p.getRef() %>"><%= p.getDescripcion() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        
        <div class="mb-3">
            <label for="fechaAlquiler" class="form-label">Fecha para el Alquiler</label>
            <input type="date" class="form-control" id="fechaAlquiler" name="fechaAlquiler" required>
        </div>
        
        <button type="submit" class="btn btn-primary">Confirmar Alquiler</button>
        <a href="index.jsp" class="btn btn-secondary">Cancelar</a>
    </form>
</main>

<jsp:include page="footer.jsp" />
