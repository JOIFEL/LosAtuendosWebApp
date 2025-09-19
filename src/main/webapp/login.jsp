<%-- Archivo: login.jsp (Versión Final con Estética Corregida) --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso - Los Atuendos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding-top: 5rem;
            padding-bottom: 5rem;
        }
        .login-card {
            width: 100%;
            max-width: 420px;
            margin: auto; /* Centra horizontalmente */
            border: none;
            border-radius: 1rem;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }
        .login-icon {
            font-size: 3rem;
            color: #6a11cb;
        }
    </style>
</head>
<body>
    <main>
        <div class="card login-card">
            <div class="card-body p-4 p-sm-5">
                
                <h5 class="card-title text-center">Iniciar Sesión</h5>

                <%-- Bloque para mostrar el mensaje de registro exitoso --%>
                <% String registroMsg = (String) request.getAttribute("mensajeRegistro");
                   if (registroMsg != null) { %>
                    <div class="alert alert-success"><%= registroMsg %></div>
                <% } %>
                
                <div class="text-center mb-4">
                    <i class="bi bi-hanger login-icon"></i>
                    <h1 class="h3 mt-2 mb-3 fw-normal">Los Atuendos</h1>
                </div>
                
                <%-- Bloque para mostrar mensajes de error --%>
                <% String error = (String) request.getAttribute("error");
                   if (error != null) { %>
                    <div class="alert alert-danger" role="alert"><%= error %></div>
                <% } %>
                
                <form action="controlador" method="POST">
                    <input type="hidden" name="accion" value="login">
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" id="username" name="username" placeholder="Usuario" required>
                        <label for="username">Usuario (o Correo si eres cliente)</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="password" class="form-control" id="password" name="password" placeholder="Contraseña" required>
                        <label for="password">Contraseña (o ID si eres cliente)</label>
                    </div>
                    <div class="d-grid">
                        <button class="btn btn-primary btn-lg" type="submit">Ingresar</button>
                    </div>
                </form>
                
                <hr class="my-4">
                
                <div class="text-center">
                    <p class="text-muted">¿Eres un cliente nuevo?</p>
                    <a href="controlador?accion=mostrarFormularioCliente" class="btn btn-outline-success">Regístrate Aquí</a>
                </div>
            </div>
        </div>
    </main>
</body>
</html>