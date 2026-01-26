package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.dao.DBConnection;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

@WebServlet(name = "AdminProductSaveServlet", value = "/admin/prodotto/save")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class AdminProductSaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        request.setCharacterEncoding("UTF-8");

        // ---- ID (edit vs new)
        String idParam = request.getParameter("id");
        boolean isEdit = (idParam != null && !idParam.isBlank() && !"0".equals(idParam));
        Integer id = null;
        if (isEdit) {
            try { id = Integer.parseInt(idParam); }
            catch (Exception e) { response.sendError(400, "ID prodotto non valido"); return; }
        }

        // ---- Campi
        String nomeModello = request.getParameter("nomeModello");
        String marca = request.getParameter("marca");
        String descrizione = request.getParameter("descrizione");

        String movimento = request.getParameter("movimento");
        String materialeCassa = request.getParameter("materialeCassa");
        String impermeabilita = request.getParameter("impermeabilita");

        Integer idCategoria = null;
        String idCategoriaParam = request.getParameter("idCategoria");
        if (idCategoriaParam != null && !idCategoriaParam.isBlank()) {
            try { idCategoria = Integer.parseInt(idCategoriaParam); } catch (Exception ignored) {}
        }

        Integer diametroMM = null;
        String diametroParam = request.getParameter("diametroMM");
        if (diametroParam != null && !diametroParam.isBlank()) {
            try { diametroMM = Integer.parseInt(diametroParam); } catch (Exception ignored) {}
        }

        BigDecimal prezzo;
        int stock;

        try { prezzo = new BigDecimal(request.getParameter("prezzoAttuale")); }
        catch (Exception e) { response.sendError(400, "Prezzo non valido"); return; }

        try { stock = Integer.parseInt(request.getParameter("stock")); }
        catch (Exception e) { response.sendError(400, "Stock non valido"); return; }

        boolean attivo = request.getParameter("attivo") != null;

        if (nomeModello == null || nomeModello.isBlank() || marca == null || marca.isBlank()) {
            response.sendError(400, "Nome modello e marca sono obbligatori");
            return;
        }

        // ---- Upload immagine (opzionale in edit)
        Part imgPart = request.getPart("immagine");
        String imgPathToStore = null;

        if (imgPart != null && imgPart.getSize() > 0) {
            String original = imgPart.getSubmittedFileName(); // [web:1240]
            String ext = "";
            int dot = (original == null) ? -1 : original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot).toLowerCase();

            String uploadDir = getServletContext().getRealPath("/uploads");
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String safeName = ("prod_" + System.currentTimeMillis() + ext)
                    .replaceAll("[^a-zA-Z0-9._-]", "_");

            imgPart.write(uploadDir + File.separator + safeName); // [web:1240]
            imgPathToStore = "uploads/" + safeName;
        }

        try (Connection con = DBConnection.getConnection()) {

            if (!isEdit) {
                // INSERT
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO orologio " +
                                "(NomeModello, Marca, IDCategoria, Movimento, MaterialeCassa, DiametroMM, Impermeabilita, " +
                                " Descrizione, Immagine, PrezzoAttuale, Stock, Attivo) " +
                                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)")) {

                    ps.setString(1, nomeModello);
                    ps.setString(2, marca);
                    if (idCategoria == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, idCategoria);
                    ps.setString(4, movimento);
                    ps.setString(5, materialeCassa);
                    if (diametroMM == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, diametroMM);
                    ps.setString(7, impermeabilita);
                    ps.setString(8, descrizione);
                    ps.setString(9, imgPathToStore); // può essere null
                    ps.setBigDecimal(10, prezzo);
                    ps.setInt(11, stock);
                    ps.setBoolean(12, attivo);

                    ps.executeUpdate();
                }

            } else {
                // UPDATE
                if (imgPathToStore == null) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE orologio SET " +
                                    "NomeModello=?, Marca=?, IDCategoria=?, Movimento=?, MaterialeCassa=?, DiametroMM=?, Impermeabilita=?, " +
                                    "Descrizione=?, PrezzoAttuale=?, Stock=?, Attivo=? " +
                                    "WHERE ID=?")) {

                        ps.setString(1, nomeModello);
                        ps.setString(2, marca);
                        if (idCategoria == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, idCategoria);
                        ps.setString(4, movimento);
                        ps.setString(5, materialeCassa);
                        if (diametroMM == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, diametroMM);
                        ps.setString(7, impermeabilita);
                        ps.setString(8, descrizione);
                        ps.setBigDecimal(9, prezzo);
                        ps.setInt(10, stock);
                        ps.setBoolean(11, attivo);
                        ps.setInt(12, id);

                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE orologio SET " +
                                    "NomeModello=?, Marca=?, IDCategoria=?, Movimento=?, MaterialeCassa=?, DiametroMM=?, Impermeabilita=?, " +
                                    "Descrizione=?, Immagine=?, PrezzoAttuale=?, Stock=?, Attivo=? " +
                                    "WHERE ID=?")) {

                        ps.setString(1, nomeModello);
                        ps.setString(2, marca);
                        if (idCategoria == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, idCategoria);
                        ps.setString(4, movimento);
                        ps.setString(5, materialeCassa);
                        if (diametroMM == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, diametroMM);
                        ps.setString(7, impermeabilita);
                        ps.setString(8, descrizione);
                        ps.setString(9, imgPathToStore);
                        ps.setBigDecimal(10, prezzo);
                        ps.setInt(11, stock);
                        ps.setBoolean(12, attivo);
                        ps.setInt(13, id);

                        ps.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            throw new ServletException("Errore DB (admin save prodotto)", e);
        }

        response.sendRedirect(request.getContextPath() + "/admin/prodotti");

    }
}