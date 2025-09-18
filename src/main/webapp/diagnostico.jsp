<%-- Archivo: diagnostico.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Diagnóstico de Conexión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">
    <div class="card">
        <div class="card-header">
            <h1>Diagnóstico de Conexión a Base de Datos</h1>
        </div>
        <div class="card-body">
            <%
                Boolean exito = (Boolean) request.getAttribute("conexionExitosa");
                String errorMsg = (String) request.getAttribute("errorDetallado");

                if (exito != null && exito) {
            %>
                <div class="alert alert-success">
                    <h2 class="alert-heading">¡CONEXIÓN EXITOSA! ✅</h2>
                    <p>La aplicación pudo conectarse a la base de datos sin problemas.</p>
                    <hr>
                    <p class="mb-0">Esto confirma que el driver de MySQL está bien instalado y la URL/usuario/contraseña son correctos.</p>
                </div>
            <% } else { %>
                <div class="alert alert-danger">
                    <h2 class="alert-heading">¡FALLO EN LA CONEXIÓN! ❌</h2>
                    <p>La aplicación no pudo establecer una conexión con la base de datos. El error detallado fue:</p>
                    <hr>
                    <%-- Este bloque mostrará el mensaje de error exacto del servidor --%>
                    <pre><code><%= errorMsg != null ? errorMsg : "No se proporcionó un mensaje de error detallado." %></code></pre>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>
