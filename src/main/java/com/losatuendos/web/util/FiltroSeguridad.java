// Archivo: FiltroSeguridad.java (Versión Corregida)
package com.losatuendos.web.util;

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
        
        // ¿Está intentando acceder a la página de login?
        boolean loginPageRequest = requestURI.endsWith("login.jsp");
        
        // ¿Está intentando procesar un login?
        boolean loginActionRequest = "login".equals(accion);
        
        // NUEVO: ¿Está intentando acceder a la página de diagnóstico?
        boolean diagnosticoRequest = "diagnostico".equals(accion);

        // Si el usuario ya inició sesión, O va a la página de login, O intenta loguearse, O va a la página de diagnóstico...
        if (loggedIn || loginPageRequest || loginActionRequest || diagnosticoRequest) {
            // ...entonces déjalo pasar.
            chain.doFilter(request, response);
        } else {
            // Si no cumple ninguna de esas condiciones, redirígelo a la página de login.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }
}