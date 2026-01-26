package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.dao.DBConnection;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "AdminProductFormServlet", value = "/admin/prodotto")
public class AdminProductFormServlet extends HttpServlet {

    public static class ProdottoForm {
        private int id;
        private String nomeModello;
        private String marca;
        private Integer idCategoria;
        private String movimento;
        private String materialeCassa;
        private Integer diametroMM;
        private String impermeabilita;
        private String descrizione;
        private String immagine;
        private BigDecimal prezzoAttuale;
        private int stock;
        private boolean attivo;

        public int getId() { return id; }
        public String getNomeModello() { return nomeModello; }
        public String getMarca() { return marca; }
        public Integer getIdCategoria() { return idCategoria; }
        public String getMovimento() { return movimento; }
        public String getMaterialeCassa() { return materialeCassa; }
        public Integer getDiametroMM() { return diametroMM; }
        public String getImpermeabilita() { return impermeabilita; }
        public String getDescrizione() { return descrizione; }
        public String getImmagine() { return immagine; }
        public BigDecimal getPrezzoAttuale() { return prezzoAttuale; }
        public int getStock() { return stock; }
        public boolean getAttivo() { return attivo; }

        public void setId(int id) { this.id = id; }
        public void setNomeModello(String nomeModello) { this.nomeModello = nomeModello; }
        public void setMarca(String marca) { this.marca = marca; }
        public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
        public void setMovimento(String movimento) { this.movimento = movimento; }
        public void setMaterialeCassa(String materialeCassa) { this.materialeCassa = materialeCassa; }
        public void setDiametroMM(Integer diametroMM) { this.diametroMM = diametroMM; }
        public void setImpermeabilita(String impermeabilita) { this.impermeabilita = impermeabilita; }
        public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
        public void setImmagine(String immagine) { this.immagine = immagine; }
        public void setPrezzoAttuale(BigDecimal prezzoAttuale) { this.prezzoAttuale = prezzoAttuale; }
        public void setStock(int stock) { this.stock = stock; }
        public void setAttivo(boolean attivo) { this.attivo = attivo; }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            ProdottoForm p = new ProdottoForm();
            p.setAttivo(true);
            request.setAttribute("prodotto", p);
            request.getRequestDispatcher("/view/admin/prodottoForm.jsp").forward(request, response);
            return;
        }

        int id;
        try { id = Integer.parseInt(idParam); }
        catch (Exception e) { response.sendError(400, "ID prodotto non valido"); return; }

        ProdottoForm p = new ProdottoForm();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT ID, NomeModello, Marca, IDCategoria, Movimento, MaterialeCassa, DiametroMM, " +
                             "       Impermeabilita, Descrizione, Immagine, PrezzoAttuale, Stock, Attivo " +
                             "FROM orologio WHERE ID=?")) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    response.sendError(404, "Prodotto non trovato");
                    return;
                }

                p.setId(rs.getInt("ID"));
                p.setNomeModello(rs.getString("NomeModello"));
                p.setMarca(rs.getString("Marca"));

                int cat = rs.getInt("IDCategoria");
                p.setIdCategoria(rs.wasNull() ? null : cat);

                p.setMovimento(rs.getString("Movimento"));
                p.setMaterialeCassa(rs.getString("MaterialeCassa"));

                int d = rs.getInt("DiametroMM");
                p.setDiametroMM(rs.wasNull() ? null : d);

                p.setImpermeabilita(rs.getString("Impermeabilita"));
                p.setDescrizione(rs.getString("Descrizione"));
                p.setImmagine(rs.getString("Immagine"));
                p.setPrezzoAttuale(rs.getBigDecimal("PrezzoAttuale"));
                p.setStock(rs.getInt("Stock"));
                p.setAttivo(rs.getBoolean("Attivo"));
            }

        } catch (SQLException e) {
            throw new ServletException("Errore DB (admin form prodotto)", e);
        }

        request.setAttribute("prodotto", p);
        request.getRequestDispatcher("/view/admin/prodottoForm.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}