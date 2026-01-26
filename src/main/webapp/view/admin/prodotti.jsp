<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Prodotti</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <div class="cartCard">

        <div class="cartHeader">
            <h1>Prodotti (Admin)</h1>
            <div style="display:flex; gap:10px; flex-wrap:wrap;">
                <a class="btnGhost" href="${pageContext.request.contextPath}/admin">Dashboard</a>
                <a class="btnGhost" href="${pageContext.request.contextPath}/admin/prodotto">Nuovo prodotto</a>
            </div>
        </div>

        <c:choose>
            <c:when test="${empty prodotti}">
                <div class="formError">Nessun prodotto presente.</div>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Prodotto</th>
                        <th class="num">Prezzo</th>
                        <th class="num">Stock</th>
                        <th>Attivo</th>
                        <th>Immagine</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${prodotti}" var="p">
                        <tr>
                            <td>#${p.id}</td>
                            <td>${p.nomeModello} (${p.marca})</td>
                            <td class="num">€ ${p.prezzoAttuale}</td>
                            <td class="num">${p.stock}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.attivo}">Sì</c:when>
                                    <c:otherwise>No</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty p.immagine}">
                                        <a class="btnGhost" target="_blank"
                                           href="${pageContext.request.contextPath}/${p.immagine}">Apri</a>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a class="btnGhost" href="${pageContext.request.contextPath}/admin/prodotto?id=${p.id}">Modifica</a>
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
