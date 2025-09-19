<%-- Archivo: registrarPrenda.jsp --%>
<jsp:include page="header.jsp" />

<main class="container mt-4">
    <h1>Añadir Nueva Prenda al Inventario</h1>
    
    <form action="controlador" method="POST" class="mt-4">
        <input type="hidden" name="accion" value="registrarPrenda">
        
        <%-- Campos Comunes para todas las Prendas --%>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label for="tipo" class="form-label">Tipo de Prenda</label>
                <select class="form-select" id="tipo" name="tipo" required onchange="mostrarCamposEspecificos()">
                    <option value="" disabled selected>-- Elija un tipo --</option>
                    <option value="vestido">Vestido de Dama</option>
                    <option value="traje">Traje de Caballero</option>
                    <option value="disfraz">Disfraz</option>
                </select>
            </div>
            <div class="col-md-6 mb-3">
                <label for="marca" class="form-label">Marca</label>
                <input type="text" class="form-control" id="marca" name="marca" required>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4 mb-3">
                <label for="color" class="form-label">Color</label>
                <input type="text" class="form-control" id="color" name="color" required>
            </div>
            <div class="col-md-4 mb-3">
                <label for="talla" class="form-label">Talla</label>
                <%-- menú desplegable --%>
                <select class="form-select" id="talla" name="talla" required>
                    <option value="" disabled selected>-- Seleccione --</option>
                    <option value="XS">XS</option>
                    <option value="S">S</option>
                    <option value="M">M</option>
                    <option value="L">L</option>
                    <option value="XL">XL</option>
                    <option value="XXL">XXL</option>
                </select>
            </div>
            <div class="col-md-4 mb-3">
                <label for="valor" class="form-label">Valor del Alquiler</label>
                <input type="number" step="0.01" class="form-control" id="valor" name="valor" required>
            </div>
        </div>
        
        <hr>
        
        <%-- Campos Específicos que se mostrarán dinámicamente --%>
        
        <div id="campos-vestido" class="specific-fields" style="display:none;">
            <h5>Detalles del Vestido</h5>
            <div class="row">
                <div class="col-md-4 mb-3">
                    <label for="altura" class="form-label">Altura (Largo/Corto)</label>
                    <input type="text" class="form-control" id="altura" name="altura">
                </div>
                <div class="col-md-4 mb-3">
                    <label for="cantPiezas" class="form-label">Cantidad de Piezas</label>
                    <input type="number" class="form-control" id="cantPiezas" name="cantPiezas">
                </div>
                <div class="col-md-4 mb-3 d-flex align-items-center">
                    <div class="form-check mt-4">
                        <input class="form-check-input" type="checkbox" id="pedreria" name="pedreria">
                        <label class="form-check-label" for="pedreria">Tiene Pedrería</label>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="campos-traje" class="specific-fields" style="display:none;">
            <h5>Detalles del Traje</h5>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="tipoTraje" class="form-label">Tipo de Traje (Frac, etc.)</label>
                    <input type="text" class="form-control" id="tipoTraje" name="tipoTraje">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="aderezo" class="form-label">Aderezo (Corbata, etc.)</label>
                    <input type="text" class="form-control" id="aderezo" name="aderezo">
                </div>
            </div>
        </div>
        
        <div id="campos-disfraz" class="specific-fields" style="display:none;">
            <h5>Detalles del Disfraz</h5>
            <div class="row">
                 <div class="col-md-12 mb-3">
                    <label for="nombreDisfraz" class="form-label">Nombre del Disfraz</label>
                    <input type="text" class="form-control" id="nombreDisfraz" name="nombreDisfraz">
                </div>
            </div>
        </div>
        
        <button type="submit" class="btn btn-primary mt-3">Guardar Prenda</button>
        <a href="controlador?accion=verInventario" class="btn btn-secondary mt-3">Cancelar</a>
    </form>
</main>

<%-- Lógica JavaScript para el formulario dinámico --%>
<script>
    function mostrarCamposEspecificos() {
        // Primero, ocultamos todos los bloques de campos específicos
        document.querySelectorAll('.specific-fields').forEach(function(div) {
            div.style.display = 'none';
        });

        // Obtenemos el valor seleccionado en el menú de tipo de prenda
        var tipoSeleccionado = document.getElementById('tipo').value;
        
        // Mostramos el bloque de campos que corresponde al tipo seleccionado
        if (tipoSeleccionado) {
            var idDelDiv = 'campos-' + tipoSeleccionado;
            var divAMostrar = document.getElementById(idDelDiv);
            if (divAMostrar) {
                divAMostrar.style.display = 'block';
            }
        }
    }
</script>

<jsp:include page="footer.jsp" />
