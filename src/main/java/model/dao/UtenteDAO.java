package model.dao;

import model.Utente;

import java.sql.*;

public class UtenteDAO {

    public Utente findByEmail(Connection con, String email) throws SQLException {
        String sql = "SELECT ID, Nome, Cognome, Email, Password, Tipo FROM utente WHERE Email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Utente u = new Utente();
                u.setId(rs.getInt("ID"));
                u.setNome(rs.getString("Nome"));
                u.setCognome(rs.getString("Cognome"));
                u.setEmail(rs.getString("Email"));
                u.setTipo(rs.getString("Tipo"));
                // NOTA: la password hash la gestiamo in servlet (o ritorniamo anche hash se preferisci)
                return u;
            }
        }
    }

    public String findPasswordHashByEmail(Connection con, String email) throws SQLException {
        String sql = "SELECT Password FROM utente WHERE Email = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("Password") : null;
            }
        }
    }

    public int create(Connection con, String nome, String cognome, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO utente (Nome, Cognome, Email, Password, Tipo) VALUES (?,?,?,?, 'Cliente')";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, cognome);
            ps.setString(3, email);
            ps.setString(4, passwordHash);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Creazione utente fallita: nessuna chiave generata.");
    }
}
