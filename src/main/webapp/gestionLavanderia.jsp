<%-- Archivo: gestionLavanderia.jsp --%>
<%@page import="java.util.List"%>
<%@page import="com.losatuendos.model.Prenda"%>

<%-- Definimos las variables --%>
<%
    List<Prenda> inventario = (List<Prenda>) request.getAttribute("listaInventario");
    List<Prenda> listaLavado = (List<Prenda>) request.getAttribute("listaLavado");
%>

<jsp:include page="header.jsp" />

<main class="container mt-4">
    <div class="row">
        <div class="col-md-7">
            <h1>Gestión de Lavandería</h1>
            <p>Selecciona las prendas del inventario para enviarlas a la lista de lavado.</p>
            <table class="table table-sm table-hover mt-4">
                <thead class="table-dark">
                    <tr>
                        <th>Descripción</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (inventario != null) {
                            for (Prenda p : inventario) {
                    %>
                    <tr>
                        <td><%= p.getDescripcion() %></td>
                        <td>
                            <form action="controlador" method="POST" class="d-flex align-items-center">
                                <input type="hidden" name="accion" value="enviarALavado">
                                <input type="hidden" name="ref" value="<%= p.getRef() %>">
                                <div class="form-check form-switch me-3">
                                    <input class="form-check-input" type="checkbox" name="prioridad" id="prioridad-<%= p.getRef() %>">
                                    <label class="form-check-label" for="prioridad-<%= p.getRef() %>">Prioridad</label>
                                </div>
                                <button type="submit" class="btn btn-primary btn-sm">Enviar</button>
                            </form>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>

        <div class="col-md-5">
            <div class="card bg-light">
                <div class="card-body">
                    <h5 class="card-title">Prendas en Lista para Lavado</h5>
                    <ul class="list-group list-group-flush">
                        <%
                            if (listaLavado != null && !listaLavado.isEmpty()) {
                                for (Prenda p : listaLavado) {
                        %>
                        <li class="list-group-item"><%= p.getDescripcion() %></li>
                        <%
                                }
                            } else {
                        %>
                        <li class="list-group-item">No hay prendas en la lista.</li>
                        <%
                            }
                        %>
                    </ul>

                    <% if (listaLavado != null && !listaLavado.isEmpty()) { %>
                    <div class="d-grid gap-2 mt-3">
                        <form action="controlador" method="POST">
                            <input type="hidden" name="accion" value="confirmarEnvioLavanderia">
                            <button class="btn btn-success" type="submit">Confirmar Envío a Lavandería</button>
                        </form>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="footer.jsp" />