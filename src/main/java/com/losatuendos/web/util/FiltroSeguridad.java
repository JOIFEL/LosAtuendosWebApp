// Archivo: FiltroSeguridad.java
package com.losatuendos.web.util;

import com.losatuendos.model.Usuario;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*") // Este filtro se aplica a TODAS las URLs
public class FiltroSeguridad implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String requestURI = httpRequest.getRequestURI();
        
        // Obtenemos la acción que el usuario quiere realizar
        String accion = httpRequest.getParameter("accion");
        
        // ¿El usuario tiene una sesión iniciada?
        boolean loggedIn = (session != null && session.getAttribute("usuarioLogueado") != null);
        
        // Reglas para permitir el acceso a usuarios SIN sesión:
        boolean loginPageRequest = requestURI.endsWith("login.jsp");
        boolean loginActionRequest = "login".equals(accion);
        boolean diagnosticoRequest = "diagnostico".equals(accion);
        
        // NUEVAS REGLAS: Permitir el acceso al formulario de registro y a la acción de registrar
        boolean showRegisterFormRequest = "mostrarFormularioCliente".equals(accion);
        boolean processRegisterRequest = "registrarCliente".equals(accion);

        // Si el usuario ya inició sesión, O si está intentando acceder a una de las páginas/acciones permitidas...
        if (loggedIn || loginPageRequest || loginActionRequest || diagnosticoRequest || showRegisterFormRequest || processRegisterRequest) {
            // ...entonces déjalo pasar.
            chain.doFilter(request, response);
        } else {
            // Si no cumple ninguna de esas condiciones, redirígelo a la página de login.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }
}