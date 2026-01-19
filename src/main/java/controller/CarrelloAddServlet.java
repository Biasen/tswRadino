package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;
import model.Orologio;
import model.dao.OrologioDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CarrelloAddServlet", value = "/CarrelloAddServlet")
public class CarrelloAddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        String idParam = request.getParameter("id");
        String qtaParam = request.getParameter("qta");

        int id;
        int qta = 1;

        try {
            id = Integer.parseInt(idParam);
            if (qtaParam != null && !qtaParam.isBlank()) qta = Integer.parseInt(qtaParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri non validi");
            return;
        }

        try {
            OrologioDAO dao = new OrologioDAO();
            Orologio o = dao.doRetrieveById(id);

            if (o == null || !o.isAttivo()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
                return;
            }

            HttpSession session = request.getSession(true);
            Carrello cart = (Carrello) session.getAttribute("carrello");
            if (cart == null) {
                cart = new Carrello();
                session.setAttribute("carrello", cart); // carrello in sessione [web:130]
            }

            cart.aggiungi(o, qta);

            response.sendRedirect(request.getContextPath() + "/CarrelloViewServlet");
        } catch (SQLException e) {
            throw new ServletException("Errore DB", e);
        }
    }
}