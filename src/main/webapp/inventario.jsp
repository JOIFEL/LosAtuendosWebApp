<%-- Archivo: inventario.jsp (Versión Corregida) --%>
<%@page import="java.util.List"%>
<%@page import="com.losatuendos.model.Prenda"%>

<jsp:include page="header.jsp" />

<main class="container mt-4">
    <div class="d-flex justify-content-between align-items-center">
        <h1>
            <%-- Título dinámico: cambia si es una búsqueda o la lista completa --%>
            <% 
                String terminoBusqueda = (String) request.getAttribute("terminoBusqueda");
                if (terminoBusqueda != null && !terminoBusqueda.isEmpty()) { 
            %>
                Resultados para la talla: <b><%= terminoBusqueda %></b>
                <%-- Enlace para limpiar la búsqueda y volver a ver todo el inventario --%>
                <a href="controlador?accion=verInventario" class="btn btn-sm btn-outline-secondary ms-2">Ver todos</a>
            <% } else { %>
                Inventario Disponible
            <% } %>
        </h1>
        <a href="controlador?accion=mostrarFormularioPrenda" class="btn btn-primary">Añadir Nueva Prenda</a>
    </div>
    
    <div class="card bg-light my-4">
        <div class="card-body">
            <form action="controlador" method="GET" class="d-flex">
                <input type="hidden" name="accion" value="consultarPorTalla">
                <input type="text" class="form-control me-2" name="tallaBusqueda" placeholder="Buscar por talla (ej: M, L, S)...">
                <button type="submit" class="btn btn-info">Buscar</button>
            </form>
        </div>
    </div>
    
    <table class="table table-striped table-hover mt-4">
        <thead class="table-dark">
            <tr>
                <th>Referencia</th>
                <th>Descripción</th>
                <th>Talla</th>
                <th>Valor Alquiler</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Prenda> prendas = (List<Prenda>) request.getAttribute("listaPrendas");
                if (prendas != null) {
                    for (Prenda p : prendas) {
            %>
            <tr>
                <td><%= p.getRef() %></td>
                <td><%= p.getDescripcion() %></td>
                <td><%= p.getTalla() %></td>
                <td>$<%= String.format("%.0f", p.getValorAlquiler()) %></td>
            </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
</main>

<jsp:include page="footer.jsp" />