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

@WebServlet(name = "AdminOrderDetailServlet", value = "/admin/ordine")
public class AdminOrderDetailServlet extends HttpServlet {

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

    public static class RigaOrdineView {
        private String nome;
        private String marca;
        private BigDecimal prezzoUnitario;
        private int quantita;
        private BigDecimal subtotale;

        public String getNome() { return nome; }
        public String getMarca() { return marca; }
        public BigDecimal getPrezzoUnitario() { return prezzoUnitario; }
        public int getQuantita() { return quantita; }
        public BigDecimal getSubtotale() { return subtotale; }

        public void setNome(String nome) { this.nome = nome; }
        public void setMarca(String marca) { this.marca = marca; }
        public void setPrezzoUnitario(BigDecimal prezzoUnitario) { this.prezzoUnitario = prezzoUnitario; }
        public void setQuantita(int quantita) { this.quantita = quantita; }
        public void setSubtotale(BigDecimal subtotale) { this.subtotale = subtotale; }
    }

    public static class OrdineView {
        private int id;
        private int idUtente;
        private String nome;
        private String cognome;
        private BigDecimal totale;
        private Timestamp dataOrdine;
        private IndirizzoView indirizzo;
        private List<RigaOrdineView> righe = new ArrayList<>();

        public int getId() { return id; }
        public int getIdUtente() { return idUtente; }
        public String getNome() { return nome; }
        public String getCognome() { return cognome; }
        public BigDecimal getTotale() { return totale; }
        public Timestamp getDataOrdine() { return dataOrdine; }
        public IndirizzoView getIndirizzo() { return indirizzo; }
        public List<RigaOrdineView> getRighe() { return righe; }

        public void setId(int id) { this.id = id; }
        public void setIdUtente(int idUtente) { this.idUtente = idUtente; }
        public void setNome(String nome) { this.nome = nome; }
        public void setCognome(String cognome) { this.cognome = cognome; }
        public void setTotale(BigDecimal totale) { this.totale = totale; }
        public void setDataOrdine(Timestamp dataOrdine) { this.dataOrdine = dataOrdine; }
        public void setIndirizzo(IndirizzoView indirizzo) { this.indirizzo = indirizzo; }
        public void setRighe(List<RigaOrdineView> righe) { this.righe = righe; }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        int idOrdine;
        try {
            idOrdine = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non valido"); // 400 [web:1189]
            return;
        }

        OrdineView ordine = new OrdineView();

        try (Connection con = DBConnection.getConnection()) {

            // 1) Header ordine + utente + indirizzo (JOIN con alias colonne)
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT o.ID AS OrdineID, o.IDUtente AS OrdineIDUtente, o.Totale AS OrdineTotale, o.DataOrdine AS OrdineData, " +
                            "       u.Nome AS UtenteNome, u.Cognome AS UtenteCognome, " +
                            "       a.ID AS IndirizzoID, a.Via AS IndirizzoVia, a.Citta AS IndirizzoCitta, a.CAP AS IndirizzoCAP, " +
                            "       a.Provincia AS IndirizzoProvincia, a.Nazione AS IndirizzoNazione, a.Telefono AS IndirizzoTelefono " +
                            "FROM ordine o " +
                            "JOIN utente u ON u.ID = o.IDUtente " +
                            "JOIN indirizzo a ON a.ID = o.IDIndirizzo " +
                            "WHERE o.ID=?")) {

                ps.setInt(1, idOrdine);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Ordine non trovato");
                        return;
                    }

                    ordine.setId(rs.getInt("OrdineID"));
                    ordine.setIdUtente(rs.getInt("OrdineIDUtente"));
                    ordine.setTotale(rs.getBigDecimal("OrdineTotale"));
                    ordine.setDataOrdine(rs.getTimestamp("OrdineData"));
                    ordine.setNome(rs.getString("UtenteNome"));
                    ordine.setCognome(rs.getString("UtenteCognome"));

                    IndirizzoView ind = new IndirizzoView();
                    ind.setId(rs.getInt("IndirizzoID"));
                    ind.setVia(rs.getString("IndirizzoVia"));
                    ind.setCitta(rs.getString("IndirizzoCitta"));
                    ind.setCap(rs.getString("IndirizzoCAP"));
                    ind.setProvincia(rs.getString("IndirizzoProvincia"));
                    ind.setNazione(rs.getString("IndirizzoNazione"));
                    ind.setTelefono(rs.getString("IndirizzoTelefono"));

                    ordine.setIndirizzo(ind);
                }
            }

            // 2) Righe ordine
            List<RigaOrdineView> righe = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT NomeProdottoAcquisto, MarcaAcquisto, PrezzoUnitarioAcquisto, Quantita " +
                            "FROM ordine_item WHERE IDOrdine=?")) {

                ps.setInt(1, idOrdine);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        RigaOrdineView r = new RigaOrdineView();
                        r.setNome(rs.getString("NomeProdottoAcquisto"));
                        r.setMarca(rs.getString("MarcaAcquisto"));
                        r.setPrezzoUnitario(rs.getBigDecimal("PrezzoUnitarioAcquisto"));
                        r.setQuantita(rs.getInt("Quantita"));

                        BigDecimal subtot = r.getPrezzoUnitario()
                                .multiply(BigDecimal.valueOf(r.getQuantita()));
                        r.setSubtotale(subtot);

                        righe.add(r);
                    }
                }
            }

            ordine.setRighe(righe);

        } catch (SQLException e) {
            throw new ServletException("Errore DB (admin dettaglio ordine)", e);
        }

        request.setAttribute("ordine", ordine);
        request.getRequestDispatcher("/view/admin/ordineDettaglio.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}