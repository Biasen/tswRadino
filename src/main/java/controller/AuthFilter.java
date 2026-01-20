package controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/CheckoutServlet"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        Object utente = (session == null) ? null : session.getAttribute("utente");

        if (utente == null) {
            // (opzionale ma utile) salvo destinazione dopo login
            HttpSession s = request.getSession(true);
            String target = request.getContextPath() + "/CheckoutServlet";
            s.setAttribute("targetAfterLogin", target);
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }

        chain.doFilter(req, res);
    }
}
