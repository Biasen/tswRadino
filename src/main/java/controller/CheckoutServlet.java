package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Carrello;
import model.RigaCarrello;
import model.Utente;
import model.dao.DBConnection;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CheckoutServlet", value = "/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    public static class MetodoPagamentoView {
        private int id;
        private String tipo;
        private String circuito;
        private String descrizione;

        public int getId() { return id; }
        public String getTipo() { return tipo; }
        public String getCircuito() { return circuito; }
        public String getDescrizione() { return descrizione; }

        public void setId(int id) { this.id = id; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public void setCircuito(String circuito) { this.circuito = circuito; }
        public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    }

    public static class IndirizzoView {
        private int id;
        private String via;
        private String citta;
        private String cap;
        private String provincia;
        private String nazione;
        private String telefono;

        public int getId() { return id; }
        public String getVia() { return via; }
        public String getCitta() { return citta; }
        public String getCap() { return cap; }
        public String getProvincia() { return provincia; }
        public String getNazione() { return nazione; }
        public String getTelefono() { return telefono; }

        public void setId(int id) { this.id = id; }
        public void setVia(String via) { this.via = via; }
        public void setCitta(String citta) { this.citta = citta; }
        public void setCap(String cap) { this.cap = cap; }
        public void setProvincia(String provincia) { this.provincia = provincia; }
        public void setNazione(String nazione) { this.nazione = nazione; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session == null) ? null : (Utente) session.getAttribute("utente");
        Carrello carrello = (session == null) ? null : (Carrello) session.getAttribute("carrello");

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }
        if (carrello == null || carrello.isVuoto()) {
            response.sendRedirect(request.getContextPath() + "/CarrelloViewServlet");
            return;
        }

        // Flash messages (PRG)
        if (session != null) {
            Object flashError = session.getAttribute("flashError");
            Object flashOk = session.getAttribute("flashOk");
            if (flashError != null) {
                request.setAttribute("error", flashError.toString());
                session.removeAttribute("flashError");
            }
            if (flashOk != null) {
                request.setAttribute("ok", flashOk.toString());
                session.removeAttribute("flashOk");
            }
        }

        try (Connection con = DBConnection.getConnection()) {

            List<MetodoPagamentoView> metodi = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT ID, Tipo, Circuito, Descrizione FROM metodopagamento ORDER BY ID")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        MetodoPagamentoView m = new MetodoPagamentoView();
                        m.setId(rs.getInt("ID"));
                        m.setTipo(rs.getString("Tipo"));
                        m.setCircuito(rs.getString("Circuito"));
                        m.setDescrizione(rs.getString("Descrizione"));
                        metodi.add(m);
                    }
                }
            }

            List<IndirizzoView> indirizzi = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT ID, Via, Citta, CAP, Provincia, Nazione, Telefono " +
                            "FROM indirizzo WHERE IDUtente=? ORDER BY ID")) {
                ps.setInt(1, utente.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        IndirizzoView a = new IndirizzoView();
                        a.setId(rs.getInt("ID"));
                        a.setVia(rs.getString("Via"));
                        a.setCitta(rs.getString("Citta"));
                        a.setCap(rs.getString("CAP"));
                        a.setProvincia(rs.getString("Provincia"));
                        a.setNazione(rs.getString("Nazione"));
                        a.setTelefono(rs.getString("Telefono"));
                        indirizzi.add(a);
                    }
                }
            }

            request.setAttribute("metodi", metodi);
            request.setAttribute("indirizzi", indirizzi);
            request.setAttribute("totale", carrello.getTotale());

            request.getRequestDispatcher("/view/checkout.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Errore DB (GET checkout)", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Utente utente = (session == null) ? null : (Utente) session.getAttribute("utente");
        Carrello carrello = (session == null) ? null : (Carrello) session.getAttribute("carrello");

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/view/login.jsp");
            return;
        }
        if (carrello == null || carrello.isVuoto()) {
            response.sendRedirect(request.getContextPath() + "/CarrelloViewServlet");
            return;
        }

        int idMetodo;
        int idIndirizzo;

        try {
            idMetodo = Integer.parseInt(request.getParameter("idMetodo"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Metodo pagamento non valido");
            return;
        }

        try {
            idIndirizzo = Integer.parseInt(request.getParameter("idIndirizzo"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Indirizzo non valido");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false); // transazione manuale [web:820]

            try {
                // 1) verifica proprietà indirizzo
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT 1 FROM indirizzo WHERE ID=? AND IDUtente=?")) {
                    ps.setInt(1, idIndirizzo);
                    ps.setInt(2, utente.getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new ServletException("Indirizzo non valido per l'utente.");
                    }
                }

                // 2) re-check stock + calcolo totale (lock)
                BigDecimal totale = BigDecimal.ZERO;

                for (RigaCarrello r : carrello.getRighe()) {
                    int idOrologio = r.getProdotto().getId();
                    int qta = r.getQuantita();

                    try (PreparedStatement ps = con.prepareStatement(
                            "SELECT NomeModello, PrezzoAttuale, Stock, Attivo " +
                                    "FROM orologio WHERE ID=? FOR UPDATE")) {
                        ps.setInt(1, idOrologio);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) throw new ServletException("Prodotto non trovato: " + idOrologio);

                            int stock = rs.getInt("Stock");
                            boolean attivo = rs.getBoolean("Attivo");

                            if (!attivo) throw new ServletException("Prodotto non disponibile: " + rs.getString("NomeModello"));
                            if (qta > stock) throw new ServletException("Stock insufficiente per: " + rs.getString("NomeModello"));

                            BigDecimal prezzo = rs.getBigDecimal("PrezzoAttuale");
                            totale = totale.add(prezzo.multiply(BigDecimal.valueOf(qta)));
                        }
                    }
                }

                // 3) inserisci ordine e prendi ID generato
                int idOrdine;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO ordine (IDUtente, IDIndirizzo, IDMetodoPagamento, Totale) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, utente.getId());
                    ps.setInt(2, idIndirizzo);
                    ps.setInt(3, idMetodo);
                    ps.setBigDecimal(4, totale);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("ID ordine non generato.");
                        idOrdine = keys.getInt(1);
                    }
                }

                // 4) righe ordine + decremento stock
                for (RigaCarrello r : carrello.getRighe()) {
                    int idOrologio = r.getProdotto().getId();
                    int qta = r.getQuantita();

                    String nome;
                    String marca;
                    BigDecimal prezzo;

                    try (PreparedStatement ps = con.prepareStatement(
                            "SELECT NomeModello, Marca, PrezzoAttuale FROM orologio WHERE ID=?")) {
                        ps.setInt(1, idOrologio);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) throw new ServletException("Prodotto non trovato: " + idOrologio);
                            nome = rs.getString("NomeModello");
                            marca = rs.getString("Marca");
                            prezzo = rs.getBigDecimal("PrezzoAttuale");
                        }
                    }

                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO ordine_item " +
                                    "(IDOrdine, IDOrologio, Quantita, PrezzoUnitarioAcquisto, NomeProdottoAcquisto, MarcaAcquisto) " +
                                    "VALUES (?,?,?,?,?,?)")) {
                        ps.setInt(1, idOrdine);
                        ps.setInt(2, idOrologio);
                        ps.setInt(3, qta);
                        ps.setBigDecimal(4, prezzo);
                        ps.setString(5, nome);
                        ps.setString(6, marca);
                        ps.executeUpdate();
                    }

                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE orologio SET Stock = Stock - ? WHERE ID=?")) {
                        ps.setInt(1, qta);
                        ps.setInt(2, idOrologio);
                        ps.executeUpdate();
                    }
                }

                con.commit(); // commit [web:823]
                session.removeAttribute("carrello");
                response.sendRedirect(request.getContextPath() + "/OrdineConfermaServlet?id=" + idOrdine);

            } catch (Exception ex) {
                con.rollback(); // rollback [web:823]
                // qui meglio PRG: metto errore in sessione e redirect a GET checkout
                session.setAttribute("flashError", ex.getMessage());
                response.sendRedirect(request.getContextPath() + "/CheckoutServlet");
            } finally {
                con.setAutoCommit(true); // ripristina [web:820]
            }

        } catch (SQLException e) {
            throw new ServletException("Errore DB (POST checkout)", e);
        }
    }
}
