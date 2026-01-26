package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import model.Utente;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        Utente u = (session == null) ? null : (Utente) session.getAttribute("utente");

        // Adatta qui al tuo model: es. u.getTipo().equals("Admin")
        if (u == null || !"Admin".equalsIgnoreCase(u.getTipo())) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }

        chain.doFilter(req, res);
    }
}
