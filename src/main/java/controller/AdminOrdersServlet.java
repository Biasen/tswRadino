package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.dao.DBConnection;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminOrdersServlet", value ="/admin/ordini")
public class AdminOrdersServlet extends HttpServlet {

    public static class AdminOrdineRow {
        private int id;
        private int idUtente;
        private BigDecimal totale;
        private Timestamp dataOrdine;

        public int getId() { return id; }
        public int getIdUtente() { return idUtente; }
        public BigDecimal getTotale() { return totale; }
        public Timestamp getDataOrdine() { return dataOrdine; }

        public void setId(int id) { this.id = id; }
        public void setIdUtente(int idUtente) { this.idUtente = idUtente; }
        public void setTotale(BigDecimal totale) { this.totale = totale; }
        public void setDataOrdine(Timestamp dataOrdine) { this.dataOrdine = dataOrdine; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        List<AdminOrdineRow> ordini = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT ID, IDUtente, Totale, DataOrdine " +
                             "FROM ordine ORDER BY DataOrdine DESC, ID DESC")) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminOrdineRow o = new AdminOrdineRow();
                    o.setId(rs.getInt("ID"));
                    o.setIdUtente(rs.getInt("IDUtente"));
                    o.setTotale(rs.getBigDecimal("Totale"));
                    o.setDataOrdine(rs.getTimestamp("DataOrdine"));
                    ordini.add(o);
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Errore DB (admin lista ordini)", e);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object ok = session.getAttribute("flashOk");
            if (ok != null) {
                request.setAttribute("ok", ok.toString());
                session.removeAttribute("flashOk");
            }
        }


        request.setAttribute("ordini", ordini);
        request.getRequestDispatcher("/view/admin/ordini.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}