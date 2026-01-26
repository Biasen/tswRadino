<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ordine confermato</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>
<div class="page">
    <div class="cartCard">

        <div class="cartHeader">
            <h1>Ordine confermato</h1>
            <a class="btnGhost" href="${pageContext.request.contextPath}/CatalogoServlet">Torna al catalogo</a>
            <a class="btnGhost" href="${pageContext.request.contextPath}/MyOrdersServlet">I miei ordini</a>
        </div>

        <div class="totalPanel" style="margin: 10px 0 14px; max-width: 520px;">
            <div class="totalRow">
                <span class="totalLabel">Numero ordine</span>
                <span class="totalValue">#${ordine.id}</span>
            </div>
            <div class="totalRow" style="margin-bottom:0;">
                <span class="totalLabel">Totale</span>
                <span class="totalValue">€ ${ordine.totale}</span>
            </div>
        </div>

        <div style="margin: 10px 0 14px;">
            <div><strong>Spedizione</strong>: ${ordine.via}, ${ordine.citta} (${ordine.provincia}) ${ordine.cap} - ${ordine.nazione}</div>
            <div><strong>Pagamento</strong>: ${ordine.metodoTipo} ${ordine.metodoCircuito}</div>
            <c:if test="${not empty ordine.dataOrdine}">
                <div><strong>Data</strong>: ${ordine.dataOrdine}</div>
            </c:if>
        </div>

        <div class="tableWrap">
            <table class="cart">
                <thead>
                <tr>
                    <th>Prodotto</th>
                    <th>Prezzo</th>
                    <th>Q.tà</th>
                    <th>Subtotale</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${ordine.righe}" var="r">
                    <tr>
                        <td>${r.nome} (${r.marca})</td>
                        <td class="num">€ ${r.prezzoUnitario}</td>
                        <td class="num">${r.quantita}</td>
                        <td class="num">€ ${r.subtotale}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div style="margin-top: 14px;">
            <a class="btnPrimary" href="${pageContext.request.contextPath}/CatalogoServlet">Continua lo shopping</a>
        </div>

    </div>
</div>
</body>
</html>