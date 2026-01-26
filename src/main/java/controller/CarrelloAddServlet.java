package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Carrello;
import model.Orologio;
import model.RigaCarrello;
import model.dao.OrologioDAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CarrelloAddServlet", value = "/CarrelloAddServlet")
public class CarrelloAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String qtaParam = request.getParameter("qta");

        int id;
        int qta;

        try {
            id = Integer.parseInt(idParam);
            qta = (qtaParam == null || qtaParam.isBlank()) ? 1 : Integer.parseInt(qtaParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri non validi");
            return;
        }

        if (qta < 1) qta = 1;

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
                session.setAttribute("carrello", cart);
            }

            // quantità già nel carrello per quell'orologio
            int giaNelCarrello = 0;
            for (RigaCarrello r : cart.getRighe()) {
                if (r.getProdotto() != null && r.getProdotto().getId() == o.getId()) {
                    giaNelCarrello = r.getQuantita();
                    break;
                }
            }

            int totaleRichiesto = giaNelCarrello + qta;

            // controllo stock lato server
            if (o.getStock() <= 0) {
                request.setAttribute("orologio", o);
                request.setAttribute("error", "Prodotto non disponibile al momento.");
                request.getRequestDispatcher("/view/prodotto.jsp").forward(request, response);
                return;
            }

            if (totaleRichiesto > o.getStock()) {
                request.setAttribute("orologio", o);
                request.setAttribute("error",
                        "Quantità non disponibile. Nel carrello: " + giaNelCarrello +
                                ", richiesti ora: " + qta +
                                ", stock disponibile: " + o.getStock() + ".");
                request.getRequestDispatcher("/view/prodotto.jsp").forward(request, response);
                return;
            }

            // ok: aggiungo
            cart.aggiungi(o, qta);
            response.sendRedirect(request.getContextPath() + "/CarrelloViewServlet");

        } catch (SQLException e) {
            throw new ServletException("Errore DB", e);
        }
    }
}
