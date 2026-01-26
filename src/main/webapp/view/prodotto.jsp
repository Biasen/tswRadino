<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${orologio.nomeModello}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>
<div class="page">
    <div class="catalogCard">

        <header class="catalogHeader">
            <h1>${orologio.nomeModello}</h1>

            <div class="topActions">
                <a class="btnGhost" href="${pageContext.request.contextPath}/CatalogoServlet">Catalogo</a>
                <a class="btnGhost" href="${pageContext.request.contextPath}/CarrelloViewServlet">Carrello</a>
                <a class="btnGhost" href="${pageContext.request.contextPath}/MyOrdersServlet">I miei ordini</a>

                <c:choose>
                    <c:when test="${empty sessionScope.utente}">
                        <a class="btnGhost" href="${pageContext.request.contextPath}/view/login.jsp">Login</a>
                        <a class="btnGhost" href="${pageContext.request.contextPath}/view/register.jsp">Registrati</a>
                    </c:when>

                    <c:otherwise>
                        <span class="helloUser">Ciao, ${sessionScope.utente.nome}</span>
                        <a class="btnGhost" href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
                    </c:otherwise>
                </c:choose>


                <c:if test="${not empty error}">
                    <div class="formError">${error}</div>
                </c:if>

                <c:choose>
                    <c:when test="${orologio.stock <= 0}">
                        <div class="outOfStock">Non disponibile al momento.</div>

                        <div class="qtyRow">
                            <input class="qtyInput" type="number" value="1" disabled>
                            <button class="btnPrimary" type="button" disabled>Aggiungi al carrello</button>
                        </div>
                    </c:when>
                </c:choose>

            </div>
        </header>

        <main class="productCard">
            <div class="productGrid">
                <div class="productMedia">
                    <img class="productImg"
                         src="${pageContext.request.contextPath}${orologio.immagine}"
                         alt="Immagine ${orologio.nomeModello}">
                </div>

                <div class="productInfo">
                    <div class="productTop">
                        <div class="brandBig">${orologio.marca}</div>
                        <div class="priceBig">€ ${orologio.prezzoAttuale}</div>
                        <div class="stockLine">Disponibilità: <strong>${orologio.stock}</strong></div>
                    </div>

                    <div class="specs">
                        <div class="specRow"><span>Movimento</span><span>${orologio.movimento}</span></div>
                        <div class="specRow"><span>Materiale cassa</span><span>${orologio.materialeCassa}</span></div>
                        <div class="specRow"><span>Diametro</span><span>${orologio.diametroMM} mm</span></div>
                        <div class="specRow"><span>Impermeabilità</span><span>${orologio.impermeabilita}</span></div>
                    </div>

                    <p class="desc">${orologio.descrizione}</p>

                    <form class="addForm" action="${pageContext.request.contextPath}/CarrelloAddServlet" method="post">
                        <input type="hidden" name="id" value="${orologio.id}">

                        <label for="qta">Quantità</label>
                        <div class="qtyRow">
                            <input class="qtyInput" id="qta" name="qta" type="number" min="1" value="1">
                            <button class="btnPrimary" type="submit">Aggiungi al carrello</button>
                        </div>
                    </form>
                </div>
            </div>
        </main>

    </div>
</div>
</body>
</html>
