package model.DAO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DataSource ds;

    public static Connection getConnection() throws SQLException {
        try {
            if (ds == null) {
                Context env = (Context) new InitialContext().lookup("java:/comp/env");
                ds = (DataSource) env.lookup("jdbc/tsw_radino");
            }
            return ds.getConnection();
        } catch (NamingException e) {
            throw new SQLException("JNDI DataSource not found", e);
        }
    }
}
