package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import model.dao.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "IndirizzoAddServlet", value = "/IndirizzoAddServlet")
public class IndirizzoAddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Utente utente = (session == null) ? null : (Utente) session.getAttribute("utente");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }

        String via = request.getParameter("via");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String provincia = request.getParameter("provincia");
        String nazione = request.getParameter("nazione");
        String telefono = request.getParameter("telefono");

        if (via == null || via.isBlank() ||
                citta == null || citta.isBlank() ||
                cap == null || cap.isBlank() ||
                provincia == null || provincia.isBlank() ||
                nazione == null || nazione.isBlank()) {

            session.setAttribute("flashError", "Compila tutti i campi indirizzo obbligatori.");
            response.sendRedirect(request.getContextPath() + "/CheckoutServlet");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO indirizzo (IDUtente, Via, Citta, CAP, Provincia, Nazione, Telefono) " +
                             "VALUES (?,?,?,?,?,?,?)")) {

            ps.setInt(1, utente.getId());
            ps.setString(2, via);
            ps.setString(3, citta);
            ps.setString(4, cap);
            ps.setString(5, provincia);
            ps.setString(6, nazione);
            ps.setString(7, telefono);

            ps.executeUpdate();

            session.setAttribute("flashOk", "Indirizzo salvato.");

        } catch (SQLException e) {
            throw new ServletException("Errore DB salvataggio indirizzo", e);
        }

        response.sendRedirect(request.getContextPath() + "/CheckoutServlet");

    }
}