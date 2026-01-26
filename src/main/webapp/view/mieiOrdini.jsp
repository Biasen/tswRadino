<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>I miei ordini</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <div class="cartCard">
        <h1>I miei ordini</h1>
        <div class="topActions">
            <a class="btnGhost" href="${pageContext.request.contextPath}/CarrelloViewServlet">Carrello</a>
            <a class="btnGhost" href="${pageContext.request.contextPath}/CarrelloViewServlet">Catalogo</a>
            <span class="helloUser">Ciao, ${sessionScope.utente.nome}</span>
            <a class="btnGhost" href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
        </div>

        <c:choose>
            <c:when test="${empty ordini}">
                <div class="formError">Non hai ancora effettuato ordini.</div>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Data</th>
                        <th class="num">Totale</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${ordini}" var="o">
                        <tr>
                            <td>#${o.id}</td>
                            <td><fmt:formatDate value="${o.dataOrdine}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td class="num">€ ${o.totale}</td>
                            <td>
                                <a class="btnGhost"
                                   href="${pageContext.request.contextPath}/OrdineConfermaServlet?id=${o.id}">
                                    Dettaglio
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

    </div>
</div>
</body>
</html>
