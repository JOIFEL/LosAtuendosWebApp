<%-- Archivo: consultas.jsp --%>
<jsp:include page="header.jsp" />

<main class="container mt-4">
    <h1>Consultar Servicios de Alquiler</h1>
    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">Buscar por Número de Servicio</div>
                <div class="card-body">
                    <form action="controlador" method="GET">
                        <input type="hidden" name="accion" value="consultarPorNumero">
                        <div class="mb-3">
                            <label for="numeroServicio" class="form-label">Número de Servicio</label>
                            <input type="number" class="form-control" name="numeroServicio" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Buscar</button>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">Buscar por Fecha de Alquiler</div>
                <div class="card-body">
                    <form action="controlador" method="GET">
                        <input type="hidden" name="accion" value="consultarPorFecha">
                        <div class="mb-3">
                            <label for="fechaAlquiler" class="form-label">Fecha de Alquiler</label>
                            <input type="date" class="form-control" name="fechaAlquiler" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Buscar</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="footer.jsp" />
