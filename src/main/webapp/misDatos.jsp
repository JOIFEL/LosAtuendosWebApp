<%-- Archivo: misDatos.jsp (Versi�n Final con Mejor Est�tica) --%>
<%@page import="com.losatuendos.model.Cliente"%>
<jsp:include page="header.jsp" />

<%-- ESTE C�DIGO ES NUEVO Y MEJORA LA PRESENTACI�N --%>
<style>
    .form-readonly {
        background-color: #e9ecef; /* Color gris est�ndar de Bootstrap para campos deshabilitados */
        cursor: not-allowed; /* Muestra un cursor de "no permitido" al pasar el rat�n */
    }
    .profile-card {
        border: none;
        border-radius: 0.75rem;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    }
</style>

<main class="container mt-4">
    <%
        Cliente cliente = (Cliente) request.getAttribute("clienteDatos");
        if (cliente == null) {
            cliente = new Cliente("", "No encontrado", "", "", "");
        }
    %>

    <h1>Mis Datos Personales</h1>
    <p class="lead">Aqu� puedes ver y actualizar tu informaci�n de contacto.</p>
    
    <form action="controlador" method="POST" class="mt-4 card profile-card p-4">
        <input type="hidden" name="accion" value="actualizarMisDatos">
        
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="id" class="form-label">N�mero de Identificaci�n</label>
                <%-- Aplicamos la clase CSS y mantenemos el atributo readonly --%>
                <input type="text" class="form-control form-readonly" id="id" name="id" value="<%= cliente.getId() %>" readonly>
                <div class="form-text">Tu identificaci�n no se puede modificar.</div>
            </div>
            <div class="col-md-6 mb-3">
                <label for="mail" class="form-label">Correo Electr�nico (Usuario)</label>
                <input type="email" class="form-control form-readonly" id="mail" name="mail" value="<%= cliente.getMail() %>" readonly>
                <div class="form-text">Tu correo de usuario no se puede modificar.</div>
            </div>
        </div>
        
        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre Completo</label>
            <input type="text" class="form-control" id="nombre" name="nombre" value="<%= cliente.getNombre() %>" required>
        </div>

        <div class="mb-3">
            <label for="direccion" class="form-label">Direcci�n</label>
            <input type="text" class="form-control" id="direccion" name="direccion" value="<%= cliente.getDireccion() != null ? cliente.getDireccion() : "" %>">
        </div>

        <div class="mb-3">
            <label for="telefono" class="form-label">Tel�fono</label>
            <input type="text" class="form-control" id="telefono" name="telefono" value="<%= cliente.getTelefono() != null ? cliente.getTelefono() : "" %>">
        </div>
        
        <button type="submit" class="btn btn-primary">Guardar Cambios</button>
    </form>
</main>

<jsp:include page="footer.jsp" />
