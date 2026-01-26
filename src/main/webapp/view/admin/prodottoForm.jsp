<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Prodotto</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <div class="cartCard">

        <div class="cartHeader">
            <h1>
                <c:choose>
                    <c:when test="${not empty prodotto and prodotto.id > 0}">Modifica prodotto</c:when>
                    <c:otherwise>Nuovo prodotto</c:otherwise>
                </c:choose>
            </h1>
            <a class="btnGhost" href="${pageContext.request.contextPath}/admin/prodotti">Torna ai prodotti</a>
        </div>

        <!-- IMPORTANTE: enctype multipart per inviare file -->
        <form action="${pageContext.request.contextPath}/admin/prodotto/save"
              method="post" enctype="multipart/form-data">

            <input type="hidden" name="id" value="${prodotto.id}">

            <label for="nomeModello">Nome modello</label>
            <input id="nomeModello" name="nomeModello" type="text"
                   value="${prodotto.nomeModello}" maxlength="120" required>

            <label for="marca">Marca</label>
            <input id="marca" name="marca" type="text"
                   value="${prodotto.marca}" maxlength="80" required>

            <label for="prezzoAttuale">Prezzo attuale</label>
            <input id="prezzoAttuale" name="prezzoAttuale" type="text"
                   value="${prodotto.prezzoAttuale}" required>

            <label for="stock">Stock</label>
            <input id="stock" name="stock" type="number" min="0"
                   value="${prodotto.stock}" required>

            <label for="movimento">Movimento</label>
            <input id="movimento" name="movimento" type="text"
                   value="${prodotto.movimento}" maxlength="30">

            <label for="materialeCassa">Materiale cassa</label>
            <input id="materialeCassa" name="materialeCassa" type="text"
                   value="${prodotto.materialeCassa}" maxlength="30">

            <label for="diametroMM">Diametro (mm)</label>
            <input id="diametroMM" name="diametroMM" type="number" min="0"
                   value="${prodotto.diametroMM}">

            <label for="impermeabilita">Impermeabilità</label>
            <input id="impermeabilita" name="impermeabilita" type="text"
                   value="${prodotto.impermeabilita}" maxlength="10">

            <label for="descrizione">Descrizione</label>
            <textarea id="descrizione" name="descrizione" rows="5">${prodotto.descrizione}</textarea>

            <label style="display:flex; gap:10px; align-items:center; margin-top:10px;">
                <input type="checkbox" name="attivo" <c:if test="${prodotto.attivo}">checked</c:if> >
                Attivo
            </label>

            <c:if test="${not empty prodotto.immagine}">
                <div style="margin:10px 0;">
                    Immagine attuale:
                    <a class="btnGhost" target="_blank"
                       href="${pageContext.request.contextPath}/${prodotto.immagine}">Apri</a>
                </div>
            </c:if>

            <label for="immagine">Carica nuova immagine (opzionale in modifica)</label>
            <input id="immagine" name="immagine" type="file" accept="image/*">

            <button class="btnPrimary" type="submit" style="margin-top:14px;">Salva</button>
        </form>

    </div>
</div>
</body>
</html>
