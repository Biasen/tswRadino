package model.dao;

import model.Orologio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrologioDAO {

    public List<Orologio> doRetrieveAll(boolean onlyActive) throws SQLException {
        String sql = "SELECT ID, NomeModello, Marca, IDCategoria, Movimento, MaterialeCassa, " +
                "DiametroMM, Impermeabilita, Descrizione, Immagine, PrezzoAttuale, Stock, Attivo " +
                "FROM orologio " +
                (onlyActive ? "WHERE Attivo = 1 " : "") +
                "ORDER BY Marca, NomeModello";

        List<Orologio> result = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // try-with-resources per chiudere tutto automaticamente [web:36]

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        }

        return result;
    }

    public Orologio doRetrieveById(int id) throws SQLException {
        String sql = "SELECT ID, NomeModello, Marca, IDCategoria, Movimento, MaterialeCassa, " +
                "DiametroMM, Impermeabilita, Descrizione, Immagine, PrezzoAttuale, Stock, Attivo " +
                "FROM orologio WHERE ID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) { // PreparedStatement con placeholder ? [web:37]
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }

        return null;
    }

    private Orologio mapRow(ResultSet rs) throws SQLException {
        Orologio o = new Orologio();
        o.setId(rs.getInt("ID"));
        o.setNomeModello(rs.getString("NomeModello"));
        o.setMarca(rs.getString("Marca"));
        o.setIdCategoria(rs.getInt("IDCategoria"));
        o.setMovimento(rs.getString("Movimento"));
        o.setMaterialeCassa(rs.getString("MaterialeCassa"));
        o.setDiametroMM(rs.getInt("DiametroMM"));
        o.setImpermeabilita(rs.getString("Impermeabilita"));
        o.setDescrizione(rs.getString("Descrizione"));
        o.setImmagine(rs.getString("Immagine"));
        o.setPrezzoAttuale(rs.getBigDecimal("PrezzoAttuale"));
        o.setStock(rs.getInt("Stock"));
        o.setAttivo(rs.getBoolean("Attivo"));
        return o;
    }

}
