package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import model.dao.DBConnection;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;


@WebServlet(name = "MyOrdersServlet", value = "/MyOrdersServlet")
public class MyOrdersServlet extends HttpServlet {

    public static class OrdineListView {
        private int id;
        private BigDecimal totale;
        private Timestamp dataOrdine;

        public int getId() { return id; }
        public BigDecimal getTotale() { return totale; }
        public Timestamp getDataOrdine() { return dataOrdine; }

        public void setId(int id) { this.id = id; }
        public void setTotale(BigDecimal totale) { this.totale = totale; }
        public void setDataOrdine(Timestamp dataOrdine) { this.dataOrdine = dataOrdine; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        HttpSession session = request.getSession(false);
        Utente utente = (session == null) ? null : (Utente) session.getAttribute("utente");
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }

        List<OrdineListView> ordini = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT ID, Totale, DataOrdine " +
                             "FROM ordine WHERE IDUtente=? " +
                             "ORDER BY DataOrdine DESC, ID DESC")) { // ordinamento tipico: più recenti prima [web:1075]

            ps.setInt(1, utente.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrdineListView o = new OrdineListView();
                    o.setId(rs.getInt("ID"));
                    o.setTotale(rs.getBigDecimal("Totale"));
                    o.setDataOrdine(rs.getTimestamp("DataOrdine"));
                    ordini.add(o);
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Errore DB (lista ordini)", e);
        }

        request.setAttribute("ordini", ordini);
        request.getRequestDispatcher("/view/mieiOrdini.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}