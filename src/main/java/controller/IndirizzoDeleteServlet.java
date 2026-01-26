package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Utente;
import model.dao.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "IndirizzoDeleteServlet", value = "/IndirizzoDeleteServlet")
public class IndirizzoDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session == null) ? null : (Utente) session.getAttribute("utente");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }

        int idIndirizzo;
        try {
            idIndirizzo = Integer.parseInt(request.getParameter("idIndirizzo"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID indirizzo non valido");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM indirizzo WHERE ID=? AND IDUtente=?")) {

            ps.setInt(1, idIndirizzo);
            ps.setInt(2, utente.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                session.setAttribute("flashError", "Indirizzo non trovato oppure non appartiene all'utente.");
            }

        } catch (SQLException e) {
            // FK: se indirizzo usato in ordine, MySQL può bloccare la delete
            session.setAttribute("flashError",
                    "Non puoi eliminare questo indirizzo perché è associato a uno o più ordini.");
        }

        response.sendRedirect(request.getContextPath() + "/CheckoutServlet");
    }
}
