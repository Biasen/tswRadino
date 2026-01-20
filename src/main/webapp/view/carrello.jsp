<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrello</title>

    <!-- CSS esterno -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>
<div class="page">
    <div class="cartCard">

        <header class="catalogHeader">
            <h1>Catalogo</h1>

            <div class="topActions">
                <a class="btnGhost" href="${pageContext.request.contextPath}/CatalogoServlet">
                    Ritorna al catalogo
                </a>

                <c:choose>
                    <c:when test="${empty sessionScope.utente}">
                        <a class="btnGhost" href="${pageContext.request.contextPath}/view/login.jsp">Login</a>
                        <a class="btnPrimary" href="${pageContext.request.contextPath}/view/register.jsp">Registrati</a>
                    </c:when>

                    <c:otherwise>
                        <span class="helloUser">Ciao, ${sessionScope.utente.nome}</span>
                        <a class="btnGhost" href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </header>


        <c:choose>
            <c:when test="${carrello.vuoto}">
                <p class="emptyText">Il carrello è vuoto.</p>
                <a class="btnPrimary" href="${pageContext.request.contextPath}/CatalogoServlet">Torna al catalogo</a>
            </c:when>

            <c:otherwise>
                <div class="tableWrap">
                    <table class="cart">
                        <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo</th>
                            <th>Subtotale</th>
                            <th class="editCol">Modifica quantità</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach items="${carrello.righe}" var="r">
                            <tr>
                                <td class="prodCell">
                                    <div class="prodName">${r.prodotto.nomeModello}</div>
                                    <div class="prodBrand">${r.prodotto.marca}</div>
                                </td>

                                <td class="num">€ ${r.prodotto.prezzoAttuale}</td>
                                <td class="num">€ ${r.subtotale}</td>

                                <td class="edit">
                                    <div class="editBox">
                                        <!-- update -->
                                        <form action="${pageContext.request.contextPath}/CarrelloUpdateServlet" method="post" style="margin:0;">
                                            <input type="hidden" name="id" value="${r.prodotto.id}">
                                            <input class="qtyInput" type="number" name="qta" min="0" value="${r.quantita}">
                                            <button class="btnSmall" type="submit">Aggiorna</button>
                                        </form>

                                        <!-- remove -->
                                        <form action="${pageContext.request.contextPath}/CarrelloRemoveServlet" method="post" style="margin:0;">
                                            <input type="hidden" name="id" value="${r.prodotto.id}">
                                            <button class="btnSmall btnDanger" type="submit">Rimuovi</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="cartFooter">
                    <div class="totalPanel">
                        <div class="totalRow">
                            <span class="totalLabel">Totale</span>
                            <span class="totalValue">€ ${carrello.totale}</span>
                        </div>

                        <a class="btnPrimary btnCheckout" href="${pageContext.request.contextPath}/CheckoutServlet">
                            Procedi al checkout
                        </a>
                    </div>
                </div>

            </c:otherwise>
        </c:choose>

    </div>
</div>
</body>
</html>

