package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;
import model.dao.DBConnection;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "OrdineConfermaServlet", value = "/OrdineConfermaServlet")
public class OrdineConfermaServlet extends HttpServlet {

    public static class RigaOrdineView {
        private int idOrologio;
        private String nome;
        private String marca;
        private int quantita;
        private BigDecimal prezzoUnitario;
        private BigDecimal subtotale;

        public int getIdOrologio() {return idOrologio;}
        public String getNome() {return nome;}
        public String getMarca() {return marca;}
        public int getQuantita() {return quantita;}
        public BigDecimal getPrezzoUnitario() {return prezzoUnitario;}
        public BigDecimal getSubtotale() {return subtotale;}
    }

    public static class OrdineView {
        private int id;
        private Timestamp dataOrdine;
        private BigDecimal totale;

        private String via;
        private String citta;
        private String cap;
        private String provincia;
        private String nazione;

        private String metodoTipo;
        private String metodoCircuito;

        private List<RigaOrdineView> righe = new ArrayList<>();

        public int getId(){return id;}
        public BigDecimal getTotale(){return totale;}
        public Timestamp getDataOrdine(){return dataOrdine;}
        public String getVia(){return via;}
        public String getCitta(){return citta;}
        public String getCap(){return cap;}
        public String getProvincia(){return provincia;}
        public String getNazione(){return nazione;}
        public String getMetodoTipo(){return metodoTipo;}
        public String getMetodoCircuito(){return metodoCircuito;}
        public List<RigaOrdineView> getRighe(){return righe;}
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

        int idOrdine;
        try {
            idOrdine = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // 1) Testata ordine + controllo proprietario (IDUtente)
            OrdineView ordine = null;

            String sqlOrdine =
                    "SELECT o.ID, o.DataOrdine, o.Totale, " +
                            "       i.Via, i.Citta, i.CAP, i.Provincia, i.Nazione, " +
                            "       m.Tipo AS MetodoTipo, m.Circuito AS MetodoCircuito " +
                            "FROM ordine o " +
                            "JOIN indirizzo i ON i.ID = o.IDIndirizzo " +
                            "JOIN metodopagamento m ON m.ID = o.IDMetodoPagamento " +
                            "WHERE o.ID = ? AND o.IDUtente = ?";

            try (PreparedStatement ps = con.prepareStatement(sqlOrdine)) {
                ps.setInt(1, idOrdine);
                ps.setInt(2, utente.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ordine = new OrdineView();
                        ordine.id = rs.getInt("ID");
                        ordine.dataOrdine = rs.getTimestamp("DataOrdine");
                        ordine.totale = rs.getBigDecimal("Totale");

                        ordine.via = rs.getString("Via");
                        ordine.citta = rs.getString("Citta");
                        ordine.cap = rs.getString("CAP");
                        ordine.provincia = rs.getString("Provincia");
                        ordine.nazione = rs.getString("Nazione");

                        ordine.metodoTipo = rs.getString("MetodoTipo");
                        ordine.metodoCircuito = rs.getString("MetodoCircuito");
                    }
                }
            }

            if (ordine == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ordine non trovato");
                return;
            }

            // 2) Righe ordine
            String sqlRighe =
                    "SELECT IDOrologio, Quantita, PrezzoUnitarioAcquisto, NomeProdottoAcquisto, MarcaAcquisto " +
                            "FROM ordine_item WHERE IDOrdine = ?";

            try (PreparedStatement ps = con.prepareStatement(sqlRighe)) {
                ps.setInt(1, idOrdine);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        RigaOrdineView r = new RigaOrdineView();
                        r.idOrologio = rs.getInt("IDOrologio");
                        r.quantita = rs.getInt("Quantita");
                        r.prezzoUnitario = rs.getBigDecimal("PrezzoUnitarioAcquisto");
                        r.nome = rs.getString("NomeProdottoAcquisto");
                        r.marca = rs.getString("MarcaAcquisto");
                        r.subtotale = r.prezzoUnitario.multiply(BigDecimal.valueOf(r.quantita));
                        ordine.righe.add(r);
                    }
                }
            }

            request.setAttribute("ordine", ordine);
            request.getRequestDispatcher("/view/confermaOrdine.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Errore DB conferma ordine", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}