package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;

import java.io.IOException;

@WebServlet(name = "CarrelloRemoveServlet", value = "/CarrelloRemoveServlet")
public class CarrelloRemoveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        String idParam = request.getParameter("id");

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametro id non valido");
            return;
        }

        HttpSession session = request.getSession(true);
        Carrello cart = (Carrello) session.getAttribute("carrello");
        if (cart != null) {
            cart.rimuovi(id);
        }

        response.sendRedirect(request.getContextPath() + "/CarrelloViewServlet");

    }
}