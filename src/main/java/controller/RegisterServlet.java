package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.dao.DBConnection;
import model.dao.UtenteDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // validazioni minime (aggiungiamo anche lato client dopo)
        if (nome == null || nome.isBlank() ||
                cognome == null || cognome.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.length() < 6) {
            request.setAttribute("error", "Compila tutti i campi (password minimo 6 caratteri).");
            request.getRequestDispatcher("/view/register.jsp").forward(request, response);
            return;
        }

        String hash = BCrypt.hashpw(password, BCrypt.gensalt(12)); // work factor 12 [web:554]

        try (Connection con = DBConnection.getConnection()) {
            UtenteDAO dao = new UtenteDAO();

            // controllo email già esistente
            if (dao.findPasswordHashByEmail(con, email) != null) {
                request.setAttribute("error", "Email già registrata.");
                request.getRequestDispatcher("/view/register.jsp").forward(request, response);
                return;
            }

            dao.create(con, nome, cognome, email, hash);

            // vai al login (oppure autologin, se preferisci)
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
        } catch (Exception e) {
            throw new ServletException(e);
        }

    }

}