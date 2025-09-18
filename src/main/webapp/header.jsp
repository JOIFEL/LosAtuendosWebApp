<%-- Archivo: header.jsp --%>
<%@page import="com.losatuendos.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Obtenemos el objeto Usuario de la sesión. Si no hay sesión, será null. --%>
<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
%>
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
                <%-- El menú principal solo se muestra si el usuario ha iniciado sesión --%>
                <% if (usuarioLogueado != null) { %>
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="index.jsp">Inicio</a>
                        </li>

                        <%-- MENÚ PARA ADMINISTRADORES --%>
                        <% if (usuarioLogueado.getRol().equals("ADMIN")) { %>
                            <li class="nav-item">
                                <a class="nav-link" href="controlador?accion=verInventario">Inventario</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="controlador?accion=verClientes">Clientes</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="controlador?accion=verEmpleados">Empleados</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="controlador?accion=mostrarGestionLavanderia">Lavandería</a>
                            </li>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownAlquiler" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    Alquileres
                                </a>
                                <ul class="dropdown-menu" aria-labelledby="navbarDropdownAlquiler">
                                    <li><a class="dropdown-item" href="controlador?accion=mostrarFormularioAlquiler">Registrar Nuevo Alquiler</a></li>
                                    <li><a class="dropdown-item" href="controlador?accion=mostrarConsultas">Consultar Alquileres</a></li>
                                </ul>
                            </li>
                        <% } %>

                        <%-- MENÚ PARA CLIENTES --%>
                        <% if (usuarioLogueado.getRol().equals("CLIENTE")) { %>
                            <li class="nav-item">
                                <a class="nav-link" href="controlador?accion=verMisAlquileres">Mis Alquileres</a>
                            </li>
                            <li class="nav-item"><a class="nav-link" href="controlador?accion=mostrarFormularioAlquiler">Registrar Nuevo Alquiler</a></li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">Mis Datos Personales</a>
                            </li>
                        <% } %>
                    </ul>

                    <%-- Muestra el nombre del usuario y el botón de cerrar sesión --%>
                    <div class="d-flex">
                        <span class="navbar-text me-3">
                            Bienvenido, <%= usuarioLogueado.getUsername() %>
                        </span>
                        <a href="controlador?accion=logout" class="btn btn-outline-light">Cerrar Sesión</a>
                    </div>
                <% } %>
            </div>
        </div>
    </nav>

    <%-- Bloque para mostrar mensajes de confirmación --%>
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
        session.removeAttribute("mensaje");
        } 
    %>