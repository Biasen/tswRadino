package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dao.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.io.IOException;

@WebServlet(name = "AdminOrderDeleteServlet", value = "/admin/ordine/delete")
public class AdminOrderDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        int idOrdine;
        try {
            idOrdine = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1) cancella righe (child)
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM ordine_item WHERE IDOrdine=?")) {
                    ps.setInt(1, idOrdine);
                    ps.executeUpdate();
                }

                // 2) cancella ordine (parent)
                int rows;
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM ordine WHERE ID=?")) {
                    ps.setInt(1, idOrdine);
                    rows = ps.executeUpdate();
                }

                if (rows == 0) {
                    con.rollback();
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ordine non trovato");
                    return;
                }

                con.commit();

                // flash + redirect lista
                HttpSession session = request.getSession();
                session.setAttribute("flashOk", "Ordine #" + idOrdine + " eliminato.");
                response.sendRedirect(request.getContextPath() + "/admin/ordini");

            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new ServletException("Errore DB (admin delete ordine)", e);
        } catch (Exception e) {
            throw new ServletException("Errore eliminazione ordine", e);
        }

    }
}