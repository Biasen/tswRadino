package controller;

import model.Product;
import model.dao.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Product> products = productDAO.findAllActive();
            request.setAttribute("products", products);
            request.getRequestDispatcher("/WEB-INF/views/product-list.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Errore lettura prodotti dal DB", e);
        }
    }
}
