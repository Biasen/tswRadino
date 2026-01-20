package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;

import java.io.IOException;

@WebServlet(name = "CarrelloUpdateServlet", value = "/CarrelloUpdateServlet")
public class CarrelloUpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        String idParam = request.getParameter("id");
        String qtaParam = request.getParameter("qta");

        int id, qta;
        try {
            id = Integer.parseInt(idParam);
            qta = Integer.parseInt(qtaParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri non validi");
            return;
        }

        HttpSession session = request.getSession(true);
        Carrello cart = (Carrello) session.getAttribute("carrello");
        if (cart != null) {
            cart.aggiornaQuantita(id, qta);
        }

        response.sendRedirect(request.getContextPath() + "/CarrelloViewServlet");

    }
}