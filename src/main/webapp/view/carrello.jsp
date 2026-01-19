<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrello</title>
</head>
<body>

<h1>Carrello</h1>

<c:choose>
    <c:when test="${carrello.vuoto}">
        <p>Il carrello è vuoto.</p>
        <a href="${pageContext.request.contextPath}/CatalogoServlet">Torna al catalogo</a>
    </c:when>

    <c:otherwise>
        <table border="1" cellpadding="8">
            <tr>
                <th>Prodotto</th>
                <th>Prezzo</th>
                <th>Quantità</th>
                <th>Subtotale</th>
            </tr>

            <c:forEach items="${carrello.righe}" var="r">
                <tr>
                    <td>${r.prodotto.nomeModello} (${r.prodotto.marca})</td>
                    <td>€ ${r.prodotto.prezzoAttuale}</td>
                    <td>${r.quantita}</td>
                    <td>€ ${r.subtotale}</td>
                </tr>
            </c:forEach>

            <tr>
                <td colspan="3"><strong>Totale</strong></td>
                <td><strong>€ ${carrello.totale}</strong></td>
            </tr>
        </table>

        <p>
            <a href="${pageContext.request.contextPath}/CatalogoServlet">Continua lo shopping</a>
        </p>
    </c:otherwise>
</c:choose>

</body>
</html>
