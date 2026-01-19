<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettaglio prodotto</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        header { padding: 16px; background: #111; color: #fff; }
        main { padding: 16px; max-width: 900px; margin: 0 auto; }
        .box { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .img { width: 100%; height: 360px; object-fit: cover; background: #f3f3f3; border-radius: 10px; }
        .price { font-size: 20px; font-weight: 700; }
        .btn { display: inline-block; padding: 10px 14px; background: #1a73e8; color: #fff; text-decoration: none; border-radius: 8px; }
        @media (max-width: 700px) { .box { grid-template-columns: 1fr; } }
    </style>
</head>

<body>
<header>
    <h1>${orologio.nomeModello}</h1>
    <a href="${pageContext.request.contextPath}/CarrelloViewServlet">Visualizza carrello</a>

</header>

<main>
    <div class="box">
        <div>
            <img class="img"
                 src="${pageContext.request.contextPath}${orologio.immagine}"
                 alt="Immagine ${orologio.nomeModello}">
        </div>

        <div>
            <p><strong>Marca:</strong> ${orologio.marca}</p>
            <p class="price">€ ${orologio.prezzoAttuale}</p>
            <p><strong>Disponibilità:</strong> ${orologio.stock}</p>

            <p><strong>Movimento:</strong> ${orologio.movimento}</p>
            <p><strong>Materiale cassa:</strong> ${orologio.materialeCassa}</p>
            <p><strong>Diametro:</strong> ${orologio.diametroMM} mm</p>
            <p><strong>Impermeabilità:</strong> ${orologio.impermeabilita}</p>

            <p>${orologio.descrizione}</p>


            <form action="${pageContext.request.contextPath}/CarrelloAddServlet" method="post">
                <input type="hidden" name="id" value="${orologio.id}">
                <label for="qta">Quantità</label>
                <input id="qta" name="qta" type="number" min="1" value="1">
                <button type="submit">Aggiungi al carrello</button>
            </form>

        </div>
    </div>
</main>
</body>
</html>
