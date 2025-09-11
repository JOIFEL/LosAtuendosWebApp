<%-- Archivo: registrarPrenda.jsp --%>
<jsp:include page="header.jsp" />

<main class="container mt-4">
    <h1>Añadir Nueva Prenda al Inventario</h1>
    
    <form action="controlador" method="POST" class="mt-4">
        <input type="hidden" name="accion" value="registrarPrenda">
        
        <div class="mb-3">
            <label for="tipo" class="form-label">Tipo de Prenda</label>
            <select class="form-select" id="tipo" name="tipo" required>
                <option value="" disabled selected>-- Elija un tipo --</option>
                <option value="vestido">Vestido de Dama</option>
                <option value="traje">Traje de Caballero</option>
                <option value="disfraz">Disfraz</option>
            </select>
        </div>
        
        <div class="mb-3">
            <label for="ref" class="form-label">Referencia (ID único)</label>
            <input type="text" class="form-control" id="ref" name="ref" required>
        </div>
        
        <div class="mb-3">
            <label for="talla" class="form-label">Talla</label>
            <input type="text" class="form-control" id="talla" name="talla" required>
        </div>
        
        <div class="mb-3">
            <label for="valor" class="form-label">Valor del Alquiler</label>
            <input type="number" class="form-control" id="valor" name="valor" required>
        </div>
        
        <button type="submit" class="btn btn-primary">Guardar Prenda</button>
        <a href="controlador?accion=verInventario" class="btn btn-secondary">Cancelar</a>
    </form>
</main>

<jsp:include page="footer.jsp" />
