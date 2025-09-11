<%-- Archivo: header.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Los Atuendos - Alquiler de Vestuario</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="index.jsp">Los Atuendos</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownAlquiler" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            Alquileres
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdownAlquiler">
                            <li><a class="dropdown-item" href="controlador?accion=mostrarFormularioAlquiler">Registrar Nuevo Alquiler</a></li>
                            <li><a class="dropdown-item" href="controlador?accion=mostrarConsultas">Consultar Alquileres</a></li>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="controlador?accion=mostrarFormularioAlquiler">Registrar Alquiler</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="controlador?accion=verInventario">Inventario</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="controlador?accion=verEmpleados">Empleados</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="controlador?accion=verClientes">Clientes</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="controlador?accion=mostrarGestionLavanderia">Lavandería</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <%-- NUEVO: Bloque para mostrar mensajes de confirmación --%>
    <% 
        String mensaje = (String) session.getAttribute("mensaje");
        if (mensaje != null) { 
    %>
    <div class="container mt-3">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <%= mensaje %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </div>
    <% 
        session.removeAttribute("mensaje"); // Limpiamos el mensaje para que no se muestre de nuevo
        } 
    %>