<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Dettaglio ordine</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <div class="cartCard">

        <div class="cartHeader">
            <h1>Dettaglio ordine (Admin)</h1>

            <div style="display:flex; gap:10px; flex-wrap:wrap;">
                <a class="btnGhost" href="${pageContext.request.contextPath}/admin/ordini">Torna agli ordini</a>

                <!-- DELETE ordine -->
                <form action="${pageContext.request.contextPath}/admin/ordine/delete" method="post"
                      onsubmit="return confirm('Sei sicuro di voler eliminare definitivamente questo ordine?');">
                    <input type="hidden" name="id" value="${ordine.id}">
                    <button class="btnGhost" type="submit">Elimina ordine</button>
                </form>
            </div>
        </div>

        <c:if test="${empty ordine}">
            <div class="formError">Ordine non disponibile.</div>
        </c:if>

        <c:if test="${not empty ordine}">
            <div class="totalPanel" style="margin: 10px 0 14px; max-width: 720px;">
                <div class="totalRow">
                    <span class="totalLabel">Numero ordine</span>
                    <span class="totalValue">#${ordine.id}</span>
                </div>

                <div class="totalRow">
                    <span class="totalLabel">Cliente</span>
                    <span class="totalValue">${ordine.nome} ${ordine.cognome} (ID: ${ordine.idUtente})</span>
                </div>

                <div class="totalRow">
                    <span class="totalLabel">Indirizzo</span>
                    <span class="totalValue">
            ${ordine.indirizzo.via}, ${ordine.indirizzo.citta}
            (${ordine.indirizzo.provincia}) ${ordine.indirizzo.cap}
            - ${ordine.indirizzo.nazione}
          </span>
                </div>

                <c:if test="${not empty ordine.indirizzo.telefono}">
                    <div class="totalRow">
                        <span class="totalLabel">Telefono</span>
                        <span class="totalValue">${ordine.indirizzo.telefono}</span>
                    </div>
                </c:if>

                <div class="totalRow">
                    <span class="totalLabel">Data</span>
                    <span class="totalValue">${ordine.dataOrdine}</span>
                </div>

                <div class="totalRow" style="margin-bottom:0;">
                    <span class="totalLabel">Totale</span>
                    <span class="totalValue">€ ${ordine.totale}</span>
                </div>
            </div>

            <h3>Righe ordine</h3>

            <c:choose>
                <c:when test="${empty ordine.righe}">
                    <div class="formError">Nessuna riga per questo ordine.</div>
                </c:when>
                <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th class="num">Prezzo</th>
                            <th class="num">Q.tà</th>
                            <th class="num">Subtotale</th>
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
                </c:otherwise>
            </c:choose>
        </c:if>

    </div>
</div>
</body>
</html>
