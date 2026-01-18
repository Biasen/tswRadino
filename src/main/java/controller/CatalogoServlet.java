package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Orologio;
import model.dao.OrologioDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CatalogoServlet", value = "/CatalogoServlet")
public class CatalogoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
        OrologioDAO dao = new OrologioDAO();
        List<Orologio> lista = null;
        try {
            lista = dao.doRetrieveAll(true);
            request.setAttribute("orologi", lista);
            request.getRequestDispatcher("/view/catalogo.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }
}