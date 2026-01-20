<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogo Orologi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>
<div class="page">
    <div class="catalogCard">

        <header class="catalogHeader">
            <h1>Catalogo</h1>

            <div class="topActions">
                <a class="btnGhost" href="${pageContext.request.contextPath}/CarrelloViewServlet">
                    Visualizza carrello
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


        <main>
            <c:choose>
                <c:when test="${empty orologi}">
                    <div class="emptyState">
                        <div class="emptyTitle">Nessun prodotto disponibile</div>
                        <div class="emptySub">Prova più tardi o aggiorna il catalogo.</div>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="grid">
                        <c:forEach items="${orologi}" var="o">
                            <article class="card">
                                <a class="cardMedia" href="${pageContext.request.contextPath}/ProdottoServlet?id=${o.id}">
                                    <c:choose>
                                        <c:when test="${not empty o.immagine}">
                                            <img src="${pageContext.request.contextPath}${o.immagine}"
                                                 alt="Immagine ${o.nomeModello}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/img/placeholder.jpg"
                                                 alt="Immagine non disponibile">
                                        </c:otherwise>
                                    </c:choose>
                                </a>

                                <div class="cardBody">
                                    <div class="name">${o.nomeModello}</div>
                                    <div class="brand">${o.marca}</div>

                                    <div class="cardRow">
                                        <div class="price">€ ${o.prezzoAttuale}</div>
                                        <div class="meta">Stock: ${o.stock}</div>
                                    </div>

                                    <a class="btnPrimary btnBlock"
                                       href="${pageContext.request.contextPath}/ProdottoServlet?id=${o.id}">
                                        Vedi dettagli
                                    </a>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>

    </div>
</div>
</body>
</html>
