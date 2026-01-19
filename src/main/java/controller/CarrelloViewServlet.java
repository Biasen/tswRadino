package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Carrello;
import model.Orologio;
import model.dao.OrologioDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CarrelloViewServlet", value = "/CarrelloViewServlet")
public class CarrelloViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

        HttpSession session = request.getSession(true);
        Carrello cart = (Carrello) session.getAttribute("carrello");
        if (cart == null) {
            cart = new Carrello();
            session.setAttribute("carrello", cart);
        }

        request.setAttribute("carrello", cart);
        request.getRequestDispatcher("/view/carrello.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO: Elabora la richiesta

    }
}