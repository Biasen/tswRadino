<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Ordini</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <div class="cartCard">

        <div class="cartHeader">
            <h1>Ordini (Admin)</h1>
            <a class="btnGhost" href="${pageContext.request.contextPath}/admin">Dashboard</a>
        </div>

        <c:if test="${not empty ok}">
            <div class="formOk">${ok}</div>
        </c:if>


        <c:choose>
            <c:when test="${empty ordini}">
                <div class="formError">Nessun ordine presente.</div>
            </c:when>

            <c:otherwise>
                <table class="table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Utente</th>
                        <th>Data</th>
                        <th class="num">Totale</th>
                        <th></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${ordini}" var="o">
                        <tr>
                            <td>#${o.id}</td>
                            <td>${o.idUtente}</td>
                            <td>
                                <fmt:formatDate value="${o.dataOrdine}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td class="num">
                                € <fmt:formatNumber value="${o.totale}" minFractionDigits="2" maxFractionDigits="2"/>
                            </td>
                            <td>
                                <a class="btnGhost"
                                   href="${pageContext.request.contextPath}/admin/ordine?id=${o.id}">
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

