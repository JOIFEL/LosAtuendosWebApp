<%-- Archivo: registrarCliente.jsp --%>
<jsp:include page="header.jsp" />

<main class="container mt-4">
    <h1>Registrar Nuevo Cliente</h1>
    
    <form action="controlador" method="POST" class="mt-4">
        <input type="hidden" name="accion" value="registrarCliente">
        
        <div class="mb-3">
            <label for="id" class="form-label">Número de Identificación</label>
            <input type="text" class="form-control" id="id" name="id" required>
        </div>
        
        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre Completo</label>
            <input type="text" class="form-control" id="nombre" name="nombre" required>
        </div>
        
        <button type="submit" class="btn btn-primary">Guardar Cliente</button>
        <a href="controlador?accion=verClientes" class="btn btn-secondary">Cancelar</a>
    </form>
</main>

<jsp:include page="footer.jsp" />