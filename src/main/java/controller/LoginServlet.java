package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.dao.DBConnection;
import model.dao.UtenteDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            UtenteDAO dao = new UtenteDAO();

            String hash = dao.findPasswordHashByEmail(con, email);
            if (hash == null || !BCrypt.checkpw(password, hash)) { // verifica [web:554]
                request.setAttribute("error", "Credenziali non valide.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            // carico dati utente “puliti” (senza password)
            var utente = dao.findByEmail(con, email);

            HttpSession session = request.getSession(true);
            session.setAttribute("utente", utente); // session login [web:538]

            // se avevi salvato una destinazione prima del redirect:
            String target = (String) session.getAttribute("targetAfterLogin");
            if (target != null) {
                session.removeAttribute("targetAfterLogin");
                response.sendRedirect(target);
            } else {
                response.sendRedirect(request.getContextPath() + "/CatalogoServlet");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

    }
}