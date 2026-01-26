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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminProductServlet", value = "/admin/prodotti")
public class AdminProductServlet extends HttpServlet {

    public static class ProdottoRow {
        private int id;
        private String nomeModello;
        private String marca;
        private Integer idCategoria;
        private BigDecimal prezzoAttuale;
        private int stock;
        private boolean attivo;
        private String immagine;

        public int getId() { return id; }
        public String getNomeModello() { return nomeModello; }
        public String getMarca() { return marca; }
        public Integer getIdCategoria() { return idCategoria; }
        public BigDecimal getPrezzoAttuale() { return prezzoAttuale; }
        public int getStock() { return stock; }
        public boolean getAttivo() { return attivo; }
        public String getImmagine() { return immagine; }

        public void setId(int id) { this.id = id; }
        public void setNomeModello(String nomeModello) { this.nomeModello = nomeModello; }
        public void setMarca(String marca) { this.marca = marca; }
        public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
        public void setPrezzoAttuale(BigDecimal prezzoAttuale) { this.prezzoAttuale = prezzoAttuale; }
        public void setStock(int stock) { this.stock = stock; }
        public void setAttivo(boolean attivo) { this.attivo = attivo; }
        public void setImmagine(String immagine) { this.immagine = immagine; }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        List<ProdottoRow> prodotti = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT ID, NomeModello, Marca, IDCategoria, PrezzoAttuale, Stock, Attivo, Immagine " +
                             "FROM orologio ORDER BY ID DESC")) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProdottoRow p = new ProdottoRow();
                    p.setId(rs.getInt("ID"));
                    p.setNomeModello(rs.getString("NomeModello"));
                    p.setMarca(rs.getString("Marca"));

                    int cat = rs.getInt("IDCategoria");
                    p.setIdCategoria(rs.wasNull() ? null : cat);

                    p.setPrezzoAttuale(rs.getBigDecimal("PrezzoAttuale"));
                    p.setStock(rs.getInt("Stock"));
                    p.setAttivo(rs.getBoolean("Attivo"));
                    p.setImmagine(rs.getString("Immagine"));
                    prodotti.add(p);
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Errore DB (admin lista prodotti)", e);
        }

        request.setAttribute("prodotti", prodotti);
        request.getRequestDispatcher("/view/admin/prodotti.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}